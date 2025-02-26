package fr.coaching.maseance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.domain.enumeration.TypeSeance;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OffreCoach.
 */
@Entity
@Table(name = "offre_coach")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OffreCoach implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_seance")
    private CanalSeance canalSeance;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_seance")
    private TypeSeance typeSeance;

    @Column(name = "synthese")
    private String synthese;

    @Column(name = "description_detaillee")
    private String descriptionDetaillee;

    @NotNull
    @Column(name = "tarif", nullable = false)
    private Long tarif;

    @NotNull
    @Column(name = "duree", nullable = false)
    private Integer duree;

    @Column(name = "description_diplome")
    private String descriptionDiplome;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offreCoach")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "offreCoach" }, allowSetters = true)
    private Set<OffreCoachMedia> media = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "offres", "diplome", "themeExpertise", "coachExperts" }, allowSetters = true)
    private SpecialiteExpertise specialite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "lesAvisClients", "disponibilites", "offres", "specialiteExpertises", "diplomes" },
        allowSetters = true
    )
    private CoachExpert coachExpert;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OffreCoach id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CanalSeance getCanalSeance() {
        return this.canalSeance;
    }

    public OffreCoach canalSeance(CanalSeance canalSeance) {
        this.setCanalSeance(canalSeance);
        return this;
    }

    public void setCanalSeance(CanalSeance canalSeance) {
        this.canalSeance = canalSeance;
    }

    public TypeSeance getTypeSeance() {
        return this.typeSeance;
    }

    public OffreCoach typeSeance(TypeSeance typeSeance) {
        this.setTypeSeance(typeSeance);
        return this;
    }

    public void setTypeSeance(TypeSeance typeSeance) {
        this.typeSeance = typeSeance;
    }

    public String getSynthese() {
        return this.synthese;
    }

    public OffreCoach synthese(String synthese) {
        this.setSynthese(synthese);
        return this;
    }

    public void setSynthese(String synthese) {
        this.synthese = synthese;
    }

    public String getDescriptionDetaillee() {
        return this.descriptionDetaillee;
    }

    public OffreCoach descriptionDetaillee(String descriptionDetaillee) {
        this.setDescriptionDetaillee(descriptionDetaillee);
        return this;
    }

    public void setDescriptionDetaillee(String descriptionDetaillee) {
        this.descriptionDetaillee = descriptionDetaillee;
    }

    public Long getTarif() {
        return this.tarif;
    }

    public OffreCoach tarif(Long tarif) {
        this.setTarif(tarif);
        return this;
    }

    public void setTarif(Long tarif) {
        this.tarif = tarif;
    }

    public Integer getDuree() {
        return this.duree;
    }

    public OffreCoach duree(Integer duree) {
        this.setDuree(duree);
        return this;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public String getDescriptionDiplome() {
        return this.descriptionDiplome;
    }

    public OffreCoach descriptionDiplome(String descriptionDiplome) {
        this.setDescriptionDiplome(descriptionDiplome);
        return this;
    }

    public void setDescriptionDiplome(String descriptionDiplome) {
        this.descriptionDiplome = descriptionDiplome;
    }

    public Set<OffreCoachMedia> getMedia() {
        return this.media;
    }

    public void setMedia(Set<OffreCoachMedia> offreCoachMedias) {
        if (this.media != null) {
            this.media.forEach(i -> i.setOffreCoach(null));
        }
        if (offreCoachMedias != null) {
            offreCoachMedias.forEach(i -> i.setOffreCoach(this));
        }
        this.media = offreCoachMedias;
    }

    public OffreCoach media(Set<OffreCoachMedia> offreCoachMedias) {
        this.setMedia(offreCoachMedias);
        return this;
    }

    public OffreCoach addMedia(OffreCoachMedia offreCoachMedia) {
        this.media.add(offreCoachMedia);
        offreCoachMedia.setOffreCoach(this);
        return this;
    }

    public OffreCoach removeMedia(OffreCoachMedia offreCoachMedia) {
        this.media.remove(offreCoachMedia);
        offreCoachMedia.setOffreCoach(null);
        return this;
    }

    public SpecialiteExpertise getSpecialite() {
        return this.specialite;
    }

    public void setSpecialite(SpecialiteExpertise specialiteExpertise) {
        this.specialite = specialiteExpertise;
    }

    public OffreCoach specialite(SpecialiteExpertise specialiteExpertise) {
        this.setSpecialite(specialiteExpertise);
        return this;
    }

    public CoachExpert getCoachExpert() {
        return this.coachExpert;
    }

    public void setCoachExpert(CoachExpert coachExpert) {
        this.coachExpert = coachExpert;
    }

    public OffreCoach coachExpert(CoachExpert coachExpert) {
        this.setCoachExpert(coachExpert);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OffreCoach)) {
            return false;
        }
        return getId() != null && getId().equals(((OffreCoach) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OffreCoach{" +
            "id=" + getId() +
            ", canalSeance='" + getCanalSeance() + "'" +
            ", typeSeance='" + getTypeSeance() + "'" +
            ", synthese='" + getSynthese() + "'" +
            ", descriptionDetaillee='" + getDescriptionDetaillee() + "'" +
            ", tarif=" + getTarif() +
            ", duree=" + getDuree() +
            ", descriptionDiplome='" + getDescriptionDiplome() + "'" +
            "}";
    }
}
