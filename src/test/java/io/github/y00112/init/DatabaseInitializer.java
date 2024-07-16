package io.github.y00112.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializer {
    @Autowired
    DataSource dataSource;

    /**
     * h2 hsqldb 初始化代码
     * @throws SQLException
     */
    @PostConstruct
    public void init() throws SQLException {
        try (var conn = dataSource.getConnection()) {
            try (var stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" //
                        + "id BIGINT IDENTITY NOT NULL PRIMARY KEY, " //
                        + "email VARCHAR(100) NOT NULL, " //
                        + "password VARCHAR(100) NOT NULL, " //
                        + "name VARCHAR(100) NOT NULL, " //
                        + "createdAt BIGINT NOT NULL, " //
                        + "UNIQUE (email))");
            }
        }
    }


    // /**
    //  * mysql 初始化代码
    //  * @throws SQLException
    //  */
    // @PostConstruct
    // public void init() throws SQLException {
    //     try (var conn = dataSource.getConnection()) {
    //         try (var stmt = conn.createStatement()) {
    //             stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" //
    //                     + "id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY, " //
    //                     + "email VARCHAR(100) NOT NULL, " //
    //                     + "password VARCHAR(100) NOT NULL, " //
    //                     + "name VARCHAR(100) NOT NULL, " //
    //                     + "createdAt BIGINT NOT NULL, " //
    //                     + "UNIQUE (email))");
    //         }
    //     }
    // }
    //
    //
    // /**
    //  * SQLite
    //  *
    //  * @throws SQLException
    //  */
    // @PostConstruct
    // public void init() throws SQLException {
    //     try (Connection conn = dataSource.getConnection()) {
    //         try (Statement stmt = conn.createStatement()) {
    //             stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" //
    //                     + "id INTEGER PRIMARY KEY AUTOINCREMENT, " // 使用 AUTOINCREMENT 关键字
    //                     + "email VARCHAR(100) NOT NULL, " //
    //                     + "password VARCHAR(100) NOT NULL, " //
    //                     + "name VARCHAR(100) NOT NULL, " //
    //                     + "createdAt BIGINT NOT NULL, " //
    //                     + "UNIQUE (email))");
    //         }
    //     }
    // }
}