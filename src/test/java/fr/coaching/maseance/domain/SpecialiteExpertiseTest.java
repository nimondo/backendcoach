package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.CoachExpertTestSamples.*;
import static fr.coaching.maseance.domain.DiplomeTestSamples.*;
import static fr.coaching.maseance.domain.OffreCoachTestSamples.*;
import static fr.coaching.maseance.domain.SpecialiteExpertiseTestSamples.*;
import static fr.coaching.maseance.domain.ThemeExpertiseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SpecialiteExpertiseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialiteExpertise.class);
        SpecialiteExpertise specialiteExpertise1 = getSpecialiteExpertiseSample1();
        SpecialiteExpertise specialiteExpertise2 = new SpecialiteExpertise();
        assertThat(specialiteExpertise1).isNotEqualTo(specialiteExpertise2);

        specialiteExpertise2.setId(specialiteExpertise1.getId());
        assertThat(specialiteExpertise1).isEqualTo(specialiteExpertise2);

        specialiteExpertise2 = getSpecialiteExpertiseSample2();
        assertThat(specialiteExpertise1).isNotEqualTo(specialiteExpertise2);
    }

    @Test
    void offreTest() {
        SpecialiteExpertise specialiteExpertise = getSpecialiteExpertiseRandomSampleGenerator();
        OffreCoach offreCoachBack = getOffreCoachRandomSampleGenerator();

        specialiteExpertise.addOffre(offreCoachBack);
        assertThat(specialiteExpertise.getOffres()).containsOnly(offreCoachBack);
        assertThat(offreCoachBack.getSpecialite()).isEqualTo(specialiteExpertise);

        specialiteExpertise.removeOffre(offreCoachBack);
        assertThat(specialiteExpertise.getOffres()).doesNotContain(offreCoachBack);
        assertThat(offreCoachBack.getSpecialite()).isNull();

        specialiteExpertise.offres(new HashSet<>(Set.of(offreCoachBack)));
        assertThat(specialiteExpertise.getOffres()).containsOnly(offreCoachBack);
        assertThat(offreCoachBack.getSpecialite()).isEqualTo(specialiteExpertise);

        specialiteExpertise.setOffres(new HashSet<>());
        assertThat(specialiteExpertise.getOffres()).doesNotContain(offreCoachBack);
        assertThat(offreCoachBack.getSpecialite()).isNull();
    }

    @Test
    void diplomeTest() {
        SpecialiteExpertise specialiteExpertise = getSpecialiteExpertiseRandomSampleGenerator();
        Diplome diplomeBack = getDiplomeRandomSampleGenerator();

        specialiteExpertise.setDiplome(diplomeBack);
        assertThat(specialiteExpertise.getDiplome()).isEqualTo(diplomeBack);

        specialiteExpertise.diplome(null);
        assertThat(specialiteExpertise.getDiplome()).isNull();
    }

    @Test
    void themeExpertiseTest() {
        SpecialiteExpertise specialiteExpertise = getSpecialiteExpertiseRandomSampleGenerator();
        ThemeExpertise themeExpertiseBack = getThemeExpertiseRandomSampleGenerator();

        specialiteExpertise.setThemeExpertise(themeExpertiseBack);
        assertThat(specialiteExpertise.getThemeExpertise()).isEqualTo(themeExpertiseBack);

        specialiteExpertise.themeExpertise(null);
        assertThat(specialiteExpertise.getThemeExpertise()).isNull();
    }

    @Test
    void coachExpertTest() {
        SpecialiteExpertise specialiteExpertise = getSpecialiteExpertiseRandomSampleGenerator();
        CoachExpert coachExpertBack = getCoachExpertRandomSampleGenerator();

        specialiteExpertise.addCoachExpert(coachExpertBack);
        assertThat(specialiteExpertise.getCoachExperts()).containsOnly(coachExpertBack);
        assertThat(coachExpertBack.getSpecialiteExpertises()).containsOnly(specialiteExpertise);

        specialiteExpertise.removeCoachExpert(coachExpertBack);
        assertThat(specialiteExpertise.getCoachExperts()).doesNotContain(coachExpertBack);
        assertThat(coachExpertBack.getSpecialiteExpertises()).doesNotContain(specialiteExpertise);

        specialiteExpertise.coachExperts(new HashSet<>(Set.of(coachExpertBack)));
        assertThat(specialiteExpertise.getCoachExperts()).containsOnly(coachExpertBack);
        assertThat(coachExpertBack.getSpecialiteExpertises()).containsOnly(specialiteExpertise);

        specialiteExpertise.setCoachExperts(new HashSet<>());
        assertThat(specialiteExpertise.getCoachExperts()).doesNotContain(coachExpertBack);
        assertThat(coachExpertBack.getSpecialiteExpertises()).doesNotContain(specialiteExpertise);
    }
}
