package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.SeanceReservationCoachAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.Client;
import fr.coaching.maseance.domain.CoachExpert;
import fr.coaching.maseance.domain.Facture;
import fr.coaching.maseance.domain.OffreCoach;
import fr.coaching.maseance.domain.SeanceReservationCoach;
import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.domain.enumeration.StatutSeance;
import fr.coaching.maseance.domain.enumeration.TypeSeance;
import fr.coaching.maseance.repository.SeanceReservationCoachRepository;
import fr.coaching.maseance.service.SeanceReservationCoachService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SeanceReservationCoachResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SeanceReservationCoachResourceIT {

    private static final Instant DEFAULT_HEURE_DEBUT_CRENEAU_RESERVE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HEURE_DEBUT_CRENEAU_RESERVE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_HEURE_FIN_CRENEAU_RESERVE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HEURE_FIN_CRENEAU_RESERVE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final CanalSeance DEFAULT_CANAL_SEANCE = CanalSeance.AdressePhysique;
    private static final CanalSeance UPDATED_CANAL_SEANCE = CanalSeance.AppelVisio;

    private static final TypeSeance DEFAULT_TYPE_SEANCE = TypeSeance.Individuelle;
    private static final TypeSeance UPDATED_TYPE_SEANCE = TypeSeance.Collective;

    private static final StatutSeance DEFAULT_STATUT_REALISATION = StatutSeance.ReservationFaite;
    private static final StatutSeance UPDATED_STATUT_REALISATION = StatutSeance.SeanceEffectuee;

    private static final String ENTITY_API_URL = "/api/seance-reservation-coaches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SeanceReservationCoachRepository seanceReservationCoachRepository;

    @Mock
    private SeanceReservationCoachRepository seanceReservationCoachRepositoryMock;

    @Mock
    private SeanceReservationCoachService seanceReservationCoachServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeanceReservationCoachMockMvc;

    private SeanceReservationCoach seanceReservationCoach;

    private SeanceReservationCoach insertedSeanceReservationCoach;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeanceReservationCoach createEntity() {
        return new SeanceReservationCoach()
            .heureDebutCreneauReserve(DEFAULT_HEURE_DEBUT_CRENEAU_RESERVE)
            .heureFinCreneauReserve(DEFAULT_HEURE_FIN_CRENEAU_RESERVE)
            .canalSeance(DEFAULT_CANAL_SEANCE)
            .typeSeance(DEFAULT_TYPE_SEANCE)
            .statutRealisation(DEFAULT_STATUT_REALISATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeanceReservationCoach createUpdatedEntity() {
        return new SeanceReservationCoach()
            .heureDebutCreneauReserve(UPDATED_HEURE_DEBUT_CRENEAU_RESERVE)
            .heureFinCreneauReserve(UPDATED_HEURE_FIN_CRENEAU_RESERVE)
            .canalSeance(UPDATED_CANAL_SEANCE)
            .typeSeance(UPDATED_TYPE_SEANCE)
            .statutRealisation(UPDATED_STATUT_REALISATION);
    }

    @BeforeEach
    public void initTest() {
        seanceReservationCoach = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSeanceReservationCoach != null) {
            seanceReservationCoachRepository.delete(insertedSeanceReservationCoach);
            insertedSeanceReservationCoach = null;
        }
    }

    @Test
    @Transactional
    void createSeanceReservationCoach() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SeanceReservationCoach
        var returnedSeanceReservationCoach = om.readValue(
            restSeanceReservationCoachMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seanceReservationCoach)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SeanceReservationCoach.class
        );

        // Validate the SeanceReservationCoach in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSeanceReservationCoachUpdatableFieldsEquals(
            returnedSeanceReservationCoach,
            getPersistedSeanceReservationCoach(returnedSeanceReservationCoach)
        );

        insertedSeanceReservationCoach = returnedSeanceReservationCoach;
    }

    @Test
    @Transactional
    void createSeanceReservationCoachWithExistingId() throws Exception {
        // Create the SeanceReservationCoach with an existing ID
        seanceReservationCoach.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeanceReservationCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seanceReservationCoach)))
            .andExpect(status().isBadRequest());

        // Validate the SeanceReservationCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHeureDebutCreneauReserveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seanceReservationCoach.setHeureDebutCreneauReserve(null);

        // Create the SeanceReservationCoach, which fails.

        restSeanceReservationCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seanceReservationCoach)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHeureFinCreneauReserveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seanceReservationCoach.setHeureFinCreneauReserve(null);

        // Create the SeanceReservationCoach, which fails.

        restSeanceReservationCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seanceReservationCoach)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCanalSeanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seanceReservationCoach.setCanalSeance(null);

        // Create the SeanceReservationCoach, which fails.

        restSeanceReservationCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seanceReservationCoach)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeSeanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seanceReservationCoach.setTypeSeance(null);

        // Create the SeanceReservationCoach, which fails.

        restSeanceReservationCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seanceReservationCoach)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutRealisationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seanceReservationCoach.setStatutRealisation(null);

        // Create the SeanceReservationCoach, which fails.

        restSeanceReservationCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seanceReservationCoach)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoaches() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList
        restSeanceReservationCoachMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seanceReservationCoach.getId().intValue())))
            .andExpect(jsonPath("$.[*].heureDebutCreneauReserve").value(hasItem(DEFAULT_HEURE_DEBUT_CRENEAU_RESERVE.toString())))
            .andExpect(jsonPath("$.[*].heureFinCreneauReserve").value(hasItem(DEFAULT_HEURE_FIN_CRENEAU_RESERVE.toString())))
            .andExpect(jsonPath("$.[*].canalSeance").value(hasItem(DEFAULT_CANAL_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].typeSeance").value(hasItem(DEFAULT_TYPE_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].statutRealisation").value(hasItem(DEFAULT_STATUT_REALISATION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSeanceReservationCoachesWithEagerRelationshipsIsEnabled() throws Exception {
        when(seanceReservationCoachServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSeanceReservationCoachMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(seanceReservationCoachServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSeanceReservationCoachesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(seanceReservationCoachServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSeanceReservationCoachMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(seanceReservationCoachRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSeanceReservationCoach() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get the seanceReservationCoach
        restSeanceReservationCoachMockMvc
            .perform(get(ENTITY_API_URL_ID, seanceReservationCoach.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seanceReservationCoach.getId().intValue()))
            .andExpect(jsonPath("$.heureDebutCreneauReserve").value(DEFAULT_HEURE_DEBUT_CRENEAU_RESERVE.toString()))
            .andExpect(jsonPath("$.heureFinCreneauReserve").value(DEFAULT_HEURE_FIN_CRENEAU_RESERVE.toString()))
            .andExpect(jsonPath("$.canalSeance").value(DEFAULT_CANAL_SEANCE.toString()))
            .andExpect(jsonPath("$.typeSeance").value(DEFAULT_TYPE_SEANCE.toString()))
            .andExpect(jsonPath("$.statutRealisation").value(DEFAULT_STATUT_REALISATION.toString()));
    }

    @Test
    @Transactional
    void getSeanceReservationCoachesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        Long id = seanceReservationCoach.getId();

        defaultSeanceReservationCoachFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSeanceReservationCoachFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSeanceReservationCoachFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByHeureDebutCreneauReserveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where heureDebutCreneauReserve equals to
        defaultSeanceReservationCoachFiltering(
            "heureDebutCreneauReserve.equals=" + DEFAULT_HEURE_DEBUT_CRENEAU_RESERVE,
            "heureDebutCreneauReserve.equals=" + UPDATED_HEURE_DEBUT_CRENEAU_RESERVE
        );
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByHeureDebutCreneauReserveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where heureDebutCreneauReserve in
        defaultSeanceReservationCoachFiltering(
            "heureDebutCreneauReserve.in=" + DEFAULT_HEURE_DEBUT_CRENEAU_RESERVE + "," + UPDATED_HEURE_DEBUT_CRENEAU_RESERVE,
            "heureDebutCreneauReserve.in=" + UPDATED_HEURE_DEBUT_CRENEAU_RESERVE
        );
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByHeureDebutCreneauReserveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where heureDebutCreneauReserve is not null
        defaultSeanceReservationCoachFiltering("heureDebutCreneauReserve.specified=true", "heureDebutCreneauReserve.specified=false");
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByHeureFinCreneauReserveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where heureFinCreneauReserve equals to
        defaultSeanceReservationCoachFiltering(
            "heureFinCreneauReserve.equals=" + DEFAULT_HEURE_FIN_CRENEAU_RESERVE,
            "heureFinCreneauReserve.equals=" + UPDATED_HEURE_FIN_CRENEAU_RESERVE
        );
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByHeureFinCreneauReserveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where heureFinCreneauReserve in
        defaultSeanceReservationCoachFiltering(
            "heureFinCreneauReserve.in=" + DEFAULT_HEURE_FIN_CRENEAU_RESERVE + "," + UPDATED_HEURE_FIN_CRENEAU_RESERVE,
            "heureFinCreneauReserve.in=" + UPDATED_HEURE_FIN_CRENEAU_RESERVE
        );
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByHeureFinCreneauReserveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where heureFinCreneauReserve is not null
        defaultSeanceReservationCoachFiltering("heureFinCreneauReserve.specified=true", "heureFinCreneauReserve.specified=false");
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByCanalSeanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where canalSeance equals to
        defaultSeanceReservationCoachFiltering("canalSeance.equals=" + DEFAULT_CANAL_SEANCE, "canalSeance.equals=" + UPDATED_CANAL_SEANCE);
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByCanalSeanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where canalSeance in
        defaultSeanceReservationCoachFiltering(
            "canalSeance.in=" + DEFAULT_CANAL_SEANCE + "," + UPDATED_CANAL_SEANCE,
            "canalSeance.in=" + UPDATED_CANAL_SEANCE
        );
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByCanalSeanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where canalSeance is not null
        defaultSeanceReservationCoachFiltering("canalSeance.specified=true", "canalSeance.specified=false");
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByTypeSeanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where typeSeance equals to
        defaultSeanceReservationCoachFiltering("typeSeance.equals=" + DEFAULT_TYPE_SEANCE, "typeSeance.equals=" + UPDATED_TYPE_SEANCE);
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByTypeSeanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where typeSeance in
        defaultSeanceReservationCoachFiltering(
            "typeSeance.in=" + DEFAULT_TYPE_SEANCE + "," + UPDATED_TYPE_SEANCE,
            "typeSeance.in=" + UPDATED_TYPE_SEANCE
        );
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByTypeSeanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where typeSeance is not null
        defaultSeanceReservationCoachFiltering("typeSeance.specified=true", "typeSeance.specified=false");
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByStatutRealisationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where statutRealisation equals to
        defaultSeanceReservationCoachFiltering(
            "statutRealisation.equals=" + DEFAULT_STATUT_REALISATION,
            "statutRealisation.equals=" + UPDATED_STATUT_REALISATION
        );
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByStatutRealisationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where statutRealisation in
        defaultSeanceReservationCoachFiltering(
            "statutRealisation.in=" + DEFAULT_STATUT_REALISATION + "," + UPDATED_STATUT_REALISATION,
            "statutRealisation.in=" + UPDATED_STATUT_REALISATION
        );
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByStatutRealisationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        // Get all the seanceReservationCoachList where statutRealisation is not null
        defaultSeanceReservationCoachFiltering("statutRealisation.specified=true", "statutRealisation.specified=false");
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByFactureIsEqualToSomething() throws Exception {
        Facture facture;
        if (TestUtil.findAll(em, Facture.class).isEmpty()) {
            seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);
            facture = FactureResourceIT.createEntity();
        } else {
            facture = TestUtil.findAll(em, Facture.class).get(0);
        }
        em.persist(facture);
        em.flush();
        seanceReservationCoach.setFacture(facture);
        seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);
        Long factureId = facture.getId();
        // Get all the seanceReservationCoachList where facture equals to factureId
        defaultSeanceReservationCoachShouldBeFound("factureId.equals=" + factureId);

        // Get all the seanceReservationCoachList where facture equals to (factureId + 1)
        defaultSeanceReservationCoachShouldNotBeFound("factureId.equals=" + (factureId + 1));
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByCoachExpertIsEqualToSomething() throws Exception {
        CoachExpert coachExpert;
        if (TestUtil.findAll(em, CoachExpert.class).isEmpty()) {
            seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);
            coachExpert = CoachExpertResourceIT.createEntity();
        } else {
            coachExpert = TestUtil.findAll(em, CoachExpert.class).get(0);
        }
        em.persist(coachExpert);
        em.flush();
        seanceReservationCoach.setCoachExpert(coachExpert);
        seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);
        Long coachExpertId = coachExpert.getId();
        // Get all the seanceReservationCoachList where coachExpert equals to coachExpertId
        defaultSeanceReservationCoachShouldBeFound("coachExpertId.equals=" + coachExpertId);

        // Get all the seanceReservationCoachList where coachExpert equals to (coachExpertId + 1)
        defaultSeanceReservationCoachShouldNotBeFound("coachExpertId.equals=" + (coachExpertId + 1));
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        seanceReservationCoach.setClient(client);
        seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);
        Long clientId = client.getId();
        // Get all the seanceReservationCoachList where client equals to clientId
        defaultSeanceReservationCoachShouldBeFound("clientId.equals=" + clientId);

        // Get all the seanceReservationCoachList where client equals to (clientId + 1)
        defaultSeanceReservationCoachShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    @Test
    @Transactional
    void getAllSeanceReservationCoachesByOffreIsEqualToSomething() throws Exception {
        OffreCoach offre;
        if (TestUtil.findAll(em, OffreCoach.class).isEmpty()) {
            seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);
            offre = OffreCoachResourceIT.createEntity();
        } else {
            offre = TestUtil.findAll(em, OffreCoach.class).get(0);
        }
        em.persist(offre);
        em.flush();
        seanceReservationCoach.setOffre(offre);
        seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);
        Long offreId = offre.getId();
        // Get all the seanceReservationCoachList where offre equals to offreId
        defaultSeanceReservationCoachShouldBeFound("offreId.equals=" + offreId);

        // Get all the seanceReservationCoachList where offre equals to (offreId + 1)
        defaultSeanceReservationCoachShouldNotBeFound("offreId.equals=" + (offreId + 1));
    }

    private void defaultSeanceReservationCoachFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSeanceReservationCoachShouldBeFound(shouldBeFound);
        defaultSeanceReservationCoachShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSeanceReservationCoachShouldBeFound(String filter) throws Exception {
        restSeanceReservationCoachMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seanceReservationCoach.getId().intValue())))
            .andExpect(jsonPath("$.[*].heureDebutCreneauReserve").value(hasItem(DEFAULT_HEURE_DEBUT_CRENEAU_RESERVE.toString())))
            .andExpect(jsonPath("$.[*].heureFinCreneauReserve").value(hasItem(DEFAULT_HEURE_FIN_CRENEAU_RESERVE.toString())))
            .andExpect(jsonPath("$.[*].canalSeance").value(hasItem(DEFAULT_CANAL_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].typeSeance").value(hasItem(DEFAULT_TYPE_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].statutRealisation").value(hasItem(DEFAULT_STATUT_REALISATION.toString())));

        // Check, that the count call also returns 1
        restSeanceReservationCoachMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSeanceReservationCoachShouldNotBeFound(String filter) throws Exception {
        restSeanceReservationCoachMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSeanceReservationCoachMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSeanceReservationCoach() throws Exception {
        // Get the seanceReservationCoach
        restSeanceReservationCoachMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeanceReservationCoach() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seanceReservationCoach
        SeanceReservationCoach updatedSeanceReservationCoach = seanceReservationCoachRepository
            .findById(seanceReservationCoach.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSeanceReservationCoach are not directly saved in db
        em.detach(updatedSeanceReservationCoach);
        updatedSeanceReservationCoach
            .heureDebutCreneauReserve(UPDATED_HEURE_DEBUT_CRENEAU_RESERVE)
            .heureFinCreneauReserve(UPDATED_HEURE_FIN_CRENEAU_RESERVE)
            .canalSeance(UPDATED_CANAL_SEANCE)
            .typeSeance(UPDATED_TYPE_SEANCE)
            .statutRealisation(UPDATED_STATUT_REALISATION);

        restSeanceReservationCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeanceReservationCoach.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSeanceReservationCoach))
            )
            .andExpect(status().isOk());

        // Validate the SeanceReservationCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSeanceReservationCoachToMatchAllProperties(updatedSeanceReservationCoach);
    }

    @Test
    @Transactional
    void putNonExistingSeanceReservationCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seanceReservationCoach.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeanceReservationCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seanceReservationCoach.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(seanceReservationCoach))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeanceReservationCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeanceReservationCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seanceReservationCoach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceReservationCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(seanceReservationCoach))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeanceReservationCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeanceReservationCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seanceReservationCoach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceReservationCoachMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seanceReservationCoach)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeanceReservationCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeanceReservationCoachWithPatch() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seanceReservationCoach using partial update
        SeanceReservationCoach partialUpdatedSeanceReservationCoach = new SeanceReservationCoach();
        partialUpdatedSeanceReservationCoach.setId(seanceReservationCoach.getId());

        partialUpdatedSeanceReservationCoach
            .heureFinCreneauReserve(UPDATED_HEURE_FIN_CRENEAU_RESERVE)
            .canalSeance(UPDATED_CANAL_SEANCE)
            .typeSeance(UPDATED_TYPE_SEANCE)
            .statutRealisation(UPDATED_STATUT_REALISATION);

        restSeanceReservationCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeanceReservationCoach.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeanceReservationCoach))
            )
            .andExpect(status().isOk());

        // Validate the SeanceReservationCoach in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSeanceReservationCoachUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSeanceReservationCoach, seanceReservationCoach),
            getPersistedSeanceReservationCoach(seanceReservationCoach)
        );
    }

    @Test
    @Transactional
    void fullUpdateSeanceReservationCoachWithPatch() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seanceReservationCoach using partial update
        SeanceReservationCoach partialUpdatedSeanceReservationCoach = new SeanceReservationCoach();
        partialUpdatedSeanceReservationCoach.setId(seanceReservationCoach.getId());

        partialUpdatedSeanceReservationCoach
            .heureDebutCreneauReserve(UPDATED_HEURE_DEBUT_CRENEAU_RESERVE)
            .heureFinCreneauReserve(UPDATED_HEURE_FIN_CRENEAU_RESERVE)
            .canalSeance(UPDATED_CANAL_SEANCE)
            .typeSeance(UPDATED_TYPE_SEANCE)
            .statutRealisation(UPDATED_STATUT_REALISATION);

        restSeanceReservationCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeanceReservationCoach.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeanceReservationCoach))
            )
            .andExpect(status().isOk());

        // Validate the SeanceReservationCoach in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSeanceReservationCoachUpdatableFieldsEquals(
            partialUpdatedSeanceReservationCoach,
            getPersistedSeanceReservationCoach(partialUpdatedSeanceReservationCoach)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSeanceReservationCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seanceReservationCoach.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeanceReservationCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seanceReservationCoach.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(seanceReservationCoach))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeanceReservationCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeanceReservationCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seanceReservationCoach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceReservationCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(seanceReservationCoach))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeanceReservationCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeanceReservationCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seanceReservationCoach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceReservationCoachMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(seanceReservationCoach))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeanceReservationCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeanceReservationCoach() throws Exception {
        // Initialize the database
        insertedSeanceReservationCoach = seanceReservationCoachRepository.saveAndFlush(seanceReservationCoach);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the seanceReservationCoach
        restSeanceReservationCoachMockMvc
            .perform(delete(ENTITY_API_URL_ID, seanceReservationCoach.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return seanceReservationCoachRepository.count();
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

    protected SeanceReservationCoach getPersistedSeanceReservationCoach(SeanceReservationCoach seanceReservationCoach) {
        return seanceReservationCoachRepository.findById(seanceReservationCoach.getId()).orElseThrow();
    }

    protected void assertPersistedSeanceReservationCoachToMatchAllProperties(SeanceReservationCoach expectedSeanceReservationCoach) {
        assertSeanceReservationCoachAllPropertiesEquals(
            expectedSeanceReservationCoach,
            getPersistedSeanceReservationCoach(expectedSeanceReservationCoach)
        );
    }

    protected void assertPersistedSeanceReservationCoachToMatchUpdatableProperties(SeanceReservationCoach expectedSeanceReservationCoach) {
        assertSeanceReservationCoachAllUpdatablePropertiesEquals(
            expectedSeanceReservationCoach,
            getPersistedSeanceReservationCoach(expectedSeanceReservationCoach)
        );
    }
}
