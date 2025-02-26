package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.*; // for static metamodels
import fr.coaching.maseance.domain.OffreCoachMedia;
import fr.coaching.maseance.repository.OffreCoachMediaRepository;
import fr.coaching.maseance.service.criteria.OffreCoachMediaCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OffreCoachMedia} entities in the database.
 * The main input is a {@link OffreCoachMediaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OffreCoachMedia} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OffreCoachMediaQueryService extends QueryService<OffreCoachMedia> {

    private static final Logger LOG = LoggerFactory.getLogger(OffreCoachMediaQueryService.class);

    private final OffreCoachMediaRepository offreCoachMediaRepository;

    public OffreCoachMediaQueryService(OffreCoachMediaRepository offreCoachMediaRepository) {
        this.offreCoachMediaRepository = offreCoachMediaRepository;
    }

    /**
     * Return a {@link List} of {@link OffreCoachMedia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OffreCoachMedia> findByCriteria(OffreCoachMediaCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<OffreCoachMedia> specification = createSpecification(criteria);
        return offreCoachMediaRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OffreCoachMediaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OffreCoachMedia> specification = createSpecification(criteria);
        return offreCoachMediaRepository.count(specification);
    }

    /**
     * Function to convert {@link OffreCoachMediaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OffreCoachMedia> createSpecification(OffreCoachMediaCriteria criteria) {
        Specification<OffreCoachMedia> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OffreCoachMedia_.id));
            }
            if (criteria.getUrlMedia() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlMedia(), OffreCoachMedia_.urlMedia));
            }
            if (criteria.getTypeMedia() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeMedia(), OffreCoachMedia_.typeMedia));
            }
            if (criteria.getOffreCoachId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOffreCoachId(), root ->
                        root.join(OffreCoachMedia_.offreCoach, JoinType.LEFT).get(OffreCoach_.id)
                    )
                );
            }
        }
        return specification;
    }
}
