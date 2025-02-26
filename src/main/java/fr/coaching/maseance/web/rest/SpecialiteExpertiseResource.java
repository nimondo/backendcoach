package fr.coaching.maseance.web.rest;

import fr.coaching.maseance.domain.SpecialiteExpertise;
import fr.coaching.maseance.repository.SpecialiteExpertiseRepository;
import fr.coaching.maseance.service.SpecialiteExpertiseQueryService;
import fr.coaching.maseance.service.SpecialiteExpertiseService;
import fr.coaching.maseance.service.criteria.SpecialiteExpertiseCriteria;
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
 * REST controller for managing {@link fr.coaching.maseance.domain.SpecialiteExpertise}.
 */
@RestController
@RequestMapping("/api/specialite-expertises")
public class SpecialiteExpertiseResource {

    private static final Logger LOG = LoggerFactory.getLogger(SpecialiteExpertiseResource.class);

    private static final String ENTITY_NAME = "specialiteExpertise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialiteExpertiseService specialiteExpertiseService;

    private final SpecialiteExpertiseRepository specialiteExpertiseRepository;

    private final SpecialiteExpertiseQueryService specialiteExpertiseQueryService;

    public SpecialiteExpertiseResource(
        SpecialiteExpertiseService specialiteExpertiseService,
        SpecialiteExpertiseRepository specialiteExpertiseRepository,
        SpecialiteExpertiseQueryService specialiteExpertiseQueryService
    ) {
        this.specialiteExpertiseService = specialiteExpertiseService;
        this.specialiteExpertiseRepository = specialiteExpertiseRepository;
        this.specialiteExpertiseQueryService = specialiteExpertiseQueryService;
    }

    /**
     * {@code POST  /specialite-expertises} : Create a new specialiteExpertise.
     *
     * @param specialiteExpertise the specialiteExpertise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialiteExpertise, or with status {@code 400 (Bad Request)} if the specialiteExpertise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpecialiteExpertise> createSpecialiteExpertise(@Valid @RequestBody SpecialiteExpertise specialiteExpertise)
        throws URISyntaxException {
        LOG.debug("REST request to save SpecialiteExpertise : {}", specialiteExpertise);
        if (specialiteExpertise.getId() != null) {
            throw new BadRequestAlertException("A new specialiteExpertise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        specialiteExpertise = specialiteExpertiseService.save(specialiteExpertise);
        return ResponseEntity.created(new URI("/api/specialite-expertises/" + specialiteExpertise.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, specialiteExpertise.getId().toString()))
            .body(specialiteExpertise);
    }

    /**
     * {@code PUT  /specialite-expertises/:id} : Updates an existing specialiteExpertise.
     *
     * @param id the id of the specialiteExpertise to save.
     * @param specialiteExpertise the specialiteExpertise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialiteExpertise,
     * or with status {@code 400 (Bad Request)} if the specialiteExpertise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialiteExpertise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpecialiteExpertise> updateSpecialiteExpertise(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecialiteExpertise specialiteExpertise
    ) throws URISyntaxException {
        LOG.debug("REST request to update SpecialiteExpertise : {}, {}", id, specialiteExpertise);
        if (specialiteExpertise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialiteExpertise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialiteExpertiseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        specialiteExpertise = specialiteExpertiseService.update(specialiteExpertise);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specialiteExpertise.getId().toString()))
            .body(specialiteExpertise);
    }

    /**
     * {@code PATCH  /specialite-expertises/:id} : Partial updates given fields of an existing specialiteExpertise, field will ignore if it is null
     *
     * @param id the id of the specialiteExpertise to save.
     * @param specialiteExpertise the specialiteExpertise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialiteExpertise,
     * or with status {@code 400 (Bad Request)} if the specialiteExpertise is not valid,
     * or with status {@code 404 (Not Found)} if the specialiteExpertise is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialiteExpertise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecialiteExpertise> partialUpdateSpecialiteExpertise(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecialiteExpertise specialiteExpertise
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SpecialiteExpertise partially : {}, {}", id, specialiteExpertise);
        if (specialiteExpertise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialiteExpertise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialiteExpertiseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecialiteExpertise> result = specialiteExpertiseService.partialUpdate(specialiteExpertise);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specialiteExpertise.getId().toString())
        );
    }

    /**
     * {@code GET  /specialite-expertises} : get all the specialiteExpertises.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialiteExpertises in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SpecialiteExpertise>> getAllSpecialiteExpertises(SpecialiteExpertiseCriteria criteria) {
        LOG.debug("REST request to get SpecialiteExpertises by criteria: {}", criteria);

        List<SpecialiteExpertise> entityList = specialiteExpertiseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /specialite-expertises/count} : count all the specialiteExpertises.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSpecialiteExpertises(SpecialiteExpertiseCriteria criteria) {
        LOG.debug("REST request to count SpecialiteExpertises by criteria: {}", criteria);
        return ResponseEntity.ok().body(specialiteExpertiseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /specialite-expertises/:id} : get the "id" specialiteExpertise.
     *
     * @param id the id of the specialiteExpertise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialiteExpertise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecialiteExpertise> getSpecialiteExpertise(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SpecialiteExpertise : {}", id);
        Optional<SpecialiteExpertise> specialiteExpertise = specialiteExpertiseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialiteExpertise);
    }

    /**
     * {@code DELETE  /specialite-expertises/:id} : delete the "id" specialiteExpertise.
     *
     * @param id the id of the specialiteExpertise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialiteExpertise(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SpecialiteExpertise : {}", id);
        specialiteExpertiseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
