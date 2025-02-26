package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.ClientAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.Client;
import fr.coaching.maseance.domain.User;
import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.domain.enumeration.GenrePersonne;
import fr.coaching.maseance.repository.ClientRepository;
import fr.coaching.maseance.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final GenrePersonne DEFAULT_GENRE = GenrePersonne.Madame;
    private static final GenrePersonne UPDATED_GENRE = GenrePersonne.Monsieur;

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

    private static final CanalSeance DEFAULT_PREFERENCE_CANAL_SEANCE = CanalSeance.AdressePhysique;
    private static final CanalSeance UPDATED_PREFERENCE_CANAL_SEANCE = CanalSeance.AppelVisio;

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_URL_PHOTO_PROFIL = "AAAAAAAAAA";
    private static final String UPDATED_URL_PHOTO_PROFIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    private Client insertedClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity() {
        return new Client()
            .genre(DEFAULT_GENRE)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .adresseEmail(DEFAULT_ADRESSE_EMAIL)
            .numeroTelephone(DEFAULT_NUMERO_TELEPHONE)
            .ville(DEFAULT_VILLE)
            .codePostal(DEFAULT_CODE_POSTAL)
            .numeroEtNomVoie(DEFAULT_NUMERO_ET_NOM_VOIE)
            .preferenceCanalSeance(DEFAULT_PREFERENCE_CANAL_SEANCE)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .urlPhotoProfil(DEFAULT_URL_PHOTO_PROFIL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity() {
        return new Client()
            .genre(UPDATED_GENRE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresseEmail(UPDATED_ADRESSE_EMAIL)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .ville(UPDATED_VILLE)
            .codePostal(UPDATED_CODE_POSTAL)
            .numeroEtNomVoie(UPDATED_NUMERO_ET_NOM_VOIE)
            .preferenceCanalSeance(UPDATED_PREFERENCE_CANAL_SEANCE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .urlPhotoProfil(UPDATED_URL_PHOTO_PROFIL);
    }

    @BeforeEach
    public void initTest() {
        client = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedClient != null) {
            clientRepository.delete(insertedClient);
            insertedClient = null;
        }
    }

    @Test
    @Transactional
    void createClient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Client
        var returnedClient = om.readValue(
            restClientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Client.class
        );

        // Validate the Client in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertClientUpdatableFieldsEquals(returnedClient, getPersistedClient(returnedClient));

        insertedClient = returnedClient;
    }

    @Test
    @Transactional
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        client.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGenreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setGenre(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setNom(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setPrenom(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateNaissanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setDateNaissance(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setAdresseEmail(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVilleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setVille(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodePostalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setCodePostal(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroEtNomVoieIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setNumeroEtNomVoie(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].adresseEmail").value(hasItem(DEFAULT_ADRESSE_EMAIL)))
            .andExpect(jsonPath("$.[*].numeroTelephone").value(hasItem(DEFAULT_NUMERO_TELEPHONE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].numeroEtNomVoie").value(hasItem(DEFAULT_NUMERO_ET_NOM_VOIE)))
            .andExpect(jsonPath("$.[*].preferenceCanalSeance").value(hasItem(DEFAULT_PREFERENCE_CANAL_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].urlPhotoProfil").value(hasItem(DEFAULT_URL_PHOTO_PROFIL)));
    }

    @Test
    @Transactional
    void getClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.adresseEmail").value(DEFAULT_ADRESSE_EMAIL))
            .andExpect(jsonPath("$.numeroTelephone").value(DEFAULT_NUMERO_TELEPHONE))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.numeroEtNomVoie").value(DEFAULT_NUMERO_ET_NOM_VOIE))
            .andExpect(jsonPath("$.preferenceCanalSeance").value(DEFAULT_PREFERENCE_CANAL_SEANCE.toString()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64.getEncoder().encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.urlPhotoProfil").value(DEFAULT_URL_PHOTO_PROFIL));
    }

    @Test
    @Transactional
    void getClientsByIdFiltering() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        Long id = client.getId();

        defaultClientFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultClientFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultClientFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientsByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where genre equals to
        defaultClientFiltering("genre.equals=" + DEFAULT_GENRE, "genre.equals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllClientsByGenreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where genre in
        defaultClientFiltering("genre.in=" + DEFAULT_GENRE + "," + UPDATED_GENRE, "genre.in=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllClientsByGenreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where genre is not null
        defaultClientFiltering("genre.specified=true", "genre.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nom equals to
        defaultClientFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nom in
        defaultClientFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nom is not null
        defaultClientFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nom contains
        defaultClientFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nom does not contain
        defaultClientFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom equals to
        defaultClientFiltering("prenom.equals=" + DEFAULT_PRENOM, "prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllClientsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom in
        defaultClientFiltering("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM, "prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllClientsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom is not null
        defaultClientFiltering("prenom.specified=true", "prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom contains
        defaultClientFiltering("prenom.contains=" + DEFAULT_PRENOM, "prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllClientsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom does not contain
        defaultClientFiltering("prenom.doesNotContain=" + UPDATED_PRENOM, "prenom.doesNotContain=" + DEFAULT_PRENOM);
    }

    @Test
    @Transactional
    void getAllClientsByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where dateNaissance equals to
        defaultClientFiltering("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE, "dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllClientsByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where dateNaissance in
        defaultClientFiltering(
            "dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE,
            "dateNaissance.in=" + UPDATED_DATE_NAISSANCE
        );
    }

    @Test
    @Transactional
    void getAllClientsByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where dateNaissance is not null
        defaultClientFiltering("dateNaissance.specified=true", "dateNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByAdresseEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where adresseEmail equals to
        defaultClientFiltering("adresseEmail.equals=" + DEFAULT_ADRESSE_EMAIL, "adresseEmail.equals=" + UPDATED_ADRESSE_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByAdresseEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where adresseEmail in
        defaultClientFiltering(
            "adresseEmail.in=" + DEFAULT_ADRESSE_EMAIL + "," + UPDATED_ADRESSE_EMAIL,
            "adresseEmail.in=" + UPDATED_ADRESSE_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllClientsByAdresseEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where adresseEmail is not null
        defaultClientFiltering("adresseEmail.specified=true", "adresseEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByAdresseEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where adresseEmail contains
        defaultClientFiltering("adresseEmail.contains=" + DEFAULT_ADRESSE_EMAIL, "adresseEmail.contains=" + UPDATED_ADRESSE_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByAdresseEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where adresseEmail does not contain
        defaultClientFiltering(
            "adresseEmail.doesNotContain=" + UPDATED_ADRESSE_EMAIL,
            "adresseEmail.doesNotContain=" + DEFAULT_ADRESSE_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllClientsByNumeroTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroTelephone equals to
        defaultClientFiltering("numeroTelephone.equals=" + DEFAULT_NUMERO_TELEPHONE, "numeroTelephone.equals=" + UPDATED_NUMERO_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllClientsByNumeroTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroTelephone in
        defaultClientFiltering(
            "numeroTelephone.in=" + DEFAULT_NUMERO_TELEPHONE + "," + UPDATED_NUMERO_TELEPHONE,
            "numeroTelephone.in=" + UPDATED_NUMERO_TELEPHONE
        );
    }

    @Test
    @Transactional
    void getAllClientsByNumeroTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroTelephone is not null
        defaultClientFiltering("numeroTelephone.specified=true", "numeroTelephone.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNumeroTelephoneContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroTelephone contains
        defaultClientFiltering(
            "numeroTelephone.contains=" + DEFAULT_NUMERO_TELEPHONE,
            "numeroTelephone.contains=" + UPDATED_NUMERO_TELEPHONE
        );
    }

    @Test
    @Transactional
    void getAllClientsByNumeroTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroTelephone does not contain
        defaultClientFiltering(
            "numeroTelephone.doesNotContain=" + UPDATED_NUMERO_TELEPHONE,
            "numeroTelephone.doesNotContain=" + DEFAULT_NUMERO_TELEPHONE
        );
    }

    @Test
    @Transactional
    void getAllClientsByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where ville equals to
        defaultClientFiltering("ville.equals=" + DEFAULT_VILLE, "ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllClientsByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where ville in
        defaultClientFiltering("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE, "ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllClientsByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where ville is not null
        defaultClientFiltering("ville.specified=true", "ville.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByVilleContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where ville contains
        defaultClientFiltering("ville.contains=" + DEFAULT_VILLE, "ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllClientsByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where ville does not contain
        defaultClientFiltering("ville.doesNotContain=" + UPDATED_VILLE, "ville.doesNotContain=" + DEFAULT_VILLE);
    }

    @Test
    @Transactional
    void getAllClientsByCodePostalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal equals to
        defaultClientFiltering("codePostal.equals=" + DEFAULT_CODE_POSTAL, "codePostal.equals=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllClientsByCodePostalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal in
        defaultClientFiltering("codePostal.in=" + DEFAULT_CODE_POSTAL + "," + UPDATED_CODE_POSTAL, "codePostal.in=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllClientsByCodePostalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal is not null
        defaultClientFiltering("codePostal.specified=true", "codePostal.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByCodePostalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal is greater than or equal to
        defaultClientFiltering(
            "codePostal.greaterThanOrEqual=" + DEFAULT_CODE_POSTAL,
            "codePostal.greaterThanOrEqual=" + UPDATED_CODE_POSTAL
        );
    }

    @Test
    @Transactional
    void getAllClientsByCodePostalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal is less than or equal to
        defaultClientFiltering("codePostal.lessThanOrEqual=" + DEFAULT_CODE_POSTAL, "codePostal.lessThanOrEqual=" + SMALLER_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllClientsByCodePostalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal is less than
        defaultClientFiltering("codePostal.lessThan=" + UPDATED_CODE_POSTAL, "codePostal.lessThan=" + DEFAULT_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllClientsByCodePostalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal is greater than
        defaultClientFiltering("codePostal.greaterThan=" + SMALLER_CODE_POSTAL, "codePostal.greaterThan=" + DEFAULT_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllClientsByNumeroEtNomVoieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroEtNomVoie equals to
        defaultClientFiltering(
            "numeroEtNomVoie.equals=" + DEFAULT_NUMERO_ET_NOM_VOIE,
            "numeroEtNomVoie.equals=" + UPDATED_NUMERO_ET_NOM_VOIE
        );
    }

    @Test
    @Transactional
    void getAllClientsByNumeroEtNomVoieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroEtNomVoie in
        defaultClientFiltering(
            "numeroEtNomVoie.in=" + DEFAULT_NUMERO_ET_NOM_VOIE + "," + UPDATED_NUMERO_ET_NOM_VOIE,
            "numeroEtNomVoie.in=" + UPDATED_NUMERO_ET_NOM_VOIE
        );
    }

    @Test
    @Transactional
    void getAllClientsByNumeroEtNomVoieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroEtNomVoie is not null
        defaultClientFiltering("numeroEtNomVoie.specified=true", "numeroEtNomVoie.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNumeroEtNomVoieContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroEtNomVoie contains
        defaultClientFiltering(
            "numeroEtNomVoie.contains=" + DEFAULT_NUMERO_ET_NOM_VOIE,
            "numeroEtNomVoie.contains=" + UPDATED_NUMERO_ET_NOM_VOIE
        );
    }

    @Test
    @Transactional
    void getAllClientsByNumeroEtNomVoieNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroEtNomVoie does not contain
        defaultClientFiltering(
            "numeroEtNomVoie.doesNotContain=" + UPDATED_NUMERO_ET_NOM_VOIE,
            "numeroEtNomVoie.doesNotContain=" + DEFAULT_NUMERO_ET_NOM_VOIE
        );
    }

    @Test
    @Transactional
    void getAllClientsByPreferenceCanalSeanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where preferenceCanalSeance equals to
        defaultClientFiltering(
            "preferenceCanalSeance.equals=" + DEFAULT_PREFERENCE_CANAL_SEANCE,
            "preferenceCanalSeance.equals=" + UPDATED_PREFERENCE_CANAL_SEANCE
        );
    }

    @Test
    @Transactional
    void getAllClientsByPreferenceCanalSeanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where preferenceCanalSeance in
        defaultClientFiltering(
            "preferenceCanalSeance.in=" + DEFAULT_PREFERENCE_CANAL_SEANCE + "," + UPDATED_PREFERENCE_CANAL_SEANCE,
            "preferenceCanalSeance.in=" + UPDATED_PREFERENCE_CANAL_SEANCE
        );
    }

    @Test
    @Transactional
    void getAllClientsByPreferenceCanalSeanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where preferenceCanalSeance is not null
        defaultClientFiltering("preferenceCanalSeance.specified=true", "preferenceCanalSeance.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByUrlPhotoProfilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where urlPhotoProfil equals to
        defaultClientFiltering("urlPhotoProfil.equals=" + DEFAULT_URL_PHOTO_PROFIL, "urlPhotoProfil.equals=" + UPDATED_URL_PHOTO_PROFIL);
    }

    @Test
    @Transactional
    void getAllClientsByUrlPhotoProfilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where urlPhotoProfil in
        defaultClientFiltering(
            "urlPhotoProfil.in=" + DEFAULT_URL_PHOTO_PROFIL + "," + UPDATED_URL_PHOTO_PROFIL,
            "urlPhotoProfil.in=" + UPDATED_URL_PHOTO_PROFIL
        );
    }

    @Test
    @Transactional
    void getAllClientsByUrlPhotoProfilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where urlPhotoProfil is not null
        defaultClientFiltering("urlPhotoProfil.specified=true", "urlPhotoProfil.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByUrlPhotoProfilContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where urlPhotoProfil contains
        defaultClientFiltering(
            "urlPhotoProfil.contains=" + DEFAULT_URL_PHOTO_PROFIL,
            "urlPhotoProfil.contains=" + UPDATED_URL_PHOTO_PROFIL
        );
    }

    @Test
    @Transactional
    void getAllClientsByUrlPhotoProfilNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where urlPhotoProfil does not contain
        defaultClientFiltering(
            "urlPhotoProfil.doesNotContain=" + UPDATED_URL_PHOTO_PROFIL,
            "urlPhotoProfil.doesNotContain=" + DEFAULT_URL_PHOTO_PROFIL
        );
    }

    @Test
    @Transactional
    void getAllClientsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            clientRepository.saveAndFlush(client);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        client.setUser(user);
        clientRepository.saveAndFlush(client);
        Long userId = user.getId();
        // Get all the clientList where user equals to userId
        defaultClientShouldBeFound("userId.equals=" + userId);

        // Get all the clientList where user equals to (userId + 1)
        defaultClientShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultClientFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClientShouldBeFound(shouldBeFound);
        defaultClientShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].adresseEmail").value(hasItem(DEFAULT_ADRESSE_EMAIL)))
            .andExpect(jsonPath("$.[*].numeroTelephone").value(hasItem(DEFAULT_NUMERO_TELEPHONE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].numeroEtNomVoie").value(hasItem(DEFAULT_NUMERO_ET_NOM_VOIE)))
            .andExpect(jsonPath("$.[*].preferenceCanalSeance").value(hasItem(DEFAULT_PREFERENCE_CANAL_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].urlPhotoProfil").value(hasItem(DEFAULT_URL_PHOTO_PROFIL)));

        // Check, that the count call also returns 1
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .genre(UPDATED_GENRE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresseEmail(UPDATED_ADRESSE_EMAIL)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .ville(UPDATED_VILLE)
            .codePostal(UPDATED_CODE_POSTAL)
            .numeroEtNomVoie(UPDATED_NUMERO_ET_NOM_VOIE)
            .preferenceCanalSeance(UPDATED_PREFERENCE_CANAL_SEANCE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .urlPhotoProfil(UPDATED_URL_PHOTO_PROFIL);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientToMatchAllProperties(updatedClient);
    }

    @Test
    @Transactional
    void putNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL_ID, client.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(client)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .nom(UPDATED_NOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresseEmail(UPDATED_ADRESSE_EMAIL)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .numeroEtNomVoie(UPDATED_NUMERO_ET_NOM_VOIE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .urlPhotoProfil(UPDATED_URL_PHOTO_PROFIL);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedClient, client), getPersistedClient(client));
    }

    @Test
    @Transactional
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .genre(UPDATED_GENRE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresseEmail(UPDATED_ADRESSE_EMAIL)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .ville(UPDATED_VILLE)
            .codePostal(UPDATED_CODE_POSTAL)
            .numeroEtNomVoie(UPDATED_NUMERO_ET_NOM_VOIE)
            .preferenceCanalSeance(UPDATED_PREFERENCE_CANAL_SEANCE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .urlPhotoProfil(UPDATED_URL_PHOTO_PROFIL);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientUpdatableFieldsEquals(partialUpdatedClient, getPersistedClient(partialUpdatedClient));
    }

    @Test
    @Transactional
    void patchNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, client.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(client)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, client.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clientRepository.count();
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

    protected Client getPersistedClient(Client client) {
        return clientRepository.findById(client.getId()).orElseThrow();
    }

    protected void assertPersistedClientToMatchAllProperties(Client expectedClient) {
        assertClientAllPropertiesEquals(expectedClient, getPersistedClient(expectedClient));
    }

    protected void assertPersistedClientToMatchUpdatableProperties(Client expectedClient) {
        assertClientAllUpdatablePropertiesEquals(expectedClient, getPersistedClient(expectedClient));
    }
}
