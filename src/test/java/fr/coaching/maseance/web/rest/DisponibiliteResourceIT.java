package fr.coaching.maseance.web.rest;

import static fr.coaching.maseance.domain.DisponibiliteAsserts.*;
import static fr.coaching.maseance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.coaching.maseance.IntegrationTest;
import fr.coaching.maseance.domain.CoachExpert;
import fr.coaching.maseance.domain.Disponibilite;
import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.repository.DisponibiliteRepository;
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
 * Integration tests for the {@link DisponibiliteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisponibiliteResourceIT {

    private static final Instant DEFAULT_HEURE_DEBUT_CRENEAUX_DISPONIBILITE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HEURE_DEBUT_CRENEAUX_DISPONIBILITE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_HEURE_FIN_CRENEAUX_DISPONIBILITE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HEURE_FIN_CRENEAUX_DISPONIBILITE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final CanalSeance DEFAULT_CANAL_SEANCE = CanalSeance.AdressePhysique;
    private static final CanalSeance UPDATED_CANAL_SEANCE = CanalSeance.AppelVisio;

    private static final String ENTITY_API_URL = "/api/disponibilites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisponibiliteMockMvc;

    private Disponibilite disponibilite;

    private Disponibilite insertedDisponibilite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disponibilite createEntity() {
        return new Disponibilite()
            .heureDebutCreneauxDisponibilite(DEFAULT_HEURE_DEBUT_CRENEAUX_DISPONIBILITE)
            .heureFinCreneauxDisponibilite(DEFAULT_HEURE_FIN_CRENEAUX_DISPONIBILITE)
            .canalSeance(DEFAULT_CANAL_SEANCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disponibilite createUpdatedEntity() {
        return new Disponibilite()
            .heureDebutCreneauxDisponibilite(UPDATED_HEURE_DEBUT_CRENEAUX_DISPONIBILITE)
            .heureFinCreneauxDisponibilite(UPDATED_HEURE_FIN_CRENEAUX_DISPONIBILITE)
            .canalSeance(UPDATED_CANAL_SEANCE);
    }

    @BeforeEach
    public void initTest() {
        disponibilite = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDisponibilite != null) {
            disponibiliteRepository.delete(insertedDisponibilite);
            insertedDisponibilite = null;
        }
    }

    @Test
    @Transactional
    void createDisponibilite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Disponibilite
        var returnedDisponibilite = om.readValue(
            restDisponibiliteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibilite)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Disponibilite.class
        );

        // Validate the Disponibilite in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDisponibiliteUpdatableFieldsEquals(returnedDisponibilite, getPersistedDisponibilite(returnedDisponibilite));

        insertedDisponibilite = returnedDisponibilite;
    }

    @Test
    @Transactional
    void createDisponibiliteWithExistingId() throws Exception {
        // Create the Disponibilite with an existing ID
        disponibilite.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisponibiliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibilite)))
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHeureDebutCreneauxDisponibiliteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disponibilite.setHeureDebutCreneauxDisponibilite(null);

        // Create the Disponibilite, which fails.

        restDisponibiliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibilite)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHeureFinCreneauxDisponibiliteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disponibilite.setHeureFinCreneauxDisponibilite(null);

        // Create the Disponibilite, which fails.

        restDisponibiliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibilite)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDisponibilites() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilite.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].heureDebutCreneauxDisponibilite").value(hasItem(DEFAULT_HEURE_DEBUT_CRENEAUX_DISPONIBILITE.toString()))
            )
            .andExpect(jsonPath("$.[*].heureFinCreneauxDisponibilite").value(hasItem(DEFAULT_HEURE_FIN_CRENEAUX_DISPONIBILITE.toString())))
            .andExpect(jsonPath("$.[*].canalSeance").value(hasItem(DEFAULT_CANAL_SEANCE.toString())));
    }

    @Test
    @Transactional
    void getDisponibilite() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get the disponibilite
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL_ID, disponibilite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disponibilite.getId().intValue()))
            .andExpect(jsonPath("$.heureDebutCreneauxDisponibilite").value(DEFAULT_HEURE_DEBUT_CRENEAUX_DISPONIBILITE.toString()))
            .andExpect(jsonPath("$.heureFinCreneauxDisponibilite").value(DEFAULT_HEURE_FIN_CRENEAUX_DISPONIBILITE.toString()))
            .andExpect(jsonPath("$.canalSeance").value(DEFAULT_CANAL_SEANCE.toString()));
    }

    @Test
    @Transactional
    void getDisponibilitesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        Long id = disponibilite.getId();

        defaultDisponibiliteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDisponibiliteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDisponibiliteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByHeureDebutCreneauxDisponibiliteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where heureDebutCreneauxDisponibilite equals to
        defaultDisponibiliteFiltering(
            "heureDebutCreneauxDisponibilite.equals=" + DEFAULT_HEURE_DEBUT_CRENEAUX_DISPONIBILITE,
            "heureDebutCreneauxDisponibilite.equals=" + UPDATED_HEURE_DEBUT_CRENEAUX_DISPONIBILITE
        );
    }

    @Test
    @Transactional
    void getAllDisponibilitesByHeureDebutCreneauxDisponibiliteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where heureDebutCreneauxDisponibilite in
        defaultDisponibiliteFiltering(
            "heureDebutCreneauxDisponibilite.in=" +
            DEFAULT_HEURE_DEBUT_CRENEAUX_DISPONIBILITE +
            "," +
            UPDATED_HEURE_DEBUT_CRENEAUX_DISPONIBILITE,
            "heureDebutCreneauxDisponibilite.in=" + UPDATED_HEURE_DEBUT_CRENEAUX_DISPONIBILITE
        );
    }

    @Test
    @Transactional
    void getAllDisponibilitesByHeureDebutCreneauxDisponibiliteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where heureDebutCreneauxDisponibilite is not null
        defaultDisponibiliteFiltering("heureDebutCreneauxDisponibilite.specified=true", "heureDebutCreneauxDisponibilite.specified=false");
    }

    @Test
    @Transactional
    void getAllDisponibilitesByHeureFinCreneauxDisponibiliteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where heureFinCreneauxDisponibilite equals to
        defaultDisponibiliteFiltering(
            "heureFinCreneauxDisponibilite.equals=" + DEFAULT_HEURE_FIN_CRENEAUX_DISPONIBILITE,
            "heureFinCreneauxDisponibilite.equals=" + UPDATED_HEURE_FIN_CRENEAUX_DISPONIBILITE
        );
    }

    @Test
    @Transactional
    void getAllDisponibilitesByHeureFinCreneauxDisponibiliteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where heureFinCreneauxDisponibilite in
        defaultDisponibiliteFiltering(
            "heureFinCreneauxDisponibilite.in=" + DEFAULT_HEURE_FIN_CRENEAUX_DISPONIBILITE + "," + UPDATED_HEURE_FIN_CRENEAUX_DISPONIBILITE,
            "heureFinCreneauxDisponibilite.in=" + UPDATED_HEURE_FIN_CRENEAUX_DISPONIBILITE
        );
    }

    @Test
    @Transactional
    void getAllDisponibilitesByHeureFinCreneauxDisponibiliteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where heureFinCreneauxDisponibilite is not null
        defaultDisponibiliteFiltering("heureFinCreneauxDisponibilite.specified=true", "heureFinCreneauxDisponibilite.specified=false");
    }

    @Test
    @Transactional
    void getAllDisponibilitesByCanalSeanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where canalSeance equals to
        defaultDisponibiliteFiltering("canalSeance.equals=" + DEFAULT_CANAL_SEANCE, "canalSeance.equals=" + UPDATED_CANAL_SEANCE);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByCanalSeanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where canalSeance in
        defaultDisponibiliteFiltering(
            "canalSeance.in=" + DEFAULT_CANAL_SEANCE + "," + UPDATED_CANAL_SEANCE,
            "canalSeance.in=" + UPDATED_CANAL_SEANCE
        );
    }

    @Test
    @Transactional
    void getAllDisponibilitesByCanalSeanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where canalSeance is not null
        defaultDisponibiliteFiltering("canalSeance.specified=true", "canalSeance.specified=false");
    }

    @Test
    @Transactional
    void getAllDisponibilitesByCoachExpertIsEqualToSomething() throws Exception {
        CoachExpert coachExpert;
        if (TestUtil.findAll(em, CoachExpert.class).isEmpty()) {
            disponibiliteRepository.saveAndFlush(disponibilite);
            coachExpert = CoachExpertResourceIT.createEntity();
        } else {
            coachExpert = TestUtil.findAll(em, CoachExpert.class).get(0);
        }
        em.persist(coachExpert);
        em.flush();
        disponibilite.setCoachExpert(coachExpert);
        disponibiliteRepository.saveAndFlush(disponibilite);
        Long coachExpertId = coachExpert.getId();
        // Get all the disponibiliteList where coachExpert equals to coachExpertId
        defaultDisponibiliteShouldBeFound("coachExpertId.equals=" + coachExpertId);

        // Get all the disponibiliteList where coachExpert equals to (coachExpertId + 1)
        defaultDisponibiliteShouldNotBeFound("coachExpertId.equals=" + (coachExpertId + 1));
    }

    private void defaultDisponibiliteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDisponibiliteShouldBeFound(shouldBeFound);
        defaultDisponibiliteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDisponibiliteShouldBeFound(String filter) throws Exception {
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilite.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].heureDebutCreneauxDisponibilite").value(hasItem(DEFAULT_HEURE_DEBUT_CRENEAUX_DISPONIBILITE.toString()))
            )
            .andExpect(jsonPath("$.[*].heureFinCreneauxDisponibilite").value(hasItem(DEFAULT_HEURE_FIN_CRENEAUX_DISPONIBILITE.toString())))
            .andExpect(jsonPath("$.[*].canalSeance").value(hasItem(DEFAULT_CANAL_SEANCE.toString())));

        // Check, that the count call also returns 1
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDisponibiliteShouldNotBeFound(String filter) throws Exception {
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDisponibilite() throws Exception {
        // Get the disponibilite
        restDisponibiliteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDisponibilite() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disponibilite
        Disponibilite updatedDisponibilite = disponibiliteRepository.findById(disponibilite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDisponibilite are not directly saved in db
        em.detach(updatedDisponibilite);
        updatedDisponibilite
            .heureDebutCreneauxDisponibilite(UPDATED_HEURE_DEBUT_CRENEAUX_DISPONIBILITE)
            .heureFinCreneauxDisponibilite(UPDATED_HEURE_FIN_CRENEAUX_DISPONIBILITE)
            .canalSeance(UPDATED_CANAL_SEANCE);

        restDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDisponibilite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDisponibilite))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDisponibiliteToMatchAllProperties(updatedDisponibilite);
    }

    @Test
    @Transactional
    void putNonExistingDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disponibilite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibilite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisponibiliteWithPatch() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disponibilite using partial update
        Disponibilite partialUpdatedDisponibilite = new Disponibilite();
        partialUpdatedDisponibilite.setId(disponibilite.getId());

        partialUpdatedDisponibilite.canalSeance(UPDATED_CANAL_SEANCE);

        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisponibilite))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisponibiliteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDisponibilite, disponibilite),
            getPersistedDisponibilite(disponibilite)
        );
    }

    @Test
    @Transactional
    void fullUpdateDisponibiliteWithPatch() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disponibilite using partial update
        Disponibilite partialUpdatedDisponibilite = new Disponibilite();
        partialUpdatedDisponibilite.setId(disponibilite.getId());

        partialUpdatedDisponibilite
            .heureDebutCreneauxDisponibilite(UPDATED_HEURE_DEBUT_CRENEAUX_DISPONIBILITE)
            .heureFinCreneauxDisponibilite(UPDATED_HEURE_FIN_CRENEAUX_DISPONIBILITE)
            .canalSeance(UPDATED_CANAL_SEANCE);

        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisponibilite))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisponibiliteUpdatableFieldsEquals(partialUpdatedDisponibilite, getPersistedDisponibilite(partialUpdatedDisponibilite));
    }

    @Test
    @Transactional
    void patchNonExistingDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disponibilite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(disponibilite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisponibilite() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the disponibilite
        restDisponibiliteMockMvc
            .perform(delete(ENTITY_API_URL_ID, disponibilite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return disponibiliteRepository.count();
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

    protected Disponibilite getPersistedDisponibilite(Disponibilite disponibilite) {
        return disponibiliteRepository.findById(disponibilite.getId()).orElseThrow();
    }

    protected void assertPersistedDisponibiliteToMatchAllProperties(Disponibilite expectedDisponibilite) {
        assertDisponibiliteAllPropertiesEquals(expectedDisponibilite, getPersistedDisponibilite(expectedDisponibilite));
    }

    protected void assertPersistedDisponibiliteToMatchUpdatableProperties(Disponibilite expectedDisponibilite) {
        assertDisponibiliteAllUpdatablePropertiesEquals(expectedDisponibilite, getPersistedDisponibilite(expectedDisponibilite));
    }
}
