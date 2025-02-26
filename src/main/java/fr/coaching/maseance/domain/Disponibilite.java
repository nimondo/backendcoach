package fr.coaching.maseance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.coaching.maseance.domain.enumeration.CanalSeance;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Disponibilite.
 */
@Entity
@Table(name = "disponibilite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Disponibilite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "heure_debut_creneaux_disponibilite", nullable = false)
    private Instant heureDebutCreneauxDisponibilite;

    @NotNull
    @Column(name = "heure_fin_creneaux_disponibilite", nullable = false)
    private Instant heureFinCreneauxDisponibilite;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_seance")
    private CanalSeance canalSeance;

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

    public Disponibilite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getHeureDebutCreneauxDisponibilite() {
        return this.heureDebutCreneauxDisponibilite;
    }

    public Disponibilite heureDebutCreneauxDisponibilite(Instant heureDebutCreneauxDisponibilite) {
        this.setHeureDebutCreneauxDisponibilite(heureDebutCreneauxDisponibilite);
        return this;
    }

    public void setHeureDebutCreneauxDisponibilite(Instant heureDebutCreneauxDisponibilite) {
        this.heureDebutCreneauxDisponibilite = heureDebutCreneauxDisponibilite;
    }

    public Instant getHeureFinCreneauxDisponibilite() {
        return this.heureFinCreneauxDisponibilite;
    }

    public Disponibilite heureFinCreneauxDisponibilite(Instant heureFinCreneauxDisponibilite) {
        this.setHeureFinCreneauxDisponibilite(heureFinCreneauxDisponibilite);
        return this;
    }

    public void setHeureFinCreneauxDisponibilite(Instant heureFinCreneauxDisponibilite) {
        this.heureFinCreneauxDisponibilite = heureFinCreneauxDisponibilite;
    }

    public CanalSeance getCanalSeance() {
        return this.canalSeance;
    }

    public Disponibilite canalSeance(CanalSeance canalSeance) {
        this.setCanalSeance(canalSeance);
        return this;
    }

    public void setCanalSeance(CanalSeance canalSeance) {
        this.canalSeance = canalSeance;
    }

    public CoachExpert getCoachExpert() {
        return this.coachExpert;
    }

    public void setCoachExpert(CoachExpert coachExpert) {
        this.coachExpert = coachExpert;
    }

    public Disponibilite coachExpert(CoachExpert coachExpert) {
        this.setCoachExpert(coachExpert);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Disponibilite)) {
            return false;
        }
        return getId() != null && getId().equals(((Disponibilite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Disponibilite{" +
            "id=" + getId() +
            ", heureDebutCreneauxDisponibilite='" + getHeureDebutCreneauxDisponibilite() + "'" +
            ", heureFinCreneauxDisponibilite='" + getHeureFinCreneauxDisponibilite() + "'" +
            ", canalSeance='" + getCanalSeance() + "'" +
            "}";
    }
}
