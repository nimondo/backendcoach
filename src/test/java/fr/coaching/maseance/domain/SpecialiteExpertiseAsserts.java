package fr.coaching.maseance.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecialiteExpertiseAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSpecialiteExpertiseAllPropertiesEquals(SpecialiteExpertise expected, SpecialiteExpertise actual) {
        assertSpecialiteExpertiseAutoGeneratedPropertiesEquals(expected, actual);
        assertSpecialiteExpertiseAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSpecialiteExpertiseAllUpdatablePropertiesEquals(SpecialiteExpertise expected, SpecialiteExpertise actual) {
        assertSpecialiteExpertiseUpdatableFieldsEquals(expected, actual);
        assertSpecialiteExpertiseUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSpecialiteExpertiseAutoGeneratedPropertiesEquals(SpecialiteExpertise expected, SpecialiteExpertise actual) {
        assertThat(expected)
            .as("Verify SpecialiteExpertise auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSpecialiteExpertiseUpdatableFieldsEquals(SpecialiteExpertise expected, SpecialiteExpertise actual) {
        assertThat(expected)
            .as("Verify SpecialiteExpertise relevant properties")
            .satisfies(e -> assertThat(e.getSpecialite()).as("check specialite").isEqualTo(actual.getSpecialite()))
            .satisfies(e ->
                assertThat(e.getSpecialiteDescription()).as("check specialiteDescription").isEqualTo(actual.getSpecialiteDescription())
            )
            .satisfies(e -> assertThat(e.getTarifMoyenHeure()).as("check tarifMoyenHeure").isEqualTo(actual.getTarifMoyenHeure()))
            .satisfies(e -> assertThat(e.getDureeTarif()).as("check dureeTarif").isEqualTo(actual.getDureeTarif()))
            .satisfies(e -> assertThat(e.getDiplomeRequis()).as("check diplomeRequis").isEqualTo(actual.getDiplomeRequis()))
            .satisfies(e -> assertThat(e.getUrlPhoto()).as("check urlPhoto").isEqualTo(actual.getUrlPhoto()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSpecialiteExpertiseUpdatableRelationshipsEquals(SpecialiteExpertise expected, SpecialiteExpertise actual) {
        assertThat(expected)
            .as("Verify SpecialiteExpertise relationships")
            .satisfies(e -> assertThat(e.getDiplome()).as("check diplome").isEqualTo(actual.getDiplome()))
            .satisfies(e -> assertThat(e.getThemeExpertise()).as("check themeExpertise").isEqualTo(actual.getThemeExpertise()))
            .satisfies(e -> assertThat(e.getCoachExperts()).as("check coachExperts").isEqualTo(actual.getCoachExperts()));
    }
}
