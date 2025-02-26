package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.*; // for static metamodels
import fr.coaching.maseance.domain.ThemeExpertise;
import fr.coaching.maseance.repository.ThemeExpertiseRepository;
import fr.coaching.maseance.service.criteria.ThemeExpertiseCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ThemeExpertise} entities in the database.
 * The main input is a {@link ThemeExpertiseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ThemeExpertise} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ThemeExpertiseQueryService extends QueryService<ThemeExpertise> {

    private static final Logger LOG = LoggerFactory.getLogger(ThemeExpertiseQueryService.class);

    private final ThemeExpertiseRepository themeExpertiseRepository;

    public ThemeExpertiseQueryService(ThemeExpertiseRepository themeExpertiseRepository) {
        this.themeExpertiseRepository = themeExpertiseRepository;
    }

    /**
     * Return a {@link List} of {@link ThemeExpertise} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ThemeExpertise> findByCriteria(ThemeExpertiseCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ThemeExpertise> specification = createSpecification(criteria);
        return themeExpertiseRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ThemeExpertiseCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ThemeExpertise> specification = createSpecification(criteria);
        return themeExpertiseRepository.count(specification);
    }

    /**
     * Function to convert {@link ThemeExpertiseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ThemeExpertise> createSpecification(ThemeExpertiseCriteria criteria) {
        Specification<ThemeExpertise> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ThemeExpertise_.id));
            }
            if (criteria.getLibelleExpertise() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getLibelleExpertise(), ThemeExpertise_.libelleExpertise)
                );
            }
            if (criteria.getUrlPhoto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlPhoto(), ThemeExpertise_.urlPhoto));
            }
        }
        return specification;
    }
}
