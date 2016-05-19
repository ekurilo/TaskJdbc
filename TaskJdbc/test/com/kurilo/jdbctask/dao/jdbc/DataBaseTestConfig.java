package com.kurilo.jdbctask.dao.jdbc;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBaseTestConfig {

    private static final String databaseTestProperties = "database.properties";
    private static final String roleDataset = "dataset.xml";
    private static IDatabaseTester tester;

    public static void config() throws Exception {

	Properties databaseProp = loadProperties();
	System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
		databaseProp.getProperty("driverClassName"));
	System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, databaseProp.getProperty("url"));
	System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, databaseProp.getProperty("username"));
	System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, databaseProp.getProperty("password"));
	
	    Connection conn = DriverManager.getConnection(databaseProp.getProperty("url"), 
		    databaseProp.getProperty("username"),
		    databaseProp.getProperty("password"));
	    Statement sst = conn.createStatement();
	    sst.executeUpdate("DROP TABLE IF EXISTS ROLE; CREATE TABLE IF NOT EXISTS role (id BIGINT PRIMARY KEY ,"
		    + "name VARCHAR(20)); CREATE TABLE IF NOT EXISTS user " + "(id BIGINT PRIMARY KEY, "
		    + "login VARCHAR(40) UNIQUE , password VARCHAR(40), "
		    + "email VARCHAR(40) UNIQUE , firstName VARCHAR(40),"
		    + "lastName VARCHAR(40),birthday DATE,roleId BIGINT,"
		    + "FOREIGN KEY (roleId) REFERENCES role(id));");
	    sst.close();
	    conn.close();

	    tester = new JdbcDatabaseTester(databaseProp.getProperty("driverClassName"),
		    databaseProp.getProperty("url"), databaseProp.getProperty("username"),
		    databaseProp.getProperty("password"));

    }

    protected IDatabaseTester getTester() throws Exception {
	if (tester == null) {
	    config();
	}
	return tester;
    }

    protected IDataSet getDataSet() throws Exception {
	return new FlatXmlDataSetBuilder()
		.build(new File(this.getClass().getClassLoader().getResource(roleDataset).getFile()));
    }

    private static Properties loadProperties() throws Exception {
	Properties properties = new Properties();
	InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(databaseTestProperties);
	properties.load(in);

	in.close();
	return properties;
    }

}
