package com.belak.scheduletimetable.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DatabaseMigration implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        // Migration 1 : ajout de la colonne active dans cours_tp
        jdbcTemplate.execute("""
            ALTER TABLE cours_tp
            ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE
        """);

        // Migration 2 : ajout de created_at dans user_entity
        jdbcTemplate.execute("""
            ALTER TABLE user_entity
            ADD COLUMN IF NOT EXISTS created_at TIMESTAMP
        """);

        // Initialisation des anciennes données
        jdbcTemplate.update("""
            UPDATE user_entity
            SET created_at = COALESCE(updated_at, CURRENT_TIMESTAMP)
            WHERE created_at IS NULL
        """);
    }
}