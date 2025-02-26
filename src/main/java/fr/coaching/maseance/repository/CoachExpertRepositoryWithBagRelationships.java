package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.CoachExpert;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CoachExpertRepositoryWithBagRelationships {
    Optional<CoachExpert> fetchBagRelationships(Optional<CoachExpert> coachExpert);

    List<CoachExpert> fetchBagRelationships(List<CoachExpert> coachExperts);

    Page<CoachExpert> fetchBagRelationships(Page<CoachExpert> coachExperts);
}
