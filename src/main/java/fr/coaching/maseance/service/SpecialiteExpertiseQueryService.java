package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.*; // for static metamodels
import fr.coaching.maseance.domain.SpecialiteExpertise;
import fr.coaching.maseance.repository.SpecialiteExpertiseRepository;
import fr.coaching.maseance.service.criteria.SpecialiteExpertiseCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SpecialiteExpertise} entities in the database.
 * The main input is a {@link SpecialiteExpertiseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SpecialiteExpertise} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpecialiteExpertiseQueryService extends QueryService<SpecialiteExpertise> {

    private static final Logger LOG = LoggerFactory.getLogger(SpecialiteExpertiseQueryService.class);

    private final SpecialiteExpertiseRepository specialiteExpertiseRepository;

    public SpecialiteExpertiseQueryService(SpecialiteExpertiseRepository specialiteExpertiseRepository) {
        this.specialiteExpertiseRepository = specialiteExpertiseRepository;
    }

    /**
     * Return a {@link List} of {@link SpecialiteExpertise} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SpecialiteExpertise> findByCriteria(SpecialiteExpertiseCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<SpecialiteExpertise> specification = createSpecification(criteria);
        return specialiteExpertiseRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SpecialiteExpertiseCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SpecialiteExpertise> specification = createSpecification(criteria);
        return specialiteExpertiseRepository.count(specification);
    }

    /**
     * Function to convert {@link SpecialiteExpertiseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SpecialiteExpertise> createSpecification(SpecialiteExpertiseCriteria criteria) {
        Specification<SpecialiteExpertise> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SpecialiteExpertise_.id));
            }
            if (criteria.getSpecialite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpecialite(), SpecialiteExpertise_.specialite));
            }
            if (criteria.getSpecialiteDescription() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getSpecialiteDescription(), SpecialiteExpertise_.specialiteDescription)
                );
            }
            if (criteria.getTarifMoyenHeure() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getTarifMoyenHeure(), SpecialiteExpertise_.tarifMoyenHeure)
                );
            }
            if (criteria.getDureeTarif() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDureeTarif(), SpecialiteExpertise_.dureeTarif));
            }
            if (criteria.getDiplomeRequis() != null) {
                specification = specification.and(buildSpecification(criteria.getDiplomeRequis(), SpecialiteExpertise_.diplomeRequis));
            }
            if (criteria.getUrlPhoto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlPhoto(), SpecialiteExpertise_.urlPhoto));
            }
            if (criteria.getOffreId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOffreId(), root ->
                        root.join(SpecialiteExpertise_.offres, JoinType.LEFT).get(OffreCoach_.id)
                    )
                );
            }
            if (criteria.getDiplomeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDiplomeId(), root ->
                        root.join(SpecialiteExpertise_.diplome, JoinType.LEFT).get(Diplome_.id)
                    )
                );
            }
            if (criteria.getThemeExpertiseId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getThemeExpertiseId(), root ->
                        root.join(SpecialiteExpertise_.themeExpertise, JoinType.LEFT).get(ThemeExpertise_.id)
                    )
                );
            }
            if (criteria.getCoachExpertId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCoachExpertId(), root ->
                        root.join(SpecialiteExpertise_.coachExperts, JoinType.LEFT).get(CoachExpert_.id)
                    )
                );
            }
        }
        return specification;
    }
}
