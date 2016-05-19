package com.kurilo.jdbctask.dao.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJdbcDao {

    private String resourceFileName = "database.properties";
    private static DataSource source;

    private static final Logger LOG = LoggerFactory
            .getLogger(AbstractJdbcDao.class);

    public Connection createConnection() {

        if (source == null) {
            source = getDataSource();
        }
        Connection connection = null;
        try {
            connection = source.getConnection();
        } catch (SQLException e) {
            LOG.error("can not create connection", e);
            throw new IllegalStateException(e);
        }
        return connection;
    }

    private DataSource getDataSource() {
        Properties properties = new Properties();
        try (InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(resourceFileName)) {
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        DataSource dataSource = null;
        try {
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return dataSource;
    }

    protected void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOG.error("can not close statement", e);
            }
        }
    }

    protected void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("can not close connection", e);
            }
        }
    }

    protected void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOG.error("can not rollback", e);
            }
        }
    }

}
