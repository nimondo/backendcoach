package fr.coaching.maseance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SeanceReservationCoachCriteriaTest {

    @Test
    void newSeanceReservationCoachCriteriaHasAllFiltersNullTest() {
        var seanceReservationCoachCriteria = new SeanceReservationCoachCriteria();
        assertThat(seanceReservationCoachCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void seanceReservationCoachCriteriaFluentMethodsCreatesFiltersTest() {
        var seanceReservationCoachCriteria = new SeanceReservationCoachCriteria();

        setAllFilters(seanceReservationCoachCriteria);

        assertThat(seanceReservationCoachCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void seanceReservationCoachCriteriaCopyCreatesNullFilterTest() {
        var seanceReservationCoachCriteria = new SeanceReservationCoachCriteria();
        var copy = seanceReservationCoachCriteria.copy();

        assertThat(seanceReservationCoachCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(seanceReservationCoachCriteria)
        );
    }

    @Test
    void seanceReservationCoachCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var seanceReservationCoachCriteria = new SeanceReservationCoachCriteria();
        setAllFilters(seanceReservationCoachCriteria);

        var copy = seanceReservationCoachCriteria.copy();

        assertThat(seanceReservationCoachCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(seanceReservationCoachCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var seanceReservationCoachCriteria = new SeanceReservationCoachCriteria();

        assertThat(seanceReservationCoachCriteria).hasToString("SeanceReservationCoachCriteria{}");
    }

    private static void setAllFilters(SeanceReservationCoachCriteria seanceReservationCoachCriteria) {
        seanceReservationCoachCriteria.id();
        seanceReservationCoachCriteria.heureDebutCreneauReserve();
        seanceReservationCoachCriteria.heureFinCreneauReserve();
        seanceReservationCoachCriteria.canalSeance();
        seanceReservationCoachCriteria.typeSeance();
        seanceReservationCoachCriteria.statutRealisation();
        seanceReservationCoachCriteria.factureId();
        seanceReservationCoachCriteria.coachExpertId();
        seanceReservationCoachCriteria.clientId();
        seanceReservationCoachCriteria.offreId();
        seanceReservationCoachCriteria.distinct();
    }

    private static Condition<SeanceReservationCoachCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getHeureDebutCreneauReserve()) &&
                condition.apply(criteria.getHeureFinCreneauReserve()) &&
                condition.apply(criteria.getCanalSeance()) &&
                condition.apply(criteria.getTypeSeance()) &&
                condition.apply(criteria.getStatutRealisation()) &&
                condition.apply(criteria.getFactureId()) &&
                condition.apply(criteria.getCoachExpertId()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getOffreId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SeanceReservationCoachCriteria> copyFiltersAre(
        SeanceReservationCoachCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getHeureDebutCreneauReserve(), copy.getHeureDebutCreneauReserve()) &&
                condition.apply(criteria.getHeureFinCreneauReserve(), copy.getHeureFinCreneauReserve()) &&
                condition.apply(criteria.getCanalSeance(), copy.getCanalSeance()) &&
                condition.apply(criteria.getTypeSeance(), copy.getTypeSeance()) &&
                condition.apply(criteria.getStatutRealisation(), copy.getStatutRealisation()) &&
                condition.apply(criteria.getFactureId(), copy.getFactureId()) &&
                condition.apply(criteria.getCoachExpertId(), copy.getCoachExpertId()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getOffreId(), copy.getOffreId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
