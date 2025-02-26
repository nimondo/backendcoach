package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.*; // for static metamodels
import fr.coaching.maseance.domain.Paiement;
import fr.coaching.maseance.repository.PaiementRepository;
import fr.coaching.maseance.service.criteria.PaiementCriteria;
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
 * Service for executing complex queries for {@link Paiement} entities in the database.
 * The main input is a {@link PaiementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Paiement} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaiementQueryService extends QueryService<Paiement> {

    private static final Logger LOG = LoggerFactory.getLogger(PaiementQueryService.class);

    private final PaiementRepository paiementRepository;

    public PaiementQueryService(PaiementRepository paiementRepository) {
        this.paiementRepository = paiementRepository;
    }

    /**
     * Return a {@link Page} of {@link Paiement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Paiement> findByCriteria(PaiementCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Paiement> specification = createSpecification(criteria);
        return paiementRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaiementCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Paiement> specification = createSpecification(criteria);
        return paiementRepository.count(specification);
    }

    /**
     * Function to convert {@link PaiementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Paiement> createSpecification(PaiementCriteria criteria) {
        Specification<Paiement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Paiement_.id));
            }
            if (criteria.getHorodatage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHorodatage(), Paiement_.horodatage));
            }
            if (criteria.getMoyenPaiement() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMoyenPaiement(), Paiement_.moyenPaiement));
            }
            if (criteria.getStatutPaiement() != null) {
                specification = specification.and(buildSpecification(criteria.getStatutPaiement(), Paiement_.statutPaiement));
            }
            if (criteria.getIdStripe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdStripe(), Paiement_.idStripe));
            }
            if (criteria.getFactureId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getFactureId(), root -> root.join(Paiement_.facture, JoinType.LEFT).get(Facture_.id))
                );
            }
        }
        return specification;
    }
}
