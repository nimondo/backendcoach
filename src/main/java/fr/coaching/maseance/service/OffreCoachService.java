package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.OffreCoach;
import fr.coaching.maseance.repository.OffreCoachRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.OffreCoach}.
 */
@Service
@Transactional
public class OffreCoachService {

    private static final Logger LOG = LoggerFactory.getLogger(OffreCoachService.class);

    private final OffreCoachRepository offreCoachRepository;

    public OffreCoachService(OffreCoachRepository offreCoachRepository) {
        this.offreCoachRepository = offreCoachRepository;
    }

    /**
     * Save a offreCoach.
     *
     * @param offreCoach the entity to save.
     * @return the persisted entity.
     */
    public OffreCoach save(OffreCoach offreCoach) {
        LOG.debug("Request to save OffreCoach : {}", offreCoach);
        return offreCoachRepository.save(offreCoach);
    }

    /**
     * Update a offreCoach.
     *
     * @param offreCoach the entity to save.
     * @return the persisted entity.
     */
    public OffreCoach update(OffreCoach offreCoach) {
        LOG.debug("Request to update OffreCoach : {}", offreCoach);
        return offreCoachRepository.save(offreCoach);
    }

    /**
     * Partially update a offreCoach.
     *
     * @param offreCoach the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OffreCoach> partialUpdate(OffreCoach offreCoach) {
        LOG.debug("Request to partially update OffreCoach : {}", offreCoach);

        return offreCoachRepository
            .findById(offreCoach.getId())
            .map(existingOffreCoach -> {
                if (offreCoach.getCanalSeance() != null) {
                    existingOffreCoach.setCanalSeance(offreCoach.getCanalSeance());
                }
                if (offreCoach.getTypeSeance() != null) {
                    existingOffreCoach.setTypeSeance(offreCoach.getTypeSeance());
                }
                if (offreCoach.getSynthese() != null) {
                    existingOffreCoach.setSynthese(offreCoach.getSynthese());
                }
                if (offreCoach.getDescriptionDetaillee() != null) {
                    existingOffreCoach.setDescriptionDetaillee(offreCoach.getDescriptionDetaillee());
                }
                if (offreCoach.getTarif() != null) {
                    existingOffreCoach.setTarif(offreCoach.getTarif());
                }
                if (offreCoach.getDuree() != null) {
                    existingOffreCoach.setDuree(offreCoach.getDuree());
                }
                if (offreCoach.getDescriptionDiplome() != null) {
                    existingOffreCoach.setDescriptionDiplome(offreCoach.getDescriptionDiplome());
                }

                return existingOffreCoach;
            })
            .map(offreCoachRepository::save);
    }

    /**
     * Get one offreCoach by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OffreCoach> findOne(Long id) {
        LOG.debug("Request to get OffreCoach : {}", id);
        return offreCoachRepository.findById(id);
    }

    /**
     * Delete the offreCoach by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OffreCoach : {}", id);
        offreCoachRepository.deleteById(id);
    }
}
