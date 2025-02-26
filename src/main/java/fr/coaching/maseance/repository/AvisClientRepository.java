package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.AvisClient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AvisClient entity.
 */
@Repository
public interface AvisClientRepository extends JpaRepository<AvisClient, Long>, JpaSpecificationExecutor<AvisClient> {
    default Optional<AvisClient> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AvisClient> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AvisClient> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select avisClient from AvisClient avisClient left join fetch avisClient.client",
        countQuery = "select count(avisClient) from AvisClient avisClient"
    )
    Page<AvisClient> findAllWithToOneRelationships(Pageable pageable);

    @Query("select avisClient from AvisClient avisClient left join fetch avisClient.client")
    List<AvisClient> findAllWithToOneRelationships();

    @Query("select avisClient from AvisClient avisClient left join fetch avisClient.client where avisClient.id =:id")
    Optional<AvisClient> findOneWithToOneRelationships(@Param("id") Long id);
}
