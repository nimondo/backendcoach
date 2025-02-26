package fr.coaching.maseance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AvisClient.
 */
@Entity
@Table(name = "avis_client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AvisClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_avis", nullable = false)
    private Instant dateAvis;

    @NotNull
    @Column(name = "note", nullable = false)
    private Integer note;

    @Column(name = "description_avis")
    private String descriptionAvis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Client client;

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

    public AvisClient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateAvis() {
        return this.dateAvis;
    }

    public AvisClient dateAvis(Instant dateAvis) {
        this.setDateAvis(dateAvis);
        return this;
    }

    public void setDateAvis(Instant dateAvis) {
        this.dateAvis = dateAvis;
    }

    public Integer getNote() {
        return this.note;
    }

    public AvisClient note(Integer note) {
        this.setNote(note);
        return this;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getDescriptionAvis() {
        return this.descriptionAvis;
    }

    public AvisClient descriptionAvis(String descriptionAvis) {
        this.setDescriptionAvis(descriptionAvis);
        return this;
    }

    public void setDescriptionAvis(String descriptionAvis) {
        this.descriptionAvis = descriptionAvis;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public AvisClient client(Client client) {
        this.setClient(client);
        return this;
    }

    public CoachExpert getCoachExpert() {
        return this.coachExpert;
    }

    public void setCoachExpert(CoachExpert coachExpert) {
        this.coachExpert = coachExpert;
    }

    public AvisClient coachExpert(CoachExpert coachExpert) {
        this.setCoachExpert(coachExpert);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvisClient)) {
            return false;
        }
        return getId() != null && getId().equals(((AvisClient) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvisClient{" +
            "id=" + getId() +
            ", dateAvis='" + getDateAvis() + "'" +
            ", note=" + getNote() +
            ", descriptionAvis='" + getDescriptionAvis() + "'" +
            "}";
    }
}
