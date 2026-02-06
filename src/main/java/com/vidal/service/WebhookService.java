package com.vidal.service;

import com.vidal.dto.WebhookRequestDto;
import com.vidal.dto.WebhookResponseDto;
import com.vidal.dto.SqlSubmissionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WebhookService {
    
    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
    private static final String WEBHOOK_GENERATION_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String WEBHOOK_SUBMISSION_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private SqlProblemSolver sqlProblemSolver;
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationStart() {
        logger.info("Application started. Initiating webhook generation workflow...");
        try {
            // Step 1: Generate webhook
            WebhookResponseDto webhookResponse = generateWebhook();
            logger.info("Webhook generated successfully");
            logger.info("Webhook URL: {}", webhookResponse.getWebhook());
            
            // Step 2: Solve SQL problem
            String sqlSolution = solveSqlProblem("REG12347");
            logger.info("SQL problem solved successfully");
            
            // Step 3: Submit solution to webhook
            submitSolution(webhookResponse.getWebhook(), webhookResponse.getAccessToken(), sqlSolution);
            logger.info("Solution submitted successfully to webhook");
            
        } catch (Exception e) {
            logger.error("Error in webhook workflow", e);
        }
    }
    
    private WebhookResponseDto generateWebhook() {
        WebhookRequestDto request = new WebhookRequestDto();
        request.setName("John Doe");
        request.setRegNo("REG12347");
        request.setEmail("john@example.com");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<WebhookRequestDto> entity = new HttpEntity<>(request, headers);
        
        WebhookResponseDto response = restTemplate.postForObject(
                WEBHOOK_GENERATION_URL,
                entity,
                WebhookResponseDto.class
        );
        
        return response;
    }
    
    private String solveSqlProblem(String regNo) {
        logger.info("Solving SQL Problem");
        return sqlProblemSolver.solveQuestion1();
    }
    
    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        SqlSubmissionDto submission = new SqlSubmissionDto();
        submission.setFinalQuery(sqlQuery);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);
        
        HttpEntity<SqlSubmissionDto> entity = new HttpEntity<>(submission, headers);
        
        restTemplate.postForObject(
                WEBHOOK_SUBMISSION_URL,
                entity,
                String.class
        );
    }
}
