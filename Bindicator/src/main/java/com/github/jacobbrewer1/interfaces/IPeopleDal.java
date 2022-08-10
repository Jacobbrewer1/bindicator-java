package com.github.jacobbrewer1.interfaces;

import com.github.jacobbrewer1.entities.Person;

import java.sql.SQLException;
import java.util.List;

public interface IPeopleDal {
    List<Person> getPeople() throws SQLException;
}
