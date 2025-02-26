package fr.coaching.maseance.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.coaching.maseance.domain.SpecialiteExpertise} entity. This class is used
 * in {@link fr.coaching.maseance.web.rest.SpecialiteExpertiseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /specialite-expertises?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecialiteExpertiseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter specialite;

    private StringFilter specialiteDescription;

    private LongFilter tarifMoyenHeure;

    private StringFilter dureeTarif;

    private BooleanFilter diplomeRequis;

    private StringFilter urlPhoto;

    private LongFilter offreId;

    private LongFilter diplomeId;

    private LongFilter themeExpertiseId;

    private LongFilter coachExpertId;

    private Boolean distinct;

    public SpecialiteExpertiseCriteria() {}

    public SpecialiteExpertiseCriteria(SpecialiteExpertiseCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.specialite = other.optionalSpecialite().map(StringFilter::copy).orElse(null);
        this.specialiteDescription = other.optionalSpecialiteDescription().map(StringFilter::copy).orElse(null);
        this.tarifMoyenHeure = other.optionalTarifMoyenHeure().map(LongFilter::copy).orElse(null);
        this.dureeTarif = other.optionalDureeTarif().map(StringFilter::copy).orElse(null);
        this.diplomeRequis = other.optionalDiplomeRequis().map(BooleanFilter::copy).orElse(null);
        this.urlPhoto = other.optionalUrlPhoto().map(StringFilter::copy).orElse(null);
        this.offreId = other.optionalOffreId().map(LongFilter::copy).orElse(null);
        this.diplomeId = other.optionalDiplomeId().map(LongFilter::copy).orElse(null);
        this.themeExpertiseId = other.optionalThemeExpertiseId().map(LongFilter::copy).orElse(null);
        this.coachExpertId = other.optionalCoachExpertId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SpecialiteExpertiseCriteria copy() {
        return new SpecialiteExpertiseCriteria(this);
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

    public StringFilter getSpecialite() {
        return specialite;
    }

    public Optional<StringFilter> optionalSpecialite() {
        return Optional.ofNullable(specialite);
    }

    public StringFilter specialite() {
        if (specialite == null) {
            setSpecialite(new StringFilter());
        }
        return specialite;
    }

    public void setSpecialite(StringFilter specialite) {
        this.specialite = specialite;
    }

    public StringFilter getSpecialiteDescription() {
        return specialiteDescription;
    }

    public Optional<StringFilter> optionalSpecialiteDescription() {
        return Optional.ofNullable(specialiteDescription);
    }

    public StringFilter specialiteDescription() {
        if (specialiteDescription == null) {
            setSpecialiteDescription(new StringFilter());
        }
        return specialiteDescription;
    }

    public void setSpecialiteDescription(StringFilter specialiteDescription) {
        this.specialiteDescription = specialiteDescription;
    }

    public LongFilter getTarifMoyenHeure() {
        return tarifMoyenHeure;
    }

    public Optional<LongFilter> optionalTarifMoyenHeure() {
        return Optional.ofNullable(tarifMoyenHeure);
    }

    public LongFilter tarifMoyenHeure() {
        if (tarifMoyenHeure == null) {
            setTarifMoyenHeure(new LongFilter());
        }
        return tarifMoyenHeure;
    }

    public void setTarifMoyenHeure(LongFilter tarifMoyenHeure) {
        this.tarifMoyenHeure = tarifMoyenHeure;
    }

    public StringFilter getDureeTarif() {
        return dureeTarif;
    }

    public Optional<StringFilter> optionalDureeTarif() {
        return Optional.ofNullable(dureeTarif);
    }

    public StringFilter dureeTarif() {
        if (dureeTarif == null) {
            setDureeTarif(new StringFilter());
        }
        return dureeTarif;
    }

    public void setDureeTarif(StringFilter dureeTarif) {
        this.dureeTarif = dureeTarif;
    }

    public BooleanFilter getDiplomeRequis() {
        return diplomeRequis;
    }

    public Optional<BooleanFilter> optionalDiplomeRequis() {
        return Optional.ofNullable(diplomeRequis);
    }

    public BooleanFilter diplomeRequis() {
        if (diplomeRequis == null) {
            setDiplomeRequis(new BooleanFilter());
        }
        return diplomeRequis;
    }

    public void setDiplomeRequis(BooleanFilter diplomeRequis) {
        this.diplomeRequis = diplomeRequis;
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

    public LongFilter getOffreId() {
        return offreId;
    }

    public Optional<LongFilter> optionalOffreId() {
        return Optional.ofNullable(offreId);
    }

    public LongFilter offreId() {
        if (offreId == null) {
            setOffreId(new LongFilter());
        }
        return offreId;
    }

    public void setOffreId(LongFilter offreId) {
        this.offreId = offreId;
    }

    public LongFilter getDiplomeId() {
        return diplomeId;
    }

    public Optional<LongFilter> optionalDiplomeId() {
        return Optional.ofNullable(diplomeId);
    }

    public LongFilter diplomeId() {
        if (diplomeId == null) {
            setDiplomeId(new LongFilter());
        }
        return diplomeId;
    }

    public void setDiplomeId(LongFilter diplomeId) {
        this.diplomeId = diplomeId;
    }

    public LongFilter getThemeExpertiseId() {
        return themeExpertiseId;
    }

    public Optional<LongFilter> optionalThemeExpertiseId() {
        return Optional.ofNullable(themeExpertiseId);
    }

    public LongFilter themeExpertiseId() {
        if (themeExpertiseId == null) {
            setThemeExpertiseId(new LongFilter());
        }
        return themeExpertiseId;
    }

    public void setThemeExpertiseId(LongFilter themeExpertiseId) {
        this.themeExpertiseId = themeExpertiseId;
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
        final SpecialiteExpertiseCriteria that = (SpecialiteExpertiseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(specialite, that.specialite) &&
            Objects.equals(specialiteDescription, that.specialiteDescription) &&
            Objects.equals(tarifMoyenHeure, that.tarifMoyenHeure) &&
            Objects.equals(dureeTarif, that.dureeTarif) &&
            Objects.equals(diplomeRequis, that.diplomeRequis) &&
            Objects.equals(urlPhoto, that.urlPhoto) &&
            Objects.equals(offreId, that.offreId) &&
            Objects.equals(diplomeId, that.diplomeId) &&
            Objects.equals(themeExpertiseId, that.themeExpertiseId) &&
            Objects.equals(coachExpertId, that.coachExpertId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            specialite,
            specialiteDescription,
            tarifMoyenHeure,
            dureeTarif,
            diplomeRequis,
            urlPhoto,
            offreId,
            diplomeId,
            themeExpertiseId,
            coachExpertId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialiteExpertiseCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSpecialite().map(f -> "specialite=" + f + ", ").orElse("") +
            optionalSpecialiteDescription().map(f -> "specialiteDescription=" + f + ", ").orElse("") +
            optionalTarifMoyenHeure().map(f -> "tarifMoyenHeure=" + f + ", ").orElse("") +
            optionalDureeTarif().map(f -> "dureeTarif=" + f + ", ").orElse("") +
            optionalDiplomeRequis().map(f -> "diplomeRequis=" + f + ", ").orElse("") +
            optionalUrlPhoto().map(f -> "urlPhoto=" + f + ", ").orElse("") +
            optionalOffreId().map(f -> "offreId=" + f + ", ").orElse("") +
            optionalDiplomeId().map(f -> "diplomeId=" + f + ", ").orElse("") +
            optionalThemeExpertiseId().map(f -> "themeExpertiseId=" + f + ", ").orElse("") +
            optionalCoachExpertId().map(f -> "coachExpertId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
