package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.Consent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Consent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsentRepository extends JpaRepository<Consent, Long> {}
