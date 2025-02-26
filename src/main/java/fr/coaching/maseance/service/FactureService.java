package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.Facture;
import fr.coaching.maseance.repository.FactureRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.coaching.maseance.domain.Facture}.
 */
@Service
@Transactional
public class FactureService {

    private static final Logger LOG = LoggerFactory.getLogger(FactureService.class);

    private final FactureRepository factureRepository;

    public FactureService(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    /**
     * Save a facture.
     *
     * @param facture the entity to save.
     * @return the persisted entity.
     */
    public Facture save(Facture facture) {
        LOG.debug("Request to save Facture : {}", facture);
        return factureRepository.save(facture);
    }

    /**
     * Update a facture.
     *
     * @param facture the entity to save.
     * @return the persisted entity.
     */
    public Facture update(Facture facture) {
        LOG.debug("Request to update Facture : {}", facture);
        return factureRepository.save(facture);
    }

    /**
     * Partially update a facture.
     *
     * @param facture the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Facture> partialUpdate(Facture facture) {
        LOG.debug("Request to partially update Facture : {}", facture);

        return factureRepository
            .findById(facture.getId())
            .map(existingFacture -> {
                if (facture.getTypeFacture() != null) {
                    existingFacture.setTypeFacture(facture.getTypeFacture());
                }
                if (facture.getDateComptableFacture() != null) {
                    existingFacture.setDateComptableFacture(facture.getDateComptableFacture());
                }
                if (facture.getMontant() != null) {
                    existingFacture.setMontant(facture.getMontant());
                }
                if (facture.getTva() != null) {
                    existingFacture.setTva(facture.getTva());
                }

                return existingFacture;
            })
            .map(factureRepository::save);
    }

    /**
     * Get all the factures with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Facture> findAllWithEagerRelationships(Pageable pageable) {
        return factureRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the factures where SeanceReservationCoach is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Facture> findAllWhereSeanceReservationCoachIsNull() {
        LOG.debug("Request to get all factures where SeanceReservationCoach is null");
        return StreamSupport.stream(factureRepository.findAll().spliterator(), false)
            .filter(facture -> facture.getSeanceReservationCoach() == null)
            .toList();
    }

    /**
     * Get one facture by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Facture> findOne(Long id) {
        LOG.debug("Request to get Facture : {}", id);
        return factureRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the facture by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Facture : {}", id);
        factureRepository.deleteById(id);
    }
}
