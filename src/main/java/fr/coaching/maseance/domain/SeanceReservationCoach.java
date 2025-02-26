package fr.coaching.maseance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.domain.enumeration.StatutSeance;
import fr.coaching.maseance.domain.enumeration.TypeSeance;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SeanceReservationCoach.
 */
@Entity
@Table(name = "seance_reservation_coach")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SeanceReservationCoach implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "heure_debut_creneau_reserve", nullable = false)
    private Instant heureDebutCreneauReserve;

    @NotNull
    @Column(name = "heure_fin_creneau_reserve", nullable = false)
    private Instant heureFinCreneauReserve;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "canal_seance", nullable = false)
    private CanalSeance canalSeance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_seance", nullable = false)
    private TypeSeance typeSeance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_realisation", nullable = false)
    private StatutSeance statutRealisation;

    @JsonIgnoreProperties(value = { "paiement", "seanceReservationCoach" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Facture facture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "lesAvisClients", "disponibilites", "offres", "specialiteExpertises", "diplomes" },
        allowSetters = true
    )
    private CoachExpert coachExpert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "media", "specialite", "coachExpert" }, allowSetters = true)
    private OffreCoach offre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SeanceReservationCoach id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getHeureDebutCreneauReserve() {
        return this.heureDebutCreneauReserve;
    }

    public SeanceReservationCoach heureDebutCreneauReserve(Instant heureDebutCreneauReserve) {
        this.setHeureDebutCreneauReserve(heureDebutCreneauReserve);
        return this;
    }

    public void setHeureDebutCreneauReserve(Instant heureDebutCreneauReserve) {
        this.heureDebutCreneauReserve = heureDebutCreneauReserve;
    }

    public Instant getHeureFinCreneauReserve() {
        return this.heureFinCreneauReserve;
    }

    public SeanceReservationCoach heureFinCreneauReserve(Instant heureFinCreneauReserve) {
        this.setHeureFinCreneauReserve(heureFinCreneauReserve);
        return this;
    }

    public void setHeureFinCreneauReserve(Instant heureFinCreneauReserve) {
        this.heureFinCreneauReserve = heureFinCreneauReserve;
    }

    public CanalSeance getCanalSeance() {
        return this.canalSeance;
    }

    public SeanceReservationCoach canalSeance(CanalSeance canalSeance) {
        this.setCanalSeance(canalSeance);
        return this;
    }

    public void setCanalSeance(CanalSeance canalSeance) {
        this.canalSeance = canalSeance;
    }

    public TypeSeance getTypeSeance() {
        return this.typeSeance;
    }

    public SeanceReservationCoach typeSeance(TypeSeance typeSeance) {
        this.setTypeSeance(typeSeance);
        return this;
    }

    public void setTypeSeance(TypeSeance typeSeance) {
        this.typeSeance = typeSeance;
    }

    public StatutSeance getStatutRealisation() {
        return this.statutRealisation;
    }

    public SeanceReservationCoach statutRealisation(StatutSeance statutRealisation) {
        this.setStatutRealisation(statutRealisation);
        return this;
    }

    public void setStatutRealisation(StatutSeance statutRealisation) {
        this.statutRealisation = statutRealisation;
    }

    public Facture getFacture() {
        return this.facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public SeanceReservationCoach facture(Facture facture) {
        this.setFacture(facture);
        return this;
    }

    public CoachExpert getCoachExpert() {
        return this.coachExpert;
    }

    public void setCoachExpert(CoachExpert coachExpert) {
        this.coachExpert = coachExpert;
    }

    public SeanceReservationCoach coachExpert(CoachExpert coachExpert) {
        this.setCoachExpert(coachExpert);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public SeanceReservationCoach client(Client client) {
        this.setClient(client);
        return this;
    }

    public OffreCoach getOffre() {
        return this.offre;
    }

    public void setOffre(OffreCoach offreCoach) {
        this.offre = offreCoach;
    }

    public SeanceReservationCoach offre(OffreCoach offreCoach) {
        this.setOffre(offreCoach);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeanceReservationCoach)) {
            return false;
        }
        return getId() != null && getId().equals(((SeanceReservationCoach) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeanceReservationCoach{" +
            "id=" + getId() +
            ", heureDebutCreneauReserve='" + getHeureDebutCreneauReserve() + "'" +
            ", heureFinCreneauReserve='" + getHeureFinCreneauReserve() + "'" +
            ", canalSeance='" + getCanalSeance() + "'" +
            ", typeSeance='" + getTypeSeance() + "'" +
            ", statutRealisation='" + getStatutRealisation() + "'" +
            "}";
    }
}
