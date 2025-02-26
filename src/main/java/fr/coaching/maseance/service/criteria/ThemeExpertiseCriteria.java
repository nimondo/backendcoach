package fr.coaching.maseance.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.coaching.maseance.domain.ThemeExpertise} entity. This class is used
 * in {@link fr.coaching.maseance.web.rest.ThemeExpertiseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /theme-expertises?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ThemeExpertiseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelleExpertise;

    private StringFilter urlPhoto;

    private Boolean distinct;

    public ThemeExpertiseCriteria() {}

    public ThemeExpertiseCriteria(ThemeExpertiseCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.libelleExpertise = other.optionalLibelleExpertise().map(StringFilter::copy).orElse(null);
        this.urlPhoto = other.optionalUrlPhoto().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ThemeExpertiseCriteria copy() {
        return new ThemeExpertiseCriteria(this);
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

    public StringFilter getLibelleExpertise() {
        return libelleExpertise;
    }

    public Optional<StringFilter> optionalLibelleExpertise() {
        return Optional.ofNullable(libelleExpertise);
    }

    public StringFilter libelleExpertise() {
        if (libelleExpertise == null) {
            setLibelleExpertise(new StringFilter());
        }
        return libelleExpertise;
    }

    public void setLibelleExpertise(StringFilter libelleExpertise) {
        this.libelleExpertise = libelleExpertise;
    }

    public StringFilter getUrlPhoto() {
        return urlPhoto;
    }

    public Optional<StringFilter> optionalUrlPhoto() {
        return Optional.ofNullable(urlPhoto);
    }

    public StringFilter urlPhoto() {
        if (urlPhoto == null) {
            setUrlPhoto(new StringFilter());
        }
        return urlPhoto;
    }

    public void setUrlPhoto(StringFilter urlPhoto) {
        this.urlPhoto = urlPhoto;
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
        final ThemeExpertiseCriteria that = (ThemeExpertiseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelleExpertise, that.libelleExpertise) &&
            Objects.equals(urlPhoto, that.urlPhoto) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelleExpertise, urlPhoto, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThemeExpertiseCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLibelleExpertise().map(f -> "libelleExpertise=" + f + ", ").orElse("") +
            optionalUrlPhoto().map(f -> "urlPhoto=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
