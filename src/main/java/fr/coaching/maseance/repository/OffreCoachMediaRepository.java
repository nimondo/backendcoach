package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.OffreCoachMedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OffreCoachMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OffreCoachMediaRepository extends JpaRepository<OffreCoachMedia, Long>, JpaSpecificationExecutor<OffreCoachMedia> {}
