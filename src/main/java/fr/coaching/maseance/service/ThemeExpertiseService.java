package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.ThemeExpertise;
import fr.coaching.maseance.repository.ThemeExpertiseRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.ThemeExpertise}.
 */
@Service
@Transactional
public class ThemeExpertiseService {

    private static final Logger LOG = LoggerFactory.getLogger(ThemeExpertiseService.class);

    private final ThemeExpertiseRepository themeExpertiseRepository;

    public ThemeExpertiseService(ThemeExpertiseRepository themeExpertiseRepository) {
        this.themeExpertiseRepository = themeExpertiseRepository;
    }

    /**
     * Save a themeExpertise.
     *
     * @param themeExpertise the entity to save.
     * @return the persisted entity.
     */
    public ThemeExpertise save(ThemeExpertise themeExpertise) {
        LOG.debug("Request to save ThemeExpertise : {}", themeExpertise);
        return themeExpertiseRepository.save(themeExpertise);
    }

    /**
     * Update a themeExpertise.
     *
     * @param themeExpertise the entity to save.
     * @return the persisted entity.
     */
    public ThemeExpertise update(ThemeExpertise themeExpertise) {
        LOG.debug("Request to update ThemeExpertise : {}", themeExpertise);
        return themeExpertiseRepository.save(themeExpertise);
    }

    /**
     * Partially update a themeExpertise.
     *
     * @param themeExpertise the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ThemeExpertise> partialUpdate(ThemeExpertise themeExpertise) {
        LOG.debug("Request to partially update ThemeExpertise : {}", themeExpertise);

        return themeExpertiseRepository
            .findById(themeExpertise.getId())
            .map(existingThemeExpertise -> {
                if (themeExpertise.getLibelleExpertise() != null) {
                    existingThemeExpertise.setLibelleExpertise(themeExpertise.getLibelleExpertise());
                }
                if (themeExpertise.getUrlPhoto() != null) {
                    existingThemeExpertise.setUrlPhoto(themeExpertise.getUrlPhoto());
                }

                return existingThemeExpertise;
            })
            .map(themeExpertiseRepository::save);
    }

    /**
     * Get one themeExpertise by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ThemeExpertise> findOne(Long id) {
        LOG.debug("Request to get ThemeExpertise : {}", id);
        return themeExpertiseRepository.findById(id);
    }

    /**
     * Delete the themeExpertise by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ThemeExpertise : {}", id);
        themeExpertiseRepository.deleteById(id);
    }
}
