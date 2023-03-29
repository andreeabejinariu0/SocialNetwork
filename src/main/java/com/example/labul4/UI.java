package com.example.labul4;

import com.example.labul4.domain.Friendship;
import com.example.labul4.domain.User;
import com.example.labul4.domain.validators.InvalidId;
import com.example.labul4.domain.validators.InvalidName;
import com.example.labul4.service.FriendshipService;
import com.example.labul4.service.UserService;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class UI {
    UserService userService;
    FriendshipService friendshipService;

    public UI(UserService us, FriendshipService fs){
        this.userService = us;
        this.friendshipService = fs;
    }

    public void showMenu(){
        System.out.println("1. Adauga un utilizator.");
        System.out.println("2.. Adauga o prietenie.");
        System.out.println("3. Sterge un utilizator. ");
        System.out.println("4. Sterge o prietenie.");
        System.out.println("5. Afiseaza toti utilizatorii.");
        System.out.println("6. Afiseaza toate prieteniile existente.");
        System.out.println("0. Exit");
    }

    public void runUI() throws InvalidName, InvalidId, SQLException {
        String optiune;
        boolean ok = true;
        Scanner in = new Scanner(System.in);

        while(ok){
            System.out.println();
            showMenu();
            System.out.println("Dati optiunea: ");
            optiune = in.nextLine();

            if(Objects.equals(optiune, "1")) {
                try{
                    System.out.println("Dati prenume: ");
                    String firstName = in.nextLine();
                    System.out.println("Dati nume: ");
                    String lastName = in.nextLine();
                    System.out.println("Dati username: ");
                    String username = in.nextLine();
                    System.out.println("Dati parola: ");
                    String password = in.nextLine();

                    userService.addUser(firstName, lastName, username, password);
                    System.out.println("User adaugat!");

                }catch (InvalidId ex){
                    System.out.println(ex);
                }catch(InvalidName iN){
                    System.out.println(iN);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchProviderException e) {
                    throw new RuntimeException(e);
                }
            }

            if(Objects.equals(optiune, "2")){
                try{

                    System.out.println("Dati id-ul primului user: ");
                    Long id1 = Long.parseLong(in.nextLine());
                    System.out.println("Dati id-ul celui de al doilea user: ");
                    Long id2 = Long.parseLong(in.nextLine());
                    User user1 = userService.findUser(id1);
                    User user2 = userService.findUser(id2);

                    if((user1 != null) && (user2 != null)){
                        friendshipService.addFriendship(user1.getId(), user2.getId());
                        System.out.println("Prietenie adaugata!");
                    }
                }catch(InvalidId | SQLException ex){
                    System.out.println(ex.getMessage());

                }
            }

            if(Objects.equals(optiune, "3"))
            {
                try{
                    System.out.println("Dati id-ul userului pe care vreti sa il stergeti: ");
                    Long id3 = Long.parseLong(in.nextLine());
                    User user = userService.findUser(id3);
                    if(user!= null){
                        friendshipService.deleteFriendshipsWithUser(user);
                        userService.deleteUser(id3);
                        System.out.println("User sters!");

                    }
                }catch (InvalidId id) {
                    System.out.println(id.getMessage());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            if( Objects.equals(optiune, "4")){
                try {
                    System.out.println("Dati id-ul prieteniei: ");
                    Long idFriendship = Long.parseLong(in.nextLine());
                    Friendship friendship = friendshipService.findFriendship(idFriendship);

                    if (friendship != null) {
                        User user1 = userService.findUser(friendship.getIdUser1());
                        User user2 = userService.findUser(friendship.getIdUser2());
                        if ((user1 != null) && (user2 != null)) {
                            friendshipService.deleteFriendship(idFriendship, user1.getId(), user2.getId());
                            System.out.println("Prietenie stearsa!");

                        }
                    }
                }catch (InvalidId | SQLException invalidId){
                    System.out.println(invalidId.getMessage());
                }

            }

            if (Objects.equals(optiune, "5")){
                if(userService.findAll().iterator().hasNext()){
                    System.out.println("Toti userii sunt : ");
                    userService.showUsers();
                }
                else
                    System.out.println("Nu exista useri!");
            }

            if(Objects.equals(optiune, "6")){
                if(friendshipService.findAll().iterator().hasNext()) {
                    System.out.println("Toate prieteniile sunt: ");
                    friendshipService.showFriendships();
                }
                else
                    System.out.println("Nu exista prietenii!");
            }

            if(Objects.equals(optiune, "0"))
                ok = false;

        }

    }

}
