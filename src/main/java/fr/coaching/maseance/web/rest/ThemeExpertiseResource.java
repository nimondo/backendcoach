package fr.coaching.maseance.web.rest;

import fr.coaching.maseance.domain.ThemeExpertise;
import fr.coaching.maseance.repository.ThemeExpertiseRepository;
import fr.coaching.maseance.service.ThemeExpertiseQueryService;
import fr.coaching.maseance.service.ThemeExpertiseService;
import fr.coaching.maseance.service.criteria.ThemeExpertiseCriteria;
import fr.coaching.maseance.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.coaching.maseance.domain.ThemeExpertise}.
 */
@RestController
@RequestMapping("/api/theme-expertises")
public class ThemeExpertiseResource {

    private static final Logger LOG = LoggerFactory.getLogger(ThemeExpertiseResource.class);

    private static final String ENTITY_NAME = "themeExpertise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThemeExpertiseService themeExpertiseService;

    private final ThemeExpertiseRepository themeExpertiseRepository;

    private final ThemeExpertiseQueryService themeExpertiseQueryService;

    public ThemeExpertiseResource(
        ThemeExpertiseService themeExpertiseService,
        ThemeExpertiseRepository themeExpertiseRepository,
        ThemeExpertiseQueryService themeExpertiseQueryService
    ) {
        this.themeExpertiseService = themeExpertiseService;
        this.themeExpertiseRepository = themeExpertiseRepository;
        this.themeExpertiseQueryService = themeExpertiseQueryService;
    }

    /**
     * {@code POST  /theme-expertises} : Create a new themeExpertise.
     *
     * @param themeExpertise the themeExpertise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new themeExpertise, or with status {@code 400 (Bad Request)} if the themeExpertise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ThemeExpertise> createThemeExpertise(@Valid @RequestBody ThemeExpertise themeExpertise)
        throws URISyntaxException {
        LOG.debug("REST request to save ThemeExpertise : {}", themeExpertise);
        if (themeExpertise.getId() != null) {
            throw new BadRequestAlertException("A new themeExpertise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        themeExpertise = themeExpertiseService.save(themeExpertise);
        return ResponseEntity.created(new URI("/api/theme-expertises/" + themeExpertise.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, themeExpertise.getId().toString()))
            .body(themeExpertise);
    }

    /**
     * {@code PUT  /theme-expertises/:id} : Updates an existing themeExpertise.
     *
     * @param id the id of the themeExpertise to save.
     * @param themeExpertise the themeExpertise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeExpertise,
     * or with status {@code 400 (Bad Request)} if the themeExpertise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the themeExpertise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ThemeExpertise> updateThemeExpertise(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ThemeExpertise themeExpertise
    ) throws URISyntaxException {
        LOG.debug("REST request to update ThemeExpertise : {}, {}", id, themeExpertise);
        if (themeExpertise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeExpertise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeExpertiseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        themeExpertise = themeExpertiseService.update(themeExpertise);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, themeExpertise.getId().toString()))
            .body(themeExpertise);
    }

    /**
     * {@code PATCH  /theme-expertises/:id} : Partial updates given fields of an existing themeExpertise, field will ignore if it is null
     *
     * @param id the id of the themeExpertise to save.
     * @param themeExpertise the themeExpertise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeExpertise,
     * or with status {@code 400 (Bad Request)} if the themeExpertise is not valid,
     * or with status {@code 404 (Not Found)} if the themeExpertise is not found,
     * or with status {@code 500 (Internal Server Error)} if the themeExpertise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ThemeExpertise> partialUpdateThemeExpertise(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ThemeExpertise themeExpertise
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ThemeExpertise partially : {}, {}", id, themeExpertise);
        if (themeExpertise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeExpertise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeExpertiseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ThemeExpertise> result = themeExpertiseService.partialUpdate(themeExpertise);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, themeExpertise.getId().toString())
        );
    }

    /**
     * {@code GET  /theme-expertises} : get all the themeExpertises.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of themeExpertises in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ThemeExpertise>> getAllThemeExpertises(ThemeExpertiseCriteria criteria) {
        LOG.debug("REST request to get ThemeExpertises by criteria: {}", criteria);

        List<ThemeExpertise> entityList = themeExpertiseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /theme-expertises/count} : count all the themeExpertises.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countThemeExpertises(ThemeExpertiseCriteria criteria) {
        LOG.debug("REST request to count ThemeExpertises by criteria: {}", criteria);
        return ResponseEntity.ok().body(themeExpertiseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /theme-expertises/:id} : get the "id" themeExpertise.
     *
     * @param id the id of the themeExpertise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the themeExpertise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ThemeExpertise> getThemeExpertise(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ThemeExpertise : {}", id);
        Optional<ThemeExpertise> themeExpertise = themeExpertiseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(themeExpertise);
    }

    /**
     * {@code DELETE  /theme-expertises/:id} : delete the "id" themeExpertise.
     *
     * @param id the id of the themeExpertise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThemeExpertise(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ThemeExpertise : {}", id);
        themeExpertiseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
