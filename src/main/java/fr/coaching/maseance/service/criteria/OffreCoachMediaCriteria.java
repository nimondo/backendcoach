package fr.coaching.maseance.service.criteria;

import fr.coaching.maseance.domain.enumeration.TypeMedia;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.coaching.maseance.domain.OffreCoachMedia} entity. This class is used
 * in {@link fr.coaching.maseance.web.rest.OffreCoachMediaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /offre-coach-medias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OffreCoachMediaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeMedia
     */
    public static class TypeMediaFilter extends Filter<TypeMedia> {

        public TypeMediaFilter() {}

        public TypeMediaFilter(TypeMediaFilter filter) {
            super(filter);
        }

        @Override
        public TypeMediaFilter copy() {
            return new TypeMediaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter urlMedia;

    private TypeMediaFilter typeMedia;

    private LongFilter offreCoachId;

    private Boolean distinct;

    public OffreCoachMediaCriteria() {}

    public OffreCoachMediaCriteria(OffreCoachMediaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.urlMedia = other.optionalUrlMedia().map(StringFilter::copy).orElse(null);
        this.typeMedia = other.optionalTypeMedia().map(TypeMediaFilter::copy).orElse(null);
        this.offreCoachId = other.optionalOffreCoachId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OffreCoachMediaCriteria copy() {
        return new OffreCoachMediaCriteria(this);
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

    public StringFilter getUrlMedia() {
        return urlMedia;
    }

    public Optional<StringFilter> optionalUrlMedia() {
        return Optional.ofNullable(urlMedia);
    }

    public StringFilter urlMedia() {
        if (urlMedia == null) {
            setUrlMedia(new StringFilter());
        }
        return urlMedia;
    }

    public void setUrlMedia(StringFilter urlMedia) {
        this.urlMedia = urlMedia;
    }

    public TypeMediaFilter getTypeMedia() {
        return typeMedia;
    }

    public Optional<TypeMediaFilter> optionalTypeMedia() {
        return Optional.ofNullable(typeMedia);
    }

    public TypeMediaFilter typeMedia() {
        if (typeMedia == null) {
            setTypeMedia(new TypeMediaFilter());
        }
        return typeMedia;
    }

    public void setTypeMedia(TypeMediaFilter typeMedia) {
        this.typeMedia = typeMedia;
    }

    public LongFilter getOffreCoachId() {
        return offreCoachId;
    }

    public Optional<LongFilter> optionalOffreCoachId() {
        return Optional.ofNullable(offreCoachId);
    }

    public LongFilter offreCoachId() {
        if (offreCoachId == null) {
            setOffreCoachId(new LongFilter());
        }
        return offreCoachId;
    }

    public void setOffreCoachId(LongFilter offreCoachId) {
        this.offreCoachId = offreCoachId;
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
        final OffreCoachMediaCriteria that = (OffreCoachMediaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(urlMedia, that.urlMedia) &&
            Objects.equals(typeMedia, that.typeMedia) &&
            Objects.equals(offreCoachId, that.offreCoachId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, urlMedia, typeMedia, offreCoachId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OffreCoachMediaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUrlMedia().map(f -> "urlMedia=" + f + ", ").orElse("") +
            optionalTypeMedia().map(f -> "typeMedia=" + f + ", ").orElse("") +
            optionalOffreCoachId().map(f -> "offreCoachId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
