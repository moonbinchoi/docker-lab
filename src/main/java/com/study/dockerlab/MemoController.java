package com.study.dockerlab;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemoController {
    private final JdbcTemplate jdbc;

    public MemoController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/")
    String memo() {
        return "v3 " + jdbc.queryForObject("SELECT t FROM memo LIMIT 1", String.class);
    }
}
