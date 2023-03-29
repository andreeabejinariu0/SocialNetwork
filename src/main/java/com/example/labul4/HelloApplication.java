package com.example.labul4;

import com.example.labul4.controller.LoginController;
import com.example.labul4.domain.User;
import com.example.labul4.domain.validators.UserValidator;
import com.example.labul4.repository.Repository;
import com.example.labul4.repository.UserDbRepository;
import com.example.labul4.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String username = "postgres";
        String password = "davinciA33";
        String url = "jdbc:postgresql://localhost:5432/network";
        Repository<Long, User> userRepository = new UserDbRepository(url, username, password, new UserValidator());
        UserService userService = new UserService(userRepository);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 550, 400);
        LoginController loginController = fxmlLoader.getController();
        loginController.setUserService(userService);
        stage.setScene(scene);
        stage.setTitle("Log in!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}