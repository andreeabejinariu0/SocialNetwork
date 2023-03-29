package com.example.labul4;

import com.example.labul4.domain.Friendship;
import com.example.labul4.domain.User;
import com.example.labul4.domain.validators.FriendshipValidator;
import com.example.labul4.domain.validators.InvalidId;
import com.example.labul4.domain.validators.InvalidName;
import com.example.labul4.domain.validators.UserValidator;
import com.example.labul4.repository.FriendshipDbRepository;
import com.example.labul4.repository.Repository;
import com.example.labul4.repository.UserDbRepository;
import com.example.labul4.service.FriendshipService;
import com.example.labul4.service.UserService;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws InvalidName, SQLException, InvalidId, NoSuchAlgorithmException, NoSuchProviderException {
        String username = "postgres";
        String password = "davinciA33";
        String url = "jdbc:postgresql://localhost:5432/network";

        Repository<Long, User> userRepository = new UserDbRepository(url, username, password, new UserValidator());
        Repository<Long, Friendship> friendshipRepository = new FriendshipDbRepository(url, username,
                password, new FriendshipValidator());
        UserService userService = new UserService(userRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository);
        UI UI = new UI(userService, friendshipService);
        UI.runUI();
//
    }
}

