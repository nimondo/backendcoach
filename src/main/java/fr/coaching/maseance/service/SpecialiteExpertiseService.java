package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.SpecialiteExpertise;
import fr.coaching.maseance.repository.SpecialiteExpertiseRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.SpecialiteExpertise}.
 */
@Service
@Transactional
public class SpecialiteExpertiseService {

    private static final Logger LOG = LoggerFactory.getLogger(SpecialiteExpertiseService.class);

    private final SpecialiteExpertiseRepository specialiteExpertiseRepository;

    public SpecialiteExpertiseService(SpecialiteExpertiseRepository specialiteExpertiseRepository) {
        this.specialiteExpertiseRepository = specialiteExpertiseRepository;
    }

    /**
     * Save a specialiteExpertise.
     *
     * @param specialiteExpertise the entity to save.
     * @return the persisted entity.
     */
    public SpecialiteExpertise save(SpecialiteExpertise specialiteExpertise) {
        LOG.debug("Request to save SpecialiteExpertise : {}", specialiteExpertise);
        return specialiteExpertiseRepository.save(specialiteExpertise);
    }

    /**
     * Update a specialiteExpertise.
     *
     * @param specialiteExpertise the entity to save.
     * @return the persisted entity.
     */
    public SpecialiteExpertise update(SpecialiteExpertise specialiteExpertise) {
        LOG.debug("Request to update SpecialiteExpertise : {}", specialiteExpertise);
        return specialiteExpertiseRepository.save(specialiteExpertise);
    }

    /**
     * Partially update a specialiteExpertise.
     *
     * @param specialiteExpertise the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SpecialiteExpertise> partialUpdate(SpecialiteExpertise specialiteExpertise) {
        LOG.debug("Request to partially update SpecialiteExpertise : {}", specialiteExpertise);

        return specialiteExpertiseRepository
            .findById(specialiteExpertise.getId())
            .map(existingSpecialiteExpertise -> {
                if (specialiteExpertise.getSpecialite() != null) {
                    existingSpecialiteExpertise.setSpecialite(specialiteExpertise.getSpecialite());
                }
                if (specialiteExpertise.getSpecialiteDescription() != null) {
                    existingSpecialiteExpertise.setSpecialiteDescription(specialiteExpertise.getSpecialiteDescription());
                }
                if (specialiteExpertise.getTarifMoyenHeure() != null) {
                    existingSpecialiteExpertise.setTarifMoyenHeure(specialiteExpertise.getTarifMoyenHeure());
                }
                if (specialiteExpertise.getDureeTarif() != null) {
                    existingSpecialiteExpertise.setDureeTarif(specialiteExpertise.getDureeTarif());
                }
                if (specialiteExpertise.getDiplomeRequis() != null) {
                    existingSpecialiteExpertise.setDiplomeRequis(specialiteExpertise.getDiplomeRequis());
                }
                if (specialiteExpertise.getUrlPhoto() != null) {
                    existingSpecialiteExpertise.setUrlPhoto(specialiteExpertise.getUrlPhoto());
                }

                return existingSpecialiteExpertise;
            })
            .map(specialiteExpertiseRepository::save);
    }

    /**
     * Get all the specialiteExpertises with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SpecialiteExpertise> findAllWithEagerRelationships(Pageable pageable) {
        return specialiteExpertiseRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one specialiteExpertise by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SpecialiteExpertise> findOne(Long id) {
        LOG.debug("Request to get SpecialiteExpertise : {}", id);
        return specialiteExpertiseRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the specialiteExpertise by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SpecialiteExpertise : {}", id);
        specialiteExpertiseRepository.deleteById(id);
    }
}
