package com.example.labul4.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    Long idUser1;
    Long idUser2;
    LocalDateTime date;
    Status status;

    /**
     * Constructor pentru prietenie.
     * @param id id-ul prieteniei
     * @param idUser1 primul user din prietenie
     * @param idUser2 al doilea user din prietenie
     *
     */
    public Friendship(Long id, Long idUser1, Long idUser2, Status status) {
        super.setId(id);
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.date = LocalDateTime.now();
        this.status = status;
    }

    public Friendship(Long idUser1, Long idUser2, LocalDateTime dateTime, Status status){
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.status = status;
    }

    /**
     * Constructor pentru prietenie.
     * @param id id-ul prieteniei
     * @param idUser1 primul user din prietenie
     * @param idUser2 al doilea user din prietenie
     * @param date data prieteniei
     */
    public Friendship(Long id, Long idUser1, Long idUser2, LocalDateTime date, Status status){
        super.setId(id);
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.date = date;
        this.status = status;
    }

    /**
     * Getter pentru primul user din prietenie.
     * @return primul user din prietenie
     */
    public Long getIdUser1(){
        return this.idUser1;
    }


    /**
     * Getter pentru al doilea user din prietenie.
     * @return al doilea user din prietenie
     */
    public Long getIdUser2(){
        return this.idUser2;
    }


    /**
     * Getter pentru data prieteniei.
     *
     * @return data prieteniei
     */
    public LocalDateTime getDate(){return this.date;}

    public Status getStatus(){return this.status;}

    /**
     * Setter pentru primul user din prietenie.
     * @param id id-ul primului user din prietenie
     */
    public void setIdUser1(Long id){
        this.idUser1 = id;
    }

    /**
     * Setter pentru al doilea user din prietenie.
     * @param id id-ul celui de al doilea user din prietenie
     */
    public void setIdUser2(Long id){
        this.idUser2 = id;
    }

    /**
     * Setter pentru data prieteniei.
     * @param date data prieteniei
     */
    public void setDate(LocalDateTime date){this.date = date;}


    /**
     * Setter pentru statusul prieteniei.
     * @param status statusul prieteniei
     */
    public void setStatus(Status status){this.status = status;}


    /**
     * Supraincarcarea metodei equals.
     * @param obj
     * @return true daca cele doua prietenii sunt egale, fals altfel
     */
    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(!(obj instanceof Friendship))
            return false;
        Friendship friendship = (Friendship) obj;

        return Objects.equals(getIdUser1(), friendship.getIdUser1()) && Objects.equals(getIdUser2(), friendship.getIdUser2());
    }

    /**
     * Supraincarcarea metodei toString.
     * @return un string cu detalii despre prietenie
     */
    @Override
    public String toString(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return getId() + " "+ getIdUser1() + " " + getIdUser2() + " " + getDate().format(format) +" "+getStatus().toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser1, idUser2,date);
    }
}
