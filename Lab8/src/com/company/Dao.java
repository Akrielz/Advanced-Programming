package com.company;

import java.sql.SQLException;
import java.util.List;

public interface Dao <Type>{

    List<Type> getAll();

    //void create(Type object) throws SQLException, ClassNotFoundException;
    Type get(int id);
}
