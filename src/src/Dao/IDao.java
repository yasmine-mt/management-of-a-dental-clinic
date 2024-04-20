package src.Dao;

import src.entity.Patient;

import java.util.List;
public interface IDao <T,ID> {
    List<T> findAll();
    T findById (ID identifiant);


    T  save(T newElement);
    List<T> saveAll(T... elements);
    boolean update(T newValueElement);

    boolean deleteById(Long identifiant);

    boolean delete(T element);
    boolean deleteById(ID identifiant);
}
