package fr.coaching.maseance.repository;

import fr.coaching.maseance.domain.CoachExpert;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CoachExpertRepositoryWithBagRelationshipsImpl implements CoachExpertRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String COACHEXPERTS_PARAMETER = "coachExperts";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CoachExpert> fetchBagRelationships(Optional<CoachExpert> coachExpert) {
        return coachExpert.map(this::fetchSpecialiteExpertises).map(this::fetchDiplomes);
    }

    @Override
    public Page<CoachExpert> fetchBagRelationships(Page<CoachExpert> coachExperts) {
        return new PageImpl<>(
            fetchBagRelationships(coachExperts.getContent()),
            coachExperts.getPageable(),
            coachExperts.getTotalElements()
        );
    }

    @Override
    public List<CoachExpert> fetchBagRelationships(List<CoachExpert> coachExperts) {
        return Optional.of(coachExperts).map(this::fetchSpecialiteExpertises).map(this::fetchDiplomes).orElse(Collections.emptyList());
    }

    CoachExpert fetchSpecialiteExpertises(CoachExpert result) {
        return entityManager
            .createQuery(
                "select coachExpert from CoachExpert coachExpert left join fetch coachExpert.specialiteExpertises where coachExpert.id = :id",
                CoachExpert.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<CoachExpert> fetchSpecialiteExpertises(List<CoachExpert> coachExperts) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, coachExperts.size()).forEach(index -> order.put(coachExperts.get(index).getId(), index));
        List<CoachExpert> result = entityManager
            .createQuery(
                "select coachExpert from CoachExpert coachExpert left join fetch coachExpert.specialiteExpertises where coachExpert in :coachExperts",
                CoachExpert.class
            )
            .setParameter(COACHEXPERTS_PARAMETER, coachExperts)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    CoachExpert fetchDiplomes(CoachExpert result) {
        return entityManager
            .createQuery(
                "select coachExpert from CoachExpert coachExpert left join fetch coachExpert.diplomes where coachExpert.id = :id",
                CoachExpert.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<CoachExpert> fetchDiplomes(List<CoachExpert> coachExperts) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, coachExperts.size()).forEach(index -> order.put(coachExperts.get(index).getId(), index));
        List<CoachExpert> result = entityManager
            .createQuery(
                "select coachExpert from CoachExpert coachExpert left join fetch coachExpert.diplomes where coachExpert in :coachExperts",
                CoachExpert.class
            )
            .setParameter(COACHEXPERTS_PARAMETER, coachExperts)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
