package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.Disponibilite;
import fr.coaching.maseance.repository.DisponibiliteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.Disponibilite}.
 */
@Service
@Transactional
public class DisponibiliteService {

    private static final Logger LOG = LoggerFactory.getLogger(DisponibiliteService.class);

    private final DisponibiliteRepository disponibiliteRepository;

    public DisponibiliteService(DisponibiliteRepository disponibiliteRepository) {
        this.disponibiliteRepository = disponibiliteRepository;
    }

    /**
     * Save a disponibilite.
     *
     * @param disponibilite the entity to save.
     * @return the persisted entity.
     */
    public Disponibilite save(Disponibilite disponibilite) {
        LOG.debug("Request to save Disponibilite : {}", disponibilite);
        return disponibiliteRepository.save(disponibilite);
    }

    /**
     * Update a disponibilite.
     *
     * @param disponibilite the entity to save.
     * @return the persisted entity.
     */
    public Disponibilite update(Disponibilite disponibilite) {
        LOG.debug("Request to update Disponibilite : {}", disponibilite);
        return disponibiliteRepository.save(disponibilite);
    }

    /**
     * Partially update a disponibilite.
     *
     * @param disponibilite the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Disponibilite> partialUpdate(Disponibilite disponibilite) {
        LOG.debug("Request to partially update Disponibilite : {}", disponibilite);

        return disponibiliteRepository
            .findById(disponibilite.getId())
            .map(existingDisponibilite -> {
                if (disponibilite.getHeureDebutCreneauxDisponibilite() != null) {
                    existingDisponibilite.setHeureDebutCreneauxDisponibilite(disponibilite.getHeureDebutCreneauxDisponibilite());
                }
                if (disponibilite.getHeureFinCreneauxDisponibilite() != null) {
                    existingDisponibilite.setHeureFinCreneauxDisponibilite(disponibilite.getHeureFinCreneauxDisponibilite());
                }
                if (disponibilite.getCanalSeance() != null) {
                    existingDisponibilite.setCanalSeance(disponibilite.getCanalSeance());
                }

                return existingDisponibilite;
            })
            .map(disponibiliteRepository::save);
    }

    /**
     * Get one disponibilite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Disponibilite> findOne(Long id) {
        LOG.debug("Request to get Disponibilite : {}", id);
        return disponibiliteRepository.findById(id);
    }

    /**
     * Delete the disponibilite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Disponibilite : {}", id);
        disponibiliteRepository.deleteById(id);
    }
}
