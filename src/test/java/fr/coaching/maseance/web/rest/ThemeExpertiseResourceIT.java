package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.ThemeExpertiseAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.ThemeExpertise;
import fr.coaching.maseance.repository.ThemeExpertiseRepository;
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
 * Integration tests for the {@link ThemeExpertiseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ThemeExpertiseResourceIT {

    private static final String DEFAULT_LIBELLE_EXPERTISE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EXPERTISE = "BBBBBBBBBB";

    private static final String DEFAULT_URL_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_URL_PHOTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/theme-expertises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ThemeExpertiseRepository themeExpertiseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restThemeExpertiseMockMvc;

    private ThemeExpertise themeExpertise;

    private ThemeExpertise insertedThemeExpertise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThemeExpertise createEntity() {
        return new ThemeExpertise().libelleExpertise(DEFAULT_LIBELLE_EXPERTISE).urlPhoto(DEFAULT_URL_PHOTO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThemeExpertise createUpdatedEntity() {
        return new ThemeExpertise().libelleExpertise(UPDATED_LIBELLE_EXPERTISE).urlPhoto(UPDATED_URL_PHOTO);
    }

    @BeforeEach
    public void initTest() {
        themeExpertise = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedThemeExpertise != null) {
            themeExpertiseRepository.delete(insertedThemeExpertise);
            insertedThemeExpertise = null;
        }
    }

    @Test
    @Transactional
    void createThemeExpertise() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ThemeExpertise
        var returnedThemeExpertise = om.readValue(
            restThemeExpertiseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(themeExpertise)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ThemeExpertise.class
        );

        // Validate the ThemeExpertise in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertThemeExpertiseUpdatableFieldsEquals(returnedThemeExpertise, getPersistedThemeExpertise(returnedThemeExpertise));

        insertedThemeExpertise = returnedThemeExpertise;
    }

    @Test
    @Transactional
    void createThemeExpertiseWithExistingId() throws Exception {
        // Create the ThemeExpertise with an existing ID
        themeExpertise.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restThemeExpertiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(themeExpertise)))
            .andExpect(status().isBadRequest());

        // Validate the ThemeExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleExpertiseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        themeExpertise.setLibelleExpertise(null);

        // Create the ThemeExpertise, which fails.

        restThemeExpertiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(themeExpertise)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlPhotoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        themeExpertise.setUrlPhoto(null);

        // Create the ThemeExpertise, which fails.

        restThemeExpertiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(themeExpertise)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllThemeExpertises() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get all the themeExpertiseList
        restThemeExpertiseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(themeExpertise.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleExpertise").value(hasItem(DEFAULT_LIBELLE_EXPERTISE)))
            .andExpect(jsonPath("$.[*].urlPhoto").value(hasItem(DEFAULT_URL_PHOTO)));
    }

    @Test
    @Transactional
    void getThemeExpertise() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get the themeExpertise
        restThemeExpertiseMockMvc
            .perform(get(ENTITY_API_URL_ID, themeExpertise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(themeExpertise.getId().intValue()))
            .andExpect(jsonPath("$.libelleExpertise").value(DEFAULT_LIBELLE_EXPERTISE))
            .andExpect(jsonPath("$.urlPhoto").value(DEFAULT_URL_PHOTO));
    }

    @Test
    @Transactional
    void getThemeExpertisesByIdFiltering() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        Long id = themeExpertise.getId();

        defaultThemeExpertiseFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultThemeExpertiseFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultThemeExpertiseFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllThemeExpertisesByLibelleExpertiseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get all the themeExpertiseList where libelleExpertise equals to
        defaultThemeExpertiseFiltering(
            "libelleExpertise.equals=" + DEFAULT_LIBELLE_EXPERTISE,
            "libelleExpertise.equals=" + UPDATED_LIBELLE_EXPERTISE
        );
    }

    @Test
    @Transactional
    void getAllThemeExpertisesByLibelleExpertiseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get all the themeExpertiseList where libelleExpertise in
        defaultThemeExpertiseFiltering(
            "libelleExpertise.in=" + DEFAULT_LIBELLE_EXPERTISE + "," + UPDATED_LIBELLE_EXPERTISE,
            "libelleExpertise.in=" + UPDATED_LIBELLE_EXPERTISE
        );
    }

    @Test
    @Transactional
    void getAllThemeExpertisesByLibelleExpertiseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get all the themeExpertiseList where libelleExpertise is not null
        defaultThemeExpertiseFiltering("libelleExpertise.specified=true", "libelleExpertise.specified=false");
    }

    @Test
    @Transactional
    void getAllThemeExpertisesByLibelleExpertiseContainsSomething() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get all the themeExpertiseList where libelleExpertise contains
        defaultThemeExpertiseFiltering(
            "libelleExpertise.contains=" + DEFAULT_LIBELLE_EXPERTISE,
            "libelleExpertise.contains=" + UPDATED_LIBELLE_EXPERTISE
        );
    }

    @Test
    @Transactional
    void getAllThemeExpertisesByLibelleExpertiseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get all the themeExpertiseList where libelleExpertise does not contain
        defaultThemeExpertiseFiltering(
            "libelleExpertise.doesNotContain=" + UPDATED_LIBELLE_EXPERTISE,
            "libelleExpertise.doesNotContain=" + DEFAULT_LIBELLE_EXPERTISE
        );
    }

    @Test
    @Transactional
    void getAllThemeExpertisesByUrlPhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get all the themeExpertiseList where urlPhoto equals to
        defaultThemeExpertiseFiltering("urlPhoto.equals=" + DEFAULT_URL_PHOTO, "urlPhoto.equals=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    void getAllThemeExpertisesByUrlPhotoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get all the themeExpertiseList where urlPhoto in
        defaultThemeExpertiseFiltering("urlPhoto.in=" + DEFAULT_URL_PHOTO + "," + UPDATED_URL_PHOTO, "urlPhoto.in=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    void getAllThemeExpertisesByUrlPhotoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get all the themeExpertiseList where urlPhoto is not null
        defaultThemeExpertiseFiltering("urlPhoto.specified=true", "urlPhoto.specified=false");
    }

    @Test
    @Transactional
    void getAllThemeExpertisesByUrlPhotoContainsSomething() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get all the themeExpertiseList where urlPhoto contains
        defaultThemeExpertiseFiltering("urlPhoto.contains=" + DEFAULT_URL_PHOTO, "urlPhoto.contains=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    void getAllThemeExpertisesByUrlPhotoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        // Get all the themeExpertiseList where urlPhoto does not contain
        defaultThemeExpertiseFiltering("urlPhoto.doesNotContain=" + UPDATED_URL_PHOTO, "urlPhoto.doesNotContain=" + DEFAULT_URL_PHOTO);
    }

    private void defaultThemeExpertiseFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultThemeExpertiseShouldBeFound(shouldBeFound);
        defaultThemeExpertiseShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultThemeExpertiseShouldBeFound(String filter) throws Exception {
        restThemeExpertiseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(themeExpertise.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleExpertise").value(hasItem(DEFAULT_LIBELLE_EXPERTISE)))
            .andExpect(jsonPath("$.[*].urlPhoto").value(hasItem(DEFAULT_URL_PHOTO)));

        // Check, that the count call also returns 1
        restThemeExpertiseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultThemeExpertiseShouldNotBeFound(String filter) throws Exception {
        restThemeExpertiseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restThemeExpertiseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingThemeExpertise() throws Exception {
        // Get the themeExpertise
        restThemeExpertiseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingThemeExpertise() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the themeExpertise
        ThemeExpertise updatedThemeExpertise = themeExpertiseRepository.findById(themeExpertise.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedThemeExpertise are not directly saved in db
        em.detach(updatedThemeExpertise);
        updatedThemeExpertise.libelleExpertise(UPDATED_LIBELLE_EXPERTISE).urlPhoto(UPDATED_URL_PHOTO);

        restThemeExpertiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedThemeExpertise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedThemeExpertise))
            )
            .andExpect(status().isOk());

        // Validate the ThemeExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedThemeExpertiseToMatchAllProperties(updatedThemeExpertise);
    }

    @Test
    @Transactional
    void putNonExistingThemeExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        themeExpertise.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeExpertiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, themeExpertise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(themeExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchThemeExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        themeExpertise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeExpertiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(themeExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamThemeExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        themeExpertise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeExpertiseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(themeExpertise)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThemeExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateThemeExpertiseWithPatch() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the themeExpertise using partial update
        ThemeExpertise partialUpdatedThemeExpertise = new ThemeExpertise();
        partialUpdatedThemeExpertise.setId(themeExpertise.getId());

        partialUpdatedThemeExpertise.libelleExpertise(UPDATED_LIBELLE_EXPERTISE).urlPhoto(UPDATED_URL_PHOTO);

        restThemeExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThemeExpertise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedThemeExpertise))
            )
            .andExpect(status().isOk());

        // Validate the ThemeExpertise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertThemeExpertiseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedThemeExpertise, themeExpertise),
            getPersistedThemeExpertise(themeExpertise)
        );
    }

    @Test
    @Transactional
    void fullUpdateThemeExpertiseWithPatch() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the themeExpertise using partial update
        ThemeExpertise partialUpdatedThemeExpertise = new ThemeExpertise();
        partialUpdatedThemeExpertise.setId(themeExpertise.getId());

        partialUpdatedThemeExpertise.libelleExpertise(UPDATED_LIBELLE_EXPERTISE).urlPhoto(UPDATED_URL_PHOTO);

        restThemeExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThemeExpertise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedThemeExpertise))
            )
            .andExpect(status().isOk());

        // Validate the ThemeExpertise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertThemeExpertiseUpdatableFieldsEquals(partialUpdatedThemeExpertise, getPersistedThemeExpertise(partialUpdatedThemeExpertise));
    }

    @Test
    @Transactional
    void patchNonExistingThemeExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        themeExpertise.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, themeExpertise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(themeExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchThemeExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        themeExpertise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(themeExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamThemeExpertise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        themeExpertise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeExpertiseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(themeExpertise)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThemeExpertise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteThemeExpertise() throws Exception {
        // Initialize the database
        insertedThemeExpertise = themeExpertiseRepository.saveAndFlush(themeExpertise);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the themeExpertise
        restThemeExpertiseMockMvc
            .perform(delete(ENTITY_API_URL_ID, themeExpertise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return themeExpertiseRepository.count();
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

    protected ThemeExpertise getPersistedThemeExpertise(ThemeExpertise themeExpertise) {
        return themeExpertiseRepository.findById(themeExpertise.getId()).orElseThrow();
    }

    protected void assertPersistedThemeExpertiseToMatchAllProperties(ThemeExpertise expectedThemeExpertise) {
        assertThemeExpertiseAllPropertiesEquals(expectedThemeExpertise, getPersistedThemeExpertise(expectedThemeExpertise));
    }

    protected void assertPersistedThemeExpertiseToMatchUpdatableProperties(ThemeExpertise expectedThemeExpertise) {
        assertThemeExpertiseAllUpdatablePropertiesEquals(expectedThemeExpertise, getPersistedThemeExpertise(expectedThemeExpertise));
    }
}
