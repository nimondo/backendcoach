package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.OffreCoachAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.CoachExpert;
import fr.coaching.maseance.domain.OffreCoach;
import fr.coaching.maseance.domain.SpecialiteExpertise;
import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.domain.enumeration.TypeSeance;
import fr.coaching.maseance.repository.OffreCoachRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link OffreCoachResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OffreCoachResourceIT {

    private static final CanalSeance DEFAULT_CANAL_SEANCE = CanalSeance.AdressePhysique;
    private static final CanalSeance UPDATED_CANAL_SEANCE = CanalSeance.AppelVisio;

    private static final TypeSeance DEFAULT_TYPE_SEANCE = TypeSeance.Individuelle;
    private static final TypeSeance UPDATED_TYPE_SEANCE = TypeSeance.Collective;

    private static final String DEFAULT_SYNTHESE = "AAAAAAAAAA";
    private static final String UPDATED_SYNTHESE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_DETAILLEE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_DETAILLEE = "BBBBBBBBBB";

    private static final Long DEFAULT_TARIF = 1L;
    private static final Long UPDATED_TARIF = 2L;
    private static final Long SMALLER_TARIF = 1L - 1L;

    private static final Integer DEFAULT_DUREE = 1;
    private static final Integer UPDATED_DUREE = 2;
    private static final Integer SMALLER_DUREE = 1 - 1;

    private static final String DEFAULT_DESCRIPTION_DIPLOME = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_DIPLOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/offre-coaches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OffreCoachRepository offreCoachRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOffreCoachMockMvc;

    private OffreCoach offreCoach;

    private OffreCoach insertedOffreCoach;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OffreCoach createEntity() {
        return new OffreCoach()
            .canalSeance(DEFAULT_CANAL_SEANCE)
            .typeSeance(DEFAULT_TYPE_SEANCE)
            .synthese(DEFAULT_SYNTHESE)
            .descriptionDetaillee(DEFAULT_DESCRIPTION_DETAILLEE)
            .tarif(DEFAULT_TARIF)
            .duree(DEFAULT_DUREE)
            .descriptionDiplome(DEFAULT_DESCRIPTION_DIPLOME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OffreCoach createUpdatedEntity() {
        return new OffreCoach()
            .canalSeance(UPDATED_CANAL_SEANCE)
            .typeSeance(UPDATED_TYPE_SEANCE)
            .synthese(UPDATED_SYNTHESE)
            .descriptionDetaillee(UPDATED_DESCRIPTION_DETAILLEE)
            .tarif(UPDATED_TARIF)
            .duree(UPDATED_DUREE)
            .descriptionDiplome(UPDATED_DESCRIPTION_DIPLOME);
    }

    @BeforeEach
    public void initTest() {
        offreCoach = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOffreCoach != null) {
            offreCoachRepository.delete(insertedOffreCoach);
            insertedOffreCoach = null;
        }
    }

    @Test
    @Transactional
    void createOffreCoach() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OffreCoach
        var returnedOffreCoach = om.readValue(
            restOffreCoachMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreCoach)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OffreCoach.class
        );

        // Validate the OffreCoach in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOffreCoachUpdatableFieldsEquals(returnedOffreCoach, getPersistedOffreCoach(returnedOffreCoach));

        insertedOffreCoach = returnedOffreCoach;
    }

    @Test
    @Transactional
    void createOffreCoachWithExistingId() throws Exception {
        // Create the OffreCoach with an existing ID
        offreCoach.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOffreCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreCoach)))
            .andExpect(status().isBadRequest());

        // Validate the OffreCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTarifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        offreCoach.setTarif(null);

        // Create the OffreCoach, which fails.

        restOffreCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreCoach)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDureeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        offreCoach.setDuree(null);

        // Create the OffreCoach, which fails.

        restOffreCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreCoach)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOffreCoaches() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList
        restOffreCoachMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offreCoach.getId().intValue())))
            .andExpect(jsonPath("$.[*].canalSeance").value(hasItem(DEFAULT_CANAL_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].typeSeance").value(hasItem(DEFAULT_TYPE_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].synthese").value(hasItem(DEFAULT_SYNTHESE)))
            .andExpect(jsonPath("$.[*].descriptionDetaillee").value(hasItem(DEFAULT_DESCRIPTION_DETAILLEE)))
            .andExpect(jsonPath("$.[*].tarif").value(hasItem(DEFAULT_TARIF.intValue())))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].descriptionDiplome").value(hasItem(DEFAULT_DESCRIPTION_DIPLOME)));
    }

    @Test
    @Transactional
    void getOffreCoach() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get the offreCoach
        restOffreCoachMockMvc
            .perform(get(ENTITY_API_URL_ID, offreCoach.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offreCoach.getId().intValue()))
            .andExpect(jsonPath("$.canalSeance").value(DEFAULT_CANAL_SEANCE.toString()))
            .andExpect(jsonPath("$.typeSeance").value(DEFAULT_TYPE_SEANCE.toString()))
            .andExpect(jsonPath("$.synthese").value(DEFAULT_SYNTHESE))
            .andExpect(jsonPath("$.descriptionDetaillee").value(DEFAULT_DESCRIPTION_DETAILLEE))
            .andExpect(jsonPath("$.tarif").value(DEFAULT_TARIF.intValue()))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE))
            .andExpect(jsonPath("$.descriptionDiplome").value(DEFAULT_DESCRIPTION_DIPLOME));
    }

    @Test
    @Transactional
    void getOffreCoachesByIdFiltering() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        Long id = offreCoach.getId();

        defaultOffreCoachFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOffreCoachFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOffreCoachFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByCanalSeanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where canalSeance equals to
        defaultOffreCoachFiltering("canalSeance.equals=" + DEFAULT_CANAL_SEANCE, "canalSeance.equals=" + UPDATED_CANAL_SEANCE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByCanalSeanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where canalSeance in
        defaultOffreCoachFiltering(
            "canalSeance.in=" + DEFAULT_CANAL_SEANCE + "," + UPDATED_CANAL_SEANCE,
            "canalSeance.in=" + UPDATED_CANAL_SEANCE
        );
    }

    @Test
    @Transactional
    void getAllOffreCoachesByCanalSeanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where canalSeance is not null
        defaultOffreCoachFiltering("canalSeance.specified=true", "canalSeance.specified=false");
    }

    @Test
    @Transactional
    void getAllOffreCoachesByTypeSeanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where typeSeance equals to
        defaultOffreCoachFiltering("typeSeance.equals=" + DEFAULT_TYPE_SEANCE, "typeSeance.equals=" + UPDATED_TYPE_SEANCE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByTypeSeanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where typeSeance in
        defaultOffreCoachFiltering(
            "typeSeance.in=" + DEFAULT_TYPE_SEANCE + "," + UPDATED_TYPE_SEANCE,
            "typeSeance.in=" + UPDATED_TYPE_SEANCE
        );
    }

    @Test
    @Transactional
    void getAllOffreCoachesByTypeSeanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where typeSeance is not null
        defaultOffreCoachFiltering("typeSeance.specified=true", "typeSeance.specified=false");
    }

    @Test
    @Transactional
    void getAllOffreCoachesBySyntheseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where synthese equals to
        defaultOffreCoachFiltering("synthese.equals=" + DEFAULT_SYNTHESE, "synthese.equals=" + UPDATED_SYNTHESE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesBySyntheseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where synthese in
        defaultOffreCoachFiltering("synthese.in=" + DEFAULT_SYNTHESE + "," + UPDATED_SYNTHESE, "synthese.in=" + UPDATED_SYNTHESE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesBySyntheseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where synthese is not null
        defaultOffreCoachFiltering("synthese.specified=true", "synthese.specified=false");
    }

    @Test
    @Transactional
    void getAllOffreCoachesBySyntheseContainsSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where synthese contains
        defaultOffreCoachFiltering("synthese.contains=" + DEFAULT_SYNTHESE, "synthese.contains=" + UPDATED_SYNTHESE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesBySyntheseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where synthese does not contain
        defaultOffreCoachFiltering("synthese.doesNotContain=" + UPDATED_SYNTHESE, "synthese.doesNotContain=" + DEFAULT_SYNTHESE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDescriptionDetailleeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where descriptionDetaillee equals to
        defaultOffreCoachFiltering(
            "descriptionDetaillee.equals=" + DEFAULT_DESCRIPTION_DETAILLEE,
            "descriptionDetaillee.equals=" + UPDATED_DESCRIPTION_DETAILLEE
        );
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDescriptionDetailleeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where descriptionDetaillee in
        defaultOffreCoachFiltering(
            "descriptionDetaillee.in=" + DEFAULT_DESCRIPTION_DETAILLEE + "," + UPDATED_DESCRIPTION_DETAILLEE,
            "descriptionDetaillee.in=" + UPDATED_DESCRIPTION_DETAILLEE
        );
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDescriptionDetailleeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where descriptionDetaillee is not null
        defaultOffreCoachFiltering("descriptionDetaillee.specified=true", "descriptionDetaillee.specified=false");
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDescriptionDetailleeContainsSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where descriptionDetaillee contains
        defaultOffreCoachFiltering(
            "descriptionDetaillee.contains=" + DEFAULT_DESCRIPTION_DETAILLEE,
            "descriptionDetaillee.contains=" + UPDATED_DESCRIPTION_DETAILLEE
        );
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDescriptionDetailleeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where descriptionDetaillee does not contain
        defaultOffreCoachFiltering(
            "descriptionDetaillee.doesNotContain=" + UPDATED_DESCRIPTION_DETAILLEE,
            "descriptionDetaillee.doesNotContain=" + DEFAULT_DESCRIPTION_DETAILLEE
        );
    }

    @Test
    @Transactional
    void getAllOffreCoachesByTarifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where tarif equals to
        defaultOffreCoachFiltering("tarif.equals=" + DEFAULT_TARIF, "tarif.equals=" + UPDATED_TARIF);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByTarifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where tarif in
        defaultOffreCoachFiltering("tarif.in=" + DEFAULT_TARIF + "," + UPDATED_TARIF, "tarif.in=" + UPDATED_TARIF);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByTarifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where tarif is not null
        defaultOffreCoachFiltering("tarif.specified=true", "tarif.specified=false");
    }

    @Test
    @Transactional
    void getAllOffreCoachesByTarifIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where tarif is greater than or equal to
        defaultOffreCoachFiltering("tarif.greaterThanOrEqual=" + DEFAULT_TARIF, "tarif.greaterThanOrEqual=" + UPDATED_TARIF);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByTarifIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where tarif is less than or equal to
        defaultOffreCoachFiltering("tarif.lessThanOrEqual=" + DEFAULT_TARIF, "tarif.lessThanOrEqual=" + SMALLER_TARIF);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByTarifIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where tarif is less than
        defaultOffreCoachFiltering("tarif.lessThan=" + UPDATED_TARIF, "tarif.lessThan=" + DEFAULT_TARIF);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByTarifIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where tarif is greater than
        defaultOffreCoachFiltering("tarif.greaterThan=" + SMALLER_TARIF, "tarif.greaterThan=" + DEFAULT_TARIF);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDureeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where duree equals to
        defaultOffreCoachFiltering("duree.equals=" + DEFAULT_DUREE, "duree.equals=" + UPDATED_DUREE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDureeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where duree in
        defaultOffreCoachFiltering("duree.in=" + DEFAULT_DUREE + "," + UPDATED_DUREE, "duree.in=" + UPDATED_DUREE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDureeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where duree is not null
        defaultOffreCoachFiltering("duree.specified=true", "duree.specified=false");
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDureeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where duree is greater than or equal to
        defaultOffreCoachFiltering("duree.greaterThanOrEqual=" + DEFAULT_DUREE, "duree.greaterThanOrEqual=" + UPDATED_DUREE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDureeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where duree is less than or equal to
        defaultOffreCoachFiltering("duree.lessThanOrEqual=" + DEFAULT_DUREE, "duree.lessThanOrEqual=" + SMALLER_DUREE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDureeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where duree is less than
        defaultOffreCoachFiltering("duree.lessThan=" + UPDATED_DUREE, "duree.lessThan=" + DEFAULT_DUREE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDureeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where duree is greater than
        defaultOffreCoachFiltering("duree.greaterThan=" + SMALLER_DUREE, "duree.greaterThan=" + DEFAULT_DUREE);
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDescriptionDiplomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where descriptionDiplome equals to
        defaultOffreCoachFiltering(
            "descriptionDiplome.equals=" + DEFAULT_DESCRIPTION_DIPLOME,
            "descriptionDiplome.equals=" + UPDATED_DESCRIPTION_DIPLOME
        );
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDescriptionDiplomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where descriptionDiplome in
        defaultOffreCoachFiltering(
            "descriptionDiplome.in=" + DEFAULT_DESCRIPTION_DIPLOME + "," + UPDATED_DESCRIPTION_DIPLOME,
            "descriptionDiplome.in=" + UPDATED_DESCRIPTION_DIPLOME
        );
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDescriptionDiplomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where descriptionDiplome is not null
        defaultOffreCoachFiltering("descriptionDiplome.specified=true", "descriptionDiplome.specified=false");
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDescriptionDiplomeContainsSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where descriptionDiplome contains
        defaultOffreCoachFiltering(
            "descriptionDiplome.contains=" + DEFAULT_DESCRIPTION_DIPLOME,
            "descriptionDiplome.contains=" + UPDATED_DESCRIPTION_DIPLOME
        );
    }

    @Test
    @Transactional
    void getAllOffreCoachesByDescriptionDiplomeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        // Get all the offreCoachList where descriptionDiplome does not contain
        defaultOffreCoachFiltering(
            "descriptionDiplome.doesNotContain=" + UPDATED_DESCRIPTION_DIPLOME,
            "descriptionDiplome.doesNotContain=" + DEFAULT_DESCRIPTION_DIPLOME
        );
    }

    @Test
    @Transactional
    void getAllOffreCoachesBySpecialiteIsEqualToSomething() throws Exception {
        SpecialiteExpertise specialite;
        if (TestUtil.findAll(em, SpecialiteExpertise.class).isEmpty()) {
            offreCoachRepository.saveAndFlush(offreCoach);
            specialite = SpecialiteExpertiseResourceIT.createEntity();
        } else {
            specialite = TestUtil.findAll(em, SpecialiteExpertise.class).get(0);
        }
        em.persist(specialite);
        em.flush();
        offreCoach.setSpecialite(specialite);
        offreCoachRepository.saveAndFlush(offreCoach);
        Long specialiteId = specialite.getId();
        // Get all the offreCoachList where specialite equals to specialiteId
        defaultOffreCoachShouldBeFound("specialiteId.equals=" + specialiteId);

        // Get all the offreCoachList where specialite equals to (specialiteId + 1)
        defaultOffreCoachShouldNotBeFound("specialiteId.equals=" + (specialiteId + 1));
    }

    @Test
    @Transactional
    void getAllOffreCoachesByCoachExpertIsEqualToSomething() throws Exception {
        CoachExpert coachExpert;
        if (TestUtil.findAll(em, CoachExpert.class).isEmpty()) {
            offreCoachRepository.saveAndFlush(offreCoach);
            coachExpert = CoachExpertResourceIT.createEntity();
        } else {
            coachExpert = TestUtil.findAll(em, CoachExpert.class).get(0);
        }
        em.persist(coachExpert);
        em.flush();
        offreCoach.setCoachExpert(coachExpert);
        offreCoachRepository.saveAndFlush(offreCoach);
        Long coachExpertId = coachExpert.getId();
        // Get all the offreCoachList where coachExpert equals to coachExpertId
        defaultOffreCoachShouldBeFound("coachExpertId.equals=" + coachExpertId);

        // Get all the offreCoachList where coachExpert equals to (coachExpertId + 1)
        defaultOffreCoachShouldNotBeFound("coachExpertId.equals=" + (coachExpertId + 1));
    }

    private void defaultOffreCoachFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOffreCoachShouldBeFound(shouldBeFound);
        defaultOffreCoachShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOffreCoachShouldBeFound(String filter) throws Exception {
        restOffreCoachMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offreCoach.getId().intValue())))
            .andExpect(jsonPath("$.[*].canalSeance").value(hasItem(DEFAULT_CANAL_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].typeSeance").value(hasItem(DEFAULT_TYPE_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].synthese").value(hasItem(DEFAULT_SYNTHESE)))
            .andExpect(jsonPath("$.[*].descriptionDetaillee").value(hasItem(DEFAULT_DESCRIPTION_DETAILLEE)))
            .andExpect(jsonPath("$.[*].tarif").value(hasItem(DEFAULT_TARIF.intValue())))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].descriptionDiplome").value(hasItem(DEFAULT_DESCRIPTION_DIPLOME)));

        // Check, that the count call also returns 1
        restOffreCoachMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOffreCoachShouldNotBeFound(String filter) throws Exception {
        restOffreCoachMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOffreCoachMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOffreCoach() throws Exception {
        // Get the offreCoach
        restOffreCoachMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOffreCoach() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offreCoach
        OffreCoach updatedOffreCoach = offreCoachRepository.findById(offreCoach.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOffreCoach are not directly saved in db
        em.detach(updatedOffreCoach);
        updatedOffreCoach
            .canalSeance(UPDATED_CANAL_SEANCE)
            .typeSeance(UPDATED_TYPE_SEANCE)
            .synthese(UPDATED_SYNTHESE)
            .descriptionDetaillee(UPDATED_DESCRIPTION_DETAILLEE)
            .tarif(UPDATED_TARIF)
            .duree(UPDATED_DUREE)
            .descriptionDiplome(UPDATED_DESCRIPTION_DIPLOME);

        restOffreCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOffreCoach.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOffreCoach))
            )
            .andExpect(status().isOk());

        // Validate the OffreCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOffreCoachToMatchAllProperties(updatedOffreCoach);
    }

    @Test
    @Transactional
    void putNonExistingOffreCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoach.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offreCoach.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreCoach))
            )
            .andExpect(status().isBadRequest());

        // Validate the OffreCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOffreCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(offreCoach))
            )
            .andExpect(status().isBadRequest());

        // Validate the OffreCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOffreCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreCoachMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreCoach)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OffreCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOffreCoachWithPatch() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offreCoach using partial update
        OffreCoach partialUpdatedOffreCoach = new OffreCoach();
        partialUpdatedOffreCoach.setId(offreCoach.getId());

        partialUpdatedOffreCoach.canalSeance(UPDATED_CANAL_SEANCE).duree(UPDATED_DUREE).descriptionDiplome(UPDATED_DESCRIPTION_DIPLOME);

        restOffreCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffreCoach.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOffreCoach))
            )
            .andExpect(status().isOk());

        // Validate the OffreCoach in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOffreCoachUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOffreCoach, offreCoach),
            getPersistedOffreCoach(offreCoach)
        );
    }

    @Test
    @Transactional
    void fullUpdateOffreCoachWithPatch() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offreCoach using partial update
        OffreCoach partialUpdatedOffreCoach = new OffreCoach();
        partialUpdatedOffreCoach.setId(offreCoach.getId());

        partialUpdatedOffreCoach
            .canalSeance(UPDATED_CANAL_SEANCE)
            .typeSeance(UPDATED_TYPE_SEANCE)
            .synthese(UPDATED_SYNTHESE)
            .descriptionDetaillee(UPDATED_DESCRIPTION_DETAILLEE)
            .tarif(UPDATED_TARIF)
            .duree(UPDATED_DUREE)
            .descriptionDiplome(UPDATED_DESCRIPTION_DIPLOME);

        restOffreCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffreCoach.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOffreCoach))
            )
            .andExpect(status().isOk());

        // Validate the OffreCoach in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOffreCoachUpdatableFieldsEquals(partialUpdatedOffreCoach, getPersistedOffreCoach(partialUpdatedOffreCoach));
    }

    @Test
    @Transactional
    void patchNonExistingOffreCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoach.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offreCoach.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(offreCoach))
            )
            .andExpect(status().isBadRequest());

        // Validate the OffreCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOffreCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(offreCoach))
            )
            .andExpect(status().isBadRequest());

        // Validate the OffreCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOffreCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreCoachMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(offreCoach)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OffreCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOffreCoach() throws Exception {
        // Initialize the database
        insertedOffreCoach = offreCoachRepository.saveAndFlush(offreCoach);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the offreCoach
        restOffreCoachMockMvc
            .perform(delete(ENTITY_API_URL_ID, offreCoach.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return offreCoachRepository.count();
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

    protected OffreCoach getPersistedOffreCoach(OffreCoach offreCoach) {
        return offreCoachRepository.findById(offreCoach.getId()).orElseThrow();
    }

    protected void assertPersistedOffreCoachToMatchAllProperties(OffreCoach expectedOffreCoach) {
        assertOffreCoachAllPropertiesEquals(expectedOffreCoach, getPersistedOffreCoach(expectedOffreCoach));
    }

    protected void assertPersistedOffreCoachToMatchUpdatableProperties(OffreCoach expectedOffreCoach) {
        assertOffreCoachAllUpdatablePropertiesEquals(expectedOffreCoach, getPersistedOffreCoach(expectedOffreCoach));
    }
}
