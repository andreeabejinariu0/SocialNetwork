package com.example.labul4.repository;

import com.example.labul4.domain.User;
import com.example.labul4.domain.validators.InvalidId;
import com.example.labul4.domain.validators.InvalidName;
import com.example.labul4.domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class UserDbRepository implements Repository<Long, User> {
    private String url;
    private String user;
    private String password;
    private Validator<User> userValidator;

    public UserDbRepository(String url, String user, String password, Validator<User> validator) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.userValidator = validator;
    }

    public User findOne(Long id) throws SQLException {
        String sql = "select * from users where id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    User u = new User(id, firstName, lastName, username, password);
                    Vector<Long> friends = findFriends(u);
                    u.setFriends(friends);
                    return u;
                }

            }

        } catch (SQLException sq) {
            System.out.println(sq.getMessage());
        }
        return null;
    }


    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");


                User user = new User(id, firstName, lastName, username,password);
                Vector<Long> friends = findFriends(user);
                user.setFriends(friends);
                users.add(user);
            }
            return users;
        } catch (SQLException sq) {
            System.out.println(sq.getMessage());
        }
        return users;
    }

    @Override
    public User save(User entity) throws InvalidName, InvalidId, SQLException {
        String sql = "insert into users (first_name, last_name, username, password) values (?,?,?,?)";
        userValidator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = connection.prepareStatement(sql);) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getUsername());
            ps.setString(4, entity.getPassword());

            ps.executeUpdate();
        } catch (SQLException sq) {
            System.out.println(sq.getMessage());
        }
        return null;
    }

    @Override
    public User delete(Long aLong) throws SQLException {
        String sql = "delete from users where id = ?";
        User user1 = findOne(aLong);
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();

        } catch (SQLException sq) {
            System.out.println(sq.getMessage());
        }
        return user1;
    }

    @Override
    public User update(User entity) throws InvalidName,  InvalidId, SQLException {
        String sql = "update users set first_name = ?, last_name = ?, username=? , password =? where id = ?";
        userValidator.validate(entity);
        User user1 = findOne(entity.getId());

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getUsername());
            ps.setString(4, entity.getPassword());
            ps.setLong(5, entity.getId());
            ps.executeUpdate();
        } catch (SQLException sq) {
            System.out.println(sq.getMessage());
        }
        return user1;
    }


    public Vector<Long> findFriends(User u) {
        Vector<Long> users = new Vector<>();
        String sql = "select id_user1 as id from friendships where id_user2 = ? and status=? union select id_user2 as id from friendships where id_user1 = ? and status = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, u.getId());
            ps.setString(2, "Accepted");
            ps.setLong(3, u.getId());
            ps.setString(4, "Accepted");

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    users.add(id);
                }
            }

            return users;

        } catch (SQLException sq) {
            System.out.println(sq.getMessage());
        }
        return users;
    }


    public User find(String username) {
        String sql = "select * from users where username =?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String username2 = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    User u = new User(id, firstName, lastName, username2, password);
                    Vector<Long> friends = findFriends(u);
                    u.setFriends(friends);
                    return u;
                }
            }

        } catch (SQLException sq) {
            System.out.println(sq.getMessage());
        }
        return null;
    }

    public Vector<User> findByName(String name) {
        Vector<User> users = new Vector<>();
        String[] nameSeparated = name.split(" ");
        if (nameSeparated.length == 2) {
            String sql = "select * from users where first_name = ? and last_name = ? union select * from users where first_name = ? or last_name = ? ";
            try (Connection connection = DriverManager.getConnection(url, user, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, nameSeparated[0]);
                ps.setString(2, nameSeparated[1]);
                ps.setString(3, nameSeparated[1]);
                ps.setString(4, nameSeparated[0]);

                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        Long id = resultSet.getLong("id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String username2 = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        User u = new User(id, firstName, lastName, username2, password);
                        users.add(u);
                    }
                }

            } catch (SQLException sq) {
                System.out.println(sq.getMessage());
            }
            return users;
        }

        if (nameSeparated.length == 1) {
            String sql = "select * from users where first_name = ? or last_name = ? ";
            try (Connection connection = DriverManager.getConnection(url, user, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, nameSeparated[0]);
                ps.setString(2, nameSeparated[0]);


                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        Long id = resultSet.getLong("id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String username2 = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        User u = new User(id, firstName, lastName, username2, password);
                        users.add(u);
                    }
                }

            } catch (SQLException sq) {
                System.out.println(sq.getMessage());
            }
            return users;
        }


        return users;
    }
}
