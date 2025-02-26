package fr.coaching.maseance.domain;

import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.domain.enumeration.GenrePersonne;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    private GenrePersonne genre;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "date_naissance", nullable = false)
    private Instant dateNaissance;

    @NotNull
    @Column(name = "adresse_email", nullable = false)
    private String adresseEmail;

    @Column(name = "numero_telephone")
    private String numeroTelephone;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    @NotNull
    @Column(name = "code_postal", nullable = false)
    private Integer codePostal;

    @NotNull
    @Column(name = "numero_et_nom_voie", nullable = false)
    private String numeroEtNomVoie;

    @Enumerated(EnumType.STRING)
    @Column(name = "preference_canal_seance")
    private CanalSeance preferenceCanalSeance;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "url_photo_profil")
    private String urlPhotoProfil;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenrePersonne getGenre() {
        return this.genre;
    }

    public Client genre(GenrePersonne genre) {
        this.setGenre(genre);
        return this;
    }

    public void setGenre(GenrePersonne genre) {
        this.genre = genre;
    }

    public String getNom() {
        return this.nom;
    }

    public Client nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Client prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Instant getDateNaissance() {
        return this.dateNaissance;
    }

    public Client dateNaissance(Instant dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresseEmail() {
        return this.adresseEmail;
    }

    public Client adresseEmail(String adresseEmail) {
        this.setAdresseEmail(adresseEmail);
        return this;
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public String getNumeroTelephone() {
        return this.numeroTelephone;
    }

    public Client numeroTelephone(String numeroTelephone) {
        this.setNumeroTelephone(numeroTelephone);
        return this;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getVille() {
        return this.ville;
    }

    public Client ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getCodePostal() {
        return this.codePostal;
    }

    public Client codePostal(Integer codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public void setCodePostal(Integer codePostal) {
        this.codePostal = codePostal;
    }

    public String getNumeroEtNomVoie() {
        return this.numeroEtNomVoie;
    }

    public Client numeroEtNomVoie(String numeroEtNomVoie) {
        this.setNumeroEtNomVoie(numeroEtNomVoie);
        return this;
    }

    public void setNumeroEtNomVoie(String numeroEtNomVoie) {
        this.numeroEtNomVoie = numeroEtNomVoie;
    }

    public CanalSeance getPreferenceCanalSeance() {
        return this.preferenceCanalSeance;
    }

    public Client preferenceCanalSeance(CanalSeance preferenceCanalSeance) {
        this.setPreferenceCanalSeance(preferenceCanalSeance);
        return this;
    }

    public void setPreferenceCanalSeance(CanalSeance preferenceCanalSeance) {
        this.preferenceCanalSeance = preferenceCanalSeance;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Client photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Client photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getUrlPhotoProfil() {
        return this.urlPhotoProfil;
    }

    public Client urlPhotoProfil(String urlPhotoProfil) {
        this.setUrlPhotoProfil(urlPhotoProfil);
        return this;
    }

    public void setUrlPhotoProfil(String urlPhotoProfil) {
        this.urlPhotoProfil = urlPhotoProfil;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Client user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return getId() != null && getId().equals(((Client) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", genre='" + getGenre() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", adresseEmail='" + getAdresseEmail() + "'" +
            ", numeroTelephone='" + getNumeroTelephone() + "'" +
            ", ville='" + getVille() + "'" +
            ", codePostal=" + getCodePostal() +
            ", numeroEtNomVoie='" + getNumeroEtNomVoie() + "'" +
            ", preferenceCanalSeance='" + getPreferenceCanalSeance() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", urlPhotoProfil='" + getUrlPhotoProfil() + "'" +
            "}";
    }
}
