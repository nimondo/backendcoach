package fr.coaching.maseance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OffreCoachMediaCriteriaTest {

    @Test
    void newOffreCoachMediaCriteriaHasAllFiltersNullTest() {
        var offreCoachMediaCriteria = new OffreCoachMediaCriteria();
        assertThat(offreCoachMediaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void offreCoachMediaCriteriaFluentMethodsCreatesFiltersTest() {
        var offreCoachMediaCriteria = new OffreCoachMediaCriteria();

        setAllFilters(offreCoachMediaCriteria);

        assertThat(offreCoachMediaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void offreCoachMediaCriteriaCopyCreatesNullFilterTest() {
        var offreCoachMediaCriteria = new OffreCoachMediaCriteria();
        var copy = offreCoachMediaCriteria.copy();

        assertThat(offreCoachMediaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(offreCoachMediaCriteria)
        );
    }

    @Test
    void offreCoachMediaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var offreCoachMediaCriteria = new OffreCoachMediaCriteria();
        setAllFilters(offreCoachMediaCriteria);

        var copy = offreCoachMediaCriteria.copy();

        assertThat(offreCoachMediaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(offreCoachMediaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var offreCoachMediaCriteria = new OffreCoachMediaCriteria();

        assertThat(offreCoachMediaCriteria).hasToString("OffreCoachMediaCriteria{}");
    }

    private static void setAllFilters(OffreCoachMediaCriteria offreCoachMediaCriteria) {
        offreCoachMediaCriteria.id();
        offreCoachMediaCriteria.urlMedia();
        offreCoachMediaCriteria.typeMedia();
        offreCoachMediaCriteria.offreCoachId();
        offreCoachMediaCriteria.distinct();
    }

    private static Condition<OffreCoachMediaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUrlMedia()) &&
                condition.apply(criteria.getTypeMedia()) &&
                condition.apply(criteria.getOffreCoachId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OffreCoachMediaCriteria> copyFiltersAre(
        OffreCoachMediaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUrlMedia(), copy.getUrlMedia()) &&
                condition.apply(criteria.getTypeMedia(), copy.getTypeMedia()) &&
                condition.apply(criteria.getOffreCoachId(), copy.getOffreCoachId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
