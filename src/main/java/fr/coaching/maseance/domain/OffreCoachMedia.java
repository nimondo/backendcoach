package fr.coaching.maseance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.coaching.maseance.domain.enumeration.TypeMedia;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OffreCoachMedia.
 */
@Entity
@Table(name = "offre_coach_media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OffreCoachMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url_media")
    private String urlMedia;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_media")
    private TypeMedia typeMedia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "media", "specialite", "coachExpert" }, allowSetters = true)
    private OffreCoach offreCoach;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OffreCoachMedia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlMedia() {
        return this.urlMedia;
    }

    public OffreCoachMedia urlMedia(String urlMedia) {
        this.setUrlMedia(urlMedia);
        return this;
    }

    public void setUrlMedia(String urlMedia) {
        this.urlMedia = urlMedia;
    }

    public TypeMedia getTypeMedia() {
        return this.typeMedia;
    }

    public OffreCoachMedia typeMedia(TypeMedia typeMedia) {
        this.setTypeMedia(typeMedia);
        return this;
    }

    public void setTypeMedia(TypeMedia typeMedia) {
        this.typeMedia = typeMedia;
    }

    public OffreCoach getOffreCoach() {
        return this.offreCoach;
    }

    public void setOffreCoach(OffreCoach offreCoach) {
        this.offreCoach = offreCoach;
    }

    public OffreCoachMedia offreCoach(OffreCoach offreCoach) {
        this.setOffreCoach(offreCoach);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OffreCoachMedia)) {
            return false;
        }
        return getId() != null && getId().equals(((OffreCoachMedia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OffreCoachMedia{" +
            "id=" + getId() +
            ", urlMedia='" + getUrlMedia() + "'" +
            ", typeMedia='" + getTypeMedia() + "'" +
            "}";
    }
}
