package ar.edu.itba.paw.models;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Image {

    private long imageId;
    private byte[] imageData;
    private long petId;
    private String url;

    public Image(long imageId, byte[] imageData, long petId) {
        this.imageId = imageId;
        this.imageData = imageData;
        this.petId = petId;
        this.url = "data:image/png;base64," + Base64.encode(imageData);
    }

    public Image(long image_id) {

    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
