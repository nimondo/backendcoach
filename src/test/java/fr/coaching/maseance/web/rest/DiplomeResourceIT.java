package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.DiplomeAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.Diplome;
import fr.coaching.maseance.repository.DiplomeRepository;
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
 * Integration tests for the {@link DiplomeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DiplomeResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NB_ANNEES_ETUDE_POST_BAC = 1;
    private static final Integer UPDATED_NB_ANNEES_ETUDE_POST_BAC = 2;

    private static final String ENTITY_API_URL = "/api/diplomes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DiplomeRepository diplomeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiplomeMockMvc;

    private Diplome diplome;

    private Diplome insertedDiplome;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diplome createEntity() {
        return new Diplome().libelle(DEFAULT_LIBELLE).nbAnneesEtudePostBac(DEFAULT_NB_ANNEES_ETUDE_POST_BAC);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diplome createUpdatedEntity() {
        return new Diplome().libelle(UPDATED_LIBELLE).nbAnneesEtudePostBac(UPDATED_NB_ANNEES_ETUDE_POST_BAC);
    }

    @BeforeEach
    public void initTest() {
        diplome = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDiplome != null) {
            diplomeRepository.delete(insertedDiplome);
            insertedDiplome = null;
        }
    }

    @Test
    @Transactional
    void createDiplome() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Diplome
        var returnedDiplome = om.readValue(
            restDiplomeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diplome)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Diplome.class
        );

        // Validate the Diplome in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDiplomeUpdatableFieldsEquals(returnedDiplome, getPersistedDiplome(returnedDiplome));

        insertedDiplome = returnedDiplome;
    }

    @Test
    @Transactional
    void createDiplomeWithExistingId() throws Exception {
        // Create the Diplome with an existing ID
        diplome.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiplomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diplome)))
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        diplome.setLibelle(null);

        // Create the Diplome, which fails.

        restDiplomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diplome)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDiplomes() throws Exception {
        // Initialize the database
        insertedDiplome = diplomeRepository.saveAndFlush(diplome);

        // Get all the diplomeList
        restDiplomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diplome.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].nbAnneesEtudePostBac").value(hasItem(DEFAULT_NB_ANNEES_ETUDE_POST_BAC)));
    }

    @Test
    @Transactional
    void getDiplome() throws Exception {
        // Initialize the database
        insertedDiplome = diplomeRepository.saveAndFlush(diplome);

        // Get the diplome
        restDiplomeMockMvc
            .perform(get(ENTITY_API_URL_ID, diplome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(diplome.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.nbAnneesEtudePostBac").value(DEFAULT_NB_ANNEES_ETUDE_POST_BAC));
    }

    @Test
    @Transactional
    void getNonExistingDiplome() throws Exception {
        // Get the diplome
        restDiplomeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDiplome() throws Exception {
        // Initialize the database
        insertedDiplome = diplomeRepository.saveAndFlush(diplome);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diplome
        Diplome updatedDiplome = diplomeRepository.findById(diplome.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDiplome are not directly saved in db
        em.detach(updatedDiplome);
        updatedDiplome.libelle(UPDATED_LIBELLE).nbAnneesEtudePostBac(UPDATED_NB_ANNEES_ETUDE_POST_BAC);

        restDiplomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDiplome.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDiplome))
            )
            .andExpect(status().isOk());

        // Validate the Diplome in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDiplomeToMatchAllProperties(updatedDiplome);
    }

    @Test
    @Transactional
    void putNonExistingDiplome() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diplome.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(put(ENTITY_API_URL_ID, diplome.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diplome)))
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDiplome() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diplome.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(diplome))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDiplome() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diplome.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diplome)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diplome in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDiplomeWithPatch() throws Exception {
        // Initialize the database
        insertedDiplome = diplomeRepository.saveAndFlush(diplome);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diplome using partial update
        Diplome partialUpdatedDiplome = new Diplome();
        partialUpdatedDiplome.setId(diplome.getId());

        partialUpdatedDiplome.libelle(UPDATED_LIBELLE).nbAnneesEtudePostBac(UPDATED_NB_ANNEES_ETUDE_POST_BAC);

        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiplome.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDiplome))
            )
            .andExpect(status().isOk());

        // Validate the Diplome in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiplomeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDiplome, diplome), getPersistedDiplome(diplome));
    }

    @Test
    @Transactional
    void fullUpdateDiplomeWithPatch() throws Exception {
        // Initialize the database
        insertedDiplome = diplomeRepository.saveAndFlush(diplome);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diplome using partial update
        Diplome partialUpdatedDiplome = new Diplome();
        partialUpdatedDiplome.setId(diplome.getId());

        partialUpdatedDiplome.libelle(UPDATED_LIBELLE).nbAnneesEtudePostBac(UPDATED_NB_ANNEES_ETUDE_POST_BAC);

        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiplome.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDiplome))
            )
            .andExpect(status().isOk());

        // Validate the Diplome in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiplomeUpdatableFieldsEquals(partialUpdatedDiplome, getPersistedDiplome(partialUpdatedDiplome));
    }

    @Test
    @Transactional
    void patchNonExistingDiplome() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diplome.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, diplome.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(diplome))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDiplome() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diplome.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(diplome))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDiplome() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diplome.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(diplome)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diplome in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDiplome() throws Exception {
        // Initialize the database
        insertedDiplome = diplomeRepository.saveAndFlush(diplome);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the diplome
        restDiplomeMockMvc
            .perform(delete(ENTITY_API_URL_ID, diplome.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return diplomeRepository.count();
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

    protected Diplome getPersistedDiplome(Diplome diplome) {
        return diplomeRepository.findById(diplome.getId()).orElseThrow();
    }

    protected void assertPersistedDiplomeToMatchAllProperties(Diplome expectedDiplome) {
        assertDiplomeAllPropertiesEquals(expectedDiplome, getPersistedDiplome(expectedDiplome));
    }

    protected void assertPersistedDiplomeToMatchUpdatableProperties(Diplome expectedDiplome) {
        assertDiplomeAllUpdatablePropertiesEquals(expectedDiplome, getPersistedDiplome(expectedDiplome));
    }
}
