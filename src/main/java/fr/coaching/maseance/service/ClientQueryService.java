package fr.coaching.maseance.service;

import fr.coaching.maseance.domain.*; // for static metamodels
import fr.coaching.maseance.domain.Client;
import fr.coaching.maseance.repository.ClientRepository;
import fr.coaching.maseance.service.criteria.ClientCriteria;
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
 * Service for executing complex queries for {@link Client} entities in the database.
 * The main input is a {@link ClientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Client} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClientQueryService extends QueryService<Client> {

    private static final Logger LOG = LoggerFactory.getLogger(ClientQueryService.class);

    private final ClientRepository clientRepository;

    public ClientQueryService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Return a {@link Page} of {@link Client} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Client> findByCriteria(ClientCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Client> specification = createSpecification(criteria);
        return clientRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClientCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Client> specification = createSpecification(criteria);
        return clientRepository.count(specification);
    }

    /**
     * Function to convert {@link ClientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Client> createSpecification(ClientCriteria criteria) {
        Specification<Client> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Client_.id));
            }
            if (criteria.getGenre() != null) {
                specification = specification.and(buildSpecification(criteria.getGenre(), Client_.genre));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Client_.nom));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Client_.prenom));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissance(), Client_.dateNaissance));
            }
            if (criteria.getAdresseEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdresseEmail(), Client_.adresseEmail));
            }
            if (criteria.getNumeroTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroTelephone(), Client_.numeroTelephone));
            }
            if (criteria.getVille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVille(), Client_.ville));
            }
            if (criteria.getCodePostal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCodePostal(), Client_.codePostal));
            }
            if (criteria.getNumeroEtNomVoie() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroEtNomVoie(), Client_.numeroEtNomVoie));
            }
            if (criteria.getPreferenceCanalSeance() != null) {
                specification = specification.and(buildSpecification(criteria.getPreferenceCanalSeance(), Client_.preferenceCanalSeance));
            }
            if (criteria.getUrlPhotoProfil() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlPhotoProfil(), Client_.urlPhotoProfil));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(Client_.user, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
