package com.example.labul4.domain;

import java.util.Objects;
import java.util.Vector;

public class User extends Entity<Long>{
    String firstName;
    String lastName;
    String userName;
    String password;
    Vector<Long> friends;

    /**
     *Constructor pentru User.
     * @param id id-ul user-ului
     * @param firstName prenumele user-ului
     * @param lastName numele user-ului
     * @param username username-ul user-ului
     * @param password parola user-ului
     */

    public User(Long id, String firstName, String lastName, String username, String password) {
        super.setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = username;
        this.password = password;
        this.friends = new Vector<Long>();
    }

    /**
     * Constructor pentru User.
     * @param firstName prenumele user-ului
     * @param lastName numele user-ului
     * @param username username-ul user-ului
     * @param password parola user-ului
     */

    public User(String firstName, String lastName, String username,  String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = username;
        this.password = password;
        this.friends = new Vector<>();
    }

    /**
     * Constructor pentru User.
     * @param firstName prenumele user-ului
     * @param lastName numele user-ului
     * @param username username-ul user-ului
     * @param password parola user-ului
     * @param friends id-urile prietenilor user-ului
     */
    public User(String firstName, String lastName, String username, String password,  Vector<Long> friends){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = username;
        this.password = password;
        this.friends = new Vector<>(friends);
    }

    /**
     * Getter pentru prenumele user-ului.
     * @return prenumele user-ului
     */
    public String getFirstName(){
        return this.firstName;
    }

    /**
     * Getter pentru numele user-ului.
     * @return numele user-ului
     */
    public String getLastName(){
        return this.lastName;
    }


    /**
     * Getter pentru parola.
     * @return parola user-ului
     */
    public String getPassword(){ return this.password;}


    /**
     * Getter pentru username-ul user-ului.
     * @return username-ul user-ului
     */
    public String getUsername(){return this.userName;}

    /**
     * Getter pentru prietenii user-ului.
     * @return prietenii user-ului
     */

    public Vector<Long> getFriends(){
        return this.friends;
    }


    /**
     * Setter pentru prenumele user-ului.
     * @param fName prenumele user-ului
     */
    public void setFirstName(String fName){
        this.firstName = fName;
    }

    /**
     * Setter pentru numele user-ului.
     * @param lName numele user-ului
     */
    public void setLastName(String lName){
        this.lastName = lName;
    }

    /**
     * Setter pentru username.
     * @param username username-ul userului
     */
    public void setUsername(String username)
    {this.userName = username;
    }


    /**
     * Setter pentru parola.
     * @param password parola user-ului
     */
    public void setPassword(String password){
        this.password = password;
    }


    /**
     * Adauga un prieten in lista de prieteni a user-ului.
     * @param friend prietenul pe care il adaugam
     */
    public void addFriend(Long friend){
        this.friends.add(friend);
    }

    /**
     * Sterge un prieten din lista de prieteni.
     * @param friend prietenul pe care il stergem
     */
    public void deleteFriend(Long friend) {
        this.friends.remove(friend);
    }

    /**
     * Setter pentru prietenii user-ului.
     * @param friends id-urile prietenilor user-ului
     */
    public void setFriends(Vector<Long> friends){ this.friends = friends;}
    /**
     * Sterge un prieten din lista de prieteni a user-ului.
     * @param friend prietenul pe care il stergem
     */

    public void deleteFriend(User friend){
        int index = -1;
        for(int i = 0; i < this.friends.size(); i++)
        {
            if(Objects.equals(this.friends.elementAt(i), friend.getId())) {
                index = i;
                break;
            }
        }
        if(index != -1)
            this.friends.remove(index);
    }

    /**
     * Supraincarcarea metodei equals.
     * @param obj obiectul cu care verificam egalitatea
     * @return true daca obiectele sunt egale, false daca nu sunt egale
     */
    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(!(obj instanceof User))
            return false;
        User user = (User)obj;
        return Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.lastName) && Objects.equals(getId(), user.getId());

    }

    /**
     * Supraincarcarea metodei toString.
     * @return un string cu detalii despre user
     */
    @Override
    public String toString(){
        return "Utilizator{" + "ID-ul = "+ getId()+
                " Prenumele = '" + firstName + '\'' +
                ", Numele = '" + lastName + '\'' +
                ", Username-ul = '" + userName +
                "'}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
