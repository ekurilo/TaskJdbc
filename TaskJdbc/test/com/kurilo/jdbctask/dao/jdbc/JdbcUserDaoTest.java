package com.kurilo.jdbctask.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.util.List;

import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.kurilo.jdbctask.entity.User;

@RunWith(JUnit4.class)
public class JdbcUserDaoTest extends DataBaseTestConfig {

    private JdbcUserDao dao;

    private IDatabaseConnection connection;
    
    @Before
    public void before() throws Exception {
	IDatabaseTester tester = getTester();
	tester.setDataSet(getDataSet());
	connection = tester.getConnection();
	dao = new JdbcUserDao();
	tester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
	tester.onSetup();
    }

    @Test(expected = NullPointerException.class)
    public void testCreateUserNull() {
	dao.create(null);
    }

    @Test(timeout = 1000)
    public void testCreateUser() throws Exception {
	IDataSet dataSet = connection.createDataSet();
	ITable table = dataSet.getTable("user");
	int rowCount = table.getRowCount();
	User user = new User(5L, "user5", "user5", "user5@mail.com", "user5", "user5", Date.valueOf("1993-1-1"), 1L);
	dao.create(user);
	IDataSet dataSet1 = connection.createDataSet();
	ITable tableAfter = dataSet1.getTable("user");
	assertEquals(rowCount + 1, tableAfter.getRowCount());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoubleUser() {
	User user = new User(5L, "user5", "user5", "user5@mail.com", "user5", 
		"user5", Date.valueOf("1993-1-1"), 1L);
	dao.create(user);
	dao.create(user);

    }

    @Test(expected = NullPointerException.class)
    public void testUpdateUserNull() {
	dao.update(null);
    }

    @Test(timeout = 1000)
    public void testUpdateUser() throws Exception {
	User user = new User(1L, "user1", "user1", "user1@mail.com", "user1", "user1", Date.valueOf("1993-1-1"), 1L);
	user.setLogin("user5");
	user.setEmail("user5@mail.com");
	dao.update(user);
	IDataSet dataSet = connection.createDataSet();
	ITable table = dataSet.getTable("user");
	assertEquals(user.getLogin(), table.getValue(0, "login"));
	assertEquals(user.getEmail(), table.getValue(0, "email"));

    }

    @Test(expected = NullPointerException.class)
    public void testRemoveUserNull() {
	dao.remove(null);
    }

    @Test(timeout = 1000)
    public void testRemoveUser() throws Exception {
	IDataSet dataSet = connection.createDataSet();
	ITable table = dataSet.getTable("user");
	int rowCount = table.getRowCount();
	User user = new User(1L, "user1", "user1", "user1@mail.com", "user1", "user1", Date.valueOf("1993-1-1"), 1L);
	dao.remove(user);
	IDataSet dataSet1 = connection.createDataSet();
	ITable tableAfter = dataSet1.getTable("user");
	assertEquals(rowCount - 1, tableAfter.getRowCount());
    }

    @Test(timeout = 1500)
    public void testFindAll() throws Exception {
	List<User> res = dao.findAll();
	IDataSet dataSet = connection.createDataSet();
	ITable table = dataSet.getTable("user");
	int rowCount = table.getRowCount();
	assertEquals(rowCount, res.size());
    }

    @Test(expected = NullPointerException.class)
    public void testFindByLoginNull() {
	dao.findByLogin(null);
    }

    @Test(timeout = 1000)
    public void testFindByLogin() {
	String login = "user1";
	User user = dao.findByLogin(login);
	assertNotNull(user);
	assertEquals(login, user.getLogin());

	String notExistsLogin = "notExistsLogin";
	User user2 = dao.findByLogin(notExistsLogin);
	assertNull(user2);
    }

    @Test(expected = NullPointerException.class)
    public void testFinfByEmailNull() {
	dao.findByEmail(null);
    }

    @Test(timeout = 1000)
    public void testFindByEmail() {
	String email = "user1@mail.com";
	User user = dao.findByEmail(email);
	assertNotNull(user);
	assertEquals(email, user.getEmail());

	String notExistsEmail = "notExistsLogin@mail.com";
	User user2 = dao.findByEmail(notExistsEmail);
	assertNull(user2);
    }

}
