package fr.coaching.maseance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CoachExpertCriteriaTest {

    @Test
    void newCoachExpertCriteriaHasAllFiltersNullTest() {
        var coachExpertCriteria = new CoachExpertCriteria();
        assertThat(coachExpertCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void coachExpertCriteriaFluentMethodsCreatesFiltersTest() {
        var coachExpertCriteria = new CoachExpertCriteria();

        setAllFilters(coachExpertCriteria);

        assertThat(coachExpertCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void coachExpertCriteriaCopyCreatesNullFilterTest() {
        var coachExpertCriteria = new CoachExpertCriteria();
        var copy = coachExpertCriteria.copy();

        assertThat(coachExpertCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(coachExpertCriteria)
        );
    }

    @Test
    void coachExpertCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var coachExpertCriteria = new CoachExpertCriteria();
        setAllFilters(coachExpertCriteria);

        var copy = coachExpertCriteria.copy();

        assertThat(coachExpertCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(coachExpertCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var coachExpertCriteria = new CoachExpertCriteria();

        assertThat(coachExpertCriteria).hasToString("CoachExpertCriteria{}");
    }

    private static void setAllFilters(CoachExpertCriteria coachExpertCriteria) {
        coachExpertCriteria.id();
        coachExpertCriteria.civilite();
        coachExpertCriteria.nom();
        coachExpertCriteria.prenom();
        coachExpertCriteria.dateNaissance();
        coachExpertCriteria.adresseEmail();
        coachExpertCriteria.numeroTelephone();
        coachExpertCriteria.ville();
        coachExpertCriteria.codePostal();
        coachExpertCriteria.numeroEtNomVoie();
        coachExpertCriteria.tarifActuel();
        coachExpertCriteria.formatProposeSeance();
        coachExpertCriteria.urlPhotoProfil();
        coachExpertCriteria.bio();
        coachExpertCriteria.userId();
        coachExpertCriteria.lesAvisClientId();
        coachExpertCriteria.disponibiliteId();
        coachExpertCriteria.offreId();
        coachExpertCriteria.specialiteExpertiseId();
        coachExpertCriteria.diplomeId();
        coachExpertCriteria.distinct();
    }

    private static Condition<CoachExpertCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCivilite()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getPrenom()) &&
                condition.apply(criteria.getDateNaissance()) &&
                condition.apply(criteria.getAdresseEmail()) &&
                condition.apply(criteria.getNumeroTelephone()) &&
                condition.apply(criteria.getVille()) &&
                condition.apply(criteria.getCodePostal()) &&
                condition.apply(criteria.getNumeroEtNomVoie()) &&
                condition.apply(criteria.getTarifActuel()) &&
                condition.apply(criteria.getFormatProposeSeance()) &&
                condition.apply(criteria.getUrlPhotoProfil()) &&
                condition.apply(criteria.getBio()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getLesAvisClientId()) &&
                condition.apply(criteria.getDisponibiliteId()) &&
                condition.apply(criteria.getOffreId()) &&
                condition.apply(criteria.getSpecialiteExpertiseId()) &&
                condition.apply(criteria.getDiplomeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CoachExpertCriteria> copyFiltersAre(CoachExpertCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCivilite(), copy.getCivilite()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getPrenom(), copy.getPrenom()) &&
                condition.apply(criteria.getDateNaissance(), copy.getDateNaissance()) &&
                condition.apply(criteria.getAdresseEmail(), copy.getAdresseEmail()) &&
                condition.apply(criteria.getNumeroTelephone(), copy.getNumeroTelephone()) &&
                condition.apply(criteria.getVille(), copy.getVille()) &&
                condition.apply(criteria.getCodePostal(), copy.getCodePostal()) &&
                condition.apply(criteria.getNumeroEtNomVoie(), copy.getNumeroEtNomVoie()) &&
                condition.apply(criteria.getTarifActuel(), copy.getTarifActuel()) &&
                condition.apply(criteria.getFormatProposeSeance(), copy.getFormatProposeSeance()) &&
                condition.apply(criteria.getUrlPhotoProfil(), copy.getUrlPhotoProfil()) &&
                condition.apply(criteria.getBio(), copy.getBio()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getLesAvisClientId(), copy.getLesAvisClientId()) &&
                condition.apply(criteria.getDisponibiliteId(), copy.getDisponibiliteId()) &&
                condition.apply(criteria.getOffreId(), copy.getOffreId()) &&
                condition.apply(criteria.getSpecialiteExpertiseId(), copy.getSpecialiteExpertiseId()) &&
                condition.apply(criteria.getDiplomeId(), copy.getDiplomeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
