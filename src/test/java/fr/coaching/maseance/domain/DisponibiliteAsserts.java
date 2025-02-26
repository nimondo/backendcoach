package fr.coaching.maseance.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DisponibiliteAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDisponibiliteAllPropertiesEquals(Disponibilite expected, Disponibilite actual) {
        assertDisponibiliteAutoGeneratedPropertiesEquals(expected, actual);
        assertDisponibiliteAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDisponibiliteAllUpdatablePropertiesEquals(Disponibilite expected, Disponibilite actual) {
        assertDisponibiliteUpdatableFieldsEquals(expected, actual);
        assertDisponibiliteUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDisponibiliteAutoGeneratedPropertiesEquals(Disponibilite expected, Disponibilite actual) {
        assertThat(expected)
            .as("Verify Disponibilite auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDisponibiliteUpdatableFieldsEquals(Disponibilite expected, Disponibilite actual) {
        assertThat(expected)
            .as("Verify Disponibilite relevant properties")
            .satisfies(e ->
                assertThat(e.getHeureDebutCreneauxDisponibilite())
                    .as("check heureDebutCreneauxDisponibilite")
                    .isEqualTo(actual.getHeureDebutCreneauxDisponibilite())
            )
            .satisfies(e ->
                assertThat(e.getHeureFinCreneauxDisponibilite())
                    .as("check heureFinCreneauxDisponibilite")
                    .isEqualTo(actual.getHeureFinCreneauxDisponibilite())
            )
            .satisfies(e -> assertThat(e.getCanalSeance()).as("check canalSeance").isEqualTo(actual.getCanalSeance()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDisponibiliteUpdatableRelationshipsEquals(Disponibilite expected, Disponibilite actual) {
        assertThat(expected)
            .as("Verify Disponibilite relationships")
            .satisfies(e -> assertThat(e.getCoachExpert()).as("check coachExpert").isEqualTo(actual.getCoachExpert()));
    }
}
