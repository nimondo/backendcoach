package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.*; // for static metamodels
import fr.coaching.maseance.domain.OffreCoach;
import fr.coaching.maseance.repository.OffreCoachRepository;
import fr.coaching.maseance.service.criteria.OffreCoachCriteria;
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
 * Service for executing complex queries for {@link OffreCoach} entities in the database.
 * The main input is a {@link OffreCoachCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OffreCoach} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OffreCoachQueryService extends QueryService<OffreCoach> {

    private static final Logger LOG = LoggerFactory.getLogger(OffreCoachQueryService.class);

    private final OffreCoachRepository offreCoachRepository;

    public OffreCoachQueryService(OffreCoachRepository offreCoachRepository) {
        this.offreCoachRepository = offreCoachRepository;
    }

    /**
     * Return a {@link Page} of {@link OffreCoach} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OffreCoach> findByCriteria(OffreCoachCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OffreCoach> specification = createSpecification(criteria);
        return offreCoachRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OffreCoachCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OffreCoach> specification = createSpecification(criteria);
        return offreCoachRepository.count(specification);
    }

    /**
     * Function to convert {@link OffreCoachCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OffreCoach> createSpecification(OffreCoachCriteria criteria) {
        Specification<OffreCoach> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OffreCoach_.id));
            }
            if (criteria.getCanalSeance() != null) {
                specification = specification.and(buildSpecification(criteria.getCanalSeance(), OffreCoach_.canalSeance));
            }
            if (criteria.getTypeSeance() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeSeance(), OffreCoach_.typeSeance));
            }
            if (criteria.getSynthese() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSynthese(), OffreCoach_.synthese));
            }
            if (criteria.getDescriptionDetaillee() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getDescriptionDetaillee(), OffreCoach_.descriptionDetaillee)
                );
            }
            if (criteria.getTarif() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTarif(), OffreCoach_.tarif));
            }
            if (criteria.getDuree() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuree(), OffreCoach_.duree));
            }
            if (criteria.getDescriptionDiplome() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getDescriptionDiplome(), OffreCoach_.descriptionDiplome)
                );
            }
            if (criteria.getMediaId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMediaId(), root -> root.join(OffreCoach_.media, JoinType.LEFT).get(OffreCoachMedia_.id))
                );
            }
            if (criteria.getSpecialiteId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSpecialiteId(), root ->
                        root.join(OffreCoach_.specialite, JoinType.LEFT).get(SpecialiteExpertise_.id)
                    )
                );
            }
            if (criteria.getCoachExpertId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCoachExpertId(), root ->
                        root.join(OffreCoach_.coachExpert, JoinType.LEFT).get(CoachExpert_.id)
                    )
                );
            }
        }
        return specification;
    }
}
