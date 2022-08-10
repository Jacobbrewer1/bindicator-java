package com.github.jacobbrewer1.api;

import com.github.jacobbrewer1.entities.Bin;
import com.github.jacobbrewer1.entities.Person;
import com.github.jacobbrewer1.logging.Logging;
import com.google.gson.Gson;

import java.io.IOException;

public class GetBins extends BcpHelper {

    private final Logging logging;

    public GetBins(String endpoint, Logging logging, Gson gson) {
        super(endpoint, gson);
        this.logging = logging;
    }

    public Bin[] fetch(Person person) throws IOException {
        logging.logInfo("Requesting bins");

        Bin[] bcpBins = getBinsRequest(person);

        logging.logInfo(String.format("%d Bins received", bcpBins.length));

        return bcpBins;
    }
}
