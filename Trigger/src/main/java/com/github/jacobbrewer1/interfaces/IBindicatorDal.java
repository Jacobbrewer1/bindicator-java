package com.github.jacobbrewer1.interfaces;

import com.github.jacobbrewer1.entities.Run;
import org.joda.time.DateTime;

import java.sql.SQLException;

public interface IBindicatorDal {
    Run getNextRun() throws SQLException;
    void saveRun(DateTime dateTime) throws SQLException;
}
