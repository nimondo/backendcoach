package fr.coaching.maseance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.coaching.maseance.domain.enumeration.TypeFacture;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Facture.
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_facture")
    private TypeFacture typeFacture;

    @NotNull
    @Column(name = "date_comptable_facture", nullable = false)
    private Instant dateComptableFacture;

    @NotNull
    @Column(name = "montant", nullable = false)
    private Double montant;

    @NotNull
    @Column(name = "tva", nullable = false)
    private Double tva;

    @JsonIgnoreProperties(value = { "facture" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Paiement paiement;

    @JsonIgnoreProperties(value = { "facture", "coachExpert", "client", "offre" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "facture")
    private SeanceReservationCoach seanceReservationCoach;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Facture id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeFacture getTypeFacture() {
        return this.typeFacture;
    }

    public Facture typeFacture(TypeFacture typeFacture) {
        this.setTypeFacture(typeFacture);
        return this;
    }

    public void setTypeFacture(TypeFacture typeFacture) {
        this.typeFacture = typeFacture;
    }

    public Instant getDateComptableFacture() {
        return this.dateComptableFacture;
    }

    public Facture dateComptableFacture(Instant dateComptableFacture) {
        this.setDateComptableFacture(dateComptableFacture);
        return this;
    }

    public void setDateComptableFacture(Instant dateComptableFacture) {
        this.dateComptableFacture = dateComptableFacture;
    }

    public Double getMontant() {
        return this.montant;
    }

    public Facture montant(Double montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Double getTva() {
        return this.tva;
    }

    public Facture tva(Double tva) {
        this.setTva(tva);
        return this;
    }

    public void setTva(Double tva) {
        this.tva = tva;
    }

    public Paiement getPaiement() {
        return this.paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public Facture paiement(Paiement paiement) {
        this.setPaiement(paiement);
        return this;
    }

    public SeanceReservationCoach getSeanceReservationCoach() {
        return this.seanceReservationCoach;
    }

    public void setSeanceReservationCoach(SeanceReservationCoach seanceReservationCoach) {
        if (this.seanceReservationCoach != null) {
            this.seanceReservationCoach.setFacture(null);
        }
        if (seanceReservationCoach != null) {
            seanceReservationCoach.setFacture(this);
        }
        this.seanceReservationCoach = seanceReservationCoach;
    }

    public Facture seanceReservationCoach(SeanceReservationCoach seanceReservationCoach) {
        this.setSeanceReservationCoach(seanceReservationCoach);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facture)) {
            return false;
        }
        return getId() != null && getId().equals(((Facture) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facture{" +
            "id=" + getId() +
            ", typeFacture='" + getTypeFacture() + "'" +
            ", dateComptableFacture='" + getDateComptableFacture() + "'" +
            ", montant=" + getMontant() +
            ", tva=" + getTva() +
            "}";
    }
}
