package fr.coaching.maseance.web.rest;

import fr.coaching.maseance.domain.Paiement;
import fr.coaching.maseance.repository.PaiementRepository;
import fr.coaching.maseance.service.PaiementQueryService;
import fr.coaching.maseance.service.PaiementService;
import fr.coaching.maseance.service.criteria.PaiementCriteria;
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
 * REST controller for managing {@link fr.coaching.maseance.domain.Paiement}.
 */
@RestController
@RequestMapping("/api/paiements")
public class PaiementResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaiementResource.class);

    private static final String ENTITY_NAME = "paiement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaiementService paiementService;

    private final PaiementRepository paiementRepository;

    private final PaiementQueryService paiementQueryService;

    public PaiementResource(
        PaiementService paiementService,
        PaiementRepository paiementRepository,
        PaiementQueryService paiementQueryService
    ) {
        this.paiementService = paiementService;
        this.paiementRepository = paiementRepository;
        this.paiementQueryService = paiementQueryService;
    }

    /**
     * {@code POST  /paiements} : Create a new paiement.
     *
     * @param paiement the paiement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paiement, or with status {@code 400 (Bad Request)} if the paiement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Paiement> createPaiement(@Valid @RequestBody Paiement paiement) throws URISyntaxException {
        LOG.debug("REST request to save Paiement : {}", paiement);
        if (paiement.getId() != null) {
            throw new BadRequestAlertException("A new paiement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paiement = paiementService.save(paiement);
        return ResponseEntity.created(new URI("/api/paiements/" + paiement.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, paiement.getId().toString()))
            .body(paiement);
    }

    /**
     * {@code PUT  /paiements/:id} : Updates an existing paiement.
     *
     * @param id the id of the paiement to save.
     * @param paiement the paiement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiement,
     * or with status {@code 400 (Bad Request)} if the paiement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paiement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Paiement> updatePaiement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Paiement paiement
    ) throws URISyntaxException {
        LOG.debug("REST request to update Paiement : {}, {}", id, paiement);
        if (paiement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paiement = paiementService.update(paiement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paiement.getId().toString()))
            .body(paiement);
    }

    /**
     * {@code PATCH  /paiements/:id} : Partial updates given fields of an existing paiement, field will ignore if it is null
     *
     * @param id the id of the paiement to save.
     * @param paiement the paiement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiement,
     * or with status {@code 400 (Bad Request)} if the paiement is not valid,
     * or with status {@code 404 (Not Found)} if the paiement is not found,
     * or with status {@code 500 (Internal Server Error)} if the paiement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Paiement> partialUpdatePaiement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Paiement paiement
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Paiement partially : {}, {}", id, paiement);
        if (paiement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Paiement> result = paiementService.partialUpdate(paiement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paiement.getId().toString())
        );
    }

    /**
     * {@code GET  /paiements} : get all the paiements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paiements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Paiement>> getAllPaiements(
        PaiementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Paiements by criteria: {}", criteria);

        Page<Paiement> page = paiementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paiements/count} : count all the paiements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaiements(PaiementCriteria criteria) {
        LOG.debug("REST request to count Paiements by criteria: {}", criteria);
        return ResponseEntity.ok().body(paiementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /paiements/:id} : get the "id" paiement.
     *
     * @param id the id of the paiement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paiement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getPaiement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Paiement : {}", id);
        Optional<Paiement> paiement = paiementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paiement);
    }

    /**
     * {@code DELETE  /paiements/:id} : delete the "id" paiement.
     *
     * @param id the id of the paiement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaiement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Paiement : {}", id);
        paiementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
