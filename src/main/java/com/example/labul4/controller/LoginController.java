package com.example.labul4.controller;

import com.example.labul4.HelloApplication;
import com.example.labul4.domain.User;
import com.example.labul4.domain.validators.FriendshipValidator;
import com.example.labul4.domain.validators.UserValidator;
import com.example.labul4.repository.FriendshipDbRepository;
import com.example.labul4.repository.UserDbRepository;
import com.example.labul4.service.FriendshipService;
import com.example.labul4.service.UserService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {
    public Button loginButton;
    public TextField usernameField;
    public TextField passwordField;
    UserService userService;

    public void setUserService(UserService userService){
        this.userService = userService;
    }

    public void login() throws IOException {
        System.out.println("Clicked!!");
        String username = usernameField.getText();
        User user = userService.findUserByUsername(usernameField.getText());
        if(username == null || user == null)
        {
            MessageAlert.showErrorMessage(null, "Username gresit!");
        }

        if(user != null){
            String checkpassword = String.valueOf(passwordField.getText());
            try{
                if(Objects.equals(checkpassword, user.getPassword())) {
                    URL url = HelloApplication.class.getResource("views/UserView.fxml");
                    FXMLLoader loader = new FXMLLoader(url);
                    AnchorPane root = loader.load();

                    UserController controller = loader.getController();
                    controller.setUserService(
                            new UserService(
                                    new UserDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "davinciA33", new UserValidator())),
                            new FriendshipService(new
                                    FriendshipDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "davinciA33", new FriendshipValidator()) ,
                                    new UserDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "davinciA33", new UserValidator())),
                            user);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root, 600.0, 400.0));
                    stage.show();
                    stage.setTitle("Social Network!");
                    Stage thisStage = (Stage) loginButton.getScene().getWindow();
                    thisStage.close();


                }
                else
                {
                    MessageAlert.showErrorMessage(null, "Parola gresita!");
                }
            } catch(IOException io){
                System.out.println(io.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
        }
        }

    }

}
