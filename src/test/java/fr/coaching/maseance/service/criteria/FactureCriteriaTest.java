package fr.coaching.maseance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FactureCriteriaTest {

    @Test
    void newFactureCriteriaHasAllFiltersNullTest() {
        var factureCriteria = new FactureCriteria();
        assertThat(factureCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void factureCriteriaFluentMethodsCreatesFiltersTest() {
        var factureCriteria = new FactureCriteria();

        setAllFilters(factureCriteria);

        assertThat(factureCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void factureCriteriaCopyCreatesNullFilterTest() {
        var factureCriteria = new FactureCriteria();
        var copy = factureCriteria.copy();

        assertThat(factureCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(factureCriteria)
        );
    }

    @Test
    void factureCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var factureCriteria = new FactureCriteria();
        setAllFilters(factureCriteria);

        var copy = factureCriteria.copy();

        assertThat(factureCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(factureCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var factureCriteria = new FactureCriteria();

        assertThat(factureCriteria).hasToString("FactureCriteria{}");
    }

    private static void setAllFilters(FactureCriteria factureCriteria) {
        factureCriteria.id();
        factureCriteria.typeFacture();
        factureCriteria.dateComptableFacture();
        factureCriteria.montant();
        factureCriteria.tva();
        factureCriteria.paiementId();
        factureCriteria.seanceReservationCoachId();
        factureCriteria.distinct();
    }

    private static Condition<FactureCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTypeFacture()) &&
                condition.apply(criteria.getDateComptableFacture()) &&
                condition.apply(criteria.getMontant()) &&
                condition.apply(criteria.getTva()) &&
                condition.apply(criteria.getPaiementId()) &&
                condition.apply(criteria.getSeanceReservationCoachId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FactureCriteria> copyFiltersAre(FactureCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTypeFacture(), copy.getTypeFacture()) &&
                condition.apply(criteria.getDateComptableFacture(), copy.getDateComptableFacture()) &&
                condition.apply(criteria.getMontant(), copy.getMontant()) &&
                condition.apply(criteria.getTva(), copy.getTva()) &&
                condition.apply(criteria.getPaiementId(), copy.getPaiementId()) &&
                condition.apply(criteria.getSeanceReservationCoachId(), copy.getSeanceReservationCoachId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
