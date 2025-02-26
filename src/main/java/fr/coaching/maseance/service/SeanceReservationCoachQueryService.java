package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.*; // for static metamodels
import fr.coaching.maseance.domain.SeanceReservationCoach;
import fr.coaching.maseance.repository.SeanceReservationCoachRepository;
import fr.coaching.maseance.service.criteria.SeanceReservationCoachCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SeanceReservationCoach} entities in the database.
 * The main input is a {@link SeanceReservationCoachCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SeanceReservationCoach} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SeanceReservationCoachQueryService extends QueryService<SeanceReservationCoach> {

    private static final Logger LOG = LoggerFactory.getLogger(SeanceReservationCoachQueryService.class);

    private final SeanceReservationCoachRepository seanceReservationCoachRepository;

    public SeanceReservationCoachQueryService(SeanceReservationCoachRepository seanceReservationCoachRepository) {
        this.seanceReservationCoachRepository = seanceReservationCoachRepository;
    }

    /**
     * Return a {@link List} of {@link SeanceReservationCoach} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SeanceReservationCoach> findByCriteria(SeanceReservationCoachCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<SeanceReservationCoach> specification = createSpecification(criteria);
        return seanceReservationCoachRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SeanceReservationCoachCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SeanceReservationCoach> specification = createSpecification(criteria);
        return seanceReservationCoachRepository.count(specification);
    }

    /**
     * Function to convert {@link SeanceReservationCoachCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SeanceReservationCoach> createSpecification(SeanceReservationCoachCriteria criteria) {
        Specification<SeanceReservationCoach> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SeanceReservationCoach_.id));
            }
            if (criteria.getHeureDebutCreneauReserve() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getHeureDebutCreneauReserve(), SeanceReservationCoach_.heureDebutCreneauReserve)
                );
            }
            if (criteria.getHeureFinCreneauReserve() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getHeureFinCreneauReserve(), SeanceReservationCoach_.heureFinCreneauReserve)
                );
            }
            if (criteria.getCanalSeance() != null) {
                specification = specification.and(buildSpecification(criteria.getCanalSeance(), SeanceReservationCoach_.canalSeance));
            }
            if (criteria.getTypeSeance() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeSeance(), SeanceReservationCoach_.typeSeance));
            }
            if (criteria.getStatutRealisation() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getStatutRealisation(), SeanceReservationCoach_.statutRealisation)
                );
            }
            if (criteria.getFactureId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getFactureId(), root ->
                        root.join(SeanceReservationCoach_.facture, JoinType.LEFT).get(Facture_.id)
                    )
                );
            }
            if (criteria.getCoachExpertId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCoachExpertId(), root ->
                        root.join(SeanceReservationCoach_.coachExpert, JoinType.LEFT).get(CoachExpert_.id)
                    )
                );
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClientId(), root ->
                        root.join(SeanceReservationCoach_.client, JoinType.LEFT).get(Client_.id)
                    )
                );
            }
            if (criteria.getOffreId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOffreId(), root ->
                        root.join(SeanceReservationCoach_.offre, JoinType.LEFT).get(OffreCoach_.id)
                    )
                );
            }
        }
        return specification;
    }
}
