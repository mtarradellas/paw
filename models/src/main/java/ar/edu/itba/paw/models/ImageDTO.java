package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity(name = "ImagesDTO")
public class ImageDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "images_id_seq", name = "images_id_seq")
    private Long id;

    @Column
    private Long petId;

    public ImageDTO() {
        //Hibernate
    }

    public ImageDTO(long imageId, long petId) {
        this.id = imageId;
        this.petId = petId;
    }

    public Long getId() {
        return id;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }
}
