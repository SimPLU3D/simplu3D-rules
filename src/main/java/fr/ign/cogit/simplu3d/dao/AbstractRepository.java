package fr.ign.cogit.simplu3d.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbstractRepository<T> implements Repository<T> {
    protected final List<T> items;
    public int count(){
        return items.size();
    }
    public Collection<T> findAll() {
        return items;
    }
    public AbstractRepository(Collection<T> items) {
        this.items = new ArrayList<>(items);
    }
}
