package com.github.jacobbrewer1.dataacess;

import com.github.jacobbrewer1.entities.Person;
import com.github.jacobbrewer1.interfaces.IPeopleDal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PeopleDal extends MySqlDal implements IPeopleDal {

    public PeopleDal(String schema, String host, String port, String username, String password) throws SQLException {
        super(schema, host, port, username, password);
    }

    @Override
    public List<Person> getPeople() throws SQLException {
        List<Person> people = new ArrayList<>();

        try (Connection connection = getConnection(); CallableStatement statement = connection.prepareCall("{ call getPeople() }")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Person person = new Person();

                    person.setId(resultSet.getInt("id"));
                    person.setName(resultSet.getString("name"));
                    person.setEmail(resultSet.getString("email"));
                    person.setUprn(resultSet.getLong("uprn"));

                    people.add(person);
                }
            }
        }

        return people;
    }
}
