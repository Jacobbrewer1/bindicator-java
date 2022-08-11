package com.github.jacobbrewer1.bindicator;

import com.github.jacobbrewer1.bindicator.businesslogic.LoopingLogic;
import com.github.jacobbrewer1.bindicator.businesslogic.RunLogic;
import com.github.jacobbrewer1.bindicator.comms.TriggerAllComms;
import com.github.jacobbrewer1.bindicator.dataaccess.BindicatorDal;
import com.github.jacobbrewer1.bindicator.interfaces.IBindicatorDal;
import com.github.jacobbrewer1.bindicator.logging.Logging;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class Trigger {

    private static final Logging logging = new Logging(Logger.getLogger(Trigger.class.getName()));

    private final LoopingLogic loopingLogic;
    private final RunLogic runLogic;

    private final TriggerAllComms triggerAllComms;

    private final IBindicatorDal bindicatorDal;

    public static void main(String[] args) throws SQLException, InterruptedException {
        logging.logInfo("Trigger application start");

        if (args.length < 1) {
            logging.logInfo("No path to properties file provided");
            throw new RuntimeException("No path to properties file provided.");
        }

        String path = args[0];
        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            logging.logInfo("Unable to parse properties file");
            throw new RuntimeException("Unable to parse properties file", e);
        }

        Trigger trigger = new Trigger(properties);
        trigger.execute();
    }

    private Trigger(Properties properties) throws SQLException {
        this.bindicatorDal = new BindicatorDal(properties.getProperty("schema"), properties.getProperty("host"), properties.getProperty("port"),
                properties.getProperty("username"), properties.getProperty("dbPassword"));

        this.triggerAllComms = new TriggerAllComms(properties.getProperty("serverLocation", "localhost"),
                properties.getProperty("serverPort", "50051"));

        this.runLogic = new RunLogic(logging, triggerAllComms);
        this.loopingLogic = new LoopingLogic(logging, runLogic, bindicatorDal);
    }

    private void execute() throws InterruptedException {
        loopingLogic.loop();
    }
}
