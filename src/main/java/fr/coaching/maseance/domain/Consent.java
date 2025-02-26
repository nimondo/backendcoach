package fr.coaching.maseance.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Consent.
 */
@Entity
@Table(name = "consent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Consent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "necessary")
    private Boolean necessary;

    @Column(name = "analytics")
    private Boolean analytics;

    @Column(name = "marketing")
    private Boolean marketing;

    @Column(name = "preferences")
    private Boolean preferences;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Consent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public Consent email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getNecessary() {
        return this.necessary;
    }

    public Consent necessary(Boolean necessary) {
        this.setNecessary(necessary);
        return this;
    }

    public void setNecessary(Boolean necessary) {
        this.necessary = necessary;
    }

    public Boolean getAnalytics() {
        return this.analytics;
    }

    public Consent analytics(Boolean analytics) {
        this.setAnalytics(analytics);
        return this;
    }

    public void setAnalytics(Boolean analytics) {
        this.analytics = analytics;
    }

    public Boolean getMarketing() {
        return this.marketing;
    }

    public Consent marketing(Boolean marketing) {
        this.setMarketing(marketing);
        return this;
    }

    public void setMarketing(Boolean marketing) {
        this.marketing = marketing;
    }

    public Boolean getPreferences() {
        return this.preferences;
    }

    public Consent preferences(Boolean preferences) {
        this.setPreferences(preferences);
        return this;
    }

    public void setPreferences(Boolean preferences) {
        this.preferences = preferences;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consent)) {
            return false;
        }
        return getId() != null && getId().equals(((Consent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consent{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", necessary='" + getNecessary() + "'" +
            ", analytics='" + getAnalytics() + "'" +
            ", marketing='" + getMarketing() + "'" +
            ", preferences='" + getPreferences() + "'" +
            "}";
    }
}
