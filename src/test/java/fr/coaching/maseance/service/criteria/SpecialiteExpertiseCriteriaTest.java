package fr.coaching.maseance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SpecialiteExpertiseCriteriaTest {

    @Test
    void newSpecialiteExpertiseCriteriaHasAllFiltersNullTest() {
        var specialiteExpertiseCriteria = new SpecialiteExpertiseCriteria();
        assertThat(specialiteExpertiseCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void specialiteExpertiseCriteriaFluentMethodsCreatesFiltersTest() {
        var specialiteExpertiseCriteria = new SpecialiteExpertiseCriteria();

        setAllFilters(specialiteExpertiseCriteria);

        assertThat(specialiteExpertiseCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void specialiteExpertiseCriteriaCopyCreatesNullFilterTest() {
        var specialiteExpertiseCriteria = new SpecialiteExpertiseCriteria();
        var copy = specialiteExpertiseCriteria.copy();

        assertThat(specialiteExpertiseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(specialiteExpertiseCriteria)
        );
    }

    @Test
    void specialiteExpertiseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var specialiteExpertiseCriteria = new SpecialiteExpertiseCriteria();
        setAllFilters(specialiteExpertiseCriteria);

        var copy = specialiteExpertiseCriteria.copy();

        assertThat(specialiteExpertiseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(specialiteExpertiseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var specialiteExpertiseCriteria = new SpecialiteExpertiseCriteria();

        assertThat(specialiteExpertiseCriteria).hasToString("SpecialiteExpertiseCriteria{}");
    }

    private static void setAllFilters(SpecialiteExpertiseCriteria specialiteExpertiseCriteria) {
        specialiteExpertiseCriteria.id();
        specialiteExpertiseCriteria.specialite();
        specialiteExpertiseCriteria.specialiteDescription();
        specialiteExpertiseCriteria.tarifMoyenHeure();
        specialiteExpertiseCriteria.dureeTarif();
        specialiteExpertiseCriteria.diplomeRequis();
        specialiteExpertiseCriteria.urlPhoto();
        specialiteExpertiseCriteria.offreId();
        specialiteExpertiseCriteria.diplomeId();
        specialiteExpertiseCriteria.themeExpertiseId();
        specialiteExpertiseCriteria.coachExpertId();
        specialiteExpertiseCriteria.distinct();
    }

    private static Condition<SpecialiteExpertiseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSpecialite()) &&
                condition.apply(criteria.getSpecialiteDescription()) &&
                condition.apply(criteria.getTarifMoyenHeure()) &&
                condition.apply(criteria.getDureeTarif()) &&
                condition.apply(criteria.getDiplomeRequis()) &&
                condition.apply(criteria.getUrlPhoto()) &&
                condition.apply(criteria.getOffreId()) &&
                condition.apply(criteria.getDiplomeId()) &&
                condition.apply(criteria.getThemeExpertiseId()) &&
                condition.apply(criteria.getCoachExpertId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SpecialiteExpertiseCriteria> copyFiltersAre(
        SpecialiteExpertiseCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSpecialite(), copy.getSpecialite()) &&
                condition.apply(criteria.getSpecialiteDescription(), copy.getSpecialiteDescription()) &&
                condition.apply(criteria.getTarifMoyenHeure(), copy.getTarifMoyenHeure()) &&
                condition.apply(criteria.getDureeTarif(), copy.getDureeTarif()) &&
                condition.apply(criteria.getDiplomeRequis(), copy.getDiplomeRequis()) &&
                condition.apply(criteria.getUrlPhoto(), copy.getUrlPhoto()) &&
                condition.apply(criteria.getOffreId(), copy.getOffreId()) &&
                condition.apply(criteria.getDiplomeId(), copy.getDiplomeId()) &&
                condition.apply(criteria.getThemeExpertiseId(), copy.getThemeExpertiseId()) &&
                condition.apply(criteria.getCoachExpertId(), copy.getCoachExpertId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
