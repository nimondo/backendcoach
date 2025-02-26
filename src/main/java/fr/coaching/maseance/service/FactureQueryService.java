package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.*; // for static metamodels
import fr.coaching.maseance.domain.Facture;
import fr.coaching.maseance.repository.FactureRepository;
import fr.coaching.maseance.service.criteria.FactureCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Facture} entities in the database.
 * The main input is a {@link FactureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Facture} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FactureQueryService extends QueryService<Facture> {

    private static final Logger LOG = LoggerFactory.getLogger(FactureQueryService.class);

    private final FactureRepository factureRepository;

    public FactureQueryService(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    /**
     * Return a {@link Page} of {@link Facture} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Facture> findByCriteria(FactureCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Facture> specification = createSpecification(criteria);
        return factureRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FactureCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Facture> specification = createSpecification(criteria);
        return factureRepository.count(specification);
    }

    /**
     * Function to convert {@link FactureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Facture> createSpecification(FactureCriteria criteria) {
        Specification<Facture> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Facture_.id));
            }
            if (criteria.getTypeFacture() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeFacture(), Facture_.typeFacture));
            }
            if (criteria.getDateComptableFacture() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getDateComptableFacture(), Facture_.dateComptableFacture)
                );
            }
            if (criteria.getMontant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontant(), Facture_.montant));
            }
            if (criteria.getTva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTva(), Facture_.tva));
            }
            if (criteria.getPaiementId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaiementId(), root -> root.join(Facture_.paiement, JoinType.LEFT).get(Paiement_.id))
                );
            }
            if (criteria.getSeanceReservationCoachId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSeanceReservationCoachId(), root ->
                        root.join(Facture_.seanceReservationCoach, JoinType.LEFT).get(SeanceReservationCoach_.id)
                    )
                );
            }
        }
        return specification;
    }
}
