package com.github.jacobbrewer1.entities;

import com.github.jacobbrewer1.enums.BinType;
import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Bin {

    @SerializedName("BinType")
    private String typeString;
    private BinType type;

    @SerializedName("Next")
    private String nextString;
    private DateTime next;

    @SerializedName("Subsequent")
    private String followingString;
    private DateTime following;

    @SerializedName("PdfLink")
    private String pdfLink;

    @SerializedName("Communal")
    private boolean communal;

    public Bin(BinType type) {
        this.type = type;
    }

    public void postGet() {
        type = BinType.valueOf(typeString.toUpperCase().replace(" ", "_"));

        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");

        followingString = followingString.substring(0, followingString.length() - 3);
        nextString = nextString.substring(0, nextString.length() - 3);

        following = DateTime.parse(followingString, formatter).withTimeAtStartOfDay().plusDays(1);
        next = DateTime.parse(nextString, formatter).withTimeAtStartOfDay().plusDays(1);
    }

    public BinType getType() {
        return type;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public boolean isCommunal() {
        return communal;
    }

    public void setCommunal(boolean communal) {
        this.communal = communal;
    }

    public DateTime getNext() {
        return next;
    }

    public void setNext(DateTime next) {
        this.next = next;
    }

    public DateTime getFollowing() {
        return following;
    }

    public void setFollowing(DateTime following) {
        this.following = following;
    }
}
