/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.agoppe.database;

import ru.agoppe.ServiceRequest;
import ru.agoppe.data.User;
import ru.agoppe.exceptions.WrongPasswordException;
import ru.agoppe.exceptions.UserAlreadyExistsException;
import ru.agoppe.exceptions.UserNotExistException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author AGoppe
 */
public class DatabaseHandler {

    private static final String POSTGRESQL_URL = "jdbc:postgresql://localhost:5433/postgres?user=postgres&password=postgres";//&ssl=true
    private static final String GET_USER_QUERY = "SELECT \"LOGIN\", \"PASSWORD\", \"BALANCE\"\n"
            + "  FROM \"USERS\" WHERE \"LOGIN\" = ?";
    private static final String CREATE_AGT_QUERY = "INSERT INTO \"USERS\"(\n"
            + "            \"LOGIN\", \"PASSWORD\", \"BALANCE\")\n"
            + "    VALUES (?, ?, ?);";

    public static void createAgent(ServiceRequest request) throws SQLException, UserAlreadyExistsException {
        User user = getUser(request);
        if (user == null) {
            createUser(request);
        } else {
            throw new UserAlreadyExistsException("User with such name aleady exists");
        }
    }

    public static double getBalance(ServiceRequest request) throws WrongPasswordException, UserNotExistException, SQLException {
        User user = getUser(request);
        if (user == null) {
            throw new UserNotExistException("No such user");
        } else if (user.getPassword().equals(request.getPassword())) {
            return user.getBalance();
        } else {
            throw new WrongPasswordException("Wrong password");
        }
    }

    private static User getUser(ServiceRequest request) throws SQLException {
        User user = null;
        Connection connection = DriverManager.getConnection(POSTGRESQL_URL);
        PreparedStatement statement = connection.prepareCall(GET_USER_QUERY);
        statement.setString(1, request.getLogin());
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            user = new User(rs.getString(1), rs.getString(2), rs.getDouble(3));
        }
        return user;
    }

    private static int createUser(ServiceRequest request) throws SQLException {
        Connection connection = DriverManager.getConnection(POSTGRESQL_URL);
        PreparedStatement statement = connection.prepareCall(CREATE_AGT_QUERY);
        statement.setString(1, request.getLogin());
        statement.setString(2, request.getPassword());
        statement.setDouble(3, 0);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            System.out.println(rs.getString(1));
        }
        return 0;
    }
}
