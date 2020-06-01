package ar.edu.itba.paw.models;

import javax.persistence.*;

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

    public Image() {
        //Hibernate
    }

    public Image(long imageId, byte[] imageData, long petId) {
        this.id = imageId;
        this.img = imageData;
        this.petId = petId;
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
}
