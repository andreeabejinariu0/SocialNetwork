package com.example.labul4.domain.validators;

import com.example.labul4.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {

    /**
     * Erori pentru clasa Friendship
     * @param entity de tip Friendship
     * @throws ValidationException : erori de validare a obiectului
     */
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getIdUser1().equals(entity.getIdUser2()))
            throw new ValidationException("Prietenii nu pot avea acelasi id!");
    }
}
