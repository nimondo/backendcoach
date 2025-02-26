package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.*; // for static metamodels
import fr.coaching.maseance.domain.Disponibilite;
import fr.coaching.maseance.repository.DisponibiliteRepository;
import fr.coaching.maseance.service.criteria.DisponibiliteCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Disponibilite} entities in the database.
 * The main input is a {@link DisponibiliteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Disponibilite} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DisponibiliteQueryService extends QueryService<Disponibilite> {

    private static final Logger LOG = LoggerFactory.getLogger(DisponibiliteQueryService.class);

    private final DisponibiliteRepository disponibiliteRepository;

    public DisponibiliteQueryService(DisponibiliteRepository disponibiliteRepository) {
        this.disponibiliteRepository = disponibiliteRepository;
    }

    /**
     * Return a {@link List} of {@link Disponibilite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Disponibilite> findByCriteria(DisponibiliteCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Disponibilite> specification = createSpecification(criteria);
        return disponibiliteRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DisponibiliteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Disponibilite> specification = createSpecification(criteria);
        return disponibiliteRepository.count(specification);
    }

    /**
     * Function to convert {@link DisponibiliteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Disponibilite> createSpecification(DisponibiliteCriteria criteria) {
        Specification<Disponibilite> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Disponibilite_.id));
            }
            if (criteria.getHeureDebutCreneauxDisponibilite() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getHeureDebutCreneauxDisponibilite(), Disponibilite_.heureDebutCreneauxDisponibilite)
                );
            }
            if (criteria.getHeureFinCreneauxDisponibilite() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getHeureFinCreneauxDisponibilite(), Disponibilite_.heureFinCreneauxDisponibilite)
                );
            }
            if (criteria.getCanalSeance() != null) {
                specification = specification.and(buildSpecification(criteria.getCanalSeance(), Disponibilite_.canalSeance));
            }
            if (criteria.getCoachExpertId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCoachExpertId(), root ->
                        root.join(Disponibilite_.coachExpert, JoinType.LEFT).get(CoachExpert_.id)
                    )
                );
            }
        }
        return specification;
    }
}
