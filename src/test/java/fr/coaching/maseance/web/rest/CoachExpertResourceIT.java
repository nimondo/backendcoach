package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.CoachExpertAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.CoachExpert;
import fr.coaching.maseance.domain.Diplome;
import fr.coaching.maseance.domain.SpecialiteExpertise;
import fr.coaching.maseance.domain.User;
import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.domain.enumeration.GenrePersonne;
import fr.coaching.maseance.repository.CoachExpertRepository;
import fr.coaching.maseance.repository.UserRepository;
import fr.coaching.maseance.service.CoachExpertService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CoachExpertResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CoachExpertResourceIT {

    private static final GenrePersonne DEFAULT_CIVILITE = GenrePersonne.Madame;
    private static final GenrePersonne UPDATED_CIVILITE = GenrePersonne.Monsieur;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_NAISSANCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_NAISSANCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADRESSE_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODE_POSTAL = 1;
    private static final Integer UPDATED_CODE_POSTAL = 2;
    private static final Integer SMALLER_CODE_POSTAL = 1 - 1;

    private static final String DEFAULT_NUMERO_ET_NOM_VOIE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_ET_NOM_VOIE = "BBBBBBBBBB";

    private static final Long DEFAULT_TARIF_ACTUEL = 1L;
    private static final Long UPDATED_TARIF_ACTUEL = 2L;
    private static final Long SMALLER_TARIF_ACTUEL = 1L - 1L;

    private static final CanalSeance DEFAULT_FORMAT_PROPOSE_SEANCE = CanalSeance.AdressePhysique;
    private static final CanalSeance UPDATED_FORMAT_PROPOSE_SEANCE = CanalSeance.AppelVisio;

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_URL_PHOTO_PROFIL = "AAAAAAAAAA";
    private static final String UPDATED_URL_PHOTO_PROFIL = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/coach-experts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CoachExpertRepository coachExpertRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CoachExpertRepository coachExpertRepositoryMock;

    @Mock
    private CoachExpertService coachExpertServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoachExpertMockMvc;

    private CoachExpert coachExpert;

    private CoachExpert insertedCoachExpert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoachExpert createEntity() {
        return new CoachExpert()
            .civilite(DEFAULT_CIVILITE)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .adresseEmail(DEFAULT_ADRESSE_EMAIL)
            .numeroTelephone(DEFAULT_NUMERO_TELEPHONE)
            .ville(DEFAULT_VILLE)
            .codePostal(DEFAULT_CODE_POSTAL)
            .numeroEtNomVoie(DEFAULT_NUMERO_ET_NOM_VOIE)
            .tarifActuel(DEFAULT_TARIF_ACTUEL)
            .formatProposeSeance(DEFAULT_FORMAT_PROPOSE_SEANCE)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .urlPhotoProfil(DEFAULT_URL_PHOTO_PROFIL)
            .bio(DEFAULT_BIO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoachExpert createUpdatedEntity() {
        return new CoachExpert()
            .civilite(UPDATED_CIVILITE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresseEmail(UPDATED_ADRESSE_EMAIL)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .ville(UPDATED_VILLE)
            .codePostal(UPDATED_CODE_POSTAL)
            .numeroEtNomVoie(UPDATED_NUMERO_ET_NOM_VOIE)
            .tarifActuel(UPDATED_TARIF_ACTUEL)
            .formatProposeSeance(UPDATED_FORMAT_PROPOSE_SEANCE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .urlPhotoProfil(UPDATED_URL_PHOTO_PROFIL)
            .bio(UPDATED_BIO);
    }

    @BeforeEach
    public void initTest() {
        coachExpert = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCoachExpert != null) {
            coachExpertRepository.delete(insertedCoachExpert);
            insertedCoachExpert = null;
        }
    }

    @Test
    @Transactional
    void createCoachExpert() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CoachExpert
        var returnedCoachExpert = om.readValue(
            restCoachExpertMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CoachExpert.class
        );

        // Validate the CoachExpert in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCoachExpertUpdatableFieldsEquals(returnedCoachExpert, getPersistedCoachExpert(returnedCoachExpert));

        insertedCoachExpert = returnedCoachExpert;
    }

    @Test
    @Transactional
    void createCoachExpertWithExistingId() throws Exception {
        // Create the CoachExpert with an existing ID
        coachExpert.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        // Validate the CoachExpert in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCiviliteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachExpert.setCivilite(null);

        // Create the CoachExpert, which fails.

        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachExpert.setNom(null);

        // Create the CoachExpert, which fails.

        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachExpert.setPrenom(null);

        // Create the CoachExpert, which fails.

        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateNaissanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachExpert.setDateNaissance(null);

        // Create the CoachExpert, which fails.

        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachExpert.setAdresseEmail(null);

        // Create the CoachExpert, which fails.

        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVilleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachExpert.setVille(null);

        // Create the CoachExpert, which fails.

        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodePostalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachExpert.setCodePostal(null);

        // Create the CoachExpert, which fails.

        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroEtNomVoieIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachExpert.setNumeroEtNomVoie(null);

        // Create the CoachExpert, which fails.

        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTarifActuelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachExpert.setTarifActuel(null);

        // Create the CoachExpert, which fails.

        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFormatProposeSeanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachExpert.setFormatProposeSeance(null);

        // Create the CoachExpert, which fails.

        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlPhotoProfilIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachExpert.setUrlPhotoProfil(null);

        // Create the CoachExpert, which fails.

        restCoachExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCoachExperts() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList
        restCoachExpertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coachExpert.getId().intValue())))
            .andExpect(jsonPath("$.[*].civilite").value(hasItem(DEFAULT_CIVILITE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].adresseEmail").value(hasItem(DEFAULT_ADRESSE_EMAIL)))
            .andExpect(jsonPath("$.[*].numeroTelephone").value(hasItem(DEFAULT_NUMERO_TELEPHONE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].numeroEtNomVoie").value(hasItem(DEFAULT_NUMERO_ET_NOM_VOIE)))
            .andExpect(jsonPath("$.[*].tarifActuel").value(hasItem(DEFAULT_TARIF_ACTUEL.intValue())))
            .andExpect(jsonPath("$.[*].formatProposeSeance").value(hasItem(DEFAULT_FORMAT_PROPOSE_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].urlPhotoProfil").value(hasItem(DEFAULT_URL_PHOTO_PROFIL)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCoachExpertsWithEagerRelationshipsIsEnabled() throws Exception {
        when(coachExpertServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCoachExpertMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(coachExpertServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCoachExpertsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(coachExpertServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCoachExpertMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(coachExpertRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCoachExpert() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get the coachExpert
        restCoachExpertMockMvc
            .perform(get(ENTITY_API_URL_ID, coachExpert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coachExpert.getId().intValue()))
            .andExpect(jsonPath("$.civilite").value(DEFAULT_CIVILITE.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.adresseEmail").value(DEFAULT_ADRESSE_EMAIL))
            .andExpect(jsonPath("$.numeroTelephone").value(DEFAULT_NUMERO_TELEPHONE))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.numeroEtNomVoie").value(DEFAULT_NUMERO_ET_NOM_VOIE))
            .andExpect(jsonPath("$.tarifActuel").value(DEFAULT_TARIF_ACTUEL.intValue()))
            .andExpect(jsonPath("$.formatProposeSeance").value(DEFAULT_FORMAT_PROPOSE_SEANCE.toString()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64.getEncoder().encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.urlPhotoProfil").value(DEFAULT_URL_PHOTO_PROFIL))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO));
    }

    @Test
    @Transactional
    void getCoachExpertsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        Long id = coachExpert.getId();

        defaultCoachExpertFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCoachExpertFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCoachExpertFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByCiviliteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where civilite equals to
        defaultCoachExpertFiltering("civilite.equals=" + DEFAULT_CIVILITE, "civilite.equals=" + UPDATED_CIVILITE);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByCiviliteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where civilite in
        defaultCoachExpertFiltering("civilite.in=" + DEFAULT_CIVILITE + "," + UPDATED_CIVILITE, "civilite.in=" + UPDATED_CIVILITE);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByCiviliteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where civilite is not null
        defaultCoachExpertFiltering("civilite.specified=true", "civilite.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where nom equals to
        defaultCoachExpertFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where nom in
        defaultCoachExpertFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where nom is not null
        defaultCoachExpertFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where nom contains
        defaultCoachExpertFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where nom does not contain
        defaultCoachExpertFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where prenom equals to
        defaultCoachExpertFiltering("prenom.equals=" + DEFAULT_PRENOM, "prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where prenom in
        defaultCoachExpertFiltering("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM, "prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where prenom is not null
        defaultCoachExpertFiltering("prenom.specified=true", "prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where prenom contains
        defaultCoachExpertFiltering("prenom.contains=" + DEFAULT_PRENOM, "prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where prenom does not contain
        defaultCoachExpertFiltering("prenom.doesNotContain=" + UPDATED_PRENOM, "prenom.doesNotContain=" + DEFAULT_PRENOM);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where dateNaissance equals to
        defaultCoachExpertFiltering("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE, "dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where dateNaissance in
        defaultCoachExpertFiltering(
            "dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE,
            "dateNaissance.in=" + UPDATED_DATE_NAISSANCE
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where dateNaissance is not null
        defaultCoachExpertFiltering("dateNaissance.specified=true", "dateNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByAdresseEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where adresseEmail equals to
        defaultCoachExpertFiltering("adresseEmail.equals=" + DEFAULT_ADRESSE_EMAIL, "adresseEmail.equals=" + UPDATED_ADRESSE_EMAIL);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByAdresseEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where adresseEmail in
        defaultCoachExpertFiltering(
            "adresseEmail.in=" + DEFAULT_ADRESSE_EMAIL + "," + UPDATED_ADRESSE_EMAIL,
            "adresseEmail.in=" + UPDATED_ADRESSE_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByAdresseEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where adresseEmail is not null
        defaultCoachExpertFiltering("adresseEmail.specified=true", "adresseEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByAdresseEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where adresseEmail contains
        defaultCoachExpertFiltering("adresseEmail.contains=" + DEFAULT_ADRESSE_EMAIL, "adresseEmail.contains=" + UPDATED_ADRESSE_EMAIL);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByAdresseEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where adresseEmail does not contain
        defaultCoachExpertFiltering(
            "adresseEmail.doesNotContain=" + UPDATED_ADRESSE_EMAIL,
            "adresseEmail.doesNotContain=" + DEFAULT_ADRESSE_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNumeroTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where numeroTelephone equals to
        defaultCoachExpertFiltering(
            "numeroTelephone.equals=" + DEFAULT_NUMERO_TELEPHONE,
            "numeroTelephone.equals=" + UPDATED_NUMERO_TELEPHONE
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNumeroTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where numeroTelephone in
        defaultCoachExpertFiltering(
            "numeroTelephone.in=" + DEFAULT_NUMERO_TELEPHONE + "," + UPDATED_NUMERO_TELEPHONE,
            "numeroTelephone.in=" + UPDATED_NUMERO_TELEPHONE
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNumeroTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where numeroTelephone is not null
        defaultCoachExpertFiltering("numeroTelephone.specified=true", "numeroTelephone.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNumeroTelephoneContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where numeroTelephone contains
        defaultCoachExpertFiltering(
            "numeroTelephone.contains=" + DEFAULT_NUMERO_TELEPHONE,
            "numeroTelephone.contains=" + UPDATED_NUMERO_TELEPHONE
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNumeroTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where numeroTelephone does not contain
        defaultCoachExpertFiltering(
            "numeroTelephone.doesNotContain=" + UPDATED_NUMERO_TELEPHONE,
            "numeroTelephone.doesNotContain=" + DEFAULT_NUMERO_TELEPHONE
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where ville equals to
        defaultCoachExpertFiltering("ville.equals=" + DEFAULT_VILLE, "ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where ville in
        defaultCoachExpertFiltering("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE, "ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where ville is not null
        defaultCoachExpertFiltering("ville.specified=true", "ville.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByVilleContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where ville contains
        defaultCoachExpertFiltering("ville.contains=" + DEFAULT_VILLE, "ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where ville does not contain
        defaultCoachExpertFiltering("ville.doesNotContain=" + UPDATED_VILLE, "ville.doesNotContain=" + DEFAULT_VILLE);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByCodePostalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where codePostal equals to
        defaultCoachExpertFiltering("codePostal.equals=" + DEFAULT_CODE_POSTAL, "codePostal.equals=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByCodePostalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where codePostal in
        defaultCoachExpertFiltering(
            "codePostal.in=" + DEFAULT_CODE_POSTAL + "," + UPDATED_CODE_POSTAL,
            "codePostal.in=" + UPDATED_CODE_POSTAL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByCodePostalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where codePostal is not null
        defaultCoachExpertFiltering("codePostal.specified=true", "codePostal.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByCodePostalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where codePostal is greater than or equal to
        defaultCoachExpertFiltering(
            "codePostal.greaterThanOrEqual=" + DEFAULT_CODE_POSTAL,
            "codePostal.greaterThanOrEqual=" + UPDATED_CODE_POSTAL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByCodePostalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where codePostal is less than or equal to
        defaultCoachExpertFiltering(
            "codePostal.lessThanOrEqual=" + DEFAULT_CODE_POSTAL,
            "codePostal.lessThanOrEqual=" + SMALLER_CODE_POSTAL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByCodePostalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where codePostal is less than
        defaultCoachExpertFiltering("codePostal.lessThan=" + UPDATED_CODE_POSTAL, "codePostal.lessThan=" + DEFAULT_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByCodePostalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where codePostal is greater than
        defaultCoachExpertFiltering("codePostal.greaterThan=" + SMALLER_CODE_POSTAL, "codePostal.greaterThan=" + DEFAULT_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNumeroEtNomVoieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where numeroEtNomVoie equals to
        defaultCoachExpertFiltering(
            "numeroEtNomVoie.equals=" + DEFAULT_NUMERO_ET_NOM_VOIE,
            "numeroEtNomVoie.equals=" + UPDATED_NUMERO_ET_NOM_VOIE
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNumeroEtNomVoieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where numeroEtNomVoie in
        defaultCoachExpertFiltering(
            "numeroEtNomVoie.in=" + DEFAULT_NUMERO_ET_NOM_VOIE + "," + UPDATED_NUMERO_ET_NOM_VOIE,
            "numeroEtNomVoie.in=" + UPDATED_NUMERO_ET_NOM_VOIE
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNumeroEtNomVoieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where numeroEtNomVoie is not null
        defaultCoachExpertFiltering("numeroEtNomVoie.specified=true", "numeroEtNomVoie.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNumeroEtNomVoieContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where numeroEtNomVoie contains
        defaultCoachExpertFiltering(
            "numeroEtNomVoie.contains=" + DEFAULT_NUMERO_ET_NOM_VOIE,
            "numeroEtNomVoie.contains=" + UPDATED_NUMERO_ET_NOM_VOIE
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByNumeroEtNomVoieNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where numeroEtNomVoie does not contain
        defaultCoachExpertFiltering(
            "numeroEtNomVoie.doesNotContain=" + UPDATED_NUMERO_ET_NOM_VOIE,
            "numeroEtNomVoie.doesNotContain=" + DEFAULT_NUMERO_ET_NOM_VOIE
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByTarifActuelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where tarifActuel equals to
        defaultCoachExpertFiltering("tarifActuel.equals=" + DEFAULT_TARIF_ACTUEL, "tarifActuel.equals=" + UPDATED_TARIF_ACTUEL);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByTarifActuelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where tarifActuel in
        defaultCoachExpertFiltering(
            "tarifActuel.in=" + DEFAULT_TARIF_ACTUEL + "," + UPDATED_TARIF_ACTUEL,
            "tarifActuel.in=" + UPDATED_TARIF_ACTUEL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByTarifActuelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where tarifActuel is not null
        defaultCoachExpertFiltering("tarifActuel.specified=true", "tarifActuel.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByTarifActuelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where tarifActuel is greater than or equal to
        defaultCoachExpertFiltering(
            "tarifActuel.greaterThanOrEqual=" + DEFAULT_TARIF_ACTUEL,
            "tarifActuel.greaterThanOrEqual=" + UPDATED_TARIF_ACTUEL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByTarifActuelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where tarifActuel is less than or equal to
        defaultCoachExpertFiltering(
            "tarifActuel.lessThanOrEqual=" + DEFAULT_TARIF_ACTUEL,
            "tarifActuel.lessThanOrEqual=" + SMALLER_TARIF_ACTUEL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByTarifActuelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where tarifActuel is less than
        defaultCoachExpertFiltering("tarifActuel.lessThan=" + UPDATED_TARIF_ACTUEL, "tarifActuel.lessThan=" + DEFAULT_TARIF_ACTUEL);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByTarifActuelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where tarifActuel is greater than
        defaultCoachExpertFiltering("tarifActuel.greaterThan=" + SMALLER_TARIF_ACTUEL, "tarifActuel.greaterThan=" + DEFAULT_TARIF_ACTUEL);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByFormatProposeSeanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where formatProposeSeance equals to
        defaultCoachExpertFiltering(
            "formatProposeSeance.equals=" + DEFAULT_FORMAT_PROPOSE_SEANCE,
            "formatProposeSeance.equals=" + UPDATED_FORMAT_PROPOSE_SEANCE
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByFormatProposeSeanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where formatProposeSeance in
        defaultCoachExpertFiltering(
            "formatProposeSeance.in=" + DEFAULT_FORMAT_PROPOSE_SEANCE + "," + UPDATED_FORMAT_PROPOSE_SEANCE,
            "formatProposeSeance.in=" + UPDATED_FORMAT_PROPOSE_SEANCE
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByFormatProposeSeanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where formatProposeSeance is not null
        defaultCoachExpertFiltering("formatProposeSeance.specified=true", "formatProposeSeance.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByUrlPhotoProfilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where urlPhotoProfil equals to
        defaultCoachExpertFiltering(
            "urlPhotoProfil.equals=" + DEFAULT_URL_PHOTO_PROFIL,
            "urlPhotoProfil.equals=" + UPDATED_URL_PHOTO_PROFIL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByUrlPhotoProfilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where urlPhotoProfil in
        defaultCoachExpertFiltering(
            "urlPhotoProfil.in=" + DEFAULT_URL_PHOTO_PROFIL + "," + UPDATED_URL_PHOTO_PROFIL,
            "urlPhotoProfil.in=" + UPDATED_URL_PHOTO_PROFIL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByUrlPhotoProfilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where urlPhotoProfil is not null
        defaultCoachExpertFiltering("urlPhotoProfil.specified=true", "urlPhotoProfil.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByUrlPhotoProfilContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where urlPhotoProfil contains
        defaultCoachExpertFiltering(
            "urlPhotoProfil.contains=" + DEFAULT_URL_PHOTO_PROFIL,
            "urlPhotoProfil.contains=" + UPDATED_URL_PHOTO_PROFIL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByUrlPhotoProfilNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where urlPhotoProfil does not contain
        defaultCoachExpertFiltering(
            "urlPhotoProfil.doesNotContain=" + UPDATED_URL_PHOTO_PROFIL,
            "urlPhotoProfil.doesNotContain=" + DEFAULT_URL_PHOTO_PROFIL
        );
    }

    @Test
    @Transactional
    void getAllCoachExpertsByBioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where bio equals to
        defaultCoachExpertFiltering("bio.equals=" + DEFAULT_BIO, "bio.equals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByBioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where bio in
        defaultCoachExpertFiltering("bio.in=" + DEFAULT_BIO + "," + UPDATED_BIO, "bio.in=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByBioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where bio is not null
        defaultCoachExpertFiltering("bio.specified=true", "bio.specified=false");
    }

    @Test
    @Transactional
    void getAllCoachExpertsByBioContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where bio contains
        defaultCoachExpertFiltering("bio.contains=" + DEFAULT_BIO, "bio.contains=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByBioNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        // Get all the coachExpertList where bio does not contain
        defaultCoachExpertFiltering("bio.doesNotContain=" + UPDATED_BIO, "bio.doesNotContain=" + DEFAULT_BIO);
    }

    @Test
    @Transactional
    void getAllCoachExpertsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            coachExpertRepository.saveAndFlush(coachExpert);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        coachExpert.setUser(user);
        coachExpertRepository.saveAndFlush(coachExpert);
        Long userId = user.getId();
        // Get all the coachExpertList where user equals to userId
        defaultCoachExpertShouldBeFound("userId.equals=" + userId);

        // Get all the coachExpertList where user equals to (userId + 1)
        defaultCoachExpertShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllCoachExpertsBySpecialiteExpertiseIsEqualToSomething() throws Exception {
        SpecialiteExpertise specialiteExpertise;
        if (TestUtil.findAll(em, SpecialiteExpertise.class).isEmpty()) {
            coachExpertRepository.saveAndFlush(coachExpert);
            specialiteExpertise = SpecialiteExpertiseResourceIT.createEntity();
        } else {
            specialiteExpertise = TestUtil.findAll(em, SpecialiteExpertise.class).get(0);
        }
        em.persist(specialiteExpertise);
        em.flush();
        coachExpert.addSpecialiteExpertise(specialiteExpertise);
        coachExpertRepository.saveAndFlush(coachExpert);
        Long specialiteExpertiseId = specialiteExpertise.getId();
        // Get all the coachExpertList where specialiteExpertise equals to specialiteExpertiseId
        defaultCoachExpertShouldBeFound("specialiteExpertiseId.equals=" + specialiteExpertiseId);

        // Get all the coachExpertList where specialiteExpertise equals to (specialiteExpertiseId + 1)
        defaultCoachExpertShouldNotBeFound("specialiteExpertiseId.equals=" + (specialiteExpertiseId + 1));
    }

    @Test
    @Transactional
    void getAllCoachExpertsByDiplomeIsEqualToSomething() throws Exception {
        Diplome diplome;
        if (TestUtil.findAll(em, Diplome.class).isEmpty()) {
            coachExpertRepository.saveAndFlush(coachExpert);
            diplome = DiplomeResourceIT.createEntity();
        } else {
            diplome = TestUtil.findAll(em, Diplome.class).get(0);
        }
        em.persist(diplome);
        em.flush();
        coachExpert.addDiplome(diplome);
        coachExpertRepository.saveAndFlush(coachExpert);
        Long diplomeId = diplome.getId();
        // Get all the coachExpertList where diplome equals to diplomeId
        defaultCoachExpertShouldBeFound("diplomeId.equals=" + diplomeId);

        // Get all the coachExpertList where diplome equals to (diplomeId + 1)
        defaultCoachExpertShouldNotBeFound("diplomeId.equals=" + (diplomeId + 1));
    }

    private void defaultCoachExpertFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCoachExpertShouldBeFound(shouldBeFound);
        defaultCoachExpertShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCoachExpertShouldBeFound(String filter) throws Exception {
        restCoachExpertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coachExpert.getId().intValue())))
            .andExpect(jsonPath("$.[*].civilite").value(hasItem(DEFAULT_CIVILITE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].adresseEmail").value(hasItem(DEFAULT_ADRESSE_EMAIL)))
            .andExpect(jsonPath("$.[*].numeroTelephone").value(hasItem(DEFAULT_NUMERO_TELEPHONE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].numeroEtNomVoie").value(hasItem(DEFAULT_NUMERO_ET_NOM_VOIE)))
            .andExpect(jsonPath("$.[*].tarifActuel").value(hasItem(DEFAULT_TARIF_ACTUEL.intValue())))
            .andExpect(jsonPath("$.[*].formatProposeSeance").value(hasItem(DEFAULT_FORMAT_PROPOSE_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].urlPhotoProfil").value(hasItem(DEFAULT_URL_PHOTO_PROFIL)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)));

        // Check, that the count call also returns 1
        restCoachExpertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCoachExpertShouldNotBeFound(String filter) throws Exception {
        restCoachExpertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCoachExpertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCoachExpert() throws Exception {
        // Get the coachExpert
        restCoachExpertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoachExpert() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coachExpert
        CoachExpert updatedCoachExpert = coachExpertRepository.findById(coachExpert.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCoachExpert are not directly saved in db
        em.detach(updatedCoachExpert);
        updatedCoachExpert
            .civilite(UPDATED_CIVILITE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresseEmail(UPDATED_ADRESSE_EMAIL)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .ville(UPDATED_VILLE)
            .codePostal(UPDATED_CODE_POSTAL)
            .numeroEtNomVoie(UPDATED_NUMERO_ET_NOM_VOIE)
            .tarifActuel(UPDATED_TARIF_ACTUEL)
            .formatProposeSeance(UPDATED_FORMAT_PROPOSE_SEANCE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .urlPhotoProfil(UPDATED_URL_PHOTO_PROFIL)
            .bio(UPDATED_BIO);

        restCoachExpertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCoachExpert.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCoachExpert))
            )
            .andExpect(status().isOk());

        // Validate the CoachExpert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCoachExpertToMatchAllProperties(updatedCoachExpert);
    }

    @Test
    @Transactional
    void putNonExistingCoachExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachExpert.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoachExpertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coachExpert.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coachExpert))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoachExpert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoachExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachExpert.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachExpertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coachExpert))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoachExpert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoachExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachExpert.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachExpertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoachExpert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoachExpertWithPatch() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coachExpert using partial update
        CoachExpert partialUpdatedCoachExpert = new CoachExpert();
        partialUpdatedCoachExpert.setId(coachExpert.getId());

        partialUpdatedCoachExpert
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .ville(UPDATED_VILLE)
            .tarifActuel(UPDATED_TARIF_ACTUEL)
            .formatProposeSeance(UPDATED_FORMAT_PROPOSE_SEANCE);

        restCoachExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoachExpert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoachExpert))
            )
            .andExpect(status().isOk());

        // Validate the CoachExpert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoachExpertUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCoachExpert, coachExpert),
            getPersistedCoachExpert(coachExpert)
        );
    }

    @Test
    @Transactional
    void fullUpdateCoachExpertWithPatch() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coachExpert using partial update
        CoachExpert partialUpdatedCoachExpert = new CoachExpert();
        partialUpdatedCoachExpert.setId(coachExpert.getId());

        partialUpdatedCoachExpert
            .civilite(UPDATED_CIVILITE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresseEmail(UPDATED_ADRESSE_EMAIL)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .ville(UPDATED_VILLE)
            .codePostal(UPDATED_CODE_POSTAL)
            .numeroEtNomVoie(UPDATED_NUMERO_ET_NOM_VOIE)
            .tarifActuel(UPDATED_TARIF_ACTUEL)
            .formatProposeSeance(UPDATED_FORMAT_PROPOSE_SEANCE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .urlPhotoProfil(UPDATED_URL_PHOTO_PROFIL)
            .bio(UPDATED_BIO);

        restCoachExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoachExpert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoachExpert))
            )
            .andExpect(status().isOk());

        // Validate the CoachExpert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoachExpertUpdatableFieldsEquals(partialUpdatedCoachExpert, getPersistedCoachExpert(partialUpdatedCoachExpert));
    }

    @Test
    @Transactional
    void patchNonExistingCoachExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachExpert.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoachExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coachExpert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coachExpert))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoachExpert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoachExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachExpert.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coachExpert))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoachExpert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoachExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachExpert.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachExpertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(coachExpert)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoachExpert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoachExpert() throws Exception {
        // Initialize the database
        insertedCoachExpert = coachExpertRepository.saveAndFlush(coachExpert);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the coachExpert
        restCoachExpertMockMvc
            .perform(delete(ENTITY_API_URL_ID, coachExpert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return coachExpertRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CoachExpert getPersistedCoachExpert(CoachExpert coachExpert) {
        return coachExpertRepository.findById(coachExpert.getId()).orElseThrow();
    }

    protected void assertPersistedCoachExpertToMatchAllProperties(CoachExpert expectedCoachExpert) {
        assertCoachExpertAllPropertiesEquals(expectedCoachExpert, getPersistedCoachExpert(expectedCoachExpert));
    }

    protected void assertPersistedCoachExpertToMatchUpdatableProperties(CoachExpert expectedCoachExpert) {
        assertCoachExpertAllUpdatablePropertiesEquals(expectedCoachExpert, getPersistedCoachExpert(expectedCoachExpert));
    }
}
