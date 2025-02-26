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
 * Criteria class for the {@link fr.coaching.maseance.domain.Client} entity. This class is used
 * in {@link fr.coaching.maseance.web.rest.ClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientCriteria implements Serializable, Criteria {

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

    private GenrePersonneFilter genre;

    private StringFilter nom;

    private StringFilter prenom;

    private InstantFilter dateNaissance;

    private StringFilter adresseEmail;

    private StringFilter numeroTelephone;

    private StringFilter ville;

    private IntegerFilter codePostal;

    private StringFilter numeroEtNomVoie;

    private CanalSeanceFilter preferenceCanalSeance;

    private StringFilter urlPhotoProfil;

    private LongFilter userId;

    private Boolean distinct;

    public ClientCriteria() {}

    public ClientCriteria(ClientCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.genre = other.optionalGenre().map(GenrePersonneFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.prenom = other.optionalPrenom().map(StringFilter::copy).orElse(null);
        this.dateNaissance = other.optionalDateNaissance().map(InstantFilter::copy).orElse(null);
        this.adresseEmail = other.optionalAdresseEmail().map(StringFilter::copy).orElse(null);
        this.numeroTelephone = other.optionalNumeroTelephone().map(StringFilter::copy).orElse(null);
        this.ville = other.optionalVille().map(StringFilter::copy).orElse(null);
        this.codePostal = other.optionalCodePostal().map(IntegerFilter::copy).orElse(null);
        this.numeroEtNomVoie = other.optionalNumeroEtNomVoie().map(StringFilter::copy).orElse(null);
        this.preferenceCanalSeance = other.optionalPreferenceCanalSeance().map(CanalSeanceFilter::copy).orElse(null);
        this.urlPhotoProfil = other.optionalUrlPhotoProfil().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClientCriteria copy() {
        return new ClientCriteria(this);
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

    public GenrePersonneFilter getGenre() {
        return genre;
    }

    public Optional<GenrePersonneFilter> optionalGenre() {
        return Optional.ofNullable(genre);
    }

    public GenrePersonneFilter genre() {
        if (genre == null) {
            setGenre(new GenrePersonneFilter());
        }
        return genre;
    }

    public void setGenre(GenrePersonneFilter genre) {
        this.genre = genre;
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

    public CanalSeanceFilter getPreferenceCanalSeance() {
        return preferenceCanalSeance;
    }

    public Optional<CanalSeanceFilter> optionalPreferenceCanalSeance() {
        return Optional.ofNullable(preferenceCanalSeance);
    }

    public CanalSeanceFilter preferenceCanalSeance() {
        if (preferenceCanalSeance == null) {
            setPreferenceCanalSeance(new CanalSeanceFilter());
        }
        return preferenceCanalSeance;
    }

    public void setPreferenceCanalSeance(CanalSeanceFilter preferenceCanalSeance) {
        this.preferenceCanalSeance = preferenceCanalSeance;
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
        final ClientCriteria that = (ClientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(genre, that.genre) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(adresseEmail, that.adresseEmail) &&
            Objects.equals(numeroTelephone, that.numeroTelephone) &&
            Objects.equals(ville, that.ville) &&
            Objects.equals(codePostal, that.codePostal) &&
            Objects.equals(numeroEtNomVoie, that.numeroEtNomVoie) &&
            Objects.equals(preferenceCanalSeance, that.preferenceCanalSeance) &&
            Objects.equals(urlPhotoProfil, that.urlPhotoProfil) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            genre,
            nom,
            prenom,
            dateNaissance,
            adresseEmail,
            numeroTelephone,
            ville,
            codePostal,
            numeroEtNomVoie,
            preferenceCanalSeance,
            urlPhotoProfil,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalGenre().map(f -> "genre=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalPrenom().map(f -> "prenom=" + f + ", ").orElse("") +
            optionalDateNaissance().map(f -> "dateNaissance=" + f + ", ").orElse("") +
            optionalAdresseEmail().map(f -> "adresseEmail=" + f + ", ").orElse("") +
            optionalNumeroTelephone().map(f -> "numeroTelephone=" + f + ", ").orElse("") +
            optionalVille().map(f -> "ville=" + f + ", ").orElse("") +
            optionalCodePostal().map(f -> "codePostal=" + f + ", ").orElse("") +
            optionalNumeroEtNomVoie().map(f -> "numeroEtNomVoie=" + f + ", ").orElse("") +
            optionalPreferenceCanalSeance().map(f -> "preferenceCanalSeance=" + f + ", ").orElse("") +
            optionalUrlPhotoProfil().map(f -> "urlPhotoProfil=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
