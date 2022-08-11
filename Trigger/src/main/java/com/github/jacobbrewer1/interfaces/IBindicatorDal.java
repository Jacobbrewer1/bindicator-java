package com.github.jacobbrewer1.bindicator.interfaces;

import com.github.jacobbrewer1.bindicator.entities.Run;
import org.joda.time.DateTime;

import java.sql.SQLException;

public interface IBindicatorDal {
    Run getNextRun() throws SQLException;
    void saveRun(DateTime dateTime) throws SQLException;
}
