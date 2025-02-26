package fr.coaching.maseance.web.rest;

import fr.coaching.maseance.domain.Consent;
import fr.coaching.maseance.repository.ConsentRepository;
import fr.coaching.maseance.service.ConsentService;
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
 * REST controller for managing {@link fr.coaching.maseance.domain.Consent}.
 */
@RestController
@RequestMapping("/api/consents")
public class ConsentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConsentResource.class);

    private static final String ENTITY_NAME = "consent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsentService consentService;

    private final ConsentRepository consentRepository;

    public ConsentResource(ConsentService consentService, ConsentRepository consentRepository) {
        this.consentService = consentService;
        this.consentRepository = consentRepository;
    }

    /**
     * {@code POST  /consents} : Create a new consent.
     *
     * @param consent the consent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consent, or with status {@code 400 (Bad Request)} if the consent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Consent> createConsent(@Valid @RequestBody Consent consent) throws URISyntaxException {
        LOG.debug("REST request to save Consent : {}", consent);
        if (consent.getId() != null) {
            throw new BadRequestAlertException("A new consent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        consent = consentService.save(consent);
        return ResponseEntity.created(new URI("/api/consents/" + consent.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, consent.getId().toString()))
            .body(consent);
    }

    /**
     * {@code PUT  /consents/:id} : Updates an existing consent.
     *
     * @param id the id of the consent to save.
     * @param consent the consent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consent,
     * or with status {@code 400 (Bad Request)} if the consent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Consent> updateConsent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Consent consent
    ) throws URISyntaxException {
        LOG.debug("REST request to update Consent : {}, {}", id, consent);
        if (consent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        consent = consentService.update(consent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, consent.getId().toString()))
            .body(consent);
    }

    /**
     * {@code PATCH  /consents/:id} : Partial updates given fields of an existing consent, field will ignore if it is null
     *
     * @param id the id of the consent to save.
     * @param consent the consent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consent,
     * or with status {@code 400 (Bad Request)} if the consent is not valid,
     * or with status {@code 404 (Not Found)} if the consent is not found,
     * or with status {@code 500 (Internal Server Error)} if the consent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Consent> partialUpdateConsent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Consent consent
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Consent partially : {}, {}", id, consent);
        if (consent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Consent> result = consentService.partialUpdate(consent);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, consent.getId().toString())
        );
    }

    /**
     * {@code GET  /consents} : get all the consents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consents in body.
     */
    @GetMapping("")
    public List<Consent> getAllConsents() {
        LOG.debug("REST request to get all Consents");
        return consentService.findAll();
    }

    /**
     * {@code GET  /consents/:id} : get the "id" consent.
     *
     * @param id the id of the consent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Consent> getConsent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Consent : {}", id);
        Optional<Consent> consent = consentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consent);
    }

    /**
     * {@code DELETE  /consents/:id} : delete the "id" consent.
     *
     * @param id the id of the consent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Consent : {}", id);
        consentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
