package fr.coaching.maseance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OffreCoachCriteriaTest {

    @Test
    void newOffreCoachCriteriaHasAllFiltersNullTest() {
        var offreCoachCriteria = new OffreCoachCriteria();
        assertThat(offreCoachCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void offreCoachCriteriaFluentMethodsCreatesFiltersTest() {
        var offreCoachCriteria = new OffreCoachCriteria();

        setAllFilters(offreCoachCriteria);

        assertThat(offreCoachCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void offreCoachCriteriaCopyCreatesNullFilterTest() {
        var offreCoachCriteria = new OffreCoachCriteria();
        var copy = offreCoachCriteria.copy();

        assertThat(offreCoachCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(offreCoachCriteria)
        );
    }

    @Test
    void offreCoachCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var offreCoachCriteria = new OffreCoachCriteria();
        setAllFilters(offreCoachCriteria);

        var copy = offreCoachCriteria.copy();

        assertThat(offreCoachCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(offreCoachCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var offreCoachCriteria = new OffreCoachCriteria();

        assertThat(offreCoachCriteria).hasToString("OffreCoachCriteria{}");
    }

    private static void setAllFilters(OffreCoachCriteria offreCoachCriteria) {
        offreCoachCriteria.id();
        offreCoachCriteria.canalSeance();
        offreCoachCriteria.typeSeance();
        offreCoachCriteria.synthese();
        offreCoachCriteria.descriptionDetaillee();
        offreCoachCriteria.tarif();
        offreCoachCriteria.duree();
        offreCoachCriteria.descriptionDiplome();
        offreCoachCriteria.mediaId();
        offreCoachCriteria.specialiteId();
        offreCoachCriteria.coachExpertId();
        offreCoachCriteria.distinct();
    }

    private static Condition<OffreCoachCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCanalSeance()) &&
                condition.apply(criteria.getTypeSeance()) &&
                condition.apply(criteria.getSynthese()) &&
                condition.apply(criteria.getDescriptionDetaillee()) &&
                condition.apply(criteria.getTarif()) &&
                condition.apply(criteria.getDuree()) &&
                condition.apply(criteria.getDescriptionDiplome()) &&
                condition.apply(criteria.getMediaId()) &&
                condition.apply(criteria.getSpecialiteId()) &&
                condition.apply(criteria.getCoachExpertId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OffreCoachCriteria> copyFiltersAre(OffreCoachCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCanalSeance(), copy.getCanalSeance()) &&
                condition.apply(criteria.getTypeSeance(), copy.getTypeSeance()) &&
                condition.apply(criteria.getSynthese(), copy.getSynthese()) &&
                condition.apply(criteria.getDescriptionDetaillee(), copy.getDescriptionDetaillee()) &&
                condition.apply(criteria.getTarif(), copy.getTarif()) &&
                condition.apply(criteria.getDuree(), copy.getDuree()) &&
                condition.apply(criteria.getDescriptionDiplome(), copy.getDescriptionDiplome()) &&
                condition.apply(criteria.getMediaId(), copy.getMediaId()) &&
                condition.apply(criteria.getSpecialiteId(), copy.getSpecialiteId()) &&
                condition.apply(criteria.getCoachExpertId(), copy.getCoachExpertId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
