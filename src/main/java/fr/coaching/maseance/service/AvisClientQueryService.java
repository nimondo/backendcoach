package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.*; // for static metamodels
import fr.coaching.maseance.domain.AvisClient;
import fr.coaching.maseance.repository.AvisClientRepository;
import fr.coaching.maseance.service.criteria.AvisClientCriteria;
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
 * Service for executing complex queries for {@link AvisClient} entities in the database.
 * The main input is a {@link AvisClientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AvisClient} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AvisClientQueryService extends QueryService<AvisClient> {

    private static final Logger LOG = LoggerFactory.getLogger(AvisClientQueryService.class);

    private final AvisClientRepository avisClientRepository;

    public AvisClientQueryService(AvisClientRepository avisClientRepository) {
        this.avisClientRepository = avisClientRepository;
    }

    /**
     * Return a {@link Page} of {@link AvisClient} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AvisClient> findByCriteria(AvisClientCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AvisClient> specification = createSpecification(criteria);
        return avisClientRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AvisClientCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AvisClient> specification = createSpecification(criteria);
        return avisClientRepository.count(specification);
    }

    /**
     * Function to convert {@link AvisClientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AvisClient> createSpecification(AvisClientCriteria criteria) {
        Specification<AvisClient> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AvisClient_.id));
            }
            if (criteria.getDateAvis() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateAvis(), AvisClient_.dateAvis));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNote(), AvisClient_.note));
            }
            if (criteria.getDescriptionAvis() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescriptionAvis(), AvisClient_.descriptionAvis));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClientId(), root -> root.join(AvisClient_.client, JoinType.LEFT).get(Client_.id))
                );
            }
            if (criteria.getCoachExpertId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCoachExpertId(), root ->
                        root.join(AvisClient_.coachExpert, JoinType.LEFT).get(CoachExpert_.id)
                    )
                );
            }
        }
        return specification;
    }
}
