package fr.coaching.maseance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SpecialiteExpertise.
 */
@Entity
@Table(name = "specialite_expertise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecialiteExpertise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "specialite", nullable = false)
    private String specialite;

    @Column(name = "specialite_description")
    private String specialiteDescription;

    @Column(name = "tarif_moyen_heure")
    private Long tarifMoyenHeure;

    @Column(name = "duree_tarif")
    private String dureeTarif;

    @NotNull
    @Column(name = "diplome_requis", nullable = false)
    private Boolean diplomeRequis;

    @NotNull
    @Column(name = "url_photo", nullable = false)
    private String urlPhoto;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "specialite")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "media", "specialite", "coachExpert" }, allowSetters = true)
    private Set<OffreCoach> offres = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "coachExperts" }, allowSetters = true)
    private Diplome diplome;

    @ManyToOne(fetch = FetchType.LAZY)
    private ThemeExpertise themeExpertise;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "specialiteExpertises")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "user", "lesAvisClients", "disponibilites", "offres", "specialiteExpertises", "diplomes" },
        allowSetters = true
    )
    private Set<CoachExpert> coachExperts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SpecialiteExpertise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecialite() {
        return this.specialite;
    }

    public SpecialiteExpertise specialite(String specialite) {
        this.setSpecialite(specialite);
        return this;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getSpecialiteDescription() {
        return this.specialiteDescription;
    }

    public SpecialiteExpertise specialiteDescription(String specialiteDescription) {
        this.setSpecialiteDescription(specialiteDescription);
        return this;
    }

    public void setSpecialiteDescription(String specialiteDescription) {
        this.specialiteDescription = specialiteDescription;
    }

    public Long getTarifMoyenHeure() {
        return this.tarifMoyenHeure;
    }

    public SpecialiteExpertise tarifMoyenHeure(Long tarifMoyenHeure) {
        this.setTarifMoyenHeure(tarifMoyenHeure);
        return this;
    }

    public void setTarifMoyenHeure(Long tarifMoyenHeure) {
        this.tarifMoyenHeure = tarifMoyenHeure;
    }

    public String getDureeTarif() {
        return this.dureeTarif;
    }

    public SpecialiteExpertise dureeTarif(String dureeTarif) {
        this.setDureeTarif(dureeTarif);
        return this;
    }

    public void setDureeTarif(String dureeTarif) {
        this.dureeTarif = dureeTarif;
    }

    public Boolean getDiplomeRequis() {
        return this.diplomeRequis;
    }

    public SpecialiteExpertise diplomeRequis(Boolean diplomeRequis) {
        this.setDiplomeRequis(diplomeRequis);
        return this;
    }

    public void setDiplomeRequis(Boolean diplomeRequis) {
        this.diplomeRequis = diplomeRequis;
    }

    public String getUrlPhoto() {
        return this.urlPhoto;
    }

    public SpecialiteExpertise urlPhoto(String urlPhoto) {
        this.setUrlPhoto(urlPhoto);
        return this;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Set<OffreCoach> getOffres() {
        return this.offres;
    }

    public void setOffres(Set<OffreCoach> offreCoaches) {
        if (this.offres != null) {
            this.offres.forEach(i -> i.setSpecialite(null));
        }
        if (offreCoaches != null) {
            offreCoaches.forEach(i -> i.setSpecialite(this));
        }
        this.offres = offreCoaches;
    }

    public SpecialiteExpertise offres(Set<OffreCoach> offreCoaches) {
        this.setOffres(offreCoaches);
        return this;
    }

    public SpecialiteExpertise addOffre(OffreCoach offreCoach) {
        this.offres.add(offreCoach);
        offreCoach.setSpecialite(this);
        return this;
    }

    public SpecialiteExpertise removeOffre(OffreCoach offreCoach) {
        this.offres.remove(offreCoach);
        offreCoach.setSpecialite(null);
        return this;
    }

    public Diplome getDiplome() {
        return this.diplome;
    }

    public void setDiplome(Diplome diplome) {
        this.diplome = diplome;
    }

    public SpecialiteExpertise diplome(Diplome diplome) {
        this.setDiplome(diplome);
        return this;
    }

    public ThemeExpertise getThemeExpertise() {
        return this.themeExpertise;
    }

    public void setThemeExpertise(ThemeExpertise themeExpertise) {
        this.themeExpertise = themeExpertise;
    }

    public SpecialiteExpertise themeExpertise(ThemeExpertise themeExpertise) {
        this.setThemeExpertise(themeExpertise);
        return this;
    }

    public Set<CoachExpert> getCoachExperts() {
        return this.coachExperts;
    }

    public void setCoachExperts(Set<CoachExpert> coachExperts) {
        if (this.coachExperts != null) {
            this.coachExperts.forEach(i -> i.removeSpecialiteExpertise(this));
        }
        if (coachExperts != null) {
            coachExperts.forEach(i -> i.addSpecialiteExpertise(this));
        }
        this.coachExperts = coachExperts;
    }

    public SpecialiteExpertise coachExperts(Set<CoachExpert> coachExperts) {
        this.setCoachExperts(coachExperts);
        return this;
    }

    public SpecialiteExpertise addCoachExpert(CoachExpert coachExpert) {
        this.coachExperts.add(coachExpert);
        coachExpert.getSpecialiteExpertises().add(this);
        return this;
    }

    public SpecialiteExpertise removeCoachExpert(CoachExpert coachExpert) {
        this.coachExperts.remove(coachExpert);
        coachExpert.getSpecialiteExpertises().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialiteExpertise)) {
            return false;
        }
        return getId() != null && getId().equals(((SpecialiteExpertise) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialiteExpertise{" +
            "id=" + getId() +
            ", specialite='" + getSpecialite() + "'" +
            ", specialiteDescription='" + getSpecialiteDescription() + "'" +
            ", tarifMoyenHeure=" + getTarifMoyenHeure() +
            ", dureeTarif='" + getDureeTarif() + "'" +
            ", diplomeRequis='" + getDiplomeRequis() + "'" +
            ", urlPhoto='" + getUrlPhoto() + "'" +
            "}";
    }
}
