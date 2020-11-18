package model;

import android.net.Uri;

public class Event {
    private String title;
    private String type;
    private String date;
    private String venue;
    private String guestId;
    private String desc;
    private String gallery;
    private byte[] photo;

    public String getTitle() {
        return title;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGallery() {
        return gallery;
    }

    public void setGallery(String gallery) {
        this.gallery = gallery;
    }

    public Event(String title, String type, String date, String venue, String guestId, String desc, String gallery) {
        this.title = title;
        this.type = type;
        this.date = date;
        this.venue = venue;
        this.guestId = guestId;
        this.desc = desc;
        this.gallery = gallery;
    }

    public Event() {
    }
}
