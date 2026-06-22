package com.rabbiter.music.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DbSchemaInit {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void ensurePlayCountColumn() {
        try {
            Integer cnt = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'song' AND column_name = 'play_count'",
                    Integer.class
            );
            if (cnt == null || cnt == 0) {
                jdbcTemplate.execute("ALTER TABLE song ADD COLUMN play_count INT DEFAULT 0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void ensureAdminAvatarColumn() {
        try {
            Integer cnt = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'admin' AND column_name = 'avator'",
                    Integer.class
            );
            if (cnt == null || cnt == 0) {
                jdbcTemplate.execute("ALTER TABLE admin ADD COLUMN avator VARCHAR(255) DEFAULT NULL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void ensurePlayHistoryTable() {
        try {
            jdbcTemplate.execute(
                    "CREATE TABLE IF NOT EXISTS play_history (" +
                            "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                            "user_id INT NOT NULL," +
                            "song_id INT NOT NULL," +
                            "play_time DATETIME DEFAULT CURRENT_TIMESTAMP," +
                            "INDEX idx_user_time (user_id, play_time)," +
                            "INDEX idx_song (song_id)" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


