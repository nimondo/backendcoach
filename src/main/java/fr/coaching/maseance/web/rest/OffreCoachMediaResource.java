package fr.coaching.maseance.web.rest;

import fr.coaching.maseance.domain.OffreCoachMedia;
import fr.coaching.maseance.repository.OffreCoachMediaRepository;
import fr.coaching.maseance.service.OffreCoachMediaQueryService;
import fr.coaching.maseance.service.OffreCoachMediaService;
import fr.coaching.maseance.service.criteria.OffreCoachMediaCriteria;
import fr.coaching.maseance.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.coaching.maseance.domain.OffreCoachMedia}.
 */
@RestController
@RequestMapping("/api/offre-coach-medias")
public class OffreCoachMediaResource {

    private static final Logger LOG = LoggerFactory.getLogger(OffreCoachMediaResource.class);

    private static final String ENTITY_NAME = "offreCoachMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OffreCoachMediaService offreCoachMediaService;

    private final OffreCoachMediaRepository offreCoachMediaRepository;

    private final OffreCoachMediaQueryService offreCoachMediaQueryService;

    public OffreCoachMediaResource(
        OffreCoachMediaService offreCoachMediaService,
        OffreCoachMediaRepository offreCoachMediaRepository,
        OffreCoachMediaQueryService offreCoachMediaQueryService
    ) {
        this.offreCoachMediaService = offreCoachMediaService;
        this.offreCoachMediaRepository = offreCoachMediaRepository;
        this.offreCoachMediaQueryService = offreCoachMediaQueryService;
    }

    /**
     * {@code POST  /offre-coach-medias} : Create a new offreCoachMedia.
     *
     * @param offreCoachMedia the offreCoachMedia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new offreCoachMedia, or with status {@code 400 (Bad Request)} if the offreCoachMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OffreCoachMedia> createOffreCoachMedia(@RequestBody OffreCoachMedia offreCoachMedia) throws URISyntaxException {
        LOG.debug("REST request to save OffreCoachMedia : {}", offreCoachMedia);
        if (offreCoachMedia.getId() != null) {
            throw new BadRequestAlertException("A new offreCoachMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        offreCoachMedia = offreCoachMediaService.save(offreCoachMedia);
        return ResponseEntity.created(new URI("/api/offre-coach-medias/" + offreCoachMedia.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, offreCoachMedia.getId().toString()))
            .body(offreCoachMedia);
    }

    /**
     * {@code PUT  /offre-coach-medias/:id} : Updates an existing offreCoachMedia.
     *
     * @param id the id of the offreCoachMedia to save.
     * @param offreCoachMedia the offreCoachMedia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offreCoachMedia,
     * or with status {@code 400 (Bad Request)} if the offreCoachMedia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the offreCoachMedia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OffreCoachMedia> updateOffreCoachMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OffreCoachMedia offreCoachMedia
    ) throws URISyntaxException {
        LOG.debug("REST request to update OffreCoachMedia : {}, {}", id, offreCoachMedia);
        if (offreCoachMedia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offreCoachMedia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offreCoachMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        offreCoachMedia = offreCoachMediaService.update(offreCoachMedia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, offreCoachMedia.getId().toString()))
            .body(offreCoachMedia);
    }

    /**
     * {@code PATCH  /offre-coach-medias/:id} : Partial updates given fields of an existing offreCoachMedia, field will ignore if it is null
     *
     * @param id the id of the offreCoachMedia to save.
     * @param offreCoachMedia the offreCoachMedia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offreCoachMedia,
     * or with status {@code 400 (Bad Request)} if the offreCoachMedia is not valid,
     * or with status {@code 404 (Not Found)} if the offreCoachMedia is not found,
     * or with status {@code 500 (Internal Server Error)} if the offreCoachMedia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OffreCoachMedia> partialUpdateOffreCoachMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OffreCoachMedia offreCoachMedia
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OffreCoachMedia partially : {}, {}", id, offreCoachMedia);
        if (offreCoachMedia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offreCoachMedia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offreCoachMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OffreCoachMedia> result = offreCoachMediaService.partialUpdate(offreCoachMedia);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, offreCoachMedia.getId().toString())
        );
    }

    /**
     * {@code GET  /offre-coach-medias} : get all the offreCoachMedias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of offreCoachMedias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OffreCoachMedia>> getAllOffreCoachMedias(OffreCoachMediaCriteria criteria) {
        LOG.debug("REST request to get OffreCoachMedias by criteria: {}", criteria);

        List<OffreCoachMedia> entityList = offreCoachMediaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /offre-coach-medias/count} : count all the offreCoachMedias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOffreCoachMedias(OffreCoachMediaCriteria criteria) {
        LOG.debug("REST request to count OffreCoachMedias by criteria: {}", criteria);
        return ResponseEntity.ok().body(offreCoachMediaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /offre-coach-medias/:id} : get the "id" offreCoachMedia.
     *
     * @param id the id of the offreCoachMedia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the offreCoachMedia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OffreCoachMedia> getOffreCoachMedia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OffreCoachMedia : {}", id);
        Optional<OffreCoachMedia> offreCoachMedia = offreCoachMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(offreCoachMedia);
    }

    /**
     * {@code DELETE  /offre-coach-medias/:id} : delete the "id" offreCoachMedia.
     *
     * @param id the id of the offreCoachMedia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffreCoachMedia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OffreCoachMedia : {}", id);
        offreCoachMediaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
