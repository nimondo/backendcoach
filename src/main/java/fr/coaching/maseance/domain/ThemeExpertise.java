package fr.coaching.maseance.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ThemeExpertise.
 */
@Entity
@Table(name = "theme_expertise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ThemeExpertise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_expertise", nullable = false)
    private String libelleExpertise;

    @NotNull
    @Column(name = "url_photo", nullable = false)
    private String urlPhoto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ThemeExpertise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleExpertise() {
        return this.libelleExpertise;
    }

    public ThemeExpertise libelleExpertise(String libelleExpertise) {
        this.setLibelleExpertise(libelleExpertise);
        return this;
    }

    public void setLibelleExpertise(String libelleExpertise) {
        this.libelleExpertise = libelleExpertise;
    }

    public String getUrlPhoto() {
        return this.urlPhoto;
    }

    public ThemeExpertise urlPhoto(String urlPhoto) {
        this.setUrlPhoto(urlPhoto);
        return this;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThemeExpertise)) {
            return false;
        }
        return getId() != null && getId().equals(((ThemeExpertise) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThemeExpertise{" +
            "id=" + getId() +
            ", libelleExpertise='" + getLibelleExpertise() + "'" +
            ", urlPhoto='" + getUrlPhoto() + "'" +
            "}";
    }
}
