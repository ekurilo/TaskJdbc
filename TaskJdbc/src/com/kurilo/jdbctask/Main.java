package com.kurilo.jdbctask;

import java.sql.Date;

import com.kurilo.jdbctask.dao.RoleDao;
import com.kurilo.jdbctask.dao.UserDao;
import com.kurilo.jdbctask.dao.jdbc.JdbcRoleDao;
import com.kurilo.jdbctask.dao.jdbc.JdbcUserDao;
import com.kurilo.jdbctask.entity.Role;
import com.kurilo.jdbctask.entity.User;

public class Main {
    public static void main(String[] args) {
        RoleDao dao = new JdbcRoleDao();
        String newRoleName = "admin"; 
        Role updateRole = new Role(1L, newRoleName);
        dao.update(updateRole);
        Role newRole = dao.findByName(newRoleName);
        System.out.println(newRole.getId() + newRole.getName());
        User user1 = new User(4L, "user1", "user1", "user1@mail.com", "user1",
               "user1", Date.valueOf("1993-1-1"), 1L);
        UserDao userDao = new JdbcUserDao();
        userDao.create(user1);
        User user = userDao.findByLogin("user1"); 
        System.out.println(user);
        
    }

}
