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
 * A Diplome.
 */
@Entity
@Table(name = "diplome")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Diplome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "nb_annees_etude_post_bac")
    private Integer nbAnneesEtudePostBac;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "diplomes")
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

    public Diplome id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Diplome libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getNbAnneesEtudePostBac() {
        return this.nbAnneesEtudePostBac;
    }

    public Diplome nbAnneesEtudePostBac(Integer nbAnneesEtudePostBac) {
        this.setNbAnneesEtudePostBac(nbAnneesEtudePostBac);
        return this;
    }

    public void setNbAnneesEtudePostBac(Integer nbAnneesEtudePostBac) {
        this.nbAnneesEtudePostBac = nbAnneesEtudePostBac;
    }

    public Set<CoachExpert> getCoachExperts() {
        return this.coachExperts;
    }

    public void setCoachExperts(Set<CoachExpert> coachExperts) {
        if (this.coachExperts != null) {
            this.coachExperts.forEach(i -> i.removeDiplome(this));
        }
        if (coachExperts != null) {
            coachExperts.forEach(i -> i.addDiplome(this));
        }
        this.coachExperts = coachExperts;
    }

    public Diplome coachExperts(Set<CoachExpert> coachExperts) {
        this.setCoachExperts(coachExperts);
        return this;
    }

    public Diplome addCoachExpert(CoachExpert coachExpert) {
        this.coachExperts.add(coachExpert);
        coachExpert.getDiplomes().add(this);
        return this;
    }

    public Diplome removeCoachExpert(CoachExpert coachExpert) {
        this.coachExperts.remove(coachExpert);
        coachExpert.getDiplomes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Diplome)) {
            return false;
        }
        return getId() != null && getId().equals(((Diplome) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Diplome{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", nbAnneesEtudePostBac=" + getNbAnneesEtudePostBac() +
            "}";
    }
}
