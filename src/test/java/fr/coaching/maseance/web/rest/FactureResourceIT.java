package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.FactureAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.Facture;
import fr.coaching.maseance.domain.Paiement;
import fr.coaching.maseance.domain.enumeration.TypeFacture;
import fr.coaching.maseance.repository.FactureRepository;
import fr.coaching.maseance.service.FactureService;
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
 * Integration tests for the {@link FactureResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FactureResourceIT {

    private static final TypeFacture DEFAULT_TYPE_FACTURE = TypeFacture.FactureStandard;
    private static final TypeFacture UPDATED_TYPE_FACTURE = TypeFacture.Avoir;

    private static final Instant DEFAULT_DATE_COMPTABLE_FACTURE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_COMPTABLE_FACTURE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;
    private static final Double SMALLER_MONTANT = 1D - 1D;

    private static final Double DEFAULT_TVA = 1D;
    private static final Double UPDATED_TVA = 2D;
    private static final Double SMALLER_TVA = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/factures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactureRepository factureRepository;

    @Mock
    private FactureRepository factureRepositoryMock;

    @Mock
    private FactureService factureServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactureMockMvc;

    private Facture facture;

    private Facture insertedFacture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createEntity() {
        return new Facture()
            .typeFacture(DEFAULT_TYPE_FACTURE)
            .dateComptableFacture(DEFAULT_DATE_COMPTABLE_FACTURE)
            .montant(DEFAULT_MONTANT)
            .tva(DEFAULT_TVA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createUpdatedEntity() {
        return new Facture()
            .typeFacture(UPDATED_TYPE_FACTURE)
            .dateComptableFacture(UPDATED_DATE_COMPTABLE_FACTURE)
            .montant(UPDATED_MONTANT)
            .tva(UPDATED_TVA);
    }

    @BeforeEach
    public void initTest() {
        facture = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFacture != null) {
            factureRepository.delete(insertedFacture);
            insertedFacture = null;
        }
    }

    @Test
    @Transactional
    void createFacture() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Facture
        var returnedFacture = om.readValue(
            restFactureMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facture)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Facture.class
        );

        // Validate the Facture in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFactureUpdatableFieldsEquals(returnedFacture, getPersistedFacture(returnedFacture));

        insertedFacture = returnedFacture;
    }

    @Test
    @Transactional
    void createFactureWithExistingId() throws Exception {
        // Create the Facture with an existing ID
        facture.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facture)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateComptableFactureIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setDateComptableFacture(null);

        // Create the Facture, which fails.

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facture)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setMontant(null);

        // Create the Facture, which fails.

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facture)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setTva(null);

        // Create the Facture, which fails.

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facture)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFactures() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeFacture").value(hasItem(DEFAULT_TYPE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].dateComptableFacture").value(hasItem(DEFAULT_DATE_COMPTABLE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT)))
            .andExpect(jsonPath("$.[*].tva").value(hasItem(DEFAULT_TVA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacturesWithEagerRelationshipsIsEnabled() throws Exception {
        when(factureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFactureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(factureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacturesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(factureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFactureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(factureRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFacture() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc
            .perform(get(ENTITY_API_URL_ID, facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.typeFacture").value(DEFAULT_TYPE_FACTURE.toString()))
            .andExpect(jsonPath("$.dateComptableFacture").value(DEFAULT_DATE_COMPTABLE_FACTURE.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT))
            .andExpect(jsonPath("$.tva").value(DEFAULT_TVA));
    }

    @Test
    @Transactional
    void getFacturesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        Long id = facture.getId();

        defaultFactureFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFactureFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFactureFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacturesByTypeFactureIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where typeFacture equals to
        defaultFactureFiltering("typeFacture.equals=" + DEFAULT_TYPE_FACTURE, "typeFacture.equals=" + UPDATED_TYPE_FACTURE);
    }

    @Test
    @Transactional
    void getAllFacturesByTypeFactureIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where typeFacture in
        defaultFactureFiltering(
            "typeFacture.in=" + DEFAULT_TYPE_FACTURE + "," + UPDATED_TYPE_FACTURE,
            "typeFacture.in=" + UPDATED_TYPE_FACTURE
        );
    }

    @Test
    @Transactional
    void getAllFacturesByTypeFactureIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where typeFacture is not null
        defaultFactureFiltering("typeFacture.specified=true", "typeFacture.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByDateComptableFactureIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateComptableFacture equals to
        defaultFactureFiltering(
            "dateComptableFacture.equals=" + DEFAULT_DATE_COMPTABLE_FACTURE,
            "dateComptableFacture.equals=" + UPDATED_DATE_COMPTABLE_FACTURE
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateComptableFactureIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateComptableFacture in
        defaultFactureFiltering(
            "dateComptableFacture.in=" + DEFAULT_DATE_COMPTABLE_FACTURE + "," + UPDATED_DATE_COMPTABLE_FACTURE,
            "dateComptableFacture.in=" + UPDATED_DATE_COMPTABLE_FACTURE
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateComptableFactureIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateComptableFacture is not null
        defaultFactureFiltering("dateComptableFacture.specified=true", "dateComptableFacture.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByMontantIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montant equals to
        defaultFactureFiltering("montant.equals=" + DEFAULT_MONTANT, "montant.equals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montant in
        defaultFactureFiltering("montant.in=" + DEFAULT_MONTANT + "," + UPDATED_MONTANT, "montant.in=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montant is not null
        defaultFactureFiltering("montant.specified=true", "montant.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByMontantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montant is greater than or equal to
        defaultFactureFiltering("montant.greaterThanOrEqual=" + DEFAULT_MONTANT, "montant.greaterThanOrEqual=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montant is less than or equal to
        defaultFactureFiltering("montant.lessThanOrEqual=" + DEFAULT_MONTANT, "montant.lessThanOrEqual=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montant is less than
        defaultFactureFiltering("montant.lessThan=" + UPDATED_MONTANT, "montant.lessThan=" + DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montant is greater than
        defaultFactureFiltering("montant.greaterThan=" + SMALLER_MONTANT, "montant.greaterThan=" + DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tva equals to
        defaultFactureFiltering("tva.equals=" + DEFAULT_TVA, "tva.equals=" + UPDATED_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tva in
        defaultFactureFiltering("tva.in=" + DEFAULT_TVA + "," + UPDATED_TVA, "tva.in=" + UPDATED_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tva is not null
        defaultFactureFiltering("tva.specified=true", "tva.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tva is greater than or equal to
        defaultFactureFiltering("tva.greaterThanOrEqual=" + DEFAULT_TVA, "tva.greaterThanOrEqual=" + UPDATED_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tva is less than or equal to
        defaultFactureFiltering("tva.lessThanOrEqual=" + DEFAULT_TVA, "tva.lessThanOrEqual=" + SMALLER_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tva is less than
        defaultFactureFiltering("tva.lessThan=" + UPDATED_TVA, "tva.lessThan=" + DEFAULT_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tva is greater than
        defaultFactureFiltering("tva.greaterThan=" + SMALLER_TVA, "tva.greaterThan=" + DEFAULT_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByPaiementIsEqualToSomething() throws Exception {
        Paiement paiement;
        if (TestUtil.findAll(em, Paiement.class).isEmpty()) {
            factureRepository.saveAndFlush(facture);
            paiement = PaiementResourceIT.createEntity();
        } else {
            paiement = TestUtil.findAll(em, Paiement.class).get(0);
        }
        em.persist(paiement);
        em.flush();
        facture.setPaiement(paiement);
        factureRepository.saveAndFlush(facture);
        Long paiementId = paiement.getId();
        // Get all the factureList where paiement equals to paiementId
        defaultFactureShouldBeFound("paiementId.equals=" + paiementId);

        // Get all the factureList where paiement equals to (paiementId + 1)
        defaultFactureShouldNotBeFound("paiementId.equals=" + (paiementId + 1));
    }

    private void defaultFactureFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFactureShouldBeFound(shouldBeFound);
        defaultFactureShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFactureShouldBeFound(String filter) throws Exception {
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeFacture").value(hasItem(DEFAULT_TYPE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].dateComptableFacture").value(hasItem(DEFAULT_DATE_COMPTABLE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT)))
            .andExpect(jsonPath("$.[*].tva").value(hasItem(DEFAULT_TVA)));

        // Check, that the count call also returns 1
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFactureShouldNotBeFound(String filter) throws Exception {
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacture() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture
        Facture updatedFacture = factureRepository.findById(facture.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacture are not directly saved in db
        em.detach(updatedFacture);
        updatedFacture
            .typeFacture(UPDATED_TYPE_FACTURE)
            .dateComptableFacture(UPDATED_DATE_COMPTABLE_FACTURE)
            .montant(UPDATED_MONTANT)
            .tva(UPDATED_TVA);

        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFacture))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactureToMatchAllProperties(updatedFacture);
    }

    @Test
    @Transactional
    void putNonExistingFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(put(ENTITY_API_URL_ID, facture.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facture)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactureWithPatch() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture using partial update
        Facture partialUpdatedFacture = new Facture();
        partialUpdatedFacture.setId(facture.getId());

        partialUpdatedFacture.typeFacture(UPDATED_TYPE_FACTURE).dateComptableFacture(UPDATED_DATE_COMPTABLE_FACTURE).tva(UPDATED_TVA);

        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacture))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactureUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFacture, facture), getPersistedFacture(facture));
    }

    @Test
    @Transactional
    void fullUpdateFactureWithPatch() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture using partial update
        Facture partialUpdatedFacture = new Facture();
        partialUpdatedFacture.setId(facture.getId());

        partialUpdatedFacture
            .typeFacture(UPDATED_TYPE_FACTURE)
            .dateComptableFacture(UPDATED_DATE_COMPTABLE_FACTURE)
            .montant(UPDATED_MONTANT)
            .tva(UPDATED_TVA);

        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacture))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactureUpdatableFieldsEquals(partialUpdatedFacture, getPersistedFacture(partialUpdatedFacture));
    }

    @Test
    @Transactional
    void patchNonExistingFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facture.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacture() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facture
        restFactureMockMvc
            .perform(delete(ENTITY_API_URL_ID, facture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return factureRepository.count();
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

    protected Facture getPersistedFacture(Facture facture) {
        return factureRepository.findById(facture.getId()).orElseThrow();
    }

    protected void assertPersistedFactureToMatchAllProperties(Facture expectedFacture) {
        assertFactureAllPropertiesEquals(expectedFacture, getPersistedFacture(expectedFacture));
    }

    protected void assertPersistedFactureToMatchUpdatableProperties(Facture expectedFacture) {
        assertFactureAllUpdatablePropertiesEquals(expectedFacture, getPersistedFacture(expectedFacture));
    }
}
