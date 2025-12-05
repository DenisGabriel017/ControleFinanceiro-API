package com.dnsotware.appfinanceiro_api.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    private final JdbcTemplate jdbcTemplate;

    public TenantService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTenant(String tenantId) {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + tenantId);
    }
}
