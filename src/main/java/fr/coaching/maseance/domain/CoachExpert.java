package fr.coaching.maseance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.domain.enumeration.GenrePersonne;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CoachExpert.
 */
@Entity
@Table(name = "coach_expert")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoachExpert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "civilite", nullable = false)
    private GenrePersonne civilite;

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

    @NotNull
    @Column(name = "tarif_actuel", nullable = false)
    private Long tarifActuel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "format_propose_seance", nullable = false)
    private CanalSeance formatProposeSeance;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @NotNull
    @Column(name = "url_photo_profil", nullable = false)
    private String urlPhotoProfil;

    @Column(name = "bio")
    private String bio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coachExpert")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "coachExpert" }, allowSetters = true)
    private Set<AvisClient> lesAvisClients = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coachExpert")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coachExpert" }, allowSetters = true)
    private Set<Disponibilite> disponibilites = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coachExpert")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "media", "specialite", "coachExpert" }, allowSetters = true)
    private Set<OffreCoach> offres = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_coach_expert__specialite_expertise",
        joinColumns = @JoinColumn(name = "coach_expert_id"),
        inverseJoinColumns = @JoinColumn(name = "specialite_expertise_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "offres", "diplome", "themeExpertise", "coachExperts" }, allowSetters = true)
    private Set<SpecialiteExpertise> specialiteExpertises = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_coach_expert__diplome",
        joinColumns = @JoinColumn(name = "coach_expert_id"),
        inverseJoinColumns = @JoinColumn(name = "diplome_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coachExperts" }, allowSetters = true)
    private Set<Diplome> diplomes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CoachExpert id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenrePersonne getCivilite() {
        return this.civilite;
    }

    public CoachExpert civilite(GenrePersonne civilite) {
        this.setCivilite(civilite);
        return this;
    }

    public void setCivilite(GenrePersonne civilite) {
        this.civilite = civilite;
    }

    public String getNom() {
        return this.nom;
    }

    public CoachExpert nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public CoachExpert prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Instant getDateNaissance() {
        return this.dateNaissance;
    }

    public CoachExpert dateNaissance(Instant dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresseEmail() {
        return this.adresseEmail;
    }

    public CoachExpert adresseEmail(String adresseEmail) {
        this.setAdresseEmail(adresseEmail);
        return this;
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public String getNumeroTelephone() {
        return this.numeroTelephone;
    }

    public CoachExpert numeroTelephone(String numeroTelephone) {
        this.setNumeroTelephone(numeroTelephone);
        return this;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getVille() {
        return this.ville;
    }

    public CoachExpert ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getCodePostal() {
        return this.codePostal;
    }

    public CoachExpert codePostal(Integer codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public void setCodePostal(Integer codePostal) {
        this.codePostal = codePostal;
    }

    public String getNumeroEtNomVoie() {
        return this.numeroEtNomVoie;
    }

    public CoachExpert numeroEtNomVoie(String numeroEtNomVoie) {
        this.setNumeroEtNomVoie(numeroEtNomVoie);
        return this;
    }

    public void setNumeroEtNomVoie(String numeroEtNomVoie) {
        this.numeroEtNomVoie = numeroEtNomVoie;
    }

    public Long getTarifActuel() {
        return this.tarifActuel;
    }

    public CoachExpert tarifActuel(Long tarifActuel) {
        this.setTarifActuel(tarifActuel);
        return this;
    }

    public void setTarifActuel(Long tarifActuel) {
        this.tarifActuel = tarifActuel;
    }

    public CanalSeance getFormatProposeSeance() {
        return this.formatProposeSeance;
    }

    public CoachExpert formatProposeSeance(CanalSeance formatProposeSeance) {
        this.setFormatProposeSeance(formatProposeSeance);
        return this;
    }

    public void setFormatProposeSeance(CanalSeance formatProposeSeance) {
        this.formatProposeSeance = formatProposeSeance;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public CoachExpert photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public CoachExpert photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getUrlPhotoProfil() {
        return this.urlPhotoProfil;
    }

    public CoachExpert urlPhotoProfil(String urlPhotoProfil) {
        this.setUrlPhotoProfil(urlPhotoProfil);
        return this;
    }

    public void setUrlPhotoProfil(String urlPhotoProfil) {
        this.urlPhotoProfil = urlPhotoProfil;
    }

    public String getBio() {
        return this.bio;
    }

    public CoachExpert bio(String bio) {
        this.setBio(bio);
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CoachExpert user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<AvisClient> getLesAvisClients() {
        return this.lesAvisClients;
    }

    public void setLesAvisClients(Set<AvisClient> avisClients) {
        if (this.lesAvisClients != null) {
            this.lesAvisClients.forEach(i -> i.setCoachExpert(null));
        }
        if (avisClients != null) {
            avisClients.forEach(i -> i.setCoachExpert(this));
        }
        this.lesAvisClients = avisClients;
    }

    public CoachExpert lesAvisClients(Set<AvisClient> avisClients) {
        this.setLesAvisClients(avisClients);
        return this;
    }

    public CoachExpert addLesAvisClient(AvisClient avisClient) {
        this.lesAvisClients.add(avisClient);
        avisClient.setCoachExpert(this);
        return this;
    }

    public CoachExpert removeLesAvisClient(AvisClient avisClient) {
        this.lesAvisClients.remove(avisClient);
        avisClient.setCoachExpert(null);
        return this;
    }

    public Set<Disponibilite> getDisponibilites() {
        return this.disponibilites;
    }

    public void setDisponibilites(Set<Disponibilite> disponibilites) {
        if (this.disponibilites != null) {
            this.disponibilites.forEach(i -> i.setCoachExpert(null));
        }
        if (disponibilites != null) {
            disponibilites.forEach(i -> i.setCoachExpert(this));
        }
        this.disponibilites = disponibilites;
    }

    public CoachExpert disponibilites(Set<Disponibilite> disponibilites) {
        this.setDisponibilites(disponibilites);
        return this;
    }

    public CoachExpert addDisponibilite(Disponibilite disponibilite) {
        this.disponibilites.add(disponibilite);
        disponibilite.setCoachExpert(this);
        return this;
    }

    public CoachExpert removeDisponibilite(Disponibilite disponibilite) {
        this.disponibilites.remove(disponibilite);
        disponibilite.setCoachExpert(null);
        return this;
    }

    public Set<OffreCoach> getOffres() {
        return this.offres;
    }

    public void setOffres(Set<OffreCoach> offreCoaches) {
        if (this.offres != null) {
            this.offres.forEach(i -> i.setCoachExpert(null));
        }
        if (offreCoaches != null) {
            offreCoaches.forEach(i -> i.setCoachExpert(this));
        }
        this.offres = offreCoaches;
    }

    public CoachExpert offres(Set<OffreCoach> offreCoaches) {
        this.setOffres(offreCoaches);
        return this;
    }

    public CoachExpert addOffre(OffreCoach offreCoach) {
        this.offres.add(offreCoach);
        offreCoach.setCoachExpert(this);
        return this;
    }

    public CoachExpert removeOffre(OffreCoach offreCoach) {
        this.offres.remove(offreCoach);
        offreCoach.setCoachExpert(null);
        return this;
    }

    public Set<SpecialiteExpertise> getSpecialiteExpertises() {
        return this.specialiteExpertises;
    }

    public void setSpecialiteExpertises(Set<SpecialiteExpertise> specialiteExpertises) {
        this.specialiteExpertises = specialiteExpertises;
    }

    public CoachExpert specialiteExpertises(Set<SpecialiteExpertise> specialiteExpertises) {
        this.setSpecialiteExpertises(specialiteExpertises);
        return this;
    }

    public CoachExpert addSpecialiteExpertise(SpecialiteExpertise specialiteExpertise) {
        this.specialiteExpertises.add(specialiteExpertise);
        return this;
    }

    public CoachExpert removeSpecialiteExpertise(SpecialiteExpertise specialiteExpertise) {
        this.specialiteExpertises.remove(specialiteExpertise);
        return this;
    }

    public Set<Diplome> getDiplomes() {
        return this.diplomes;
    }

    public void setDiplomes(Set<Diplome> diplomes) {
        this.diplomes = diplomes;
    }

    public CoachExpert diplomes(Set<Diplome> diplomes) {
        this.setDiplomes(diplomes);
        return this;
    }

    public CoachExpert addDiplome(Diplome diplome) {
        this.diplomes.add(diplome);
        return this;
    }

    public CoachExpert removeDiplome(Diplome diplome) {
        this.diplomes.remove(diplome);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoachExpert)) {
            return false;
        }
        return getId() != null && getId().equals(((CoachExpert) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoachExpert{" +
            "id=" + getId() +
            ", civilite='" + getCivilite() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", adresseEmail='" + getAdresseEmail() + "'" +
            ", numeroTelephone='" + getNumeroTelephone() + "'" +
            ", ville='" + getVille() + "'" +
            ", codePostal=" + getCodePostal() +
            ", numeroEtNomVoie='" + getNumeroEtNomVoie() + "'" +
            ", tarifActuel=" + getTarifActuel() +
            ", formatProposeSeance='" + getFormatProposeSeance() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", urlPhotoProfil='" + getUrlPhotoProfil() + "'" +
            ", bio='" + getBio() + "'" +
            "}";
    }
}
