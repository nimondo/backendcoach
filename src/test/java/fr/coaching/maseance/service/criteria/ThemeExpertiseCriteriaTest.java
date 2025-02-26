package fr.coaching.maseance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ThemeExpertiseCriteriaTest {

    @Test
    void newThemeExpertiseCriteriaHasAllFiltersNullTest() {
        var themeExpertiseCriteria = new ThemeExpertiseCriteria();
        assertThat(themeExpertiseCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void themeExpertiseCriteriaFluentMethodsCreatesFiltersTest() {
        var themeExpertiseCriteria = new ThemeExpertiseCriteria();

        setAllFilters(themeExpertiseCriteria);

        assertThat(themeExpertiseCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void themeExpertiseCriteriaCopyCreatesNullFilterTest() {
        var themeExpertiseCriteria = new ThemeExpertiseCriteria();
        var copy = themeExpertiseCriteria.copy();

        assertThat(themeExpertiseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(themeExpertiseCriteria)
        );
    }

    @Test
    void themeExpertiseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var themeExpertiseCriteria = new ThemeExpertiseCriteria();
        setAllFilters(themeExpertiseCriteria);

        var copy = themeExpertiseCriteria.copy();

        assertThat(themeExpertiseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(themeExpertiseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var themeExpertiseCriteria = new ThemeExpertiseCriteria();

        assertThat(themeExpertiseCriteria).hasToString("ThemeExpertiseCriteria{}");
    }

    private static void setAllFilters(ThemeExpertiseCriteria themeExpertiseCriteria) {
        themeExpertiseCriteria.id();
        themeExpertiseCriteria.libelleExpertise();
        themeExpertiseCriteria.urlPhoto();
        themeExpertiseCriteria.distinct();
    }

    private static Condition<ThemeExpertiseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLibelleExpertise()) &&
                condition.apply(criteria.getUrlPhoto()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ThemeExpertiseCriteria> copyFiltersAre(
        ThemeExpertiseCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLibelleExpertise(), copy.getLibelleExpertise()) &&
                condition.apply(criteria.getUrlPhoto(), copy.getUrlPhoto()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
