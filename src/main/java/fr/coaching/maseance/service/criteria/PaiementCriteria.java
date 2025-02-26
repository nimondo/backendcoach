package fr.coaching.maseance.service.criteria;

import fr.coaching.maseance.domain.enumeration.StatutPaiement;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.coaching.maseance.domain.Paiement} entity. This class is used
 * in {@link fr.coaching.maseance.web.rest.PaiementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /paiements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutPaiement
     */
    public static class StatutPaiementFilter extends Filter<StatutPaiement> {

        public StatutPaiementFilter() {}

        public StatutPaiementFilter(StatutPaiementFilter filter) {
            super(filter);
        }

        @Override
        public StatutPaiementFilter copy() {
            return new StatutPaiementFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter horodatage;

    private StringFilter moyenPaiement;

    private StatutPaiementFilter statutPaiement;

    private StringFilter idStripe;

    private LongFilter factureId;

    private Boolean distinct;

    public PaiementCriteria() {}

    public PaiementCriteria(PaiementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.horodatage = other.optionalHorodatage().map(InstantFilter::copy).orElse(null);
        this.moyenPaiement = other.optionalMoyenPaiement().map(StringFilter::copy).orElse(null);
        this.statutPaiement = other.optionalStatutPaiement().map(StatutPaiementFilter::copy).orElse(null);
        this.idStripe = other.optionalIdStripe().map(StringFilter::copy).orElse(null);
        this.factureId = other.optionalFactureId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaiementCriteria copy() {
        return new PaiementCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getHorodatage() {
        return horodatage;
    }

    public Optional<InstantFilter> optionalHorodatage() {
        return Optional.ofNullable(horodatage);
    }

    public InstantFilter horodatage() {
        if (horodatage == null) {
            setHorodatage(new InstantFilter());
        }
        return horodatage;
    }

    public void setHorodatage(InstantFilter horodatage) {
        this.horodatage = horodatage;
    }

    public StringFilter getMoyenPaiement() {
        return moyenPaiement;
    }

    public Optional<StringFilter> optionalMoyenPaiement() {
        return Optional.ofNullable(moyenPaiement);
    }

    public StringFilter moyenPaiement() {
        if (moyenPaiement == null) {
            setMoyenPaiement(new StringFilter());
        }
        return moyenPaiement;
    }

    public void setMoyenPaiement(StringFilter moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

    public StatutPaiementFilter getStatutPaiement() {
        return statutPaiement;
    }

    public Optional<StatutPaiementFilter> optionalStatutPaiement() {
        return Optional.ofNullable(statutPaiement);
    }

    public StatutPaiementFilter statutPaiement() {
        if (statutPaiement == null) {
            setStatutPaiement(new StatutPaiementFilter());
        }
        return statutPaiement;
    }

    public void setStatutPaiement(StatutPaiementFilter statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public StringFilter getIdStripe() {
        return idStripe;
    }

    public Optional<StringFilter> optionalIdStripe() {
        return Optional.ofNullable(idStripe);
    }

    public StringFilter idStripe() {
        if (idStripe == null) {
            setIdStripe(new StringFilter());
        }
        return idStripe;
    }

    public void setIdStripe(StringFilter idStripe) {
        this.idStripe = idStripe;
    }

    public LongFilter getFactureId() {
        return factureId;
    }

    public Optional<LongFilter> optionalFactureId() {
        return Optional.ofNullable(factureId);
    }

    public LongFilter factureId() {
        if (factureId == null) {
            setFactureId(new LongFilter());
        }
        return factureId;
    }

    public void setFactureId(LongFilter factureId) {
        this.factureId = factureId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaiementCriteria that = (PaiementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(horodatage, that.horodatage) &&
            Objects.equals(moyenPaiement, that.moyenPaiement) &&
            Objects.equals(statutPaiement, that.statutPaiement) &&
            Objects.equals(idStripe, that.idStripe) &&
            Objects.equals(factureId, that.factureId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, horodatage, moyenPaiement, statutPaiement, idStripe, factureId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalHorodatage().map(f -> "horodatage=" + f + ", ").orElse("") +
            optionalMoyenPaiement().map(f -> "moyenPaiement=" + f + ", ").orElse("") +
            optionalStatutPaiement().map(f -> "statutPaiement=" + f + ", ").orElse("") +
            optionalIdStripe().map(f -> "idStripe=" + f + ", ").orElse("") +
            optionalFactureId().map(f -> "factureId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
