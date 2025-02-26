package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.Diplome;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Diplome entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiplomeRepository extends JpaRepository<Diplome, Long> {}
