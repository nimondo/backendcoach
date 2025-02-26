package fr.coaching.maseance.web.rest;

import fr.coaching.maseance.domain.CoachExpert;
import fr.coaching.maseance.repository.CoachExpertRepository;
import fr.coaching.maseance.service.CoachExpertQueryService;
import fr.coaching.maseance.service.CoachExpertService;
import fr.coaching.maseance.service.criteria.CoachExpertCriteria;
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
 * REST controller for managing {@link fr.coaching.maseance.domain.CoachExpert}.
 */
@RestController
@RequestMapping("/api/coach-experts")
public class CoachExpertResource {

    private static final Logger LOG = LoggerFactory.getLogger(CoachExpertResource.class);

    private static final String ENTITY_NAME = "coachExpert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoachExpertService coachExpertService;

    private final CoachExpertRepository coachExpertRepository;

    private final CoachExpertQueryService coachExpertQueryService;

    public CoachExpertResource(
        CoachExpertService coachExpertService,
        CoachExpertRepository coachExpertRepository,
        CoachExpertQueryService coachExpertQueryService
    ) {
        this.coachExpertService = coachExpertService;
        this.coachExpertRepository = coachExpertRepository;
        this.coachExpertQueryService = coachExpertQueryService;
    }

    /**
     * {@code POST  /coach-experts} : Create a new coachExpert.
     *
     * @param coachExpert the coachExpert to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coachExpert, or with status {@code 400 (Bad Request)} if the coachExpert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CoachExpert> createCoachExpert(@Valid @RequestBody CoachExpert coachExpert) throws URISyntaxException {
        LOG.debug("REST request to save CoachExpert : {}", coachExpert);
        if (coachExpert.getId() != null) {
            throw new BadRequestAlertException("A new coachExpert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        coachExpert = coachExpertService.save(coachExpert);
        return ResponseEntity.created(new URI("/api/coach-experts/" + coachExpert.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, coachExpert.getId().toString()))
            .body(coachExpert);
    }

    /**
     * {@code PUT  /coach-experts/:id} : Updates an existing coachExpert.
     *
     * @param id the id of the coachExpert to save.
     * @param coachExpert the coachExpert to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coachExpert,
     * or with status {@code 400 (Bad Request)} if the coachExpert is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coachExpert couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CoachExpert> updateCoachExpert(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CoachExpert coachExpert
    ) throws URISyntaxException {
        LOG.debug("REST request to update CoachExpert : {}, {}", id, coachExpert);
        if (coachExpert.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coachExpert.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coachExpertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        coachExpert = coachExpertService.update(coachExpert);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, coachExpert.getId().toString()))
            .body(coachExpert);
    }

    /**
     * {@code PATCH  /coach-experts/:id} : Partial updates given fields of an existing coachExpert, field will ignore if it is null
     *
     * @param id the id of the coachExpert to save.
     * @param coachExpert the coachExpert to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coachExpert,
     * or with status {@code 400 (Bad Request)} if the coachExpert is not valid,
     * or with status {@code 404 (Not Found)} if the coachExpert is not found,
     * or with status {@code 500 (Internal Server Error)} if the coachExpert couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CoachExpert> partialUpdateCoachExpert(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CoachExpert coachExpert
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CoachExpert partially : {}, {}", id, coachExpert);
        if (coachExpert.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coachExpert.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coachExpertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoachExpert> result = coachExpertService.partialUpdate(coachExpert);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, coachExpert.getId().toString())
        );
    }

    /**
     * {@code GET  /coach-experts} : get all the coachExperts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coachExperts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CoachExpert>> getAllCoachExperts(
        CoachExpertCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CoachExperts by criteria: {}", criteria);

        Page<CoachExpert> page = coachExpertQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coach-experts/count} : count all the coachExperts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCoachExperts(CoachExpertCriteria criteria) {
        LOG.debug("REST request to count CoachExperts by criteria: {}", criteria);
        return ResponseEntity.ok().body(coachExpertQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /coach-experts/:id} : get the "id" coachExpert.
     *
     * @param id the id of the coachExpert to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coachExpert, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CoachExpert> getCoachExpert(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CoachExpert : {}", id);
        Optional<CoachExpert> coachExpert = coachExpertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coachExpert);
    }

    /**
     * {@code DELETE  /coach-experts/:id} : delete the "id" coachExpert.
     *
     * @param id the id of the coachExpert to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoachExpert(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CoachExpert : {}", id);
        coachExpertService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
