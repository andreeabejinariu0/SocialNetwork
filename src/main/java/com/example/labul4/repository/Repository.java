package com.example.labul4.repository;

import com.example.labul4.domain.Entity;
import com.example.labul4.domain.validators.InvalidId;
import com.example.labul4.domain.validators.InvalidName;
import com.example.labul4.domain.validators.ValidationException;

import java.sql.SQLException;
import java.util.Vector;

/**
 * CRUD operations repository interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */

public interface Repository<ID,E extends Entity<ID>> {
    /**
     * @param id id-ul entității care urmează să fie returnată
     *           id-ul nu trebuie să fie null
     * @return entitatea cu id-ul specificat
     * sau null - dacă nu există nicio entitate cu id-ul dat
     * @throws IllegalArgumentException dacă id-ul este null.
     */
    E findOne(ID id) throws SQLException;

    /**
     * @return toate entitatile
     */
    Iterable<E> findAll();

    /**
     * @param entity entitatea nu trebuie să fie nulă
     * @return null- dacă entitatea dată este salvată
     * altfel returnează entitatea (id-ul există deja)
     * @throws ValidationException      dacă entitatea nu este valida
     * @throws IllegalArgumentException dacă entitatea dată este nulă.
     */
    E save(E entity) throws InvalidName, InvalidId, SQLException;

    /**
     * sterge entitatea cu id-ul specificat
     *
     * @param id id-ul nu trebuie să fie null
     * @return entitatea stearsa sau null dacă nu există nicio entitate cu id-ul dat
     * @throws IllegalArgumentException dacă id-ul dat este null.
     */
    E delete(ID id) throws SQLException;

    /**
     * @param entity entitatea nu trebuie să fie nulă
     * @return nul - dacă entitatea este actualizată,
     * în caz contrar returnează entitatea - (de exemplu, id nu există).
     * @throws IllegalArgumentException dacă entitatea dată este nulă.
     * @throws ValidationException      dacă entitatea nu este valida
     */
    E update(E entity) throws InvalidName, InvalidId, SQLException;

    default E find(String parameter){
        return null;
    }

    default Vector<E> findReceiver(ID id){
        return null;
    }
    default Vector<E> find(ID id){return null;}

    default Vector<E> findByName(String parameter){return null;}
}
