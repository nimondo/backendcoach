package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.SeanceReservationCoach;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SeanceReservationCoach entity.
 */
@Repository
public interface SeanceReservationCoachRepository
    extends JpaRepository<SeanceReservationCoach, Long>, JpaSpecificationExecutor<SeanceReservationCoach> {
    default Optional<SeanceReservationCoach> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SeanceReservationCoach> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SeanceReservationCoach> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select seanceReservationCoach from SeanceReservationCoach seanceReservationCoach left join fetch seanceReservationCoach.facture left join fetch seanceReservationCoach.coachExpert left join fetch seanceReservationCoach.client left join fetch seanceReservationCoach.offre",
        countQuery = "select count(seanceReservationCoach) from SeanceReservationCoach seanceReservationCoach"
    )
    Page<SeanceReservationCoach> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select seanceReservationCoach from SeanceReservationCoach seanceReservationCoach left join fetch seanceReservationCoach.facture left join fetch seanceReservationCoach.coachExpert left join fetch seanceReservationCoach.client left join fetch seanceReservationCoach.offre"
    )
    List<SeanceReservationCoach> findAllWithToOneRelationships();

    @Query(
        "select seanceReservationCoach from SeanceReservationCoach seanceReservationCoach left join fetch seanceReservationCoach.facture left join fetch seanceReservationCoach.coachExpert left join fetch seanceReservationCoach.client left join fetch seanceReservationCoach.offre where seanceReservationCoach.id =:id"
    )
    Optional<SeanceReservationCoach> findOneWithToOneRelationships(@Param("id") Long id);
}
