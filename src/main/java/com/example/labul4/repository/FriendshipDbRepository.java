package com.example.labul4.repository;

import com.example.labul4.domain.Friendship;
import com.example.labul4.domain.Status;
import com.example.labul4.domain.validators.FriendshipValidator;
import com.example.labul4.domain.validators.InvalidId;
import com.example.labul4.domain.validators.InvalidName;
import com.example.labul4.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class FriendshipDbRepository implements Repository<Long, Friendship> {
    private String url;
    private String user;
    private String password;
    private Validator<Friendship> friendshipValidator;

    public FriendshipDbRepository(String url, String user, String password, FriendshipValidator friendshipValidator) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.friendshipValidator = friendshipValidator;

    }

    @Override
    public Friendship findOne(Long idf) throws SQLException {
        String sql = "select * from friendships where id_f = ?";
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1,idf);
            try(ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    Long idUser1 = resultSet.getLong("id_user1");
                    Long idUser2 = resultSet.getLong("id_user2");
                    Timestamp date = resultSet.getTimestamp("date");
                    Status status = Status.valueOf(resultSet.getString("status"));

                    return new Friendship(idf, idUser1, idUser2, date.toLocalDateTime(), status);
                }
            }
        }catch(SQLException sq){
            System.out.println(sq.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id_f");
                Long id_user1 = resultSet.getLong("id_user1");
                Long id_user2 = resultSet.getLong("id_user2");
                Timestamp date = resultSet.getTimestamp("date");
                Status status = Status.valueOf(resultSet.getString("status"));

                Friendship friendship = new Friendship(id, id_user1, id_user2, date.toLocalDateTime(), status);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException sq) {
            System.out.println(sq.getMessage());
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) throws InvalidName, InvalidId, SQLException {
        String sql = "insert into friendships (id_user1, id_user2, date, status) values (?,?,?,?)";
        friendshipValidator.validate(entity);

        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(sql)){


                ps.setLong(1, entity.getIdUser1());
                ps.setLong(2, entity.getIdUser2());
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(4, "Pending");

//            if(entity.getIdUser1() < entity.getIdUser2())
//            else {
//                ps.setLong(1, entity.getIdUser2());
//                ps.setLong(2, entity.getIdUser1());
//                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
//                ps.setString(4, "Pending");
//            }

            ps.executeUpdate();


        }catch (SQLException sq){
            System.out.println(sq.getMessage());
        }
        return null;
    }

    @Override
    public Friendship delete(Long aLong) {
        String sql = "delete from friendships where id_f = ?";
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1, aLong);
            ps.executeUpdate();

        }catch (SQLException sq){
            System.out.println(sq.getMessage());
        }
        return null;
    }

    @Override
    public Friendship update(Friendship entity) throws InvalidName, InvalidId, SQLException {
        String sql = "update friendships set status = ? where id_f = ?";
        friendshipValidator.validate(entity);

        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1,entity.getStatus().toString());
            ps.setLong(2,entity.getId());


            ps.executeUpdate();
        }catch (SQLException sq){
            System.out.println(sq.getMessage());
        }
        return null;
    }

    public Vector<Friendship> find(Long id){
        Vector<Friendship> friendships = new Vector<>();
        String sql = "select id_f as id, id_user1 as iduser, status as status, date as date from friendships where id_user2 = ? union select id_f as id, id_user2 as iduser, status as status, date as date from friendships where id_user1 = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setLong(2, id);

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Long idFriendship = resultSet.getLong("id");
                    Long idFriend = resultSet.getLong("iduser");
                    String status = resultSet.getString("status");
                    Timestamp date = resultSet.getTimestamp("date");

                    friendships.add(new Friendship(idFriendship, id, idFriend,date.toLocalDateTime(), Status.valueOf(status)));
                }
            }

            return friendships;

        } catch (SQLException sq) {
            System.out.println(sq.getMessage());
        }
        return friendships;

    }

    public Vector<Friendship> findReceiver(Long id){
        Vector<Friendship> friendships = new Vector<>();
        String sql = "select id_f as id, id_user2 as iduser, status as status, date as date from friendships where id_user1 = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setLong(2, id);

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Long idFriendship = resultSet.getLong("id");
                    Long idFriend = resultSet.getLong("iduser");
                    String status = resultSet.getString("status");
                    Timestamp date = resultSet.getTimestamp("date");

                    friendships.add(new Friendship(idFriendship, id, idFriend,date.toLocalDateTime(), Status.valueOf(status)));
                }
            }

            return friendships;

        } catch (SQLException sq) {
            System.out.println(sq.getMessage());
        }
        return friendships;
    }



}
