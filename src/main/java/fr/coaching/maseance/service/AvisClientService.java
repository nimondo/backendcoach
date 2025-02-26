package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.AvisClient;
import fr.coaching.maseance.repository.AvisClientRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.AvisClient}.
 */
@Service
@Transactional
public class AvisClientService {

    private static final Logger LOG = LoggerFactory.getLogger(AvisClientService.class);

    private final AvisClientRepository avisClientRepository;

    public AvisClientService(AvisClientRepository avisClientRepository) {
        this.avisClientRepository = avisClientRepository;
    }

    /**
     * Save a avisClient.
     *
     * @param avisClient the entity to save.
     * @return the persisted entity.
     */
    public AvisClient save(AvisClient avisClient) {
        LOG.debug("Request to save AvisClient : {}", avisClient);
        return avisClientRepository.save(avisClient);
    }

    /**
     * Update a avisClient.
     *
     * @param avisClient the entity to save.
     * @return the persisted entity.
     */
    public AvisClient update(AvisClient avisClient) {
        LOG.debug("Request to update AvisClient : {}", avisClient);
        return avisClientRepository.save(avisClient);
    }

    /**
     * Partially update a avisClient.
     *
     * @param avisClient the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AvisClient> partialUpdate(AvisClient avisClient) {
        LOG.debug("Request to partially update AvisClient : {}", avisClient);

        return avisClientRepository
            .findById(avisClient.getId())
            .map(existingAvisClient -> {
                if (avisClient.getDateAvis() != null) {
                    existingAvisClient.setDateAvis(avisClient.getDateAvis());
                }
                if (avisClient.getNote() != null) {
                    existingAvisClient.setNote(avisClient.getNote());
                }
                if (avisClient.getDescriptionAvis() != null) {
                    existingAvisClient.setDescriptionAvis(avisClient.getDescriptionAvis());
                }

                return existingAvisClient;
            })
            .map(avisClientRepository::save);
    }

    /**
     * Get all the avisClients with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AvisClient> findAllWithEagerRelationships(Pageable pageable) {
        return avisClientRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one avisClient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AvisClient> findOne(Long id) {
        LOG.debug("Request to get AvisClient : {}", id);
        return avisClientRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the avisClient by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AvisClient : {}", id);
        avisClientRepository.deleteById(id);
    }
}
