package fr.coaching.maseance.web.rest;

import fr.coaching.maseance.domain.OffreCoach;
import fr.coaching.maseance.repository.OffreCoachRepository;
import fr.coaching.maseance.service.OffreCoachQueryService;
import fr.coaching.maseance.service.OffreCoachService;
import fr.coaching.maseance.service.criteria.OffreCoachCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.coaching.maseance.domain.OffreCoach}.
 */
@RestController
@RequestMapping("/api/offre-coaches")
public class OffreCoachResource {

    private static final Logger LOG = LoggerFactory.getLogger(OffreCoachResource.class);

    private static final String ENTITY_NAME = "offreCoach";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OffreCoachService offreCoachService;

    private final OffreCoachRepository offreCoachRepository;

    private final OffreCoachQueryService offreCoachQueryService;

    public OffreCoachResource(
        OffreCoachService offreCoachService,
        OffreCoachRepository offreCoachRepository,
        OffreCoachQueryService offreCoachQueryService
    ) {
        this.offreCoachService = offreCoachService;
        this.offreCoachRepository = offreCoachRepository;
        this.offreCoachQueryService = offreCoachQueryService;
    }

    /**
     * {@code POST  /offre-coaches} : Create a new offreCoach.
     *
     * @param offreCoach the offreCoach to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new offreCoach, or with status {@code 400 (Bad Request)} if the offreCoach has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OffreCoach> createOffreCoach(@Valid @RequestBody OffreCoach offreCoach) throws URISyntaxException {
        LOG.debug("REST request to save OffreCoach : {}", offreCoach);
        if (offreCoach.getId() != null) {
            throw new BadRequestAlertException("A new offreCoach cannot already have an ID", ENTITY_NAME, "idexists");
        }
        offreCoach = offreCoachService.save(offreCoach);
        return ResponseEntity.created(new URI("/api/offre-coaches/" + offreCoach.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, offreCoach.getId().toString()))
            .body(offreCoach);
    }

    /**
     * {@code PUT  /offre-coaches/:id} : Updates an existing offreCoach.
     *
     * @param id the id of the offreCoach to save.
     * @param offreCoach the offreCoach to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offreCoach,
     * or with status {@code 400 (Bad Request)} if the offreCoach is not valid,
     * or with status {@code 500 (Internal Server Error)} if the offreCoach couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OffreCoach> updateOffreCoach(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OffreCoach offreCoach
    ) throws URISyntaxException {
        LOG.debug("REST request to update OffreCoach : {}, {}", id, offreCoach);
        if (offreCoach.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offreCoach.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offreCoachRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        offreCoach = offreCoachService.update(offreCoach);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, offreCoach.getId().toString()))
            .body(offreCoach);
    }

    /**
     * {@code PATCH  /offre-coaches/:id} : Partial updates given fields of an existing offreCoach, field will ignore if it is null
     *
     * @param id the id of the offreCoach to save.
     * @param offreCoach the offreCoach to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offreCoach,
     * or with status {@code 400 (Bad Request)} if the offreCoach is not valid,
     * or with status {@code 404 (Not Found)} if the offreCoach is not found,
     * or with status {@code 500 (Internal Server Error)} if the offreCoach couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OffreCoach> partialUpdateOffreCoach(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OffreCoach offreCoach
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OffreCoach partially : {}, {}", id, offreCoach);
        if (offreCoach.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offreCoach.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offreCoachRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OffreCoach> result = offreCoachService.partialUpdate(offreCoach);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, offreCoach.getId().toString())
        );
    }

    /**
     * {@code GET  /offre-coaches} : get all the offreCoaches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of offreCoaches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OffreCoach>> getAllOffreCoaches(
        OffreCoachCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OffreCoaches by criteria: {}", criteria);

        Page<OffreCoach> page = offreCoachQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /offre-coaches/count} : count all the offreCoaches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOffreCoaches(OffreCoachCriteria criteria) {
        LOG.debug("REST request to count OffreCoaches by criteria: {}", criteria);
        return ResponseEntity.ok().body(offreCoachQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /offre-coaches/:id} : get the "id" offreCoach.
     *
     * @param id the id of the offreCoach to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the offreCoach, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OffreCoach> getOffreCoach(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OffreCoach : {}", id);
        Optional<OffreCoach> offreCoach = offreCoachService.findOne(id);
        return ResponseUtil.wrapOrNotFound(offreCoach);
    }

    /**
     * {@code DELETE  /offre-coaches/:id} : delete the "id" offreCoach.
     *
     * @param id the id of the offreCoach to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffreCoach(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OffreCoach : {}", id);
        offreCoachService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
