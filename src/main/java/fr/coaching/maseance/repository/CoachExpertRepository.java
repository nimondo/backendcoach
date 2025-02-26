package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.CoachExpert;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CoachExpert entity.
 *
 * When extending this class, extend CoachExpertRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CoachExpertRepository
    extends CoachExpertRepositoryWithBagRelationships, JpaRepository<CoachExpert, Long>, JpaSpecificationExecutor<CoachExpert> {
    default Optional<CoachExpert> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<CoachExpert> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<CoachExpert> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
