package com.kurilo.jdbctask.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurilo.jdbctask.dao.RoleDao;
import com.kurilo.jdbctask.entity.Role;

public class JdbcRoleDao extends AbstractJdbcDao implements RoleDao {
    
    private static final Logger LOG = LoggerFactory.getLogger(JdbcRoleDao.class);

    
    @Override
    public void create(Role role) {
        if (role == null) {
            throw new NullPointerException();
        }
        String sql = "INSERT INTO role (id, name) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = createConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            statement.setLong(1, role.getId());
            statement.setString(2, role.getName());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("cannot create role", e);
            throw new IllegalArgumentException(e);
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }

    }

    @Override
    public void update(Role role) {
        if (role == null) {
            throw new NullPointerException();
        }
        String sql = "UPDATE role SET name=? WHERE id=?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = createConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            statement.setString(1, role.getName());
            statement.setLong(2, role.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("can not update role", e);
            throw new IllegalArgumentException(e);
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }

    }

    @Override
    public void remove(Role role) {
        String sql = "DELETE FROM role WHERE id=?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = createConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            statement.setLong(1, role.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("can not remove role", e);
            throw new IllegalArgumentException(e);
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }

    }

    @Override
    public Role findByName(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        String sql = "SELECT * FROM role WHERE name=?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Role role = null;
        try {
            connection = createConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = new Role(resultSet.getLong(Fields.ROLE_ID),
                        resultSet.getString(Fields.ROLE_NAME));
            }
        } catch (SQLException e) {
            LOG.error("can not find role by name");
            throw new IllegalArgumentException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeStatement(statement);
            closeConnection(connection);
        }
        return role;
    }

}
