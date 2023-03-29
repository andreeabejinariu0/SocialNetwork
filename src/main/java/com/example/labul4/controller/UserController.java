package com.example.labul4.controller;

import com.example.labul4.domain.Friendship;
import com.example.labul4.domain.FriendshipDTO;
import com.example.labul4.domain.Status;
import com.example.labul4.domain.User;
import com.example.labul4.domain.validators.InvalidId;
import com.example.labul4.domain.validators.InvalidName;
import com.example.labul4.service.FriendshipService;
import com.example.labul4.service.UserService;
import com.example.labul4.utils.UserChangeEvent;
import com.example.labul4.utils.observer.Observable;
import com.example.labul4.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class UserController implements Observer<UserChangeEvent> {
    public TableView<User> friendsTableView;

    public TableColumn<User, String> friendsTableViewFirstName;
    public TableColumn<User, String> friendsTableViewLastName;
    public Button friendsTableViewDeleteButton;

    public TableView<FriendshipDTO> requestsTableView;
    public TableColumn<FriendshipDTO, String> requestsTableViewFirstName;
    public TableColumn<FriendshipDTO, String> requestsTableViewLastName;
    public TableColumn<FriendshipDTO, String> requestsTableViewStatus;
    public TableColumn<FriendshipDTO, String> requestsTableViewDate;
    public TableView<User> searchTableView;
    public TableColumn<User, String> searchTableViewFirstName;
    public TableColumn<User, String> searchTableViewLastName;
    public Button searchTableViewAddButton;
    public Button requestsTableViewAcceptButton;
    public Button requestsTableViewDeclineButton;
    public TextField searchTableViewTextField;

    ObservableList<User> modelFriends = FXCollections.observableArrayList();
    ObservableList<FriendshipDTO> modelRequests = FXCollections.observableArrayList();
    ObservableList<User> modelSearch = FXCollections.observableArrayList();

    //model - obiectul care reprezinta datele in program
    UserService userService;
    FriendshipService friendshipService;
    User user;

    public void setUserService(UserService userService, FriendshipService friendshipService, User user) throws SQLException{
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.user = user;
        userService.addObserver(this);
        initModelFriendships();
        initModelRequests();

    }

    @FXML
    public void initialize(){
        friendsTableViewFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        friendsTableViewLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        friendsTableView.setItems(modelFriends);
        requestsTableViewFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        requestsTableViewLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        requestsTableViewStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        requestsTableViewDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        requestsTableView.setItems(modelRequests);
        searchTableViewFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        searchTableViewLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        searchTableView.setItems(modelSearch);
    }

    private void initModelFriendships() throws SQLException{
        Vector<Long> friends = this.user.getFriends();
        List<User> users = new ArrayList<>();
        for( Long id :friends)
            users.add(userService.findUser(id));
        modelFriends.setAll(users);
    }

    public void initModelSearch(String name){
        Vector<User> users = userService.getUsersByName(name);
        modelSearch.setAll(users);
    }

    public void initModelRequests() throws SQLException{
        List<FriendshipDTO> friendships = friendshipService.findFriendshipsOfUser(this.user);
        modelRequests.setAll(friendships);
    }

    public void handleDeleteFriend() throws SQLException, InvalidId {
        User friend = friendsTableView.getSelectionModel().getSelectedItem();
        Long idFriend = friend.getId();
        if(friend != null){
            userService.deleteFriend(this.user, friend);
            List<FriendshipDTO> friendshipDTOS = friendshipService.findFriendshipsOfUser(this.user).stream().filter(x-> Objects.equals(x.getIdFriend(), idFriend)).toList();
            friendshipService.deleteFriendship(friendshipDTOS.get(0).getIdFriendship(), idFriend, user.getId());
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Confirmare","Prieten sters!");
            initModelRequests();
        }
    }

    public void handleAcceptRequests() throws InvalidName, SQLException, InvalidId {
        FriendshipDTO friend = requestsTableView.getSelectionModel().getSelectedItem();
        if(friend!= null)
        {
            if(Status.valueOf(friend.getStatus()) == Status.Pending ){
                friendshipService.updateFriendship(friend.getIdFriendship(), Status.Accepted);
                userService.addFriend(user, friend.getIdFriend());
            }
            else MessageAlert.showErrorMessage(null, "Statusul prieteniei a fost setat deja!");
        }
    }

    public void handleDeclineRequest() throws InvalidName, SQLException, InvalidId {

        FriendshipDTO friend = requestsTableView.getSelectionModel().getSelectedItem();
        if(friend!=null){
            if(Status.valueOf(friend.getStatus()) == Status.Pending) {
                friendshipService.updateFriendship(friend.getIdFriendship(), Status.Denied);
                userService.addFriend(user, -1L);
            }else
                MessageAlert.showErrorMessage(null, "Statusul prieteniei a fost setat deja!");

        }
    }
    @Override
    public void update( UserChangeEvent userChangeEvent) throws SQLException {
        initModelFriendships();
        initModelRequests();
    }

    public void handleSearchUser(){
        String name = searchTableViewTextField.getText();
        initModelSearch(name);
    }

    public void addNewFriend() throws SQLException, InvalidName, InvalidId {
        User friendToAdd = searchTableView.getSelectionModel().getSelectedItem();
        List<FriendshipDTO> friends = friendshipService.findFriendshipsOfUser(this.user);
        boolean ok = false;
        for(FriendshipDTO friendshipDTO: friends)
            if(Objects.equals(friendshipDTO.getIdFriend(), friendToAdd.getId())) {
                ok = true;
                break;
            }
        if(friendToAdd.getId().equals(this.user.getId()))
            MessageAlert.showErrorMessage(null, "Nu poti sa iti trimititi singur cerere, hihi!");

        if(!ok){
            friendshipService.addFriendship(user.getId(), friendToAdd.getId());
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Confirmare", "Cerere trimisa!");
            initModelFriendships();
            initModelRequests();
        }
        else
            MessageAlert.showErrorMessage(null, "Sunteti deja prieten/a cu aceasta persoana!");
    }


}
