package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.Paiement;
import fr.coaching.maseance.repository.PaiementRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.Paiement}.
 */
@Service
@Transactional
public class PaiementService {

    private static final Logger LOG = LoggerFactory.getLogger(PaiementService.class);

    private final PaiementRepository paiementRepository;

    public PaiementService(PaiementRepository paiementRepository) {
        this.paiementRepository = paiementRepository;
    }

    /**
     * Save a paiement.
     *
     * @param paiement the entity to save.
     * @return the persisted entity.
     */
    public Paiement save(Paiement paiement) {
        LOG.debug("Request to save Paiement : {}", paiement);
        return paiementRepository.save(paiement);
    }

    /**
     * Update a paiement.
     *
     * @param paiement the entity to save.
     * @return the persisted entity.
     */
    public Paiement update(Paiement paiement) {
        LOG.debug("Request to update Paiement : {}", paiement);
        return paiementRepository.save(paiement);
    }

    /**
     * Partially update a paiement.
     *
     * @param paiement the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Paiement> partialUpdate(Paiement paiement) {
        LOG.debug("Request to partially update Paiement : {}", paiement);

        return paiementRepository
            .findById(paiement.getId())
            .map(existingPaiement -> {
                if (paiement.getHorodatage() != null) {
                    existingPaiement.setHorodatage(paiement.getHorodatage());
                }
                if (paiement.getMoyenPaiement() != null) {
                    existingPaiement.setMoyenPaiement(paiement.getMoyenPaiement());
                }
                if (paiement.getStatutPaiement() != null) {
                    existingPaiement.setStatutPaiement(paiement.getStatutPaiement());
                }
                if (paiement.getIdStripe() != null) {
                    existingPaiement.setIdStripe(paiement.getIdStripe());
                }

                return existingPaiement;
            })
            .map(paiementRepository::save);
    }

    /**
     *  Get all the paiements where Facture is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Paiement> findAllWhereFactureIsNull() {
        LOG.debug("Request to get all paiements where Facture is null");
        return StreamSupport.stream(paiementRepository.findAll().spliterator(), false)
            .filter(paiement -> paiement.getFacture() == null)
            .toList();
    }

    /**
     * Get one paiement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Paiement> findOne(Long id) {
        LOG.debug("Request to get Paiement : {}", id);
        return paiementRepository.findById(id);
    }

    /**
     * Delete the paiement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Paiement : {}", id);
        paiementRepository.deleteById(id);
    }
}
