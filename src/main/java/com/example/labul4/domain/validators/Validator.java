package com.example.labul4.domain.validators;

public interface Validator<T> {
    /**
     * Valideaza o entitate.
     * @param entity entitatea pe care o validam
     * @throws InvalidId daca id-ul este invalid
     * @throws InvalidName daca numele este invalid
     */
    void validate(T entity) throws InvalidId, InvalidName;
}
