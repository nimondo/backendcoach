package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.*; // for static metamodels
import fr.coaching.maseance.domain.CoachExpert;
import fr.coaching.maseance.repository.CoachExpertRepository;
import fr.coaching.maseance.service.criteria.CoachExpertCriteria;
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
 * Service for executing complex queries for {@link CoachExpert} entities in the database.
 * The main input is a {@link CoachExpertCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CoachExpert} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CoachExpertQueryService extends QueryService<CoachExpert> {

    private static final Logger LOG = LoggerFactory.getLogger(CoachExpertQueryService.class);

    private final CoachExpertRepository coachExpertRepository;

    public CoachExpertQueryService(CoachExpertRepository coachExpertRepository) {
        this.coachExpertRepository = coachExpertRepository;
    }

    /**
     * Return a {@link Page} of {@link CoachExpert} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CoachExpert> findByCriteria(CoachExpertCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CoachExpert> specification = createSpecification(criteria);
        return coachExpertRepository.fetchBagRelationships(coachExpertRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CoachExpertCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CoachExpert> specification = createSpecification(criteria);
        return coachExpertRepository.count(specification);
    }

    /**
     * Function to convert {@link CoachExpertCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CoachExpert> createSpecification(CoachExpertCriteria criteria) {
        Specification<CoachExpert> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CoachExpert_.id));
            }
            if (criteria.getCivilite() != null) {
                specification = specification.and(buildSpecification(criteria.getCivilite(), CoachExpert_.civilite));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), CoachExpert_.nom));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), CoachExpert_.prenom));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissance(), CoachExpert_.dateNaissance));
            }
            if (criteria.getAdresseEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdresseEmail(), CoachExpert_.adresseEmail));
            }
            if (criteria.getNumeroTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroTelephone(), CoachExpert_.numeroTelephone));
            }
            if (criteria.getVille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVille(), CoachExpert_.ville));
            }
            if (criteria.getCodePostal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCodePostal(), CoachExpert_.codePostal));
            }
            if (criteria.getNumeroEtNomVoie() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroEtNomVoie(), CoachExpert_.numeroEtNomVoie));
            }
            if (criteria.getTarifActuel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTarifActuel(), CoachExpert_.tarifActuel));
            }
            if (criteria.getFormatProposeSeance() != null) {
                specification = specification.and(buildSpecification(criteria.getFormatProposeSeance(), CoachExpert_.formatProposeSeance));
            }
            if (criteria.getUrlPhotoProfil() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlPhotoProfil(), CoachExpert_.urlPhotoProfil));
            }
            if (criteria.getBio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBio(), CoachExpert_.bio));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(CoachExpert_.user, JoinType.LEFT).get(User_.id))
                );
            }
            if (criteria.getLesAvisClientId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLesAvisClientId(), root ->
                        root.join(CoachExpert_.lesAvisClients, JoinType.LEFT).get(AvisClient_.id)
                    )
                );
            }
            if (criteria.getDisponibiliteId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDisponibiliteId(), root ->
                        root.join(CoachExpert_.disponibilites, JoinType.LEFT).get(Disponibilite_.id)
                    )
                );
            }
            if (criteria.getOffreId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOffreId(), root -> root.join(CoachExpert_.offres, JoinType.LEFT).get(OffreCoach_.id))
                );
            }
            if (criteria.getSpecialiteExpertiseId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSpecialiteExpertiseId(), root ->
                        root.join(CoachExpert_.specialiteExpertises, JoinType.LEFT).get(SpecialiteExpertise_.id)
                    )
                );
            }
            if (criteria.getDiplomeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDiplomeId(), root -> root.join(CoachExpert_.diplomes, JoinType.LEFT).get(Diplome_.id))
                );
            }
        }
        return specification;
    }
}
