package com.example.labul4.domain;



public class FriendshipDTO {
    private Long idFriendship;
    private Long idFriend;
    private String firstName;
    private String lastName;
    private String status;
    private String requestDate;

    public FriendshipDTO(Long id,Long idFriend ,String firstName, String lastName, String status, String requestDate){
        this.idFriendship = id;
        this.idFriend = idFriend;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.requestDate = requestDate;
    }

    public void setIdFriend(Long idFriend) {
        this.idFriend = idFriend;
    }

    public Long getIdFriend(){
        return this.idFriend;
    }

    public Long getIdFriendship() {
        return idFriendship;
    }



    public void setIdFriendship(Long id) {
        this.idFriendship = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestDate(){
        return this.requestDate;
    }

    public void setRequestDate(String date){
        this.requestDate = date;
    }

}
