package com.github.jacobbrewer1.bindicator.businesslogic;

import com.github.jacobbrewer1.bindicator.entities.Run;
import com.github.jacobbrewer1.bindicator.interfaces.IBindicatorDal;
import com.github.jacobbrewer1.bindicator.logging.Logging;
import org.joda.time.DateTime;
import org.joda.time.Instant;

import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
                        Thread.sleep(releaseTime);
                    }
                }

                new Thread(() -> {
                    try {

                        runLogic.execute();

                        bindicatorDal.saveRun(run.getNextRun());
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }).start();

                bindicatorDal.saveRun(run.getNextRun());
            } catch (SQLException exception) {
                exception.printStackTrace();
                Thread.sleep(1000);
            }
        }
    }
}
