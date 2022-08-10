package com.github.jacobbrewer1.businesslogic;

import com.github.jacobbrewer1.api.GetBins;
import com.github.jacobbrewer1.entities.Bin;
import com.github.jacobbrewer1.entities.Person;
import com.github.jacobbrewer1.interfaces.IPeopleDal;
import com.github.jacobbrewer1.logging.Logging;
import com.google.gson.Gson;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class TriggerAllLogic {

    private final Logging logging;

    private final Gson gson;

    private final EmailLogic emailLogic;

    private final GetBins getBins;

    private final IPeopleDal peopleDal;

    public TriggerAllLogic(Logging logging, Gson gson, EmailLogic emailLogic, GetBins getBins, IPeopleDal peopleDal) {
        this.logging = logging;
        this.gson = gson;
        this.emailLogic = emailLogic;
        this.getBins = getBins;
        this.peopleDal = peopleDal;
    }

    public void execute() throws Exception {
        for (Person person : peopleDal.getPeople()) {
            List<Bin> bins = List.of(getBins.fetch(person));

            for (Bin bin : bins) {
                bin.postGet();
            }

            bins = determineBinsToday(bins);

            if (bins.isEmpty()) {
                logging.logInfo(String.format("%s has no bins tomorrow", person.getName()));
                continue;
            }

            logging.logInfo(String.format("%s has %d bins tomorrow", person.getName(), bins.size()));

            emailLogic.execute(bins);
        }
    }

    private List<Bin> determineBinsToday(List<Bin> bins) {
        List<Bin> binList = new ArrayList<>();

        for (Bin bin : bins) {
            if (bin.getNext().isEqual(DateTime.now().withTimeAtStartOfDay().plusDays(1))) {
                binList.add(bin);
            }
        }

        return binList;
    }
}
