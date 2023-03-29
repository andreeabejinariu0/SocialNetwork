package com.example.labul4.service;

import com.example.labul4.domain.User;
import com.example.labul4.domain.validators.InvalidId;
import com.example.labul4.domain.validators.InvalidName;
import com.example.labul4.repository.Repository;
import com.example.labul4.utils.ChangeEventType;
import com.example.labul4.utils.UserChangeEvent;
import com.example.labul4.utils.observer.Observable;
import com.example.labul4.utils.observer.Observer;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


public class UserService implements Observable<UserChangeEvent> {
    Repository<Long, User> userRepo;
    private final List<Observer<UserChangeEvent>>  observers=new ArrayList<>();

    /**
     * Constructor pentru service-ul de useri.
     *
     * @param userRepo repository-ul pentru useri
     */
    public UserService(Repository<Long, User> userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Adauga un user in repository.
     *
     * @param firstName prenumele user-ului
     * @param lastName  numele user-ului
     * @param password parola user-ului
     * @param username username-ul user-ului
     */
    public void addUser(String firstName, String lastName,  String username, String password) throws InvalidId, InvalidName, SQLException,  NoSuchAlgorithmException, NoSuchProviderException {

        User user = new User(firstName, lastName, username, password);
        userRepo.save(user);

    }

    /**
     * Adauga un prieten la lista de prieteni a user-ului.
     * @param user user-ul la care adaugam prietenul
     * @param idFriend prietenul pe care il adaugam
     */
    public void addFriend(User user, Long idFriend) throws SQLException {
        if(idFriend >= 0) {
            user.addFriend(idFriend);
        }
        UserChangeEvent userChangeEvent = new UserChangeEvent(ChangeEventType.ADD, userRepo.findOne(idFriend));
        notifyObservers(userChangeEvent);
    }

    /**
     * Sterge un prieten din lista de prieteni a unui user.
     * @param user user-ul a carui prieten il stergem
     * @param friend prietenul pe care il stergem
     */
    public void deleteFriend(User user, User friend){
        user.deleteFriend(friend.getId());
        UserChangeEvent userChangeEvent = new UserChangeEvent(ChangeEventType.DELETE, friend);
        notifyObservers(userChangeEvent);
    }

    /**
     * Sterge un user din baza de date.
     * @param id id-ul user-ului pe care il stergem
     * @throws InvalidId
     */
    public void deleteUser(Long id) throws InvalidId, FileNotFoundException, SQLException {
        User u = userRepo.findOne(id);
        if (u == null)
            throw new InvalidId("Nu exista id-ul!");
        userRepo.delete(u.getId());

    }
    /**
     * Afiseaza userii din baza de date.
     */
    public void showUsers() {
        for (User u : userRepo.findAll())
            System.out.println(u);

    }

    /**
     * Gaseste un user in daza de date.
     *
     * @param id id-ul user-ului pe care il cautam
     * @return user-ul cu id-ul dat sau null daca nu exista
     */
    public User findUser(Long id) throws SQLException {
        return this.userRepo.findOne(id);
    }

    /**
     * Getter pentru userii din baza de date.
     *
     * @return toti userii din baza de date
     */
    public Iterable<User> findAll() {
        return this.userRepo.findAll();
    }

    /**
     * Gaseste un user din baza de date dupa username.
     * @param username username-ul user-ului pe care il cautam
     * @return user-ul cu username-ul dat daca exista, null in caz contrar
     */
    public User findUserByUsername(String username){
        return this.userRepo.find(username);
    }

    /**
     * Cauta userii care au numele dat.
     * @param name numele dat
     * @return un vector cu userii gasiti
     */
    public Vector<User> getUsersByName(String name){
        return userRepo.findByName(name);
    }
    /**
     * Getter pentru ultimul utilizator adaugat.
     * @return ultimul utilizator adaugat
     */

    public User getLastUser(){
        Iterator<User> users = findAll().iterator();
        User u = null;
        while(users.hasNext()){
            u = users.next();
        }
        return u;
    }


    @Override
    public void addObserver(Observer<UserChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserChangeEvent t) {

        observers.stream().forEach(x-> {
            try {
                x.update(t);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
