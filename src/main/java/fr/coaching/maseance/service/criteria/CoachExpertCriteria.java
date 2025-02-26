package fr.coaching.maseance.service.criteria;

import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.domain.enumeration.GenrePersonne;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.coaching.maseance.domain.CoachExpert} entity. This class is used
 * in {@link fr.coaching.maseance.web.rest.CoachExpertResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /coach-experts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoachExpertCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GenrePersonne
     */
    public static class GenrePersonneFilter extends Filter<GenrePersonne> {

        public GenrePersonneFilter() {}

        public GenrePersonneFilter(GenrePersonneFilter filter) {
            super(filter);
        }

        @Override
        public GenrePersonneFilter copy() {
            return new GenrePersonneFilter(this);
        }
    }

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private GenrePersonneFilter civilite;

    private StringFilter nom;

    private StringFilter prenom;

    private InstantFilter dateNaissance;

    private StringFilter adresseEmail;

    private StringFilter numeroTelephone;

    private StringFilter ville;

    private IntegerFilter codePostal;

    private StringFilter numeroEtNomVoie;

    private LongFilter tarifActuel;

    private CanalSeanceFilter formatProposeSeance;

    private StringFilter urlPhotoProfil;

    private StringFilter bio;

    private LongFilter userId;

    private LongFilter lesAvisClientId;

    private LongFilter disponibiliteId;

    private LongFilter offreId;

    private LongFilter specialiteExpertiseId;

    private LongFilter diplomeId;

    private Boolean distinct;

    public CoachExpertCriteria() {}

    public CoachExpertCriteria(CoachExpertCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.civilite = other.optionalCivilite().map(GenrePersonneFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.prenom = other.optionalPrenom().map(StringFilter::copy).orElse(null);
        this.dateNaissance = other.optionalDateNaissance().map(InstantFilter::copy).orElse(null);
        this.adresseEmail = other.optionalAdresseEmail().map(StringFilter::copy).orElse(null);
        this.numeroTelephone = other.optionalNumeroTelephone().map(StringFilter::copy).orElse(null);
        this.ville = other.optionalVille().map(StringFilter::copy).orElse(null);
        this.codePostal = other.optionalCodePostal().map(IntegerFilter::copy).orElse(null);
        this.numeroEtNomVoie = other.optionalNumeroEtNomVoie().map(StringFilter::copy).orElse(null);
        this.tarifActuel = other.optionalTarifActuel().map(LongFilter::copy).orElse(null);
        this.formatProposeSeance = other.optionalFormatProposeSeance().map(CanalSeanceFilter::copy).orElse(null);
        this.urlPhotoProfil = other.optionalUrlPhotoProfil().map(StringFilter::copy).orElse(null);
        this.bio = other.optionalBio().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.lesAvisClientId = other.optionalLesAvisClientId().map(LongFilter::copy).orElse(null);
        this.disponibiliteId = other.optionalDisponibiliteId().map(LongFilter::copy).orElse(null);
        this.offreId = other.optionalOffreId().map(LongFilter::copy).orElse(null);
        this.specialiteExpertiseId = other.optionalSpecialiteExpertiseId().map(LongFilter::copy).orElse(null);
        this.diplomeId = other.optionalDiplomeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CoachExpertCriteria copy() {
        return new CoachExpertCriteria(this);
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

    public GenrePersonneFilter getCivilite() {
        return civilite;
    }

    public Optional<GenrePersonneFilter> optionalCivilite() {
        return Optional.ofNullable(civilite);
    }

    public GenrePersonneFilter civilite() {
        if (civilite == null) {
            setCivilite(new GenrePersonneFilter());
        }
        return civilite;
    }

    public void setCivilite(GenrePersonneFilter civilite) {
        this.civilite = civilite;
    }

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public Optional<StringFilter> optionalPrenom() {
        return Optional.ofNullable(prenom);
    }

    public StringFilter prenom() {
        if (prenom == null) {
            setPrenom(new StringFilter());
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public InstantFilter getDateNaissance() {
        return dateNaissance;
    }

    public Optional<InstantFilter> optionalDateNaissance() {
        return Optional.ofNullable(dateNaissance);
    }

    public InstantFilter dateNaissance() {
        if (dateNaissance == null) {
            setDateNaissance(new InstantFilter());
        }
        return dateNaissance;
    }

    public void setDateNaissance(InstantFilter dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public StringFilter getAdresseEmail() {
        return adresseEmail;
    }

    public Optional<StringFilter> optionalAdresseEmail() {
        return Optional.ofNullable(adresseEmail);
    }

    public StringFilter adresseEmail() {
        if (adresseEmail == null) {
            setAdresseEmail(new StringFilter());
        }
        return adresseEmail;
    }

    public void setAdresseEmail(StringFilter adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public StringFilter getNumeroTelephone() {
        return numeroTelephone;
    }

    public Optional<StringFilter> optionalNumeroTelephone() {
        return Optional.ofNullable(numeroTelephone);
    }

    public StringFilter numeroTelephone() {
        if (numeroTelephone == null) {
            setNumeroTelephone(new StringFilter());
        }
        return numeroTelephone;
    }

    public void setNumeroTelephone(StringFilter numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public StringFilter getVille() {
        return ville;
    }

    public Optional<StringFilter> optionalVille() {
        return Optional.ofNullable(ville);
    }

    public StringFilter ville() {
        if (ville == null) {
            setVille(new StringFilter());
        }
        return ville;
    }

    public void setVille(StringFilter ville) {
        this.ville = ville;
    }

    public IntegerFilter getCodePostal() {
        return codePostal;
    }

    public Optional<IntegerFilter> optionalCodePostal() {
        return Optional.ofNullable(codePostal);
    }

    public IntegerFilter codePostal() {
        if (codePostal == null) {
            setCodePostal(new IntegerFilter());
        }
        return codePostal;
    }

    public void setCodePostal(IntegerFilter codePostal) {
        this.codePostal = codePostal;
    }

    public StringFilter getNumeroEtNomVoie() {
        return numeroEtNomVoie;
    }

    public Optional<StringFilter> optionalNumeroEtNomVoie() {
        return Optional.ofNullable(numeroEtNomVoie);
    }

    public StringFilter numeroEtNomVoie() {
        if (numeroEtNomVoie == null) {
            setNumeroEtNomVoie(new StringFilter());
        }
        return numeroEtNomVoie;
    }

    public void setNumeroEtNomVoie(StringFilter numeroEtNomVoie) {
        this.numeroEtNomVoie = numeroEtNomVoie;
    }

    public LongFilter getTarifActuel() {
        return tarifActuel;
    }

    public Optional<LongFilter> optionalTarifActuel() {
        return Optional.ofNullable(tarifActuel);
    }

    public LongFilter tarifActuel() {
        if (tarifActuel == null) {
            setTarifActuel(new LongFilter());
        }
        return tarifActuel;
    }

    public void setTarifActuel(LongFilter tarifActuel) {
        this.tarifActuel = tarifActuel;
    }

    public CanalSeanceFilter getFormatProposeSeance() {
        return formatProposeSeance;
    }

    public Optional<CanalSeanceFilter> optionalFormatProposeSeance() {
        return Optional.ofNullable(formatProposeSeance);
    }

    public CanalSeanceFilter formatProposeSeance() {
        if (formatProposeSeance == null) {
            setFormatProposeSeance(new CanalSeanceFilter());
        }
        return formatProposeSeance;
    }

    public void setFormatProposeSeance(CanalSeanceFilter formatProposeSeance) {
        this.formatProposeSeance = formatProposeSeance;
    }

    public StringFilter getUrlPhotoProfil() {
        return urlPhotoProfil;
    }

    public Optional<StringFilter> optionalUrlPhotoProfil() {
        return Optional.ofNullable(urlPhotoProfil);
    }

    public StringFilter urlPhotoProfil() {
        if (urlPhotoProfil == null) {
            setUrlPhotoProfil(new StringFilter());
        }
        return urlPhotoProfil;
    }

    public void setUrlPhotoProfil(StringFilter urlPhotoProfil) {
        this.urlPhotoProfil = urlPhotoProfil;
    }

    public StringFilter getBio() {
        return bio;
    }

    public Optional<StringFilter> optionalBio() {
        return Optional.ofNullable(bio);
    }

    public StringFilter bio() {
        if (bio == null) {
            setBio(new StringFilter());
        }
        return bio;
    }

    public void setBio(StringFilter bio) {
        this.bio = bio;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getLesAvisClientId() {
        return lesAvisClientId;
    }

    public Optional<LongFilter> optionalLesAvisClientId() {
        return Optional.ofNullable(lesAvisClientId);
    }

    public LongFilter lesAvisClientId() {
        if (lesAvisClientId == null) {
            setLesAvisClientId(new LongFilter());
        }
        return lesAvisClientId;
    }

    public void setLesAvisClientId(LongFilter lesAvisClientId) {
        this.lesAvisClientId = lesAvisClientId;
    }

    public LongFilter getDisponibiliteId() {
        return disponibiliteId;
    }

    public Optional<LongFilter> optionalDisponibiliteId() {
        return Optional.ofNullable(disponibiliteId);
    }

    public LongFilter disponibiliteId() {
        if (disponibiliteId == null) {
            setDisponibiliteId(new LongFilter());
        }
        return disponibiliteId;
    }

    public void setDisponibiliteId(LongFilter disponibiliteId) {
        this.disponibiliteId = disponibiliteId;
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

    public LongFilter getSpecialiteExpertiseId() {
        return specialiteExpertiseId;
    }

    public Optional<LongFilter> optionalSpecialiteExpertiseId() {
        return Optional.ofNullable(specialiteExpertiseId);
    }

    public LongFilter specialiteExpertiseId() {
        if (specialiteExpertiseId == null) {
            setSpecialiteExpertiseId(new LongFilter());
        }
        return specialiteExpertiseId;
    }

    public void setSpecialiteExpertiseId(LongFilter specialiteExpertiseId) {
        this.specialiteExpertiseId = specialiteExpertiseId;
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
        final CoachExpertCriteria that = (CoachExpertCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(civilite, that.civilite) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(adresseEmail, that.adresseEmail) &&
            Objects.equals(numeroTelephone, that.numeroTelephone) &&
            Objects.equals(ville, that.ville) &&
            Objects.equals(codePostal, that.codePostal) &&
            Objects.equals(numeroEtNomVoie, that.numeroEtNomVoie) &&
            Objects.equals(tarifActuel, that.tarifActuel) &&
            Objects.equals(formatProposeSeance, that.formatProposeSeance) &&
            Objects.equals(urlPhotoProfil, that.urlPhotoProfil) &&
            Objects.equals(bio, that.bio) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(lesAvisClientId, that.lesAvisClientId) &&
            Objects.equals(disponibiliteId, that.disponibiliteId) &&
            Objects.equals(offreId, that.offreId) &&
            Objects.equals(specialiteExpertiseId, that.specialiteExpertiseId) &&
            Objects.equals(diplomeId, that.diplomeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            civilite,
            nom,
            prenom,
            dateNaissance,
            adresseEmail,
            numeroTelephone,
            ville,
            codePostal,
            numeroEtNomVoie,
            tarifActuel,
            formatProposeSeance,
            urlPhotoProfil,
            bio,
            userId,
            lesAvisClientId,
            disponibiliteId,
            offreId,
            specialiteExpertiseId,
            diplomeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoachExpertCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCivilite().map(f -> "civilite=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalPrenom().map(f -> "prenom=" + f + ", ").orElse("") +
            optionalDateNaissance().map(f -> "dateNaissance=" + f + ", ").orElse("") +
            optionalAdresseEmail().map(f -> "adresseEmail=" + f + ", ").orElse("") +
            optionalNumeroTelephone().map(f -> "numeroTelephone=" + f + ", ").orElse("") +
            optionalVille().map(f -> "ville=" + f + ", ").orElse("") +
            optionalCodePostal().map(f -> "codePostal=" + f + ", ").orElse("") +
            optionalNumeroEtNomVoie().map(f -> "numeroEtNomVoie=" + f + ", ").orElse("") +
            optionalTarifActuel().map(f -> "tarifActuel=" + f + ", ").orElse("") +
            optionalFormatProposeSeance().map(f -> "formatProposeSeance=" + f + ", ").orElse("") +
            optionalUrlPhotoProfil().map(f -> "urlPhotoProfil=" + f + ", ").orElse("") +
            optionalBio().map(f -> "bio=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalLesAvisClientId().map(f -> "lesAvisClientId=" + f + ", ").orElse("") +
            optionalDisponibiliteId().map(f -> "disponibiliteId=" + f + ", ").orElse("") +
            optionalOffreId().map(f -> "offreId=" + f + ", ").orElse("") +
            optionalSpecialiteExpertiseId().map(f -> "specialiteExpertiseId=" + f + ", ").orElse("") +
            optionalDiplomeId().map(f -> "diplomeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
