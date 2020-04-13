package ar.edu.itba.paw.models;

import java.sql.Blob;
import java.sql.SQLException;

public class Image {

    private Integer imageId;
    private byte[] imageData;

    public Image(Integer imageId, byte[] imageData) {
        this.imageId = imageId;
        this.imageData = imageData;
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

}
