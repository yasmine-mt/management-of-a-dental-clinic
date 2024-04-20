package src.service;

import src.exception.FormulaireException;

import java.util.List;

public interface IForms<T> {
    void createForm(T form) throws FormulaireException;

    void updateForm(T form) throws FormulaireException;

    void deleteForm(T form) throws FormulaireException;

    T getFormById(long formId) throws FormulaireException;

    List<T> getAllForms() throws FormulaireException;

    boolean validateForm(T form) throws FormulaireException;
}
