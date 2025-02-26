package fr.coaching.maseance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaiementCriteriaTest {

    @Test
    void newPaiementCriteriaHasAllFiltersNullTest() {
        var paiementCriteria = new PaiementCriteria();
        assertThat(paiementCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paiementCriteriaFluentMethodsCreatesFiltersTest() {
        var paiementCriteria = new PaiementCriteria();

        setAllFilters(paiementCriteria);

        assertThat(paiementCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paiementCriteriaCopyCreatesNullFilterTest() {
        var paiementCriteria = new PaiementCriteria();
        var copy = paiementCriteria.copy();

        assertThat(paiementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paiementCriteria)
        );
    }

    @Test
    void paiementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paiementCriteria = new PaiementCriteria();
        setAllFilters(paiementCriteria);

        var copy = paiementCriteria.copy();

        assertThat(paiementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paiementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paiementCriteria = new PaiementCriteria();

        assertThat(paiementCriteria).hasToString("PaiementCriteria{}");
    }

    private static void setAllFilters(PaiementCriteria paiementCriteria) {
        paiementCriteria.id();
        paiementCriteria.horodatage();
        paiementCriteria.moyenPaiement();
        paiementCriteria.statutPaiement();
        paiementCriteria.idStripe();
        paiementCriteria.factureId();
        paiementCriteria.distinct();
    }

    private static Condition<PaiementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getHorodatage()) &&
                condition.apply(criteria.getMoyenPaiement()) &&
                condition.apply(criteria.getStatutPaiement()) &&
                condition.apply(criteria.getIdStripe()) &&
                condition.apply(criteria.getFactureId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaiementCriteria> copyFiltersAre(PaiementCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getHorodatage(), copy.getHorodatage()) &&
                condition.apply(criteria.getMoyenPaiement(), copy.getMoyenPaiement()) &&
                condition.apply(criteria.getStatutPaiement(), copy.getStatutPaiement()) &&
                condition.apply(criteria.getIdStripe(), copy.getIdStripe()) &&
                condition.apply(criteria.getFactureId(), copy.getFactureId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
