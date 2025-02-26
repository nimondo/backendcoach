package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.OffreCoachMedia;
import fr.coaching.maseance.repository.OffreCoachMediaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.OffreCoachMedia}.
 */
@Service
@Transactional
public class OffreCoachMediaService {

    private static final Logger LOG = LoggerFactory.getLogger(OffreCoachMediaService.class);

    private final OffreCoachMediaRepository offreCoachMediaRepository;

    public OffreCoachMediaService(OffreCoachMediaRepository offreCoachMediaRepository) {
        this.offreCoachMediaRepository = offreCoachMediaRepository;
    }

    /**
     * Save a offreCoachMedia.
     *
     * @param offreCoachMedia the entity to save.
     * @return the persisted entity.
     */
    public OffreCoachMedia save(OffreCoachMedia offreCoachMedia) {
        LOG.debug("Request to save OffreCoachMedia : {}", offreCoachMedia);
        return offreCoachMediaRepository.save(offreCoachMedia);
    }

    /**
     * Update a offreCoachMedia.
     *
     * @param offreCoachMedia the entity to save.
     * @return the persisted entity.
     */
    public OffreCoachMedia update(OffreCoachMedia offreCoachMedia) {
        LOG.debug("Request to update OffreCoachMedia : {}", offreCoachMedia);
        return offreCoachMediaRepository.save(offreCoachMedia);
    }

    /**
     * Partially update a offreCoachMedia.
     *
     * @param offreCoachMedia the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OffreCoachMedia> partialUpdate(OffreCoachMedia offreCoachMedia) {
        LOG.debug("Request to partially update OffreCoachMedia : {}", offreCoachMedia);

        return offreCoachMediaRepository
            .findById(offreCoachMedia.getId())
            .map(existingOffreCoachMedia -> {
                if (offreCoachMedia.getUrlMedia() != null) {
                    existingOffreCoachMedia.setUrlMedia(offreCoachMedia.getUrlMedia());
                }
                if (offreCoachMedia.getTypeMedia() != null) {
                    existingOffreCoachMedia.setTypeMedia(offreCoachMedia.getTypeMedia());
                }

                return existingOffreCoachMedia;
            })
            .map(offreCoachMediaRepository::save);
    }

    /**
     * Get one offreCoachMedia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OffreCoachMedia> findOne(Long id) {
        LOG.debug("Request to get OffreCoachMedia : {}", id);
        return offreCoachMediaRepository.findById(id);
    }

    /**
     * Delete the offreCoachMedia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OffreCoachMedia : {}", id);
        offreCoachMediaRepository.deleteById(id);
    }
}
