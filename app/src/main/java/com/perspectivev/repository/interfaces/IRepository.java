package com.perspectivev.repository.interfaces;


import java.util.ArrayList;

public interface IRepository<T> {
    void create(T item);
    void delete(T item);
    ArrayList<T> getAll();
    void update(T item);
    T getById(int id);
    T getFirst();
}
