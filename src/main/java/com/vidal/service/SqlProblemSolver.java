package com.vidal.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SqlProblemSolver {
    
    private static final Logger logger = LoggerFactory.getLogger(SqlProblemSolver.class);
   
    public String solveQuestion1() {
        logger.info("Solving Question 1");
        
        String sqlQuery = "SELECT \n" +
                "    d.DEPARTMENT_NAME,\n" +
                "    MAX(emp_salary.TOTAL_SALARY) AS SALARY,\n" +
                "    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME,\n" +
                "    YEAR(CURDATE()) - YEAR(e.DOB) - CASE \n" +
                "        WHEN MONTH(CURDATE()) < MONTH(e.DOB) OR \n" +
                "             (MONTH(CURDATE()) = MONTH(e.DOB) AND DAY(CURDATE()) < DAY(e.DOB)) \n" +
                "        THEN 1 ELSE 0 \n" +
                "    END AS AGE\n" +
                "FROM DEPARTMENT d\n" +
                "INNER JOIN (\n" +
                "    SELECT \n" +
                "        e.EMP_ID,\n" +
                "        e.FIRST_NAME,\n" +
                "        e.LAST_NAME,\n" +
                "        e.DOB,\n" +
                "        e.DEPARTMENT,\n" +
                "        SUM(p.AMOUNT) AS TOTAL_SALARY,\n" +
                "        ROW_NUMBER() OVER (PARTITION BY e.DEPARTMENT ORDER BY SUM(p.AMOUNT) DESC) as rnk\n" +
                "    FROM EMPLOYEE e\n" +
                "    INNER JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID\n" +
                "    WHERE DAY(p.PAYMENT_TIME) != 1\n" +
                "    GROUP BY e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, e.DOB, e.DEPARTMENT\n" +
                ") emp_salary ON d.DEPARTMENT_ID = emp_salary.DEPARTMENT\n" +
                "INNER JOIN EMPLOYEE e ON emp_salary.EMP_ID = e.EMP_ID\n" +
                "WHERE emp_salary.rnk = 1\n" +
                "GROUP BY d.DEPARTMENT_NAME, emp_salary.EMP_ID, e.FIRST_NAME, e.LAST_NAME, e.DOB\n" +
                "ORDER BY d.DEPARTMENT_NAME";
        
        logger.info("Question 1 Solution: {}", sqlQuery);
        return sqlQuery;
    }
}

