package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.PaiementAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.Paiement;
import fr.coaching.maseance.domain.enumeration.StatutPaiement;
import fr.coaching.maseance.repository.PaiementRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PaiementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaiementResourceIT {

    private static final Instant DEFAULT_HORODATAGE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORODATAGE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MOYEN_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_MOYEN_PAIEMENT = "BBBBBBBBBB";

    private static final StatutPaiement DEFAULT_STATUT_PAIEMENT = StatutPaiement.PaiementAccepte;
    private static final StatutPaiement UPDATED_STATUT_PAIEMENT = StatutPaiement.PaiementRefuse;

    private static final String DEFAULT_ID_STRIPE = "AAAAAAAAAA";
    private static final String UPDATED_ID_STRIPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/paiements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementMockMvc;

    private Paiement paiement;

    private Paiement insertedPaiement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createEntity() {
        return new Paiement()
            .horodatage(DEFAULT_HORODATAGE)
            .moyenPaiement(DEFAULT_MOYEN_PAIEMENT)
            .statutPaiement(DEFAULT_STATUT_PAIEMENT)
            .idStripe(DEFAULT_ID_STRIPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createUpdatedEntity() {
        return new Paiement()
            .horodatage(UPDATED_HORODATAGE)
            .moyenPaiement(UPDATED_MOYEN_PAIEMENT)
            .statutPaiement(UPDATED_STATUT_PAIEMENT)
            .idStripe(UPDATED_ID_STRIPE);
    }

    @BeforeEach
    public void initTest() {
        paiement = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaiement != null) {
            paiementRepository.delete(insertedPaiement);
            insertedPaiement = null;
        }
    }

    @Test
    @Transactional
    void createPaiement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Paiement
        var returnedPaiement = om.readValue(
            restPaiementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paiement)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Paiement.class
        );

        // Validate the Paiement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaiementUpdatableFieldsEquals(returnedPaiement, getPersistedPaiement(returnedPaiement));

        insertedPaiement = returnedPaiement;
    }

    @Test
    @Transactional
    void createPaiementWithExistingId() throws Exception {
        // Create the Paiement with an existing ID
        paiement.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paiement)))
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHorodatageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paiement.setHorodatage(null);

        // Create the Paiement, which fails.

        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paiement)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaiements() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].horodatage").value(hasItem(DEFAULT_HORODATAGE.toString())))
            .andExpect(jsonPath("$.[*].moyenPaiement").value(hasItem(DEFAULT_MOYEN_PAIEMENT)))
            .andExpect(jsonPath("$.[*].statutPaiement").value(hasItem(DEFAULT_STATUT_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].idStripe").value(hasItem(DEFAULT_ID_STRIPE)));
    }

    @Test
    @Transactional
    void getPaiement() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get the paiement
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL_ID, paiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiement.getId().intValue()))
            .andExpect(jsonPath("$.horodatage").value(DEFAULT_HORODATAGE.toString()))
            .andExpect(jsonPath("$.moyenPaiement").value(DEFAULT_MOYEN_PAIEMENT))
            .andExpect(jsonPath("$.statutPaiement").value(DEFAULT_STATUT_PAIEMENT.toString()))
            .andExpect(jsonPath("$.idStripe").value(DEFAULT_ID_STRIPE));
    }

    @Test
    @Transactional
    void getPaiementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        Long id = paiement.getId();

        defaultPaiementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaiementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaiementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaiementsByHorodatageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where horodatage equals to
        defaultPaiementFiltering("horodatage.equals=" + DEFAULT_HORODATAGE, "horodatage.equals=" + UPDATED_HORODATAGE);
    }

    @Test
    @Transactional
    void getAllPaiementsByHorodatageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where horodatage in
        defaultPaiementFiltering("horodatage.in=" + DEFAULT_HORODATAGE + "," + UPDATED_HORODATAGE, "horodatage.in=" + UPDATED_HORODATAGE);
    }

    @Test
    @Transactional
    void getAllPaiementsByHorodatageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where horodatage is not null
        defaultPaiementFiltering("horodatage.specified=true", "horodatage.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByMoyenPaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where moyenPaiement equals to
        defaultPaiementFiltering("moyenPaiement.equals=" + DEFAULT_MOYEN_PAIEMENT, "moyenPaiement.equals=" + UPDATED_MOYEN_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByMoyenPaiementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where moyenPaiement in
        defaultPaiementFiltering(
            "moyenPaiement.in=" + DEFAULT_MOYEN_PAIEMENT + "," + UPDATED_MOYEN_PAIEMENT,
            "moyenPaiement.in=" + UPDATED_MOYEN_PAIEMENT
        );
    }

    @Test
    @Transactional
    void getAllPaiementsByMoyenPaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where moyenPaiement is not null
        defaultPaiementFiltering("moyenPaiement.specified=true", "moyenPaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByMoyenPaiementContainsSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where moyenPaiement contains
        defaultPaiementFiltering("moyenPaiement.contains=" + DEFAULT_MOYEN_PAIEMENT, "moyenPaiement.contains=" + UPDATED_MOYEN_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByMoyenPaiementNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where moyenPaiement does not contain
        defaultPaiementFiltering(
            "moyenPaiement.doesNotContain=" + UPDATED_MOYEN_PAIEMENT,
            "moyenPaiement.doesNotContain=" + DEFAULT_MOYEN_PAIEMENT
        );
    }

    @Test
    @Transactional
    void getAllPaiementsByStatutPaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where statutPaiement equals to
        defaultPaiementFiltering("statutPaiement.equals=" + DEFAULT_STATUT_PAIEMENT, "statutPaiement.equals=" + UPDATED_STATUT_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByStatutPaiementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where statutPaiement in
        defaultPaiementFiltering(
            "statutPaiement.in=" + DEFAULT_STATUT_PAIEMENT + "," + UPDATED_STATUT_PAIEMENT,
            "statutPaiement.in=" + UPDATED_STATUT_PAIEMENT
        );
    }

    @Test
    @Transactional
    void getAllPaiementsByStatutPaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where statutPaiement is not null
        defaultPaiementFiltering("statutPaiement.specified=true", "statutPaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByIdStripeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where idStripe equals to
        defaultPaiementFiltering("idStripe.equals=" + DEFAULT_ID_STRIPE, "idStripe.equals=" + UPDATED_ID_STRIPE);
    }

    @Test
    @Transactional
    void getAllPaiementsByIdStripeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where idStripe in
        defaultPaiementFiltering("idStripe.in=" + DEFAULT_ID_STRIPE + "," + UPDATED_ID_STRIPE, "idStripe.in=" + UPDATED_ID_STRIPE);
    }

    @Test
    @Transactional
    void getAllPaiementsByIdStripeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where idStripe is not null
        defaultPaiementFiltering("idStripe.specified=true", "idStripe.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByIdStripeContainsSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where idStripe contains
        defaultPaiementFiltering("idStripe.contains=" + DEFAULT_ID_STRIPE, "idStripe.contains=" + UPDATED_ID_STRIPE);
    }

    @Test
    @Transactional
    void getAllPaiementsByIdStripeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where idStripe does not contain
        defaultPaiementFiltering("idStripe.doesNotContain=" + UPDATED_ID_STRIPE, "idStripe.doesNotContain=" + DEFAULT_ID_STRIPE);
    }

    private void defaultPaiementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaiementShouldBeFound(shouldBeFound);
        defaultPaiementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaiementShouldBeFound(String filter) throws Exception {
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].horodatage").value(hasItem(DEFAULT_HORODATAGE.toString())))
            .andExpect(jsonPath("$.[*].moyenPaiement").value(hasItem(DEFAULT_MOYEN_PAIEMENT)))
            .andExpect(jsonPath("$.[*].statutPaiement").value(hasItem(DEFAULT_STATUT_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].idStripe").value(hasItem(DEFAULT_ID_STRIPE)));

        // Check, that the count call also returns 1
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaiementShouldNotBeFound(String filter) throws Exception {
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaiement() throws Exception {
        // Get the paiement
        restPaiementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaiement() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paiement
        Paiement updatedPaiement = paiementRepository.findById(paiement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaiement are not directly saved in db
        em.detach(updatedPaiement);
        updatedPaiement
            .horodatage(UPDATED_HORODATAGE)
            .moyenPaiement(UPDATED_MOYEN_PAIEMENT)
            .statutPaiement(UPDATED_STATUT_PAIEMENT)
            .idStripe(UPDATED_ID_STRIPE);

        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaiement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaiementToMatchAllProperties(updatedPaiement);
    }

    @Test
    @Transactional
    void putNonExistingPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiement.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paiement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paiement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paiement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        partialUpdatedPaiement.moyenPaiement(UPDATED_MOYEN_PAIEMENT).statutPaiement(UPDATED_STATUT_PAIEMENT);

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaiementUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPaiement, paiement), getPersistedPaiement(paiement));
    }

    @Test
    @Transactional
    void fullUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        partialUpdatedPaiement
            .horodatage(UPDATED_HORODATAGE)
            .moyenPaiement(UPDATED_MOYEN_PAIEMENT)
            .statutPaiement(UPDATED_STATUT_PAIEMENT)
            .idStripe(UPDATED_ID_STRIPE);

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaiementUpdatableFieldsEquals(partialUpdatedPaiement, getPersistedPaiement(partialUpdatedPaiement));
    }

    @Test
    @Transactional
    void patchNonExistingPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paiement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paiement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paiement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaiement() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paiement
        restPaiementMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paiementRepository.count();
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

    protected Paiement getPersistedPaiement(Paiement paiement) {
        return paiementRepository.findById(paiement.getId()).orElseThrow();
    }

    protected void assertPersistedPaiementToMatchAllProperties(Paiement expectedPaiement) {
        assertPaiementAllPropertiesEquals(expectedPaiement, getPersistedPaiement(expectedPaiement));
    }

    protected void assertPersistedPaiementToMatchUpdatableProperties(Paiement expectedPaiement) {
        assertPaiementAllUpdatablePropertiesEquals(expectedPaiement, getPersistedPaiement(expectedPaiement));
    }
}
