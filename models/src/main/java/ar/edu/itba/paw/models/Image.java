package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Base64;

@Entity(name = "Images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "images_id_seq", name = "images_id_seq")
    private Long id;

    @Column
    private byte[] img;

    @Column
    private Long petId;


    private String url;

    public Image(long imageId, byte[] imageData, long petId) {
        this.id = imageId;
        this.img = imageData;
        this.petId = petId;
        this.url = "data:image/png;base64," + Base64.getEncoder().encodeToString(imageData);
    }

    public Image() {
        //Hibernate
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Long getImageId() {
        return id;
    }

    public void setImageId(Long imageId) {
        this.id = imageId;
    }

    public byte[] getImageData() {
        return img;
    }

    public void setImageData(byte[] imageData) {
        this.img = imageData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
