package com.github.jacobbrewer1.businesslogic;

import com.github.jacobbrewer1.entities.Bin;
import com.github.jacobbrewer1.logging.Logging;
import org.joda.time.DateTime;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class EmailLogic {

    private final Logging logging;

    private final String fromAddress;
    private final String password;

    private final String host;
    private final String hostPort;

    private final Properties properties;

    public EmailLogic(Logging logging, String fromAddress, String password, String host, String hostPort, Properties properties) {
        this.logging = logging;
        this.fromAddress = fromAddress;
        this.password = password;
        this.host = host;
        this.hostPort = hostPort;
        this.properties = properties;
    }

    public void execute(List<Bin> binList) throws Exception {
        String message = createMessage(binList);

        sendEmail(message);
    }

    private String createMessage(List<Bin> bins) {
        String message = "The %s %s is being collected tomorrow (Date: %s)";
        DateTime dateTime = new DateTime();

        String binsMultiText = "bin";
        if (bins.size() > 1) {
            binsMultiText = binsMultiText + "s";
        }

        String binText = "";
        binText = bins.get(0).getType().getFriendlyName();
        dateTime = bins.get(0).getNext();

        bins.remove(bins.get(0));

        for (Bin bin : bins) {
            binText = String.format((binText + " and %s"), bin.getType().getFriendlyName());
        }

        return String.format(message, binText, binsMultiText, dateTime.toString("EEEE dd-MM-yyyy"));
    }

    private void sendEmail(String body) throws Exception {
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromAddress, password);
            }
        });

        // Used to debug SMTP issues
        session.setDebug(true);

        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(fromAddress));

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("jmbrewer2000@gmail.com"));

        // Set Subject: header field
        message.setSubject("Bin day!");

        // Now set the actual message
        message.setText(body);

        logging.logInfo("sending...");

        // Send message
        Transport.send(message);

        logging.logInfo("Message sent successfully!");
    }
}
