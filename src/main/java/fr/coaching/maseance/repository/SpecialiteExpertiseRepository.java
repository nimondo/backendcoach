package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.SpecialiteExpertise;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SpecialiteExpertise entity.
 */
@Repository
public interface SpecialiteExpertiseRepository
    extends JpaRepository<SpecialiteExpertise, Long>, JpaSpecificationExecutor<SpecialiteExpertise> {
    default Optional<SpecialiteExpertise> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SpecialiteExpertise> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SpecialiteExpertise> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select specialiteExpertise from SpecialiteExpertise specialiteExpertise left join fetch specialiteExpertise.diplome left join fetch specialiteExpertise.themeExpertise",
        countQuery = "select count(specialiteExpertise) from SpecialiteExpertise specialiteExpertise"
    )
    Page<SpecialiteExpertise> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select specialiteExpertise from SpecialiteExpertise specialiteExpertise left join fetch specialiteExpertise.diplome left join fetch specialiteExpertise.themeExpertise"
    )
    List<SpecialiteExpertise> findAllWithToOneRelationships();

    @Query(
        "select specialiteExpertise from SpecialiteExpertise specialiteExpertise left join fetch specialiteExpertise.diplome left join fetch specialiteExpertise.themeExpertise where specialiteExpertise.id =:id"
    )
    Optional<SpecialiteExpertise> findOneWithToOneRelationships(@Param("id") Long id);
}
