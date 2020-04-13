package ar.edu.itba.paw.models;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.sql.Blob;
import java.sql.SQLException;

public class Image {

    private Integer imageId;
    private byte[] imageData;
    private String url;

    public Image(Integer imageId, byte[] imageData) {
        this.imageId = imageId;
        this.imageData = imageData;
        this.url = "data:image/png;base64," + Base64.encode(imageData);
    }
//
//    public Image(int image_id, Blob img) throws SQLException {
//        this.imageId = image_id;
//        this.imageData = img.getBytes(1, (int) img.length());
//    }

    public Image(int image_id) {

    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
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
}
