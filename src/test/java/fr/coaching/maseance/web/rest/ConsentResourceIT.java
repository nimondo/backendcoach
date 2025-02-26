package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.ConsentAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.Consent;
import fr.coaching.maseance.repository.ConsentRepository;
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
 * Integration tests for the {@link ConsentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsentResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_NECESSARY = false;
    private static final Boolean UPDATED_NECESSARY = true;

    private static final Boolean DEFAULT_ANALYTICS = false;
    private static final Boolean UPDATED_ANALYTICS = true;

    private static final Boolean DEFAULT_MARKETING = false;
    private static final Boolean UPDATED_MARKETING = true;

    private static final Boolean DEFAULT_PREFERENCES = false;
    private static final Boolean UPDATED_PREFERENCES = true;

    private static final String ENTITY_API_URL = "/api/consents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsentMockMvc;

    private Consent consent;

    private Consent insertedConsent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consent createEntity() {
        return new Consent()
            .email(DEFAULT_EMAIL)
            .necessary(DEFAULT_NECESSARY)
            .analytics(DEFAULT_ANALYTICS)
            .marketing(DEFAULT_MARKETING)
            .preferences(DEFAULT_PREFERENCES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consent createUpdatedEntity() {
        return new Consent()
            .email(UPDATED_EMAIL)
            .necessary(UPDATED_NECESSARY)
            .analytics(UPDATED_ANALYTICS)
            .marketing(UPDATED_MARKETING)
            .preferences(UPDATED_PREFERENCES);
    }

    @BeforeEach
    public void initTest() {
        consent = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedConsent != null) {
            consentRepository.delete(insertedConsent);
            insertedConsent = null;
        }
    }

    @Test
    @Transactional
    void createConsent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Consent
        var returnedConsent = om.readValue(
            restConsentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consent)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Consent.class
        );

        // Validate the Consent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertConsentUpdatableFieldsEquals(returnedConsent, getPersistedConsent(returnedConsent));

        insertedConsent = returnedConsent;
    }

    @Test
    @Transactional
    void createConsentWithExistingId() throws Exception {
        // Create the Consent with an existing ID
        consent.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consent)))
            .andExpect(status().isBadRequest());

        // Validate the Consent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consent.setEmail(null);

        // Create the Consent, which fails.

        restConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consent)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConsents() throws Exception {
        // Initialize the database
        insertedConsent = consentRepository.saveAndFlush(consent);

        // Get all the consentList
        restConsentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consent.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].necessary").value(hasItem(DEFAULT_NECESSARY)))
            .andExpect(jsonPath("$.[*].analytics").value(hasItem(DEFAULT_ANALYTICS)))
            .andExpect(jsonPath("$.[*].marketing").value(hasItem(DEFAULT_MARKETING)))
            .andExpect(jsonPath("$.[*].preferences").value(hasItem(DEFAULT_PREFERENCES)));
    }

    @Test
    @Transactional
    void getConsent() throws Exception {
        // Initialize the database
        insertedConsent = consentRepository.saveAndFlush(consent);

        // Get the consent
        restConsentMockMvc
            .perform(get(ENTITY_API_URL_ID, consent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consent.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.necessary").value(DEFAULT_NECESSARY))
            .andExpect(jsonPath("$.analytics").value(DEFAULT_ANALYTICS))
            .andExpect(jsonPath("$.marketing").value(DEFAULT_MARKETING))
            .andExpect(jsonPath("$.preferences").value(DEFAULT_PREFERENCES));
    }

    @Test
    @Transactional
    void getNonExistingConsent() throws Exception {
        // Get the consent
        restConsentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsent() throws Exception {
        // Initialize the database
        insertedConsent = consentRepository.saveAndFlush(consent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consent
        Consent updatedConsent = consentRepository.findById(consent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConsent are not directly saved in db
        em.detach(updatedConsent);
        updatedConsent
            .email(UPDATED_EMAIL)
            .necessary(UPDATED_NECESSARY)
            .analytics(UPDATED_ANALYTICS)
            .marketing(UPDATED_MARKETING)
            .preferences(UPDATED_PREFERENCES);

        restConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConsent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedConsent))
            )
            .andExpect(status().isOk());

        // Validate the Consent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConsentToMatchAllProperties(updatedConsent);
    }

    @Test
    @Transactional
    void putNonExistingConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consent.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsentMockMvc
            .perform(put(ENTITY_API_URL_ID, consent.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consent)))
            .andExpect(status().isBadRequest());

        // Validate the Consent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsentWithPatch() throws Exception {
        // Initialize the database
        insertedConsent = consentRepository.saveAndFlush(consent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consent using partial update
        Consent partialUpdatedConsent = new Consent();
        partialUpdatedConsent.setId(consent.getId());

        partialUpdatedConsent.necessary(UPDATED_NECESSARY).analytics(UPDATED_ANALYTICS);

        restConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsent))
            )
            .andExpect(status().isOk());

        // Validate the Consent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedConsent, consent), getPersistedConsent(consent));
    }

    @Test
    @Transactional
    void fullUpdateConsentWithPatch() throws Exception {
        // Initialize the database
        insertedConsent = consentRepository.saveAndFlush(consent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consent using partial update
        Consent partialUpdatedConsent = new Consent();
        partialUpdatedConsent.setId(consent.getId());

        partialUpdatedConsent
            .email(UPDATED_EMAIL)
            .necessary(UPDATED_NECESSARY)
            .analytics(UPDATED_ANALYTICS)
            .marketing(UPDATED_MARKETING)
            .preferences(UPDATED_PREFERENCES);

        restConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsent))
            )
            .andExpect(status().isOk());

        // Validate the Consent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsentUpdatableFieldsEquals(partialUpdatedConsent, getPersistedConsent(partialUpdatedConsent));
    }

    @Test
    @Transactional
    void patchNonExistingConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consent.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consent.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(consent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(consent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsent() throws Exception {
        // Initialize the database
        insertedConsent = consentRepository.saveAndFlush(consent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the consent
        restConsentMockMvc
            .perform(delete(ENTITY_API_URL_ID, consent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return consentRepository.count();
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

    protected Consent getPersistedConsent(Consent consent) {
        return consentRepository.findById(consent.getId()).orElseThrow();
    }

    protected void assertPersistedConsentToMatchAllProperties(Consent expectedConsent) {
        assertConsentAllPropertiesEquals(expectedConsent, getPersistedConsent(expectedConsent));
    }

    protected void assertPersistedConsentToMatchUpdatableProperties(Consent expectedConsent) {
        assertConsentAllUpdatablePropertiesEquals(expectedConsent, getPersistedConsent(expectedConsent));
    }
}
