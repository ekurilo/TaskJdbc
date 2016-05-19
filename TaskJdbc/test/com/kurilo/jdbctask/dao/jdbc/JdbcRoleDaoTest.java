package com.kurilo.jdbctask.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.kurilo.jdbctask.entity.Role;

@RunWith(JUnit4.class)
public class JdbcRoleDaoTest extends DataBaseTestConfig {

    private JdbcRoleDao dao;

    private IDatabaseConnection connection;

    @Before
    public void before() throws Exception {
	IDatabaseTester tester = getTester();
	tester.setDataSet(getDataSet());
	connection = tester.getConnection();
	dao = new JdbcRoleDao();
	tester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
	tester.onSetup();
    }

    @Test(expected = NullPointerException.class)
    public void testCreateRoleNull() {
	dao.create(null);
    }

    @Test
    public void createRole() throws Exception {
	IDataSet dataSet = connection.createDataSet();
	ITable table = dataSet.getTable("role");
	int rowCount = table.getRowCount();
	dao.create(new Role(4L, "guest"));
	IDataSet dataSet1 = connection.createDataSet();
	ITable tableAfter = dataSet1.getTable("role");
	assertEquals(rowCount + 1, tableAfter.getRowCount());


    }
    
    

    @Test(expected = IllegalArgumentException.class)
    public void testDoubleRoleCreate() {
	Role dublicateRole = new Role(1L, "admin");
	dao.create(dublicateRole);
	dao.create(dublicateRole);

    }

    @Test(expected = NullPointerException.class)
    public void testFindByNameNull() throws Exception {
	dao.findByName(null);
    }

    @Test
    public void testFindByName() throws Exception {
	String roleName = "admin";
	Role role = dao.findByName(roleName);
	assertNotNull(role);
	assertEquals(roleName, role.getName());
	Role notExistsRole = dao.findByName("notExistsRole");
	assertNull(notExistsRole);

    }

    @Test(expected = NullPointerException.class)
    public void testUpdateRoleNull() throws Exception {
	dao.update(null);
    }

    @Test
    public void testUpdateRole() throws Exception {
	Role roleForUpdate = new Role(1L, "superuser");
	dao.update(roleForUpdate);
	IDataSet dataSet = connection.createDataSet();
	ITable table = dataSet.getTable("role");
	assertEquals(roleForUpdate.getName(), table.getValue(0, "name"));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveRoleNull() throws Exception {
	dao.remove(null);
    }

    @Test
    public void testRemoveRole() throws Exception {
	IDataSet dataSet = connection.createDataSet();
	ITable table = dataSet.getTable("role");
	int rowCount = table.getRowCount();
	dao.remove(new Role(3L, "guest"));
	IDataSet dataSet1 = connection.createDataSet();
	ITable tableAfter = dataSet1.getTable("role");
	assertEquals(rowCount - 1, tableAfter.getRowCount());
    }

}
