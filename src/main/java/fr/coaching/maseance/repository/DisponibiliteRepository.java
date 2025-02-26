package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.Disponibilite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Disponibilite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long>, JpaSpecificationExecutor<Disponibilite> {}
