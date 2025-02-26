package fr.coaching.maseance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DisponibiliteCriteriaTest {

    @Test
    void newDisponibiliteCriteriaHasAllFiltersNullTest() {
        var disponibiliteCriteria = new DisponibiliteCriteria();
        assertThat(disponibiliteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void disponibiliteCriteriaFluentMethodsCreatesFiltersTest() {
        var disponibiliteCriteria = new DisponibiliteCriteria();

        setAllFilters(disponibiliteCriteria);

        assertThat(disponibiliteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void disponibiliteCriteriaCopyCreatesNullFilterTest() {
        var disponibiliteCriteria = new DisponibiliteCriteria();
        var copy = disponibiliteCriteria.copy();

        assertThat(disponibiliteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(disponibiliteCriteria)
        );
    }

    @Test
    void disponibiliteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var disponibiliteCriteria = new DisponibiliteCriteria();
        setAllFilters(disponibiliteCriteria);

        var copy = disponibiliteCriteria.copy();

        assertThat(disponibiliteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(disponibiliteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var disponibiliteCriteria = new DisponibiliteCriteria();

        assertThat(disponibiliteCriteria).hasToString("DisponibiliteCriteria{}");
    }

    private static void setAllFilters(DisponibiliteCriteria disponibiliteCriteria) {
        disponibiliteCriteria.id();
        disponibiliteCriteria.heureDebutCreneauxDisponibilite();
        disponibiliteCriteria.heureFinCreneauxDisponibilite();
        disponibiliteCriteria.canalSeance();
        disponibiliteCriteria.coachExpertId();
        disponibiliteCriteria.distinct();
    }

    private static Condition<DisponibiliteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getHeureDebutCreneauxDisponibilite()) &&
                condition.apply(criteria.getHeureFinCreneauxDisponibilite()) &&
                condition.apply(criteria.getCanalSeance()) &&
                condition.apply(criteria.getCoachExpertId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DisponibiliteCriteria> copyFiltersAre(
        DisponibiliteCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getHeureDebutCreneauxDisponibilite(), copy.getHeureDebutCreneauxDisponibilite()) &&
                condition.apply(criteria.getHeureFinCreneauxDisponibilite(), copy.getHeureFinCreneauxDisponibilite()) &&
                condition.apply(criteria.getCanalSeance(), copy.getCanalSeance()) &&
                condition.apply(criteria.getCoachExpertId(), copy.getCoachExpertId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
