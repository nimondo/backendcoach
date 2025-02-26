package fr.coaching.maseance.web.rest;

import fr.coaching.maseance.domain.AvisClient;
import fr.coaching.maseance.repository.AvisClientRepository;
import fr.coaching.maseance.service.AvisClientQueryService;
import fr.coaching.maseance.service.AvisClientService;
import fr.coaching.maseance.service.criteria.AvisClientCriteria;
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
 * REST controller for managing {@link fr.coaching.maseance.domain.AvisClient}.
 */
@RestController
@RequestMapping("/api/avis-clients")
public class AvisClientResource {

    private static final Logger LOG = LoggerFactory.getLogger(AvisClientResource.class);

    private static final String ENTITY_NAME = "avisClient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AvisClientService avisClientService;

    private final AvisClientRepository avisClientRepository;

    private final AvisClientQueryService avisClientQueryService;

    public AvisClientResource(
        AvisClientService avisClientService,
        AvisClientRepository avisClientRepository,
        AvisClientQueryService avisClientQueryService
    ) {
        this.avisClientService = avisClientService;
        this.avisClientRepository = avisClientRepository;
        this.avisClientQueryService = avisClientQueryService;
    }

    /**
     * {@code POST  /avis-clients} : Create a new avisClient.
     *
     * @param avisClient the avisClient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new avisClient, or with status {@code 400 (Bad Request)} if the avisClient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AvisClient> createAvisClient(@Valid @RequestBody AvisClient avisClient) throws URISyntaxException {
        LOG.debug("REST request to save AvisClient : {}", avisClient);
        if (avisClient.getId() != null) {
            throw new BadRequestAlertException("A new avisClient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        avisClient = avisClientService.save(avisClient);
        return ResponseEntity.created(new URI("/api/avis-clients/" + avisClient.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, avisClient.getId().toString()))
            .body(avisClient);
    }

    /**
     * {@code PUT  /avis-clients/:id} : Updates an existing avisClient.
     *
     * @param id the id of the avisClient to save.
     * @param avisClient the avisClient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avisClient,
     * or with status {@code 400 (Bad Request)} if the avisClient is not valid,
     * or with status {@code 500 (Internal Server Error)} if the avisClient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AvisClient> updateAvisClient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AvisClient avisClient
    ) throws URISyntaxException {
        LOG.debug("REST request to update AvisClient : {}, {}", id, avisClient);
        if (avisClient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avisClient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avisClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        avisClient = avisClientService.update(avisClient);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, avisClient.getId().toString()))
            .body(avisClient);
    }

    /**
     * {@code PATCH  /avis-clients/:id} : Partial updates given fields of an existing avisClient, field will ignore if it is null
     *
     * @param id the id of the avisClient to save.
     * @param avisClient the avisClient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avisClient,
     * or with status {@code 400 (Bad Request)} if the avisClient is not valid,
     * or with status {@code 404 (Not Found)} if the avisClient is not found,
     * or with status {@code 500 (Internal Server Error)} if the avisClient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AvisClient> partialUpdateAvisClient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AvisClient avisClient
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AvisClient partially : {}, {}", id, avisClient);
        if (avisClient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avisClient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avisClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AvisClient> result = avisClientService.partialUpdate(avisClient);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, avisClient.getId().toString())
        );
    }

    /**
     * {@code GET  /avis-clients} : get all the avisClients.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of avisClients in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AvisClient>> getAllAvisClients(
        AvisClientCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AvisClients by criteria: {}", criteria);

        Page<AvisClient> page = avisClientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /avis-clients/count} : count all the avisClients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAvisClients(AvisClientCriteria criteria) {
        LOG.debug("REST request to count AvisClients by criteria: {}", criteria);
        return ResponseEntity.ok().body(avisClientQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /avis-clients/:id} : get the "id" avisClient.
     *
     * @param id the id of the avisClient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the avisClient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AvisClient> getAvisClient(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AvisClient : {}", id);
        Optional<AvisClient> avisClient = avisClientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(avisClient);
    }

    /**
     * {@code DELETE  /avis-clients/:id} : delete the "id" avisClient.
     *
     * @param id the id of the avisClient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvisClient(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AvisClient : {}", id);
        avisClientService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
