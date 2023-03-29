package com.example.labul4.service;

import com.example.labul4.domain.Friendship;
import com.example.labul4.domain.FriendshipDTO;
import com.example.labul4.domain.Status;
import com.example.labul4.domain.User;
import com.example.labul4.domain.validators.InvalidId;
import com.example.labul4.domain.validators.InvalidName;
import com.example.labul4.repository.Repository;
import com.example.labul4.utils.ChangeEventType;
import com.example.labul4.utils.FriendshipChangeEvent;
import com.example.labul4.utils.observer.Observable;
import com.example.labul4.utils.observer.Observer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.labul4.domain.Status.Pending;

public class FriendshipService implements Observable<FriendshipChangeEvent> {
    Repository<Long, Friendship> friendshipRepo;
    Repository<Long, User> userRepo;
    private final List<Observer<FriendshipChangeEvent>> observers = new ArrayList<>();

    /**
     * Constructor pentru service-ul de prietenii.
     * @param fRepo repository-ul de prietenii
     * @param uRepo repository-ul de useri
     */
    public FriendshipService(Repository<Long, Friendship> fRepo, Repository<Long, User> uRepo) {
        this.friendshipRepo = fRepo;
        this.userRepo = uRepo;
    }

    /**
     * Adauga o prietenie in repository-ul de prietenii.
     * @param id1 id-ul primului user
     * @param id2 id-ul celui de al doilea user
     */
    public void addFriendship(Long id1, Long id2) throws InvalidId, SQLException, InvalidName {
        User u1 = userRepo.findOne(id1);
        User u2 = userRepo.findOne(id2);
        LocalDateTime dateTime = LocalDateTime.now();

        if(u1 != null)
            if(u2 != null) {
                friendshipRepo.save(new Friendship(u1.getId(), u2.getId(),dateTime, Pending));

                u1.addFriend(u2.getId());
                u2.addFriend(u1.getId());
            }
    }

    /**
     * Sterge o prietenie in repository-ul de prietenii.
     *@param idFriendship id-ul prieteniei
     * @param id1 id-ul primului user
     * @param id2 id-ul celui de al doilea user
     * @throws InvalidId
     */
    public void deleteFriendship(Long idFriendship, Long id1, Long id2) throws InvalidId, SQLException {
        Friendship friendship = friendshipRepo.findOne(idFriendship);
        if(friendship == null)
            throw new InvalidId("Nu exista id-ul prieteniei!");
        if(userRepo.findOne(id1) != null)
            if(userRepo.findOne(id2) != null) {
                User user1 = userRepo.findOne(id1);
                User user2 = userRepo.findOne(id2);
                friendshipRepo.delete(friendship.getId());

                user1.deleteFriend(user2);
                user2.deleteFriend(user1);
            }
    }

    /**
     * Afiseaza prieteniile din repository-ul de prietenii.
     */
    public void showFriendships() throws SQLException {
        User u1,u2;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for(Friendship f: friendshipRepo.findAll()) {
            u1 = userRepo.findOne(f.getIdUser1());
            u2 = userRepo.findOne(f.getIdUser2());
            System.out.println("Prietenia id: " + f.getId() + " USER1: " + u1 + " USER2: " + u2 + " Data: " + f.getDate().format(format));
        }

    }

    /**
     * Sterge toate prieteniile care contin un user dat.
     * @param u user-ul dat
     * @throws InvalidId
     */
    public void deleteFriendshipsWithUser(User u) throws InvalidId, SQLException {
        Friendship friendship;
        for(Friendship f: friendshipRepo.findAll())
        {
            if(Objects.equals(f.getIdUser1() , u.getId()) || Objects.equals(f.getIdUser2(), u.getId())) {
                User u1 = userRepo.findOne(f.getIdUser1());
                User u2 = userRepo.findOne(f.getIdUser2());
                deleteFriendship(f.getId(), f.getIdUser1(), f.getIdUser2());
            }

        }
    }

    /**
     * Actualizeaza o prietenie.
     * @param id id-ul prieteniei
     * @param status statusul prieteniei
     */
    public void updateFriendship(Long id, Status status) throws InvalidName, SQLException, InvalidId {
        Friendship f = findFriendship(id);
        f.setStatus(status);
        friendshipRepo.update(f);
        notifyObservers(new FriendshipChangeEvent(ChangeEventType.UPDATE, f));
    }

    /**
     * Getter pentru prietenii.
     * @return prieteniile din repository-ul de prietenii
     */
    public Iterable<Friendship> findAll(){
        return this.friendshipRepo.findAll();
    }


    /**
     * Getter pentru o prietenie.
     * @param id id-ul prieteniei
     * @return prietenia cu id-ul id
     */
    public Friendship findFriendship(Long id) throws SQLException {
        return this.friendshipRepo.findOne(id);
    }

    /**
     * Gaseste prieteniile unui user.
     * @param u user-ul a carui prietenii le cautam
     * @return un vector de useri
     * @throws SQLException
     */
    public List<FriendshipDTO> findFriendshipsOfUser(User u) throws SQLException {
        Vector<Friendship> friendships = new Vector<>();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        List<FriendshipDTO> friendshipDtos = new ArrayList<>();
        friendships = this.friendshipRepo.find(u.getId());
        for(Friendship friendship: friendships) {
            User user = userRepo.findOne(friendship.getIdUser2());
            friendshipDtos.add(new FriendshipDTO(friendship.getId(),user.getId(),user.getFirstName(), user.getLastName(), friendship.getStatus().toString(), friendship.getDate().format(format)));
        }

        return friendshipDtos;

    }

//    public boolean checkIfSender(Long id) throws SQLException {
//        User user = userRepo.findOne(id);
//        Iterable<Friendship> friendships = new Vector<>();
//        friendships = this.friendshipRepo.findReceiver(id);
//        boolean ok = false;
//        for(Friendship friendship: friendships) {
//            if (user.getId().equals(friendship.getIdUser1()))
//                ok= true;
//        }
//        return ok;
//    }

    @Override
    public void addObserver(Observer<FriendshipChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipChangeEvent t) {
        observers.stream().forEach(x-> {
            try {
                x.update(t);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
