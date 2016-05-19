package com.kurilo.jdbctask.dao.jdbc;

import static com.kurilo.jdbctask.dao.jdbc.Fields.USER_BIRTHDAY;
import static com.kurilo.jdbctask.dao.jdbc.Fields.USER_EMAIL;
import static com.kurilo.jdbctask.dao.jdbc.Fields.USER_FIRST_NAME;
import static com.kurilo.jdbctask.dao.jdbc.Fields.USER_ID;
import static com.kurilo.jdbctask.dao.jdbc.Fields.USER_LAST_NAME;
import static com.kurilo.jdbctask.dao.jdbc.Fields.USER_LOGIN;
import static com.kurilo.jdbctask.dao.jdbc.Fields.USER_PASSWORD;
import static com.kurilo.jdbctask.dao.jdbc.Fields.USER_ROLE_ID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurilo.jdbctask.dao.UserDao;
import com.kurilo.jdbctask.entity.User;

public class JdbcUserDao extends AbstractJdbcDao implements UserDao {

    private static final Logger LOG = LoggerFactory
            .getLogger(JdbcUserDao.class);

    @Override
    public void create(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        String sql = "INSERT INTO user values (?,?,?,?,?,?,?,?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = createConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            int index = 1;
            statement.setLong(index++, user.getId());
            statement.setString(index++, user.getLogin());
            statement.setString(index++, user.getPassword());
            statement.setString(index++, user.getEmail());
            statement.setString(index++, user.getFirstName());
            statement.setString(index++, user.getLastName());
            statement.setDate(index++, user.getBirthday());
            statement.setLong(index, user.getRoleId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("can not create user", e);
            throw new IllegalArgumentException(e);
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }
    }

    @Override
    public void update(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        String sql = "UPDATE user SET login=?, password=?, email=?, firstName=?, "
                + "lastName=?, birthday=?, roleId=? WHERE id=?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = createConnection();
            connection.setAutoCommit(true);
            statement = connection.prepareStatement(sql);
            int index = 1;
            statement.setString(index++, user.getLogin());
            statement.setString(index++, user.getPassword());
            statement.setString(index++, user.getEmail());
            statement.setString(index++, user.getFirstName());
            statement.setString(index++, user.getLastName());
            statement.setDate(index++, user.getBirthday());
            statement.setLong(index++, user.getRoleId());
            statement.setLong(index, user.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("can not update user", e);
            throw new IllegalArgumentException(e);
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }

    }

    @Override
    public void remove(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        String sql = "DELETE FROM user WHERE id=?";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = createConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            statement.setLong(1, user.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("can not remove user", e);
            throw new IllegalArgumentException(e);
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }

    }

    @Override
    public List<User> findAll() {

        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = createConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                users.add(getUser(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("can not find all users", e);
            throw new IllegalArgumentException(e);
        } finally {
            closeResultSet(resultSet);
            closeStatement(statement);
            closeConnection(connection);

        }
        return users;
    }

    @Override
    public User findByLogin(String login) {
        if (login == null) {
            throw new NullPointerException();
        }
        String sql = "SELECT * FROM user WHERE login=?";
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = createConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getUser(resultSet);
            }
        } catch (SQLException e) {
            LOG.error("can not find user by login", e);
            throw new IllegalArgumentException(e);
        } finally {
            closeResultSet(resultSet);
            closeStatement(statement);
            closeConnection(connection);
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        if (email == null) {
            throw new NullPointerException();
        }
        String sql = "SELECT * FROM user WHERE email=?";
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = createConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getUser(resultSet);
            }
        } catch (SQLException e) {
            LOG.error("can not find user by email", e);
            throw new IllegalArgumentException(e);
        } finally {
            closeResultSet(resultSet);
            closeStatement(statement);
            closeConnection(connection);
        }
        return user;
    }

    private User getUser(ResultSet set) throws SQLException {
        User user = new User();
        user.setId(set.getLong(USER_ID));
        user.setLogin(set.getString(USER_LOGIN));
        user.setPassword(set.getString(USER_PASSWORD));
        user.setEmail(set.getString(USER_EMAIL));
        user.setFirstName(set.getString(USER_FIRST_NAME));
        user.setLastName(set.getString(USER_LAST_NAME));
        user.setBirthday(set.getDate(USER_BIRTHDAY));
        user.setRoleId(set.getLong(USER_ROLE_ID));
        return user;
    }

    private void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOG.error("can not close ResultSet");
            }
        }
    }

}
