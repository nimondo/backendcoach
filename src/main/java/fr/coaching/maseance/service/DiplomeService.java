package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.Diplome;
import fr.coaching.maseance.repository.DiplomeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.Diplome}.
 */
@Service
@Transactional
public class DiplomeService {

    private static final Logger LOG = LoggerFactory.getLogger(DiplomeService.class);

    private final DiplomeRepository diplomeRepository;

    public DiplomeService(DiplomeRepository diplomeRepository) {
        this.diplomeRepository = diplomeRepository;
    }

    /**
     * Save a diplome.
     *
     * @param diplome the entity to save.
     * @return the persisted entity.
     */
    public Diplome save(Diplome diplome) {
        LOG.debug("Request to save Diplome : {}", diplome);
        return diplomeRepository.save(diplome);
    }

    /**
     * Update a diplome.
     *
     * @param diplome the entity to save.
     * @return the persisted entity.
     */
    public Diplome update(Diplome diplome) {
        LOG.debug("Request to update Diplome : {}", diplome);
        return diplomeRepository.save(diplome);
    }

    /**
     * Partially update a diplome.
     *
     * @param diplome the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Diplome> partialUpdate(Diplome diplome) {
        LOG.debug("Request to partially update Diplome : {}", diplome);

        return diplomeRepository
            .findById(diplome.getId())
            .map(existingDiplome -> {
                if (diplome.getLibelle() != null) {
                    existingDiplome.setLibelle(diplome.getLibelle());
                }
                if (diplome.getNbAnneesEtudePostBac() != null) {
                    existingDiplome.setNbAnneesEtudePostBac(diplome.getNbAnneesEtudePostBac());
                }

                return existingDiplome;
            })
            .map(diplomeRepository::save);
    }

    /**
     * Get all the diplomes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Diplome> findAll() {
        LOG.debug("Request to get all Diplomes");
        return diplomeRepository.findAll();
    }

    /**
     * Get one diplome by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Diplome> findOne(Long id) {
        LOG.debug("Request to get Diplome : {}", id);
        return diplomeRepository.findById(id);
    }

    /**
     * Delete the diplome by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Diplome : {}", id);
        diplomeRepository.deleteById(id);
    }
}
