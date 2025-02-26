package fr.coaching.maseance.service.criteria;

import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.domain.enumeration.TypeSeance;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.coaching.maseance.domain.OffreCoach} entity. This class is used
 * in {@link fr.coaching.maseance.web.rest.OffreCoachResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /offre-coaches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OffreCoachCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CanalSeance
     */
    public static class CanalSeanceFilter extends Filter<CanalSeance> {

        public CanalSeanceFilter() {}

        public CanalSeanceFilter(CanalSeanceFilter filter) {
            super(filter);
        }

        @Override
        public CanalSeanceFilter copy() {
            return new CanalSeanceFilter(this);
        }
    }

    /**
     * Class for filtering TypeSeance
     */
    public static class TypeSeanceFilter extends Filter<TypeSeance> {

        public TypeSeanceFilter() {}

        public TypeSeanceFilter(TypeSeanceFilter filter) {
            super(filter);
        }

        @Override
        public TypeSeanceFilter copy() {
            return new TypeSeanceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private CanalSeanceFilter canalSeance;

    private TypeSeanceFilter typeSeance;

    private StringFilter synthese;

    private StringFilter descriptionDetaillee;

    private LongFilter tarif;

    private IntegerFilter duree;

    private StringFilter descriptionDiplome;

    private LongFilter mediaId;

    private LongFilter specialiteId;

    private LongFilter coachExpertId;

    private Boolean distinct;

    public OffreCoachCriteria() {}

    public OffreCoachCriteria(OffreCoachCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.canalSeance = other.optionalCanalSeance().map(CanalSeanceFilter::copy).orElse(null);
        this.typeSeance = other.optionalTypeSeance().map(TypeSeanceFilter::copy).orElse(null);
        this.synthese = other.optionalSynthese().map(StringFilter::copy).orElse(null);
        this.descriptionDetaillee = other.optionalDescriptionDetaillee().map(StringFilter::copy).orElse(null);
        this.tarif = other.optionalTarif().map(LongFilter::copy).orElse(null);
        this.duree = other.optionalDuree().map(IntegerFilter::copy).orElse(null);
        this.descriptionDiplome = other.optionalDescriptionDiplome().map(StringFilter::copy).orElse(null);
        this.mediaId = other.optionalMediaId().map(LongFilter::copy).orElse(null);
        this.specialiteId = other.optionalSpecialiteId().map(LongFilter::copy).orElse(null);
        this.coachExpertId = other.optionalCoachExpertId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OffreCoachCriteria copy() {
        return new OffreCoachCriteria(this);
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

    public CanalSeanceFilter getCanalSeance() {
        return canalSeance;
    }

    public Optional<CanalSeanceFilter> optionalCanalSeance() {
        return Optional.ofNullable(canalSeance);
    }

    public CanalSeanceFilter canalSeance() {
        if (canalSeance == null) {
            setCanalSeance(new CanalSeanceFilter());
        }
        return canalSeance;
    }

    public void setCanalSeance(CanalSeanceFilter canalSeance) {
        this.canalSeance = canalSeance;
    }

    public TypeSeanceFilter getTypeSeance() {
        return typeSeance;
    }

    public Optional<TypeSeanceFilter> optionalTypeSeance() {
        return Optional.ofNullable(typeSeance);
    }

    public TypeSeanceFilter typeSeance() {
        if (typeSeance == null) {
            setTypeSeance(new TypeSeanceFilter());
        }
        return typeSeance;
    }

    public void setTypeSeance(TypeSeanceFilter typeSeance) {
        this.typeSeance = typeSeance;
    }

    public StringFilter getSynthese() {
        return synthese;
    }

    public Optional<StringFilter> optionalSynthese() {
        return Optional.ofNullable(synthese);
    }

    public StringFilter synthese() {
        if (synthese == null) {
            setSynthese(new StringFilter());
        }
        return synthese;
    }

    public void setSynthese(StringFilter synthese) {
        this.synthese = synthese;
    }

    public StringFilter getDescriptionDetaillee() {
        return descriptionDetaillee;
    }

    public Optional<StringFilter> optionalDescriptionDetaillee() {
        return Optional.ofNullable(descriptionDetaillee);
    }

    public StringFilter descriptionDetaillee() {
        if (descriptionDetaillee == null) {
            setDescriptionDetaillee(new StringFilter());
        }
        return descriptionDetaillee;
    }

    public void setDescriptionDetaillee(StringFilter descriptionDetaillee) {
        this.descriptionDetaillee = descriptionDetaillee;
    }

    public LongFilter getTarif() {
        return tarif;
    }

    public Optional<LongFilter> optionalTarif() {
        return Optional.ofNullable(tarif);
    }

    public LongFilter tarif() {
        if (tarif == null) {
            setTarif(new LongFilter());
        }
        return tarif;
    }

    public void setTarif(LongFilter tarif) {
        this.tarif = tarif;
    }

    public IntegerFilter getDuree() {
        return duree;
    }

    public Optional<IntegerFilter> optionalDuree() {
        return Optional.ofNullable(duree);
    }

    public IntegerFilter duree() {
        if (duree == null) {
            setDuree(new IntegerFilter());
        }
        return duree;
    }

    public void setDuree(IntegerFilter duree) {
        this.duree = duree;
    }

    public StringFilter getDescriptionDiplome() {
        return descriptionDiplome;
    }

    public Optional<StringFilter> optionalDescriptionDiplome() {
        return Optional.ofNullable(descriptionDiplome);
    }

    public StringFilter descriptionDiplome() {
        if (descriptionDiplome == null) {
            setDescriptionDiplome(new StringFilter());
        }
        return descriptionDiplome;
    }

    public void setDescriptionDiplome(StringFilter descriptionDiplome) {
        this.descriptionDiplome = descriptionDiplome;
    }

    public LongFilter getMediaId() {
        return mediaId;
    }

    public Optional<LongFilter> optionalMediaId() {
        return Optional.ofNullable(mediaId);
    }

    public LongFilter mediaId() {
        if (mediaId == null) {
            setMediaId(new LongFilter());
        }
        return mediaId;
    }

    public void setMediaId(LongFilter mediaId) {
        this.mediaId = mediaId;
    }

    public LongFilter getSpecialiteId() {
        return specialiteId;
    }

    public Optional<LongFilter> optionalSpecialiteId() {
        return Optional.ofNullable(specialiteId);
    }

    public LongFilter specialiteId() {
        if (specialiteId == null) {
            setSpecialiteId(new LongFilter());
        }
        return specialiteId;
    }

    public void setSpecialiteId(LongFilter specialiteId) {
        this.specialiteId = specialiteId;
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
        final OffreCoachCriteria that = (OffreCoachCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(canalSeance, that.canalSeance) &&
            Objects.equals(typeSeance, that.typeSeance) &&
            Objects.equals(synthese, that.synthese) &&
            Objects.equals(descriptionDetaillee, that.descriptionDetaillee) &&
            Objects.equals(tarif, that.tarif) &&
            Objects.equals(duree, that.duree) &&
            Objects.equals(descriptionDiplome, that.descriptionDiplome) &&
            Objects.equals(mediaId, that.mediaId) &&
            Objects.equals(specialiteId, that.specialiteId) &&
            Objects.equals(coachExpertId, that.coachExpertId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            canalSeance,
            typeSeance,
            synthese,
            descriptionDetaillee,
            tarif,
            duree,
            descriptionDiplome,
            mediaId,
            specialiteId,
            coachExpertId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OffreCoachCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCanalSeance().map(f -> "canalSeance=" + f + ", ").orElse("") +
            optionalTypeSeance().map(f -> "typeSeance=" + f + ", ").orElse("") +
            optionalSynthese().map(f -> "synthese=" + f + ", ").orElse("") +
            optionalDescriptionDetaillee().map(f -> "descriptionDetaillee=" + f + ", ").orElse("") +
            optionalTarif().map(f -> "tarif=" + f + ", ").orElse("") +
            optionalDuree().map(f -> "duree=" + f + ", ").orElse("") +
            optionalDescriptionDiplome().map(f -> "descriptionDiplome=" + f + ", ").orElse("") +
            optionalMediaId().map(f -> "mediaId=" + f + ", ").orElse("") +
            optionalSpecialiteId().map(f -> "specialiteId=" + f + ", ").orElse("") +
            optionalCoachExpertId().map(f -> "coachExpertId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
