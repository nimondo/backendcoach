package fr.coaching.maseance.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.coaching.maseance.domain.AvisClient} entity. This class is used
 * in {@link fr.coaching.maseance.web.rest.AvisClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /avis-clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AvisClientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateAvis;

    private IntegerFilter note;

    private StringFilter descriptionAvis;

    private LongFilter clientId;

    private LongFilter coachExpertId;

    private Boolean distinct;

    public AvisClientCriteria() {}

    public AvisClientCriteria(AvisClientCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dateAvis = other.optionalDateAvis().map(InstantFilter::copy).orElse(null);
        this.note = other.optionalNote().map(IntegerFilter::copy).orElse(null);
        this.descriptionAvis = other.optionalDescriptionAvis().map(StringFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(LongFilter::copy).orElse(null);
        this.coachExpertId = other.optionalCoachExpertId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AvisClientCriteria copy() {
        return new AvisClientCriteria(this);
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

    public InstantFilter getDateAvis() {
        return dateAvis;
    }

    public Optional<InstantFilter> optionalDateAvis() {
        return Optional.ofNullable(dateAvis);
    }

    public InstantFilter dateAvis() {
        if (dateAvis == null) {
            setDateAvis(new InstantFilter());
        }
        return dateAvis;
    }

    public void setDateAvis(InstantFilter dateAvis) {
        this.dateAvis = dateAvis;
    }

    public IntegerFilter getNote() {
        return note;
    }

    public Optional<IntegerFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public IntegerFilter note() {
        if (note == null) {
            setNote(new IntegerFilter());
        }
        return note;
    }

    public void setNote(IntegerFilter note) {
        this.note = note;
    }

    public StringFilter getDescriptionAvis() {
        return descriptionAvis;
    }

    public Optional<StringFilter> optionalDescriptionAvis() {
        return Optional.ofNullable(descriptionAvis);
    }

    public StringFilter descriptionAvis() {
        if (descriptionAvis == null) {
            setDescriptionAvis(new StringFilter());
        }
        return descriptionAvis;
    }

    public void setDescriptionAvis(StringFilter descriptionAvis) {
        this.descriptionAvis = descriptionAvis;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public Optional<LongFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public LongFilter clientId() {
        if (clientId == null) {
            setClientId(new LongFilter());
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getCoachExpertId() {
        return coachExpertId;
    }

    public Optional<LongFilter> optionalCoachExpertId() {
        return Optional.ofNullable(coachExpertId);
    }

    public LongFilter coachExpertId() {
        if (coachExpertId == null) {
            setCoachExpertId(new LongFilter());
        }
        return coachExpertId;
    }

    public void setCoachExpertId(LongFilter coachExpertId) {
        this.coachExpertId = coachExpertId;
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
        final AvisClientCriteria that = (AvisClientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateAvis, that.dateAvis) &&
            Objects.equals(note, that.note) &&
            Objects.equals(descriptionAvis, that.descriptionAvis) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(coachExpertId, that.coachExpertId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateAvis, note, descriptionAvis, clientId, coachExpertId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvisClientCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDateAvis().map(f -> "dateAvis=" + f + ", ").orElse("") +
            optionalNote().map(f -> "note=" + f + ", ").orElse("") +
            optionalDescriptionAvis().map(f -> "descriptionAvis=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalCoachExpertId().map(f -> "coachExpertId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
