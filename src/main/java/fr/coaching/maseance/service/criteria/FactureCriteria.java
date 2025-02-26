package fr.coaching.maseance.service.criteria;

import fr.coaching.maseance.domain.enumeration.TypeFacture;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.coaching.maseance.domain.Facture} entity. This class is used
 * in {@link fr.coaching.maseance.web.rest.FactureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /factures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactureCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeFacture
     */
    public static class TypeFactureFilter extends Filter<TypeFacture> {

        public TypeFactureFilter() {}

        public TypeFactureFilter(TypeFactureFilter filter) {
            super(filter);
        }

        @Override
        public TypeFactureFilter copy() {
            return new TypeFactureFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TypeFactureFilter typeFacture;

    private InstantFilter dateComptableFacture;

    private DoubleFilter montant;

    private DoubleFilter tva;

    private LongFilter paiementId;

    private LongFilter seanceReservationCoachId;

    private Boolean distinct;

    public FactureCriteria() {}

    public FactureCriteria(FactureCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.typeFacture = other.optionalTypeFacture().map(TypeFactureFilter::copy).orElse(null);
        this.dateComptableFacture = other.optionalDateComptableFacture().map(InstantFilter::copy).orElse(null);
        this.montant = other.optionalMontant().map(DoubleFilter::copy).orElse(null);
        this.tva = other.optionalTva().map(DoubleFilter::copy).orElse(null);
        this.paiementId = other.optionalPaiementId().map(LongFilter::copy).orElse(null);
        this.seanceReservationCoachId = other.optionalSeanceReservationCoachId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FactureCriteria copy() {
        return new FactureCriteria(this);
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

    public TypeFactureFilter getTypeFacture() {
        return typeFacture;
    }

    public Optional<TypeFactureFilter> optionalTypeFacture() {
        return Optional.ofNullable(typeFacture);
    }

    public TypeFactureFilter typeFacture() {
        if (typeFacture == null) {
            setTypeFacture(new TypeFactureFilter());
        }
        return typeFacture;
    }

    public void setTypeFacture(TypeFactureFilter typeFacture) {
        this.typeFacture = typeFacture;
    }

    public InstantFilter getDateComptableFacture() {
        return dateComptableFacture;
    }

    public Optional<InstantFilter> optionalDateComptableFacture() {
        return Optional.ofNullable(dateComptableFacture);
    }

    public InstantFilter dateComptableFacture() {
        if (dateComptableFacture == null) {
            setDateComptableFacture(new InstantFilter());
        }
        return dateComptableFacture;
    }

    public void setDateComptableFacture(InstantFilter dateComptableFacture) {
        this.dateComptableFacture = dateComptableFacture;
    }

    public DoubleFilter getMontant() {
        return montant;
    }

    public Optional<DoubleFilter> optionalMontant() {
        return Optional.ofNullable(montant);
    }

    public DoubleFilter montant() {
        if (montant == null) {
            setMontant(new DoubleFilter());
        }
        return montant;
    }

    public void setMontant(DoubleFilter montant) {
        this.montant = montant;
    }

    public DoubleFilter getTva() {
        return tva;
    }

    public Optional<DoubleFilter> optionalTva() {
        return Optional.ofNullable(tva);
    }

    public DoubleFilter tva() {
        if (tva == null) {
            setTva(new DoubleFilter());
        }
        return tva;
    }

    public void setTva(DoubleFilter tva) {
        this.tva = tva;
    }

    public LongFilter getPaiementId() {
        return paiementId;
    }

    public Optional<LongFilter> optionalPaiementId() {
        return Optional.ofNullable(paiementId);
    }

    public LongFilter paiementId() {
        if (paiementId == null) {
            setPaiementId(new LongFilter());
        }
        return paiementId;
    }

    public void setPaiementId(LongFilter paiementId) {
        this.paiementId = paiementId;
    }

    public LongFilter getSeanceReservationCoachId() {
        return seanceReservationCoachId;
    }

    public Optional<LongFilter> optionalSeanceReservationCoachId() {
        return Optional.ofNullable(seanceReservationCoachId);
    }

    public LongFilter seanceReservationCoachId() {
        if (seanceReservationCoachId == null) {
            setSeanceReservationCoachId(new LongFilter());
        }
        return seanceReservationCoachId;
    }

    public void setSeanceReservationCoachId(LongFilter seanceReservationCoachId) {
        this.seanceReservationCoachId = seanceReservationCoachId;
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
        final FactureCriteria that = (FactureCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(typeFacture, that.typeFacture) &&
            Objects.equals(dateComptableFacture, that.dateComptableFacture) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(tva, that.tva) &&
            Objects.equals(paiementId, that.paiementId) &&
            Objects.equals(seanceReservationCoachId, that.seanceReservationCoachId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeFacture, dateComptableFacture, montant, tva, paiementId, seanceReservationCoachId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactureCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTypeFacture().map(f -> "typeFacture=" + f + ", ").orElse("") +
            optionalDateComptableFacture().map(f -> "dateComptableFacture=" + f + ", ").orElse("") +
            optionalMontant().map(f -> "montant=" + f + ", ").orElse("") +
            optionalTva().map(f -> "tva=" + f + ", ").orElse("") +
            optionalPaiementId().map(f -> "paiementId=" + f + ", ").orElse("") +
            optionalSeanceReservationCoachId().map(f -> "seanceReservationCoachId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
