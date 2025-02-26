package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.SeanceReservationCoach;
import fr.coaching.maseance.repository.SeanceReservationCoachRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.SeanceReservationCoach}.
 */
@Service
@Transactional
public class SeanceReservationCoachService {

    private static final Logger LOG = LoggerFactory.getLogger(SeanceReservationCoachService.class);

    private final SeanceReservationCoachRepository seanceReservationCoachRepository;

    public SeanceReservationCoachService(SeanceReservationCoachRepository seanceReservationCoachRepository) {
        this.seanceReservationCoachRepository = seanceReservationCoachRepository;
    }

    /**
     * Save a seanceReservationCoach.
     *
     * @param seanceReservationCoach the entity to save.
     * @return the persisted entity.
     */
    public SeanceReservationCoach save(SeanceReservationCoach seanceReservationCoach) {
        LOG.debug("Request to save SeanceReservationCoach : {}", seanceReservationCoach);
        return seanceReservationCoachRepository.save(seanceReservationCoach);
    }

    /**
     * Update a seanceReservationCoach.
     *
     * @param seanceReservationCoach the entity to save.
     * @return the persisted entity.
     */
    public SeanceReservationCoach update(SeanceReservationCoach seanceReservationCoach) {
        LOG.debug("Request to update SeanceReservationCoach : {}", seanceReservationCoach);
        return seanceReservationCoachRepository.save(seanceReservationCoach);
    }

    /**
     * Partially update a seanceReservationCoach.
     *
     * @param seanceReservationCoach the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SeanceReservationCoach> partialUpdate(SeanceReservationCoach seanceReservationCoach) {
        LOG.debug("Request to partially update SeanceReservationCoach : {}", seanceReservationCoach);

        return seanceReservationCoachRepository
            .findById(seanceReservationCoach.getId())
            .map(existingSeanceReservationCoach -> {
                if (seanceReservationCoach.getHeureDebutCreneauReserve() != null) {
                    existingSeanceReservationCoach.setHeureDebutCreneauReserve(seanceReservationCoach.getHeureDebutCreneauReserve());
                }
                if (seanceReservationCoach.getHeureFinCreneauReserve() != null) {
                    existingSeanceReservationCoach.setHeureFinCreneauReserve(seanceReservationCoach.getHeureFinCreneauReserve());
                }
                if (seanceReservationCoach.getCanalSeance() != null) {
                    existingSeanceReservationCoach.setCanalSeance(seanceReservationCoach.getCanalSeance());
                }
                if (seanceReservationCoach.getTypeSeance() != null) {
                    existingSeanceReservationCoach.setTypeSeance(seanceReservationCoach.getTypeSeance());
                }
                if (seanceReservationCoach.getStatutRealisation() != null) {
                    existingSeanceReservationCoach.setStatutRealisation(seanceReservationCoach.getStatutRealisation());
                }

                return existingSeanceReservationCoach;
            })
            .map(seanceReservationCoachRepository::save);
    }

    /**
     * Get all the seanceReservationCoaches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SeanceReservationCoach> findAllWithEagerRelationships(Pageable pageable) {
        return seanceReservationCoachRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one seanceReservationCoach by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SeanceReservationCoach> findOne(Long id) {
        LOG.debug("Request to get SeanceReservationCoach : {}", id);
        return seanceReservationCoachRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the seanceReservationCoach by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SeanceReservationCoach : {}", id);
        seanceReservationCoachRepository.deleteById(id);
    }
}
