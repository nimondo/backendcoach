package fr.coaching.maseance.web.rest;

import fr.coaching.maseance.domain.SeanceReservationCoach;
import fr.coaching.maseance.repository.SeanceReservationCoachRepository;
import fr.coaching.maseance.service.SeanceReservationCoachQueryService;
import fr.coaching.maseance.service.SeanceReservationCoachService;
import fr.coaching.maseance.service.criteria.SeanceReservationCoachCriteria;
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
 * REST controller for managing {@link fr.coaching.maseance.domain.SeanceReservationCoach}.
 */
@RestController
@RequestMapping("/api/seance-reservation-coaches")
public class SeanceReservationCoachResource {

    private static final Logger LOG = LoggerFactory.getLogger(SeanceReservationCoachResource.class);

    private static final String ENTITY_NAME = "seanceReservationCoach";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeanceReservationCoachService seanceReservationCoachService;

    private final SeanceReservationCoachRepository seanceReservationCoachRepository;

    private final SeanceReservationCoachQueryService seanceReservationCoachQueryService;

    public SeanceReservationCoachResource(
        SeanceReservationCoachService seanceReservationCoachService,
        SeanceReservationCoachRepository seanceReservationCoachRepository,
        SeanceReservationCoachQueryService seanceReservationCoachQueryService
    ) {
        this.seanceReservationCoachService = seanceReservationCoachService;
        this.seanceReservationCoachRepository = seanceReservationCoachRepository;
        this.seanceReservationCoachQueryService = seanceReservationCoachQueryService;
    }

    /**
     * {@code POST  /seance-reservation-coaches} : Create a new seanceReservationCoach.
     *
     * @param seanceReservationCoach the seanceReservationCoach to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seanceReservationCoach, or with status {@code 400 (Bad Request)} if the seanceReservationCoach has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SeanceReservationCoach> createSeanceReservationCoach(
        @Valid @RequestBody SeanceReservationCoach seanceReservationCoach
    ) throws URISyntaxException {
        LOG.debug("REST request to save SeanceReservationCoach : {}", seanceReservationCoach);
        if (seanceReservationCoach.getId() != null) {
            throw new BadRequestAlertException("A new seanceReservationCoach cannot already have an ID", ENTITY_NAME, "idexists");
        }
        seanceReservationCoach = seanceReservationCoachService.save(seanceReservationCoach);
        return ResponseEntity.created(new URI("/api/seance-reservation-coaches/" + seanceReservationCoach.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, seanceReservationCoach.getId().toString()))
            .body(seanceReservationCoach);
    }

    /**
     * {@code PUT  /seance-reservation-coaches/:id} : Updates an existing seanceReservationCoach.
     *
     * @param id the id of the seanceReservationCoach to save.
     * @param seanceReservationCoach the seanceReservationCoach to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seanceReservationCoach,
     * or with status {@code 400 (Bad Request)} if the seanceReservationCoach is not valid,
     * or with status {@code 500 (Internal Server Error)} if the seanceReservationCoach couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SeanceReservationCoach> updateSeanceReservationCoach(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SeanceReservationCoach seanceReservationCoach
    ) throws URISyntaxException {
        LOG.debug("REST request to update SeanceReservationCoach : {}, {}", id, seanceReservationCoach);
        if (seanceReservationCoach.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seanceReservationCoach.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seanceReservationCoachRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        seanceReservationCoach = seanceReservationCoachService.update(seanceReservationCoach);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, seanceReservationCoach.getId().toString()))
            .body(seanceReservationCoach);
    }

    /**
     * {@code PATCH  /seance-reservation-coaches/:id} : Partial updates given fields of an existing seanceReservationCoach, field will ignore if it is null
     *
     * @param id the id of the seanceReservationCoach to save.
     * @param seanceReservationCoach the seanceReservationCoach to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seanceReservationCoach,
     * or with status {@code 400 (Bad Request)} if the seanceReservationCoach is not valid,
     * or with status {@code 404 (Not Found)} if the seanceReservationCoach is not found,
     * or with status {@code 500 (Internal Server Error)} if the seanceReservationCoach couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SeanceReservationCoach> partialUpdateSeanceReservationCoach(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SeanceReservationCoach seanceReservationCoach
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SeanceReservationCoach partially : {}, {}", id, seanceReservationCoach);
        if (seanceReservationCoach.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seanceReservationCoach.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seanceReservationCoachRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SeanceReservationCoach> result = seanceReservationCoachService.partialUpdate(seanceReservationCoach);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, seanceReservationCoach.getId().toString())
        );
    }

    /**
     * {@code GET  /seance-reservation-coaches} : get all the seanceReservationCoaches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seanceReservationCoaches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SeanceReservationCoach>> getAllSeanceReservationCoaches(SeanceReservationCoachCriteria criteria) {
        LOG.debug("REST request to get SeanceReservationCoaches by criteria: {}", criteria);

        List<SeanceReservationCoach> entityList = seanceReservationCoachQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /seance-reservation-coaches/count} : count all the seanceReservationCoaches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSeanceReservationCoaches(SeanceReservationCoachCriteria criteria) {
        LOG.debug("REST request to count SeanceReservationCoaches by criteria: {}", criteria);
        return ResponseEntity.ok().body(seanceReservationCoachQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /seance-reservation-coaches/:id} : get the "id" seanceReservationCoach.
     *
     * @param id the id of the seanceReservationCoach to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seanceReservationCoach, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SeanceReservationCoach> getSeanceReservationCoach(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SeanceReservationCoach : {}", id);
        Optional<SeanceReservationCoach> seanceReservationCoach = seanceReservationCoachService.findOne(id);
        return ResponseUtil.wrapOrNotFound(seanceReservationCoach);
    }

    /**
     * {@code DELETE  /seance-reservation-coaches/:id} : delete the "id" seanceReservationCoach.
     *
     * @param id the id of the seanceReservationCoach to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeanceReservationCoach(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SeanceReservationCoach : {}", id);
        seanceReservationCoachService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
