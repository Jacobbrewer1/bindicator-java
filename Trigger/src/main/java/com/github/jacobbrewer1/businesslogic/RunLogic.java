package com.github.jacobbrewer1.businesslogic;

import com.github.jacobbrewer1.comms.TriggerAllComms;
import com.github.jacobbrewer1.logging.Logging;

public class RunLogic {

    private final Logging logging;

    private final TriggerAllComms triggerAll;

    public RunLogic(Logging logging, TriggerAllComms triggerAll) {
        this.logging = logging;
        this.triggerAll = triggerAll;
    }

    public void execute() {
        logging.logInfo("sending message");

        triggerAll.sendMessage();

        logging.logInfo("message sent");
    }
}
