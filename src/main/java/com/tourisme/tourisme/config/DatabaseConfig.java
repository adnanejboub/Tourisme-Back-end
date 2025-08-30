package com.tourisme.tourisme.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== Database Configuration Check ===");
        logger.info("Active profiles: {}", String.join(", ", environment.getActiveProfiles()));
        logger.info("Database URL: {}", environment.getProperty("spring.datasource.url"));
        logger.info("Database username: {}", environment.getProperty("spring.datasource.username"));
        logger.info("Database driver: {}", environment.getProperty("spring.datasource.driver-class-name"));
        logger.info("JPA DDL auto: {}", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        
        // Test database connection
        testDatabaseConnection();
        
        // Show table structure
        showTableStructure();
        
        logger.info("=== Database Configuration Complete ===");
    }

    private void testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("✅ Database connection successful!");
            DatabaseMetaData metaData = connection.getMetaData();
            logger.info("Database: {}", metaData.getDatabaseProductName());
            logger.info("Version: {}", metaData.getDatabaseProductVersion());
            logger.info("Driver: {}", metaData.getDriverName());
            logger.info("Driver version: {}", metaData.getDriverVersion());
        } catch (SQLException e) {
            logger.error("❌ Database connection failed!", e);
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    private void showTableStructure() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "utilisateur", null);
            
            if (tables.next()) {
                logger.info("📋 Table 'utilisateur' found");
                ResultSet columns = metaData.getColumns(null, null, "utilisateur", null);
                logger.info("Columns in 'utilisateur' table:");
                
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");
                    String nullable = columns.getString("IS_NULLABLE");
                    String defaultValue = columns.getString("COLUMN_DEF");
                    
                    logger.info("  - {}: {} (nullable: {}, default: {})", 
                        columnName, dataType, nullable, defaultValue != null ? defaultValue : "NULL");
                }
            } else {
                logger.info("📋 Table 'utilisateur' not found yet (will be created by JPA)");
            }
        } catch (SQLException e) {
            logger.warn("⚠️ Could not inspect table structure: {}", e.getMessage());
        }
    }

    @Bean
    public CommandLineRunner databaseHealthCheck() {
        return args -> {
            logger.info("=== Database Health Check ===");
            try (Connection connection = dataSource.getConnection()) {
                if (connection.isValid(5)) {
                    logger.info("✅ Database is healthy and responsive");
                } else {
                    logger.warn("⚠️ Database connection validation failed");
                }
            } catch (SQLException e) {
                logger.error("❌ Database health check failed", e);
            }
        };
    }
}
