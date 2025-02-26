package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.OffreCoach;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OffreCoach entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OffreCoachRepository extends JpaRepository<OffreCoach, Long>, JpaSpecificationExecutor<OffreCoach> {}
