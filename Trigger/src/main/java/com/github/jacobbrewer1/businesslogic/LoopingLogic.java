package com.github.jacobbrewer1.businesslogic;

import com.github.jacobbrewer1.entities.Run;
import com.github.jacobbrewer1.interfaces.IBindicatorDal;
import com.github.jacobbrewer1.logging.Logging;
import org.joda.time.DateTime;

import java.sql.SQLException;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class LoopingLogic {

    private final Logging logging;

    private final RunLogic runLogic;

    private final IBindicatorDal bindicatorDal;

    public LoopingLogic(Logging logging, RunLogic runLogic, IBindicatorDal bindicatorDal) {
        this.logging = logging;
        this.runLogic = runLogic;
        this.bindicatorDal = bindicatorDal;
    }

    public void loop() throws InterruptedException {
        while (true) {
            try {
                Run run = bindicatorDal.getNextRun();

                if (!run.getNextRun().isBeforeNow()) {
                    long releaseTime = run.getNextRun().getMillis() - DateTime.now().getMillis();

                    if (releaseTime > 0) {
                        logging.logInfo(String.format("Waiting until %s to run", run.getNextRun().toString("dd/MM/yyyy HH:mm:ss")));

                        Thread.sleep(releaseTime);
                    }
                }

                try {
                    runLogic.execute();

                    bindicatorDal.saveRun(run.getNextRun());
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

                bindicatorDal.saveRun(run.getNextRun());
            } catch (SQLException exception) {
                exception.printStackTrace();
                Thread.sleep(1000);
            }
        }
    }
}
