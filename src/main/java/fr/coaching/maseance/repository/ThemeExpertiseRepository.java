package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.ThemeExpertise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ThemeExpertise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThemeExpertiseRepository extends JpaRepository<ThemeExpertise, Long>, JpaSpecificationExecutor<ThemeExpertise> {}
