package fr.coaching.maseance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AvisClientCriteriaTest {

    @Test
    void newAvisClientCriteriaHasAllFiltersNullTest() {
        var avisClientCriteria = new AvisClientCriteria();
        assertThat(avisClientCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void avisClientCriteriaFluentMethodsCreatesFiltersTest() {
        var avisClientCriteria = new AvisClientCriteria();

        setAllFilters(avisClientCriteria);

        assertThat(avisClientCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void avisClientCriteriaCopyCreatesNullFilterTest() {
        var avisClientCriteria = new AvisClientCriteria();
        var copy = avisClientCriteria.copy();

        assertThat(avisClientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(avisClientCriteria)
        );
    }

    @Test
    void avisClientCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var avisClientCriteria = new AvisClientCriteria();
        setAllFilters(avisClientCriteria);

        var copy = avisClientCriteria.copy();

        assertThat(avisClientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(avisClientCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var avisClientCriteria = new AvisClientCriteria();

        assertThat(avisClientCriteria).hasToString("AvisClientCriteria{}");
    }

    private static void setAllFilters(AvisClientCriteria avisClientCriteria) {
        avisClientCriteria.id();
        avisClientCriteria.dateAvis();
        avisClientCriteria.note();
        avisClientCriteria.descriptionAvis();
        avisClientCriteria.clientId();
        avisClientCriteria.coachExpertId();
        avisClientCriteria.distinct();
    }

    private static Condition<AvisClientCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDateAvis()) &&
                condition.apply(criteria.getNote()) &&
                condition.apply(criteria.getDescriptionAvis()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getCoachExpertId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AvisClientCriteria> copyFiltersAre(AvisClientCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDateAvis(), copy.getDateAvis()) &&
                condition.apply(criteria.getNote(), copy.getNote()) &&
                condition.apply(criteria.getDescriptionAvis(), copy.getDescriptionAvis()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getCoachExpertId(), copy.getCoachExpertId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
