package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.CoachExpertTestSamples.*;
import static fr.coaching.maseance.domain.OffreCoachMediaTestSamples.*;
import static fr.coaching.maseance.domain.OffreCoachTestSamples.*;
import static fr.coaching.maseance.domain.SpecialiteExpertiseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OffreCoachTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OffreCoach.class);
        OffreCoach offreCoach1 = getOffreCoachSample1();
        OffreCoach offreCoach2 = new OffreCoach();
        assertThat(offreCoach1).isNotEqualTo(offreCoach2);

        offreCoach2.setId(offreCoach1.getId());
        assertThat(offreCoach1).isEqualTo(offreCoach2);

        offreCoach2 = getOffreCoachSample2();
        assertThat(offreCoach1).isNotEqualTo(offreCoach2);
    }

    @Test
    void mediaTest() {
        OffreCoach offreCoach = getOffreCoachRandomSampleGenerator();
        OffreCoachMedia offreCoachMediaBack = getOffreCoachMediaRandomSampleGenerator();

        offreCoach.addMedia(offreCoachMediaBack);
        assertThat(offreCoach.getMedia()).containsOnly(offreCoachMediaBack);
        assertThat(offreCoachMediaBack.getOffreCoach()).isEqualTo(offreCoach);

        offreCoach.removeMedia(offreCoachMediaBack);
        assertThat(offreCoach.getMedia()).doesNotContain(offreCoachMediaBack);
        assertThat(offreCoachMediaBack.getOffreCoach()).isNull();

        offreCoach.media(new HashSet<>(Set.of(offreCoachMediaBack)));
        assertThat(offreCoach.getMedia()).containsOnly(offreCoachMediaBack);
        assertThat(offreCoachMediaBack.getOffreCoach()).isEqualTo(offreCoach);

        offreCoach.setMedia(new HashSet<>());
        assertThat(offreCoach.getMedia()).doesNotContain(offreCoachMediaBack);
        assertThat(offreCoachMediaBack.getOffreCoach()).isNull();
    }

    @Test
    void specialiteTest() {
        OffreCoach offreCoach = getOffreCoachRandomSampleGenerator();
        SpecialiteExpertise specialiteExpertiseBack = getSpecialiteExpertiseRandomSampleGenerator();

        offreCoach.setSpecialite(specialiteExpertiseBack);
        assertThat(offreCoach.getSpecialite()).isEqualTo(specialiteExpertiseBack);

        offreCoach.specialite(null);
        assertThat(offreCoach.getSpecialite()).isNull();
    }

    @Test
    void coachExpertTest() {
        OffreCoach offreCoach = getOffreCoachRandomSampleGenerator();
        CoachExpert coachExpertBack = getCoachExpertRandomSampleGenerator();

        offreCoach.setCoachExpert(coachExpertBack);
        assertThat(offreCoach.getCoachExpert()).isEqualTo(coachExpertBack);

        offreCoach.coachExpert(null);
        assertThat(offreCoach.getCoachExpert()).isNull();
    }
}
