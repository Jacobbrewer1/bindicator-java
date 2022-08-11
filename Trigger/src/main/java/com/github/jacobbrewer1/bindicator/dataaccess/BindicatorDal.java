package com.github.jacobbrewer1.bindicator.dataaccess;

import com.github.jacobbrewer1.bindicator.entities.Run;
import com.github.jacobbrewer1.bindicator.interfaces.IBindicatorDal;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BindicatorDal extends MySqlDal implements IBindicatorDal {

    public BindicatorDal(String schema, String host, String port, String username, String password) throws SQLException {
        super(schema, host, port, username, password);
    }

    @Override
    public Run getNextRun() throws SQLException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        Run run = null;

        try (Connection connection = getConnection(); CallableStatement statement = connection.prepareCall("{ call getPreviousRun() }")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    run = new Run();

                    run.setId(resultSet.getInt("id"));
                    run.setDateRan(formatter.parseDateTime(resultSet.getString("dateran")));

                    run.calculateNextRun();
                }
            }
        }

        return run;
    }

    @Override
    public void saveRun(DateTime dateTime) throws SQLException {
        try (Connection connection = getConnection(); CallableStatement statement = connection.prepareCall("{ call saveRun(?) }")) {
            statement.setString(1, dateTime.toString(dateTimeFormat));

            statement.execute();
        }
    }
}
