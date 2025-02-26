package fr.coaching.maseance.web.rest;

import fr.coaching.maseance.domain.Facture;
import fr.coaching.maseance.repository.FactureRepository;
import fr.coaching.maseance.service.FactureQueryService;
import fr.coaching.maseance.service.FactureService;
import fr.coaching.maseance.service.criteria.FactureCriteria;
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
 * REST controller for managing {@link fr.coaching.maseance.domain.Facture}.
 */
@RestController
@RequestMapping("/api/factures")
public class FactureResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactureResource.class);

    private static final String ENTITY_NAME = "facture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactureService factureService;

    private final FactureRepository factureRepository;

    private final FactureQueryService factureQueryService;

    public FactureResource(FactureService factureService, FactureRepository factureRepository, FactureQueryService factureQueryService) {
        this.factureService = factureService;
        this.factureRepository = factureRepository;
        this.factureQueryService = factureQueryService;
    }

    /**
     * {@code POST  /factures} : Create a new facture.
     *
     * @param facture the facture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facture, or with status {@code 400 (Bad Request)} if the facture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Facture> createFacture(@Valid @RequestBody Facture facture) throws URISyntaxException {
        LOG.debug("REST request to save Facture : {}", facture);
        if (facture.getId() != null) {
            throw new BadRequestAlertException("A new facture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facture = factureService.save(facture);
        return ResponseEntity.created(new URI("/api/factures/" + facture.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, facture.getId().toString()))
            .body(facture);
    }

    /**
     * {@code PUT  /factures/:id} : Updates an existing facture.
     *
     * @param id the id of the facture to save.
     * @param facture the facture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facture,
     * or with status {@code 400 (Bad Request)} if the facture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Facture> updateFacture(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Facture facture
    ) throws URISyntaxException {
        LOG.debug("REST request to update Facture : {}, {}", id, facture);
        if (facture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facture = factureService.update(facture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, facture.getId().toString()))
            .body(facture);
    }

    /**
     * {@code PATCH  /factures/:id} : Partial updates given fields of an existing facture, field will ignore if it is null
     *
     * @param id the id of the facture to save.
     * @param facture the facture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facture,
     * or with status {@code 400 (Bad Request)} if the facture is not valid,
     * or with status {@code 404 (Not Found)} if the facture is not found,
     * or with status {@code 500 (Internal Server Error)} if the facture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Facture> partialUpdateFacture(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Facture facture
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Facture partially : {}, {}", id, facture);
        if (facture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Facture> result = factureService.partialUpdate(facture);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, facture.getId().toString())
        );
    }

    /**
     * {@code GET  /factures} : get all the factures.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factures in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Facture>> getAllFactures(
        FactureCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Factures by criteria: {}", criteria);

        Page<Facture> page = factureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /factures/count} : count all the factures.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFactures(FactureCriteria criteria) {
        LOG.debug("REST request to count Factures by criteria: {}", criteria);
        return ResponseEntity.ok().body(factureQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /factures/:id} : get the "id" facture.
     *
     * @param id the id of the facture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFacture(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Facture : {}", id);
        Optional<Facture> facture = factureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facture);
    }

    /**
     * {@code DELETE  /factures/:id} : delete the "id" facture.
     *
     * @param id the id of the facture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Facture : {}", id);
        factureService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
