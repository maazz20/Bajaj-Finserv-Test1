package com.vidal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SqlSubmissionDto {
    @JsonProperty("finalQuery")
    private String finalQuery;

    public SqlSubmissionDto() {
    }

    public SqlSubmissionDto(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }
}

