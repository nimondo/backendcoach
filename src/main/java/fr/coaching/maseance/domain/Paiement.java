package fr.coaching.maseance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.coaching.maseance.domain.enumeration.StatutPaiement;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Paiement.
 */
@Entity
@Table(name = "paiement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paiement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "horodatage", nullable = false)
    private Instant horodatage;

    @Column(name = "moyen_paiement")
    private String moyenPaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_paiement")
    private StatutPaiement statutPaiement;

    @Column(name = "id_stripe")
    private String idStripe;

    @JsonIgnoreProperties(value = { "paiement", "seanceReservationCoach" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "paiement")
    private Facture facture;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paiement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getHorodatage() {
        return this.horodatage;
    }

    public Paiement horodatage(Instant horodatage) {
        this.setHorodatage(horodatage);
        return this;
    }

    public void setHorodatage(Instant horodatage) {
        this.horodatage = horodatage;
    }

    public String getMoyenPaiement() {
        return this.moyenPaiement;
    }

    public Paiement moyenPaiement(String moyenPaiement) {
        this.setMoyenPaiement(moyenPaiement);
        return this;
    }

    public void setMoyenPaiement(String moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

    public StatutPaiement getStatutPaiement() {
        return this.statutPaiement;
    }

    public Paiement statutPaiement(StatutPaiement statutPaiement) {
        this.setStatutPaiement(statutPaiement);
        return this;
    }

    public void setStatutPaiement(StatutPaiement statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public String getIdStripe() {
        return this.idStripe;
    }

    public Paiement idStripe(String idStripe) {
        this.setIdStripe(idStripe);
        return this;
    }

    public void setIdStripe(String idStripe) {
        this.idStripe = idStripe;
    }

    public Facture getFacture() {
        return this.facture;
    }

    public void setFacture(Facture facture) {
        if (this.facture != null) {
            this.facture.setPaiement(null);
        }
        if (facture != null) {
            facture.setPaiement(this);
        }
        this.facture = facture;
    }

    public Paiement facture(Facture facture) {
        this.setFacture(facture);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paiement)) {
            return false;
        }
        return getId() != null && getId().equals(((Paiement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paiement{" +
            "id=" + getId() +
            ", horodatage='" + getHorodatage() + "'" +
            ", moyenPaiement='" + getMoyenPaiement() + "'" +
            ", statutPaiement='" + getStatutPaiement() + "'" +
            ", idStripe='" + getIdStripe() + "'" +
            "}";
    }
}
