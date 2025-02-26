package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.SpecialiteExpertiseAsserts.*;
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
import fr.coaching.maseance.domain.ThemeExpertise;
import fr.coaching.maseance.repository.SpecialiteExpertiseRepository;
import fr.coaching.maseance.service.SpecialiteExpertiseService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
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
 * Integration tests for the {@link SpecialiteExpertiseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SpecialiteExpertiseResourceIT {

    private static final String DEFAULT_SPECIALITE = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALITE = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALITE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALITE_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_TARIF_MOYEN_HEURE = 1L;
    private static final Long UPDATED_TARIF_MOYEN_HEURE = 2L;
    private static final Long SMALLER_TARIF_MOYEN_HEURE = 1L - 1L;

    private static final String DEFAULT_DUREE_TARIF = "AAAAAAAAAA";
    private static final String UPDATED_DUREE_TARIF = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DIPLOME_REQUIS = false;
    private static final Boolean UPDATED_DIPLOME_REQUIS = true;

    private static final String DEFAULT_URL_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_URL_PHOTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/specialite-expertises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpecialiteExpertiseRepository specialiteExpertiseRepository;

    @Mock
    private SpecialiteExpertiseRepository specialiteExpertiseRepositoryMock;

    @Mock
    private SpecialiteExpertiseService specialiteExpertiseServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialiteExpertiseMockMvc;

    private SpecialiteExpertise specialiteExpertise;

    private SpecialiteExpertise insertedSpecialiteExpertise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialiteExpertise createEntity() {
        return new SpecialiteExpertise()
            .specialite(DEFAULT_SPECIALITE)
            .specialiteDescription(DEFAULT_SPECIALITE_DESCRIPTION)
            .tarifMoyenHeure(DEFAULT_TARIF_MOYEN_HEURE)
            .dureeTarif(DEFAULT_DUREE_TARIF)
            .diplomeRequis(DEFAULT_DIPLOME_REQUIS)
            .urlPhoto(DEFAULT_URL_PHOTO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialiteExpertise createUpdatedEntity() {
        return new SpecialiteExpertise()
            .specialite(UPDATED_SPECIALITE)
            .specialiteDescription(UPDATED_SPECIALITE_DESCRIPTION)
            .tarifMoyenHeure(UPDATED_TARIF_MOYEN_HEURE)
            .dureeTarif(UPDATED_DUREE_TARIF)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .urlPhoto(UPDATED_URL_PHOTO);
    }

    @BeforeEach
    public void initTest() {
        specialiteExpertise = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSpecialiteExpertise != null) {
            specialiteExpertiseRepository.delete(insertedSpecialiteExpertise);
            insertedSpecialiteExpertise = null;
        }
    }

    @Test
    @Transactional
    void createSpecialiteExpertise() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SpecialiteExpertise
        var returnedSpecialiteExpertise = om.readValue(
            restSpecialiteExpertiseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialiteExpertise)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SpecialiteExpertise.class
        );

        // Validate the SpecialiteExpertise in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSpecialiteExpertiseUpdatableFieldsEquals(
            returnedSpecialiteExpertise,
            getPersistedSpecialiteExpertise(returnedSpecialiteExpertise)
        );

        insertedSpecialiteExpertise = returnedSpecialiteExpertise;
    }

    @Test
    @Transactional
    void createSpecialiteExpertiseWithExistingId() throws Exception {
        // Create the SpecialiteExpertise with an existing ID
        specialiteExpertise.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialiteExpertiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialiteExpertise)))
            .andExpect(status().isBadRequest());

        // Validate the SpecialiteExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSpecialiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialiteExpertise.setSpecialite(null);

        // Create the SpecialiteExpertise, which fails.

        restSpecialiteExpertiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialiteExpertise)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiplomeRequisIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialiteExpertise.setDiplomeRequis(null);

        // Create the SpecialiteExpertise, which fails.

        restSpecialiteExpertiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialiteExpertise)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlPhotoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialiteExpertise.setUrlPhoto(null);

        // Create the SpecialiteExpertise, which fails.

        restSpecialiteExpertiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialiteExpertise)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertises() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList
        restSpecialiteExpertiseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialiteExpertise.getId().intValue())))
            .andExpect(jsonPath("$.[*].specialite").value(hasItem(DEFAULT_SPECIALITE)))
            .andExpect(jsonPath("$.[*].specialiteDescription").value(hasItem(DEFAULT_SPECIALITE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].tarifMoyenHeure").value(hasItem(DEFAULT_TARIF_MOYEN_HEURE.intValue())))
            .andExpect(jsonPath("$.[*].dureeTarif").value(hasItem(DEFAULT_DUREE_TARIF)))
            .andExpect(jsonPath("$.[*].diplomeRequis").value(hasItem(DEFAULT_DIPLOME_REQUIS)))
            .andExpect(jsonPath("$.[*].urlPhoto").value(hasItem(DEFAULT_URL_PHOTO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpecialiteExpertisesWithEagerRelationshipsIsEnabled() throws Exception {
        when(specialiteExpertiseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecialiteExpertiseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(specialiteExpertiseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpecialiteExpertisesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(specialiteExpertiseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecialiteExpertiseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(specialiteExpertiseRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSpecialiteExpertise() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get the specialiteExpertise
        restSpecialiteExpertiseMockMvc
            .perform(get(ENTITY_API_URL_ID, specialiteExpertise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialiteExpertise.getId().intValue()))
            .andExpect(jsonPath("$.specialite").value(DEFAULT_SPECIALITE))
            .andExpect(jsonPath("$.specialiteDescription").value(DEFAULT_SPECIALITE_DESCRIPTION))
            .andExpect(jsonPath("$.tarifMoyenHeure").value(DEFAULT_TARIF_MOYEN_HEURE.intValue()))
            .andExpect(jsonPath("$.dureeTarif").value(DEFAULT_DUREE_TARIF))
            .andExpect(jsonPath("$.diplomeRequis").value(DEFAULT_DIPLOME_REQUIS))
            .andExpect(jsonPath("$.urlPhoto").value(DEFAULT_URL_PHOTO));
    }

    @Test
    @Transactional
    void getSpecialiteExpertisesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        Long id = specialiteExpertise.getId();

        defaultSpecialiteExpertiseFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSpecialiteExpertiseFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSpecialiteExpertiseFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesBySpecialiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where specialite equals to
        defaultSpecialiteExpertiseFiltering("specialite.equals=" + DEFAULT_SPECIALITE, "specialite.equals=" + UPDATED_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesBySpecialiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where specialite in
        defaultSpecialiteExpertiseFiltering(
            "specialite.in=" + DEFAULT_SPECIALITE + "," + UPDATED_SPECIALITE,
            "specialite.in=" + UPDATED_SPECIALITE
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesBySpecialiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where specialite is not null
        defaultSpecialiteExpertiseFiltering("specialite.specified=true", "specialite.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesBySpecialiteContainsSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where specialite contains
        defaultSpecialiteExpertiseFiltering("specialite.contains=" + DEFAULT_SPECIALITE, "specialite.contains=" + UPDATED_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesBySpecialiteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where specialite does not contain
        defaultSpecialiteExpertiseFiltering(
            "specialite.doesNotContain=" + UPDATED_SPECIALITE,
            "specialite.doesNotContain=" + DEFAULT_SPECIALITE
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesBySpecialiteDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where specialiteDescription equals to
        defaultSpecialiteExpertiseFiltering(
            "specialiteDescription.equals=" + DEFAULT_SPECIALITE_DESCRIPTION,
            "specialiteDescription.equals=" + UPDATED_SPECIALITE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesBySpecialiteDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where specialiteDescription in
        defaultSpecialiteExpertiseFiltering(
            "specialiteDescription.in=" + DEFAULT_SPECIALITE_DESCRIPTION + "," + UPDATED_SPECIALITE_DESCRIPTION,
            "specialiteDescription.in=" + UPDATED_SPECIALITE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesBySpecialiteDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where specialiteDescription is not null
        defaultSpecialiteExpertiseFiltering("specialiteDescription.specified=true", "specialiteDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesBySpecialiteDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where specialiteDescription contains
        defaultSpecialiteExpertiseFiltering(
            "specialiteDescription.contains=" + DEFAULT_SPECIALITE_DESCRIPTION,
            "specialiteDescription.contains=" + UPDATED_SPECIALITE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesBySpecialiteDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where specialiteDescription does not contain
        defaultSpecialiteExpertiseFiltering(
            "specialiteDescription.doesNotContain=" + UPDATED_SPECIALITE_DESCRIPTION,
            "specialiteDescription.doesNotContain=" + DEFAULT_SPECIALITE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByTarifMoyenHeureIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where tarifMoyenHeure equals to
        defaultSpecialiteExpertiseFiltering(
            "tarifMoyenHeure.equals=" + DEFAULT_TARIF_MOYEN_HEURE,
            "tarifMoyenHeure.equals=" + UPDATED_TARIF_MOYEN_HEURE
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByTarifMoyenHeureIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where tarifMoyenHeure in
        defaultSpecialiteExpertiseFiltering(
            "tarifMoyenHeure.in=" + DEFAULT_TARIF_MOYEN_HEURE + "," + UPDATED_TARIF_MOYEN_HEURE,
            "tarifMoyenHeure.in=" + UPDATED_TARIF_MOYEN_HEURE
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByTarifMoyenHeureIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where tarifMoyenHeure is not null
        defaultSpecialiteExpertiseFiltering("tarifMoyenHeure.specified=true", "tarifMoyenHeure.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByTarifMoyenHeureIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where tarifMoyenHeure is greater than or equal to
        defaultSpecialiteExpertiseFiltering(
            "tarifMoyenHeure.greaterThanOrEqual=" + DEFAULT_TARIF_MOYEN_HEURE,
            "tarifMoyenHeure.greaterThanOrEqual=" + UPDATED_TARIF_MOYEN_HEURE
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByTarifMoyenHeureIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where tarifMoyenHeure is less than or equal to
        defaultSpecialiteExpertiseFiltering(
            "tarifMoyenHeure.lessThanOrEqual=" + DEFAULT_TARIF_MOYEN_HEURE,
            "tarifMoyenHeure.lessThanOrEqual=" + SMALLER_TARIF_MOYEN_HEURE
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByTarifMoyenHeureIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where tarifMoyenHeure is less than
        defaultSpecialiteExpertiseFiltering(
            "tarifMoyenHeure.lessThan=" + UPDATED_TARIF_MOYEN_HEURE,
            "tarifMoyenHeure.lessThan=" + DEFAULT_TARIF_MOYEN_HEURE
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByTarifMoyenHeureIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where tarifMoyenHeure is greater than
        defaultSpecialiteExpertiseFiltering(
            "tarifMoyenHeure.greaterThan=" + SMALLER_TARIF_MOYEN_HEURE,
            "tarifMoyenHeure.greaterThan=" + DEFAULT_TARIF_MOYEN_HEURE
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByDureeTarifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where dureeTarif equals to
        defaultSpecialiteExpertiseFiltering("dureeTarif.equals=" + DEFAULT_DUREE_TARIF, "dureeTarif.equals=" + UPDATED_DUREE_TARIF);
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByDureeTarifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where dureeTarif in
        defaultSpecialiteExpertiseFiltering(
            "dureeTarif.in=" + DEFAULT_DUREE_TARIF + "," + UPDATED_DUREE_TARIF,
            "dureeTarif.in=" + UPDATED_DUREE_TARIF
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByDureeTarifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where dureeTarif is not null
        defaultSpecialiteExpertiseFiltering("dureeTarif.specified=true", "dureeTarif.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByDureeTarifContainsSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where dureeTarif contains
        defaultSpecialiteExpertiseFiltering("dureeTarif.contains=" + DEFAULT_DUREE_TARIF, "dureeTarif.contains=" + UPDATED_DUREE_TARIF);
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByDureeTarifNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where dureeTarif does not contain
        defaultSpecialiteExpertiseFiltering(
            "dureeTarif.doesNotContain=" + UPDATED_DUREE_TARIF,
            "dureeTarif.doesNotContain=" + DEFAULT_DUREE_TARIF
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByDiplomeRequisIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where diplomeRequis equals to
        defaultSpecialiteExpertiseFiltering(
            "diplomeRequis.equals=" + DEFAULT_DIPLOME_REQUIS,
            "diplomeRequis.equals=" + UPDATED_DIPLOME_REQUIS
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByDiplomeRequisIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where diplomeRequis in
        defaultSpecialiteExpertiseFiltering(
            "diplomeRequis.in=" + DEFAULT_DIPLOME_REQUIS + "," + UPDATED_DIPLOME_REQUIS,
            "diplomeRequis.in=" + UPDATED_DIPLOME_REQUIS
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByDiplomeRequisIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where diplomeRequis is not null
        defaultSpecialiteExpertiseFiltering("diplomeRequis.specified=true", "diplomeRequis.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByUrlPhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where urlPhoto equals to
        defaultSpecialiteExpertiseFiltering("urlPhoto.equals=" + DEFAULT_URL_PHOTO, "urlPhoto.equals=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByUrlPhotoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where urlPhoto in
        defaultSpecialiteExpertiseFiltering(
            "urlPhoto.in=" + DEFAULT_URL_PHOTO + "," + UPDATED_URL_PHOTO,
            "urlPhoto.in=" + UPDATED_URL_PHOTO
        );
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByUrlPhotoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where urlPhoto is not null
        defaultSpecialiteExpertiseFiltering("urlPhoto.specified=true", "urlPhoto.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByUrlPhotoContainsSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where urlPhoto contains
        defaultSpecialiteExpertiseFiltering("urlPhoto.contains=" + DEFAULT_URL_PHOTO, "urlPhoto.contains=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByUrlPhotoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        // Get all the specialiteExpertiseList where urlPhoto does not contain
        defaultSpecialiteExpertiseFiltering("urlPhoto.doesNotContain=" + UPDATED_URL_PHOTO, "urlPhoto.doesNotContain=" + DEFAULT_URL_PHOTO);
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByDiplomeIsEqualToSomething() throws Exception {
        Diplome diplome;
        if (TestUtil.findAll(em, Diplome.class).isEmpty()) {
            specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);
            diplome = DiplomeResourceIT.createEntity();
        } else {
            diplome = TestUtil.findAll(em, Diplome.class).get(0);
        }
        em.persist(diplome);
        em.flush();
        specialiteExpertise.setDiplome(diplome);
        specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);
        Long diplomeId = diplome.getId();
        // Get all the specialiteExpertiseList where diplome equals to diplomeId
        defaultSpecialiteExpertiseShouldBeFound("diplomeId.equals=" + diplomeId);

        // Get all the specialiteExpertiseList where diplome equals to (diplomeId + 1)
        defaultSpecialiteExpertiseShouldNotBeFound("diplomeId.equals=" + (diplomeId + 1));
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByThemeExpertiseIsEqualToSomething() throws Exception {
        ThemeExpertise themeExpertise;
        if (TestUtil.findAll(em, ThemeExpertise.class).isEmpty()) {
            specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);
            themeExpertise = ThemeExpertiseResourceIT.createEntity();
        } else {
            themeExpertise = TestUtil.findAll(em, ThemeExpertise.class).get(0);
        }
        em.persist(themeExpertise);
        em.flush();
        specialiteExpertise.setThemeExpertise(themeExpertise);
        specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);
        Long themeExpertiseId = themeExpertise.getId();
        // Get all the specialiteExpertiseList where themeExpertise equals to themeExpertiseId
        defaultSpecialiteExpertiseShouldBeFound("themeExpertiseId.equals=" + themeExpertiseId);

        // Get all the specialiteExpertiseList where themeExpertise equals to (themeExpertiseId + 1)
        defaultSpecialiteExpertiseShouldNotBeFound("themeExpertiseId.equals=" + (themeExpertiseId + 1));
    }

    @Test
    @Transactional
    void getAllSpecialiteExpertisesByCoachExpertIsEqualToSomething() throws Exception {
        CoachExpert coachExpert;
        if (TestUtil.findAll(em, CoachExpert.class).isEmpty()) {
            specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);
            coachExpert = CoachExpertResourceIT.createEntity();
        } else {
            coachExpert = TestUtil.findAll(em, CoachExpert.class).get(0);
        }
        em.persist(coachExpert);
        em.flush();
        specialiteExpertise.addCoachExpert(coachExpert);
        specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);
        Long coachExpertId = coachExpert.getId();
        // Get all the specialiteExpertiseList where coachExpert equals to coachExpertId
        defaultSpecialiteExpertiseShouldBeFound("coachExpertId.equals=" + coachExpertId);

        // Get all the specialiteExpertiseList where coachExpert equals to (coachExpertId + 1)
        defaultSpecialiteExpertiseShouldNotBeFound("coachExpertId.equals=" + (coachExpertId + 1));
    }

    private void defaultSpecialiteExpertiseFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSpecialiteExpertiseShouldBeFound(shouldBeFound);
        defaultSpecialiteExpertiseShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSpecialiteExpertiseShouldBeFound(String filter) throws Exception {
        restSpecialiteExpertiseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialiteExpertise.getId().intValue())))
            .andExpect(jsonPath("$.[*].specialite").value(hasItem(DEFAULT_SPECIALITE)))
            .andExpect(jsonPath("$.[*].specialiteDescription").value(hasItem(DEFAULT_SPECIALITE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].tarifMoyenHeure").value(hasItem(DEFAULT_TARIF_MOYEN_HEURE.intValue())))
            .andExpect(jsonPath("$.[*].dureeTarif").value(hasItem(DEFAULT_DUREE_TARIF)))
            .andExpect(jsonPath("$.[*].diplomeRequis").value(hasItem(DEFAULT_DIPLOME_REQUIS)))
            .andExpect(jsonPath("$.[*].urlPhoto").value(hasItem(DEFAULT_URL_PHOTO)));

        // Check, that the count call also returns 1
        restSpecialiteExpertiseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSpecialiteExpertiseShouldNotBeFound(String filter) throws Exception {
        restSpecialiteExpertiseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSpecialiteExpertiseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSpecialiteExpertise() throws Exception {
        // Get the specialiteExpertise
        restSpecialiteExpertiseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialiteExpertise() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialiteExpertise
        SpecialiteExpertise updatedSpecialiteExpertise = specialiteExpertiseRepository.findById(specialiteExpertise.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialiteExpertise are not directly saved in db
        em.detach(updatedSpecialiteExpertise);
        updatedSpecialiteExpertise
            .specialite(UPDATED_SPECIALITE)
            .specialiteDescription(UPDATED_SPECIALITE_DESCRIPTION)
            .tarifMoyenHeure(UPDATED_TARIF_MOYEN_HEURE)
            .dureeTarif(UPDATED_DUREE_TARIF)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .urlPhoto(UPDATED_URL_PHOTO);

        restSpecialiteExpertiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpecialiteExpertise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSpecialiteExpertise))
            )
            .andExpect(status().isOk());

        // Validate the SpecialiteExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSpecialiteExpertiseToMatchAllProperties(updatedSpecialiteExpertise);
    }

    @Test
    @Transactional
    void putNonExistingSpecialiteExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialiteExpertise.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialiteExpertiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialiteExpertise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialiteExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialiteExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialiteExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialiteExpertise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteExpertiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialiteExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialiteExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialiteExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialiteExpertise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteExpertiseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialiteExpertise)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialiteExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialiteExpertiseWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialiteExpertise using partial update
        SpecialiteExpertise partialUpdatedSpecialiteExpertise = new SpecialiteExpertise();
        partialUpdatedSpecialiteExpertise.setId(specialiteExpertise.getId());

        partialUpdatedSpecialiteExpertise.specialite(UPDATED_SPECIALITE).diplomeRequis(UPDATED_DIPLOME_REQUIS);

        restSpecialiteExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialiteExpertise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialiteExpertise))
            )
            .andExpect(status().isOk());

        // Validate the SpecialiteExpertise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialiteExpertiseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSpecialiteExpertise, specialiteExpertise),
            getPersistedSpecialiteExpertise(specialiteExpertise)
        );
    }

    @Test
    @Transactional
    void fullUpdateSpecialiteExpertiseWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialiteExpertise using partial update
        SpecialiteExpertise partialUpdatedSpecialiteExpertise = new SpecialiteExpertise();
        partialUpdatedSpecialiteExpertise.setId(specialiteExpertise.getId());

        partialUpdatedSpecialiteExpertise
            .specialite(UPDATED_SPECIALITE)
            .specialiteDescription(UPDATED_SPECIALITE_DESCRIPTION)
            .tarifMoyenHeure(UPDATED_TARIF_MOYEN_HEURE)
            .dureeTarif(UPDATED_DUREE_TARIF)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .urlPhoto(UPDATED_URL_PHOTO);

        restSpecialiteExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialiteExpertise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialiteExpertise))
            )
            .andExpect(status().isOk());

        // Validate the SpecialiteExpertise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialiteExpertiseUpdatableFieldsEquals(
            partialUpdatedSpecialiteExpertise,
            getPersistedSpecialiteExpertise(partialUpdatedSpecialiteExpertise)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSpecialiteExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialiteExpertise.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialiteExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialiteExpertise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialiteExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialiteExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialiteExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialiteExpertise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialiteExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialiteExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialiteExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialiteExpertise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteExpertiseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(specialiteExpertise)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialiteExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialiteExpertise() throws Exception {
        // Initialize the database
        insertedSpecialiteExpertise = specialiteExpertiseRepository.saveAndFlush(specialiteExpertise);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the specialiteExpertise
        restSpecialiteExpertiseMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialiteExpertise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return specialiteExpertiseRepository.count();
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

    protected SpecialiteExpertise getPersistedSpecialiteExpertise(SpecialiteExpertise specialiteExpertise) {
        return specialiteExpertiseRepository.findById(specialiteExpertise.getId()).orElseThrow();
    }

    protected void assertPersistedSpecialiteExpertiseToMatchAllProperties(SpecialiteExpertise expectedSpecialiteExpertise) {
        assertSpecialiteExpertiseAllPropertiesEquals(
            expectedSpecialiteExpertise,
            getPersistedSpecialiteExpertise(expectedSpecialiteExpertise)
        );
    }

    protected void assertPersistedSpecialiteExpertiseToMatchUpdatableProperties(SpecialiteExpertise expectedSpecialiteExpertise) {
        assertSpecialiteExpertiseAllUpdatablePropertiesEquals(
            expectedSpecialiteExpertise,
            getPersistedSpecialiteExpertise(expectedSpecialiteExpertise)
        );
    }
}
