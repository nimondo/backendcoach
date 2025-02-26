package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.OffreCoachMediaAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.OffreCoach;
import fr.coaching.maseance.domain.OffreCoachMedia;
import fr.coaching.maseance.domain.enumeration.TypeMedia;
import fr.coaching.maseance.repository.OffreCoachMediaRepository;
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
 * Integration tests for the {@link OffreCoachMediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OffreCoachMediaResourceIT {

    private static final String DEFAULT_URL_MEDIA = "AAAAAAAAAA";
    private static final String UPDATED_URL_MEDIA = "BBBBBBBBBB";

    private static final TypeMedia DEFAULT_TYPE_MEDIA = TypeMedia.Photo;
    private static final TypeMedia UPDATED_TYPE_MEDIA = TypeMedia.Video;

    private static final String ENTITY_API_URL = "/api/offre-coach-medias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OffreCoachMediaRepository offreCoachMediaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOffreCoachMediaMockMvc;

    private OffreCoachMedia offreCoachMedia;

    private OffreCoachMedia insertedOffreCoachMedia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OffreCoachMedia createEntity() {
        return new OffreCoachMedia().urlMedia(DEFAULT_URL_MEDIA).typeMedia(DEFAULT_TYPE_MEDIA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OffreCoachMedia createUpdatedEntity() {
        return new OffreCoachMedia().urlMedia(UPDATED_URL_MEDIA).typeMedia(UPDATED_TYPE_MEDIA);
    }

    @BeforeEach
    public void initTest() {
        offreCoachMedia = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOffreCoachMedia != null) {
            offreCoachMediaRepository.delete(insertedOffreCoachMedia);
            insertedOffreCoachMedia = null;
        }
    }

    @Test
    @Transactional
    void createOffreCoachMedia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OffreCoachMedia
        var returnedOffreCoachMedia = om.readValue(
            restOffreCoachMediaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreCoachMedia)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OffreCoachMedia.class
        );

        // Validate the OffreCoachMedia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOffreCoachMediaUpdatableFieldsEquals(returnedOffreCoachMedia, getPersistedOffreCoachMedia(returnedOffreCoachMedia));

        insertedOffreCoachMedia = returnedOffreCoachMedia;
    }

    @Test
    @Transactional
    void createOffreCoachMediaWithExistingId() throws Exception {
        // Create the OffreCoachMedia with an existing ID
        offreCoachMedia.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOffreCoachMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreCoachMedia)))
            .andExpect(status().isBadRequest());

        // Validate the OffreCoachMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOffreCoachMedias() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        // Get all the offreCoachMediaList
        restOffreCoachMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offreCoachMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].urlMedia").value(hasItem(DEFAULT_URL_MEDIA)))
            .andExpect(jsonPath("$.[*].typeMedia").value(hasItem(DEFAULT_TYPE_MEDIA.toString())));
    }

    @Test
    @Transactional
    void getOffreCoachMedia() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        // Get the offreCoachMedia
        restOffreCoachMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, offreCoachMedia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offreCoachMedia.getId().intValue()))
            .andExpect(jsonPath("$.urlMedia").value(DEFAULT_URL_MEDIA))
            .andExpect(jsonPath("$.typeMedia").value(DEFAULT_TYPE_MEDIA.toString()));
    }

    @Test
    @Transactional
    void getOffreCoachMediasByIdFiltering() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        Long id = offreCoachMedia.getId();

        defaultOffreCoachMediaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOffreCoachMediaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOffreCoachMediaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOffreCoachMediasByUrlMediaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        // Get all the offreCoachMediaList where urlMedia equals to
        defaultOffreCoachMediaFiltering("urlMedia.equals=" + DEFAULT_URL_MEDIA, "urlMedia.equals=" + UPDATED_URL_MEDIA);
    }

    @Test
    @Transactional
    void getAllOffreCoachMediasByUrlMediaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        // Get all the offreCoachMediaList where urlMedia in
        defaultOffreCoachMediaFiltering("urlMedia.in=" + DEFAULT_URL_MEDIA + "," + UPDATED_URL_MEDIA, "urlMedia.in=" + UPDATED_URL_MEDIA);
    }

    @Test
    @Transactional
    void getAllOffreCoachMediasByUrlMediaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        // Get all the offreCoachMediaList where urlMedia is not null
        defaultOffreCoachMediaFiltering("urlMedia.specified=true", "urlMedia.specified=false");
    }

    @Test
    @Transactional
    void getAllOffreCoachMediasByUrlMediaContainsSomething() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        // Get all the offreCoachMediaList where urlMedia contains
        defaultOffreCoachMediaFiltering("urlMedia.contains=" + DEFAULT_URL_MEDIA, "urlMedia.contains=" + UPDATED_URL_MEDIA);
    }

    @Test
    @Transactional
    void getAllOffreCoachMediasByUrlMediaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        // Get all the offreCoachMediaList where urlMedia does not contain
        defaultOffreCoachMediaFiltering("urlMedia.doesNotContain=" + UPDATED_URL_MEDIA, "urlMedia.doesNotContain=" + DEFAULT_URL_MEDIA);
    }

    @Test
    @Transactional
    void getAllOffreCoachMediasByTypeMediaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        // Get all the offreCoachMediaList where typeMedia equals to
        defaultOffreCoachMediaFiltering("typeMedia.equals=" + DEFAULT_TYPE_MEDIA, "typeMedia.equals=" + UPDATED_TYPE_MEDIA);
    }

    @Test
    @Transactional
    void getAllOffreCoachMediasByTypeMediaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        // Get all the offreCoachMediaList where typeMedia in
        defaultOffreCoachMediaFiltering(
            "typeMedia.in=" + DEFAULT_TYPE_MEDIA + "," + UPDATED_TYPE_MEDIA,
            "typeMedia.in=" + UPDATED_TYPE_MEDIA
        );
    }

    @Test
    @Transactional
    void getAllOffreCoachMediasByTypeMediaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        // Get all the offreCoachMediaList where typeMedia is not null
        defaultOffreCoachMediaFiltering("typeMedia.specified=true", "typeMedia.specified=false");
    }

    @Test
    @Transactional
    void getAllOffreCoachMediasByOffreCoachIsEqualToSomething() throws Exception {
        OffreCoach offreCoach;
        if (TestUtil.findAll(em, OffreCoach.class).isEmpty()) {
            offreCoachMediaRepository.saveAndFlush(offreCoachMedia);
            offreCoach = OffreCoachResourceIT.createEntity();
        } else {
            offreCoach = TestUtil.findAll(em, OffreCoach.class).get(0);
        }
        em.persist(offreCoach);
        em.flush();
        offreCoachMedia.setOffreCoach(offreCoach);
        offreCoachMediaRepository.saveAndFlush(offreCoachMedia);
        Long offreCoachId = offreCoach.getId();
        // Get all the offreCoachMediaList where offreCoach equals to offreCoachId
        defaultOffreCoachMediaShouldBeFound("offreCoachId.equals=" + offreCoachId);

        // Get all the offreCoachMediaList where offreCoach equals to (offreCoachId + 1)
        defaultOffreCoachMediaShouldNotBeFound("offreCoachId.equals=" + (offreCoachId + 1));
    }

    private void defaultOffreCoachMediaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOffreCoachMediaShouldBeFound(shouldBeFound);
        defaultOffreCoachMediaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOffreCoachMediaShouldBeFound(String filter) throws Exception {
        restOffreCoachMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offreCoachMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].urlMedia").value(hasItem(DEFAULT_URL_MEDIA)))
            .andExpect(jsonPath("$.[*].typeMedia").value(hasItem(DEFAULT_TYPE_MEDIA.toString())));

        // Check, that the count call also returns 1
        restOffreCoachMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOffreCoachMediaShouldNotBeFound(String filter) throws Exception {
        restOffreCoachMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOffreCoachMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOffreCoachMedia() throws Exception {
        // Get the offreCoachMedia
        restOffreCoachMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOffreCoachMedia() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offreCoachMedia
        OffreCoachMedia updatedOffreCoachMedia = offreCoachMediaRepository.findById(offreCoachMedia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOffreCoachMedia are not directly saved in db
        em.detach(updatedOffreCoachMedia);
        updatedOffreCoachMedia.urlMedia(UPDATED_URL_MEDIA).typeMedia(UPDATED_TYPE_MEDIA);

        restOffreCoachMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOffreCoachMedia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOffreCoachMedia))
            )
            .andExpect(status().isOk());

        // Validate the OffreCoachMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOffreCoachMediaToMatchAllProperties(updatedOffreCoachMedia);
    }

    @Test
    @Transactional
    void putNonExistingOffreCoachMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoachMedia.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreCoachMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offreCoachMedia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(offreCoachMedia))
            )
            .andExpect(status().isBadRequest());

        // Validate the OffreCoachMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOffreCoachMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoachMedia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreCoachMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(offreCoachMedia))
            )
            .andExpect(status().isBadRequest());

        // Validate the OffreCoachMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOffreCoachMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoachMedia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreCoachMediaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreCoachMedia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OffreCoachMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOffreCoachMediaWithPatch() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offreCoachMedia using partial update
        OffreCoachMedia partialUpdatedOffreCoachMedia = new OffreCoachMedia();
        partialUpdatedOffreCoachMedia.setId(offreCoachMedia.getId());

        restOffreCoachMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffreCoachMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOffreCoachMedia))
            )
            .andExpect(status().isOk());

        // Validate the OffreCoachMedia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOffreCoachMediaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOffreCoachMedia, offreCoachMedia),
            getPersistedOffreCoachMedia(offreCoachMedia)
        );
    }

    @Test
    @Transactional
    void fullUpdateOffreCoachMediaWithPatch() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offreCoachMedia using partial update
        OffreCoachMedia partialUpdatedOffreCoachMedia = new OffreCoachMedia();
        partialUpdatedOffreCoachMedia.setId(offreCoachMedia.getId());

        partialUpdatedOffreCoachMedia.urlMedia(UPDATED_URL_MEDIA).typeMedia(UPDATED_TYPE_MEDIA);

        restOffreCoachMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffreCoachMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOffreCoachMedia))
            )
            .andExpect(status().isOk());

        // Validate the OffreCoachMedia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOffreCoachMediaUpdatableFieldsEquals(
            partialUpdatedOffreCoachMedia,
            getPersistedOffreCoachMedia(partialUpdatedOffreCoachMedia)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOffreCoachMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoachMedia.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreCoachMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offreCoachMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(offreCoachMedia))
            )
            .andExpect(status().isBadRequest());

        // Validate the OffreCoachMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOffreCoachMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoachMedia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreCoachMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(offreCoachMedia))
            )
            .andExpect(status().isBadRequest());

        // Validate the OffreCoachMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOffreCoachMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offreCoachMedia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreCoachMediaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(offreCoachMedia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OffreCoachMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOffreCoachMedia() throws Exception {
        // Initialize the database
        insertedOffreCoachMedia = offreCoachMediaRepository.saveAndFlush(offreCoachMedia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the offreCoachMedia
        restOffreCoachMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, offreCoachMedia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return offreCoachMediaRepository.count();
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

    protected OffreCoachMedia getPersistedOffreCoachMedia(OffreCoachMedia offreCoachMedia) {
        return offreCoachMediaRepository.findById(offreCoachMedia.getId()).orElseThrow();
    }

    protected void assertPersistedOffreCoachMediaToMatchAllProperties(OffreCoachMedia expectedOffreCoachMedia) {
        assertOffreCoachMediaAllPropertiesEquals(expectedOffreCoachMedia, getPersistedOffreCoachMedia(expectedOffreCoachMedia));
    }

    protected void assertPersistedOffreCoachMediaToMatchUpdatableProperties(OffreCoachMedia expectedOffreCoachMedia) {
        assertOffreCoachMediaAllUpdatablePropertiesEquals(expectedOffreCoachMedia, getPersistedOffreCoachMedia(expectedOffreCoachMedia));
    }
}
