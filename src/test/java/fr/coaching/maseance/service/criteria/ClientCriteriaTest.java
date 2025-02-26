package fr.coaching.maseance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClientCriteriaTest {

    @Test
    void newClientCriteriaHasAllFiltersNullTest() {
        var clientCriteria = new ClientCriteria();
        assertThat(clientCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void clientCriteriaFluentMethodsCreatesFiltersTest() {
        var clientCriteria = new ClientCriteria();

        setAllFilters(clientCriteria);

        assertThat(clientCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void clientCriteriaCopyCreatesNullFilterTest() {
        var clientCriteria = new ClientCriteria();
        var copy = clientCriteria.copy();

        assertThat(clientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(clientCriteria)
        );
    }

    @Test
    void clientCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var clientCriteria = new ClientCriteria();
        setAllFilters(clientCriteria);

        var copy = clientCriteria.copy();

        assertThat(clientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(clientCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var clientCriteria = new ClientCriteria();

        assertThat(clientCriteria).hasToString("ClientCriteria{}");
    }

    private static void setAllFilters(ClientCriteria clientCriteria) {
        clientCriteria.id();
        clientCriteria.genre();
        clientCriteria.nom();
        clientCriteria.prenom();
        clientCriteria.dateNaissance();
        clientCriteria.adresseEmail();
        clientCriteria.numeroTelephone();
        clientCriteria.ville();
        clientCriteria.codePostal();
        clientCriteria.numeroEtNomVoie();
        clientCriteria.preferenceCanalSeance();
        clientCriteria.urlPhotoProfil();
        clientCriteria.userId();
        clientCriteria.distinct();
    }

    private static Condition<ClientCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getGenre()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getPrenom()) &&
                condition.apply(criteria.getDateNaissance()) &&
                condition.apply(criteria.getAdresseEmail()) &&
                condition.apply(criteria.getNumeroTelephone()) &&
                condition.apply(criteria.getVille()) &&
                condition.apply(criteria.getCodePostal()) &&
                condition.apply(criteria.getNumeroEtNomVoie()) &&
                condition.apply(criteria.getPreferenceCanalSeance()) &&
                condition.apply(criteria.getUrlPhotoProfil()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClientCriteria> copyFiltersAre(ClientCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getGenre(), copy.getGenre()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getPrenom(), copy.getPrenom()) &&
                condition.apply(criteria.getDateNaissance(), copy.getDateNaissance()) &&
                condition.apply(criteria.getAdresseEmail(), copy.getAdresseEmail()) &&
                condition.apply(criteria.getNumeroTelephone(), copy.getNumeroTelephone()) &&
                condition.apply(criteria.getVille(), copy.getVille()) &&
                condition.apply(criteria.getCodePostal(), copy.getCodePostal()) &&
                condition.apply(criteria.getNumeroEtNomVoie(), copy.getNumeroEtNomVoie()) &&
                condition.apply(criteria.getPreferenceCanalSeance(), copy.getPreferenceCanalSeance()) &&
                condition.apply(criteria.getUrlPhotoProfil(), copy.getUrlPhotoProfil()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
