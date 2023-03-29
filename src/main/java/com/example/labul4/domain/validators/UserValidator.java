package com.example.labul4.domain.validators;

import com.example.labul4.domain.User;

import java.util.Objects;

public class UserValidator implements Validator<User> {
    /**
     *Erori pentru clasa User
     * @param entity : entitate de tip User
     * @throws InvalidName : arunca erori de validate a obiectului
     * @throws InvalidId : arunca erori de validate a obiectului
     */
    @Override
    public void validate(User entity) throws InvalidName, InvalidId {
        validateFirstName(entity.getFirstName());
        validateLastName(entity.getLastName());
        validateUserName(entity.getUsername());
        if(Objects.equals(entity.getId(), "")) {
            throw new InvalidId("Id-ul nu poate fi null. Reintroduceti id: ");
        }
    }
    /**
     *Valideaza parametrul prenumelui
     * @param firstName : prenumele
     * @throws ValidationException : arunca erori de validate a obiectului
     */
    private void validateFirstName(String firstName) throws InvalidName{
        if(firstName == null)
            throw new ValidationException("Prenumele nu poate fi null!");
        if(firstName.length() >= 15)
            throw new ValidationException("Prenumele e prea lung!");
        if(firstName.isEmpty())
            throw new ValidationException("Prenumele nu trebuie sa fie gol!");
    }

    /**
     *Valideaza parametrul numelui
     * @param lastName : numele
     * @throws ValidationException : arunca erori de validate a obiectului
     */

    private void validateLastName(String lastName) throws InvalidName {
        if(lastName == null)
            throw new ValidationException("Numele trebuie sa fie diferit de null!");
        if(lastName.length() >= 15)
            throw new ValidationException("Numele e prea lung!");
        if(lastName.isEmpty())
            throw new ValidationException("Numele nu trebuie sa fie gol!");

    }

    /**
     * Valideaza parametrul username-ului
     * @param userName : username
     * @throws ValidationException : arunca erori de validate a obiectului
     */

    private void validateUserName(String userName) throws InvalidName {
        if(userName == null)
            throw new ValidationException("Username trebuie sa fie diferit de null!");
        if(userName.length() >= 15)
            throw new ValidationException("Username e prea lung!");
        if(userName.isEmpty())
            throw new ValidationException("Username nu trebuie sa fie gol!");

    }
}
