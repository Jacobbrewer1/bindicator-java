package com.github.jacobbrewer1;

import com.github.jacobbrewer1.api.GetBins;
import com.github.jacobbrewer1.businesslogic.EmailLogic;
import com.github.jacobbrewer1.businesslogic.TriggerAllLogic;
import com.github.jacobbrewer1.dataacess.PeopleDal;
import com.github.jacobbrewer1.interfaces.IPeopleDal;
import com.github.jacobbrewer1.logging.Logging;
import com.github.jacobbrewer1.services.BindicatorService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Bindicator {

    private static final Logging logging = new Logging(Logger.getLogger(Bindicator.class.getName()));

    private final Gson gson;

    private final GetBins getBins;

    private final BindicatorService runAllService;

    private final TriggerAllLogic triggerAllLogic;
    private final EmailLogic emailLogic;

    private final IPeopleDal peopleDal;

    public static void main(String[] args) throws Exception {

        logging.logInfo("Bindicator application start");

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

        Bindicator bindicator = new Bindicator(properties);
        bindicator.execute(properties.getProperty("listeningPort", "50051"));
    }

    public Bindicator(Properties properties) throws Exception {
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
            try {
                return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            } catch (DateTimeParseException e) {
                return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            }
        }).create();

        this.peopleDal = new PeopleDal(properties.getProperty("schema"), properties.getProperty("host"), properties.getProperty("port"),
                properties.getProperty("username"), properties.getProperty("dbPassword"));

        this.getBins = new GetBins(properties.getProperty("bcpApi"), logging, gson);

        this.emailLogic = new EmailLogic(logging, properties.getProperty("fromAddress"), properties.getProperty("emailPassword"),
                properties.getProperty("mail.smtp.host"), properties.getProperty("mail.smtp.port", "465"), properties);
        this.triggerAllLogic = new TriggerAllLogic(logging, gson, emailLogic, getBins, peopleDal);

        this.runAllService = new BindicatorService(logging, gson, triggerAllLogic);
    }

    private void execute(String port) throws IOException, InterruptedException {
        int listeningPort = Integer.parseInt(port);

        Server server = ServerBuilder.forPort(listeningPort)
                .addService(runAllService)
                .build();
        server.start();

        logging.logInfo(String.format("Listening on port %d", listeningPort));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();

            try {
                if (!server.awaitTermination(30, TimeUnit.SECONDS)) {
                    server.shutdown();

                    server.awaitTermination(5, TimeUnit.SECONDS);
                }
            } catch (Exception exception) {
                exception.printStackTrace();

                server.shutdown();
            }
        }));

        server.awaitTermination();
    }
}
