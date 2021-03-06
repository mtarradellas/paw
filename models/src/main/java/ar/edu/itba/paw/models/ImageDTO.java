package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Images")
public class ImageDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "images_id_seq", name = "images_id_seq")
    private Long id;

    @Column
    private Long petId;

    protected ImageDTO() {
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
