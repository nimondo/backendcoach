package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.AvisClientAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.AvisClient;
import fr.coaching.maseance.domain.Client;
import fr.coaching.maseance.domain.CoachExpert;
import fr.coaching.maseance.repository.AvisClientRepository;
import fr.coaching.maseance.service.AvisClientService;
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
 * Integration tests for the {@link AvisClientResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AvisClientResourceIT {

    private static final Instant DEFAULT_DATE_AVIS = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_AVIS = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NOTE = 1;
    private static final Integer UPDATED_NOTE = 2;
    private static final Integer SMALLER_NOTE = 1 - 1;

    private static final String DEFAULT_DESCRIPTION_AVIS = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_AVIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/avis-clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AvisClientRepository avisClientRepository;

    @Mock
    private AvisClientRepository avisClientRepositoryMock;

    @Mock
    private AvisClientService avisClientServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvisClientMockMvc;

    private AvisClient avisClient;

    private AvisClient insertedAvisClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvisClient createEntity() {
        return new AvisClient().dateAvis(DEFAULT_DATE_AVIS).note(DEFAULT_NOTE).descriptionAvis(DEFAULT_DESCRIPTION_AVIS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvisClient createUpdatedEntity() {
        return new AvisClient().dateAvis(UPDATED_DATE_AVIS).note(UPDATED_NOTE).descriptionAvis(UPDATED_DESCRIPTION_AVIS);
    }

    @BeforeEach
    public void initTest() {
        avisClient = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAvisClient != null) {
            avisClientRepository.delete(insertedAvisClient);
            insertedAvisClient = null;
        }
    }

    @Test
    @Transactional
    void createAvisClient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AvisClient
        var returnedAvisClient = om.readValue(
            restAvisClientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avisClient)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AvisClient.class
        );

        // Validate the AvisClient in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAvisClientUpdatableFieldsEquals(returnedAvisClient, getPersistedAvisClient(returnedAvisClient));

        insertedAvisClient = returnedAvisClient;
    }

    @Test
    @Transactional
    void createAvisClientWithExistingId() throws Exception {
        // Create the AvisClient with an existing ID
        avisClient.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvisClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avisClient)))
            .andExpect(status().isBadRequest());

        // Validate the AvisClient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateAvisIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        avisClient.setDateAvis(null);

        // Create the AvisClient, which fails.

        restAvisClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avisClient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNoteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        avisClient.setNote(null);

        // Create the AvisClient, which fails.

        restAvisClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avisClient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAvisClients() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList
        restAvisClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avisClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAvis").value(hasItem(DEFAULT_DATE_AVIS.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].descriptionAvis").value(hasItem(DEFAULT_DESCRIPTION_AVIS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAvisClientsWithEagerRelationshipsIsEnabled() throws Exception {
        when(avisClientServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAvisClientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(avisClientServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAvisClientsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(avisClientServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAvisClientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(avisClientRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAvisClient() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get the avisClient
        restAvisClientMockMvc
            .perform(get(ENTITY_API_URL_ID, avisClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(avisClient.getId().intValue()))
            .andExpect(jsonPath("$.dateAvis").value(DEFAULT_DATE_AVIS.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.descriptionAvis").value(DEFAULT_DESCRIPTION_AVIS));
    }

    @Test
    @Transactional
    void getAvisClientsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        Long id = avisClient.getId();

        defaultAvisClientFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAvisClientFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAvisClientFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAvisClientsByDateAvisIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where dateAvis equals to
        defaultAvisClientFiltering("dateAvis.equals=" + DEFAULT_DATE_AVIS, "dateAvis.equals=" + UPDATED_DATE_AVIS);
    }

    @Test
    @Transactional
    void getAllAvisClientsByDateAvisIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where dateAvis in
        defaultAvisClientFiltering("dateAvis.in=" + DEFAULT_DATE_AVIS + "," + UPDATED_DATE_AVIS, "dateAvis.in=" + UPDATED_DATE_AVIS);
    }

    @Test
    @Transactional
    void getAllAvisClientsByDateAvisIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where dateAvis is not null
        defaultAvisClientFiltering("dateAvis.specified=true", "dateAvis.specified=false");
    }

    @Test
    @Transactional
    void getAllAvisClientsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where note equals to
        defaultAvisClientFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAvisClientsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where note in
        defaultAvisClientFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAvisClientsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where note is not null
        defaultAvisClientFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllAvisClientsByNoteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where note is greater than or equal to
        defaultAvisClientFiltering("note.greaterThanOrEqual=" + DEFAULT_NOTE, "note.greaterThanOrEqual=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAvisClientsByNoteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where note is less than or equal to
        defaultAvisClientFiltering("note.lessThanOrEqual=" + DEFAULT_NOTE, "note.lessThanOrEqual=" + SMALLER_NOTE);
    }

    @Test
    @Transactional
    void getAllAvisClientsByNoteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where note is less than
        defaultAvisClientFiltering("note.lessThan=" + UPDATED_NOTE, "note.lessThan=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllAvisClientsByNoteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where note is greater than
        defaultAvisClientFiltering("note.greaterThan=" + SMALLER_NOTE, "note.greaterThan=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllAvisClientsByDescriptionAvisIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where descriptionAvis equals to
        defaultAvisClientFiltering(
            "descriptionAvis.equals=" + DEFAULT_DESCRIPTION_AVIS,
            "descriptionAvis.equals=" + UPDATED_DESCRIPTION_AVIS
        );
    }

    @Test
    @Transactional
    void getAllAvisClientsByDescriptionAvisIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where descriptionAvis in
        defaultAvisClientFiltering(
            "descriptionAvis.in=" + DEFAULT_DESCRIPTION_AVIS + "," + UPDATED_DESCRIPTION_AVIS,
            "descriptionAvis.in=" + UPDATED_DESCRIPTION_AVIS
        );
    }

    @Test
    @Transactional
    void getAllAvisClientsByDescriptionAvisIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where descriptionAvis is not null
        defaultAvisClientFiltering("descriptionAvis.specified=true", "descriptionAvis.specified=false");
    }

    @Test
    @Transactional
    void getAllAvisClientsByDescriptionAvisContainsSomething() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where descriptionAvis contains
        defaultAvisClientFiltering(
            "descriptionAvis.contains=" + DEFAULT_DESCRIPTION_AVIS,
            "descriptionAvis.contains=" + UPDATED_DESCRIPTION_AVIS
        );
    }

    @Test
    @Transactional
    void getAllAvisClientsByDescriptionAvisNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        // Get all the avisClientList where descriptionAvis does not contain
        defaultAvisClientFiltering(
            "descriptionAvis.doesNotContain=" + UPDATED_DESCRIPTION_AVIS,
            "descriptionAvis.doesNotContain=" + DEFAULT_DESCRIPTION_AVIS
        );
    }

    @Test
    @Transactional
    void getAllAvisClientsByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            avisClientRepository.saveAndFlush(avisClient);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        avisClient.setClient(client);
        avisClientRepository.saveAndFlush(avisClient);
        Long clientId = client.getId();
        // Get all the avisClientList where client equals to clientId
        defaultAvisClientShouldBeFound("clientId.equals=" + clientId);

        // Get all the avisClientList where client equals to (clientId + 1)
        defaultAvisClientShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    @Test
    @Transactional
    void getAllAvisClientsByCoachExpertIsEqualToSomething() throws Exception {
        CoachExpert coachExpert;
        if (TestUtil.findAll(em, CoachExpert.class).isEmpty()) {
            avisClientRepository.saveAndFlush(avisClient);
            coachExpert = CoachExpertResourceIT.createEntity();
        } else {
            coachExpert = TestUtil.findAll(em, CoachExpert.class).get(0);
        }
        em.persist(coachExpert);
        em.flush();
        avisClient.setCoachExpert(coachExpert);
        avisClientRepository.saveAndFlush(avisClient);
        Long coachExpertId = coachExpert.getId();
        // Get all the avisClientList where coachExpert equals to coachExpertId
        defaultAvisClientShouldBeFound("coachExpertId.equals=" + coachExpertId);

        // Get all the avisClientList where coachExpert equals to (coachExpertId + 1)
        defaultAvisClientShouldNotBeFound("coachExpertId.equals=" + (coachExpertId + 1));
    }

    private void defaultAvisClientFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAvisClientShouldBeFound(shouldBeFound);
        defaultAvisClientShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAvisClientShouldBeFound(String filter) throws Exception {
        restAvisClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avisClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAvis").value(hasItem(DEFAULT_DATE_AVIS.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].descriptionAvis").value(hasItem(DEFAULT_DESCRIPTION_AVIS)));

        // Check, that the count call also returns 1
        restAvisClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAvisClientShouldNotBeFound(String filter) throws Exception {
        restAvisClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAvisClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAvisClient() throws Exception {
        // Get the avisClient
        restAvisClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAvisClient() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the avisClient
        AvisClient updatedAvisClient = avisClientRepository.findById(avisClient.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAvisClient are not directly saved in db
        em.detach(updatedAvisClient);
        updatedAvisClient.dateAvis(UPDATED_DATE_AVIS).note(UPDATED_NOTE).descriptionAvis(UPDATED_DESCRIPTION_AVIS);

        restAvisClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAvisClient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAvisClient))
            )
            .andExpect(status().isOk());

        // Validate the AvisClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAvisClientToMatchAllProperties(updatedAvisClient);
    }

    @Test
    @Transactional
    void putNonExistingAvisClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avisClient.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvisClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, avisClient.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avisClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvisClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvisClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avisClient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvisClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(avisClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvisClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvisClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avisClient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvisClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avisClient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AvisClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvisClientWithPatch() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the avisClient using partial update
        AvisClient partialUpdatedAvisClient = new AvisClient();
        partialUpdatedAvisClient.setId(avisClient.getId());

        partialUpdatedAvisClient.dateAvis(UPDATED_DATE_AVIS).descriptionAvis(UPDATED_DESCRIPTION_AVIS);

        restAvisClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvisClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAvisClient))
            )
            .andExpect(status().isOk());

        // Validate the AvisClient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAvisClientUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAvisClient, avisClient),
            getPersistedAvisClient(avisClient)
        );
    }

    @Test
    @Transactional
    void fullUpdateAvisClientWithPatch() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the avisClient using partial update
        AvisClient partialUpdatedAvisClient = new AvisClient();
        partialUpdatedAvisClient.setId(avisClient.getId());

        partialUpdatedAvisClient.dateAvis(UPDATED_DATE_AVIS).note(UPDATED_NOTE).descriptionAvis(UPDATED_DESCRIPTION_AVIS);

        restAvisClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvisClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAvisClient))
            )
            .andExpect(status().isOk());

        // Validate the AvisClient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAvisClientUpdatableFieldsEquals(partialUpdatedAvisClient, getPersistedAvisClient(partialUpdatedAvisClient));
    }

    @Test
    @Transactional
    void patchNonExistingAvisClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avisClient.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvisClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, avisClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(avisClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvisClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvisClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avisClient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvisClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(avisClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvisClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvisClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avisClient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvisClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(avisClient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AvisClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvisClient() throws Exception {
        // Initialize the database
        insertedAvisClient = avisClientRepository.saveAndFlush(avisClient);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the avisClient
        restAvisClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, avisClient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return avisClientRepository.count();
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

    protected AvisClient getPersistedAvisClient(AvisClient avisClient) {
        return avisClientRepository.findById(avisClient.getId()).orElseThrow();
    }

    protected void assertPersistedAvisClientToMatchAllProperties(AvisClient expectedAvisClient) {
        assertAvisClientAllPropertiesEquals(expectedAvisClient, getPersistedAvisClient(expectedAvisClient));
    }

    protected void assertPersistedAvisClientToMatchUpdatableProperties(AvisClient expectedAvisClient) {
        assertAvisClientAllUpdatablePropertiesEquals(expectedAvisClient, getPersistedAvisClient(expectedAvisClient));
    }
}
