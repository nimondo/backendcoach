package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.AvisClientTestSamples.*;
import static fr.coaching.maseance.domain.CoachExpertTestSamples.*;
import static fr.coaching.maseance.domain.DiplomeTestSamples.*;
import static fr.coaching.maseance.domain.DisponibiliteTestSamples.*;
import static fr.coaching.maseance.domain.OffreCoachTestSamples.*;
import static fr.coaching.maseance.domain.SpecialiteExpertiseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CoachExpertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoachExpert.class);
        CoachExpert coachExpert1 = getCoachExpertSample1();
        CoachExpert coachExpert2 = new CoachExpert();
        assertThat(coachExpert1).isNotEqualTo(coachExpert2);

        coachExpert2.setId(coachExpert1.getId());
        assertThat(coachExpert1).isEqualTo(coachExpert2);

        coachExpert2 = getCoachExpertSample2();
        assertThat(coachExpert1).isNotEqualTo(coachExpert2);
    }

    @Test
    void lesAvisClientTest() {
        CoachExpert coachExpert = getCoachExpertRandomSampleGenerator();
        AvisClient avisClientBack = getAvisClientRandomSampleGenerator();

        coachExpert.addLesAvisClient(avisClientBack);
        assertThat(coachExpert.getLesAvisClients()).containsOnly(avisClientBack);
        assertThat(avisClientBack.getCoachExpert()).isEqualTo(coachExpert);

        coachExpert.removeLesAvisClient(avisClientBack);
        assertThat(coachExpert.getLesAvisClients()).doesNotContain(avisClientBack);
        assertThat(avisClientBack.getCoachExpert()).isNull();

        coachExpert.lesAvisClients(new HashSet<>(Set.of(avisClientBack)));
        assertThat(coachExpert.getLesAvisClients()).containsOnly(avisClientBack);
        assertThat(avisClientBack.getCoachExpert()).isEqualTo(coachExpert);

        coachExpert.setLesAvisClients(new HashSet<>());
        assertThat(coachExpert.getLesAvisClients()).doesNotContain(avisClientBack);
        assertThat(avisClientBack.getCoachExpert()).isNull();
    }

    @Test
    void disponibiliteTest() {
        CoachExpert coachExpert = getCoachExpertRandomSampleGenerator();
        Disponibilite disponibiliteBack = getDisponibiliteRandomSampleGenerator();

        coachExpert.addDisponibilite(disponibiliteBack);
        assertThat(coachExpert.getDisponibilites()).containsOnly(disponibiliteBack);
        assertThat(disponibiliteBack.getCoachExpert()).isEqualTo(coachExpert);

        coachExpert.removeDisponibilite(disponibiliteBack);
        assertThat(coachExpert.getDisponibilites()).doesNotContain(disponibiliteBack);
        assertThat(disponibiliteBack.getCoachExpert()).isNull();

        coachExpert.disponibilites(new HashSet<>(Set.of(disponibiliteBack)));
        assertThat(coachExpert.getDisponibilites()).containsOnly(disponibiliteBack);
        assertThat(disponibiliteBack.getCoachExpert()).isEqualTo(coachExpert);

        coachExpert.setDisponibilites(new HashSet<>());
        assertThat(coachExpert.getDisponibilites()).doesNotContain(disponibiliteBack);
        assertThat(disponibiliteBack.getCoachExpert()).isNull();
    }

    @Test
    void offreTest() {
        CoachExpert coachExpert = getCoachExpertRandomSampleGenerator();
        OffreCoach offreCoachBack = getOffreCoachRandomSampleGenerator();

        coachExpert.addOffre(offreCoachBack);
        assertThat(coachExpert.getOffres()).containsOnly(offreCoachBack);
        assertThat(offreCoachBack.getCoachExpert()).isEqualTo(coachExpert);

        coachExpert.removeOffre(offreCoachBack);
        assertThat(coachExpert.getOffres()).doesNotContain(offreCoachBack);
        assertThat(offreCoachBack.getCoachExpert()).isNull();

        coachExpert.offres(new HashSet<>(Set.of(offreCoachBack)));
        assertThat(coachExpert.getOffres()).containsOnly(offreCoachBack);
        assertThat(offreCoachBack.getCoachExpert()).isEqualTo(coachExpert);

        coachExpert.setOffres(new HashSet<>());
        assertThat(coachExpert.getOffres()).doesNotContain(offreCoachBack);
        assertThat(offreCoachBack.getCoachExpert()).isNull();
    }

    @Test
    void specialiteExpertiseTest() {
        CoachExpert coachExpert = getCoachExpertRandomSampleGenerator();
        SpecialiteExpertise specialiteExpertiseBack = getSpecialiteExpertiseRandomSampleGenerator();

        coachExpert.addSpecialiteExpertise(specialiteExpertiseBack);
        assertThat(coachExpert.getSpecialiteExpertises()).containsOnly(specialiteExpertiseBack);

        coachExpert.removeSpecialiteExpertise(specialiteExpertiseBack);
        assertThat(coachExpert.getSpecialiteExpertises()).doesNotContain(specialiteExpertiseBack);

        coachExpert.specialiteExpertises(new HashSet<>(Set.of(specialiteExpertiseBack)));
        assertThat(coachExpert.getSpecialiteExpertises()).containsOnly(specialiteExpertiseBack);

        coachExpert.setSpecialiteExpertises(new HashSet<>());
        assertThat(coachExpert.getSpecialiteExpertises()).doesNotContain(specialiteExpertiseBack);
    }

    @Test
    void diplomeTest() {
        CoachExpert coachExpert = getCoachExpertRandomSampleGenerator();
        Diplome diplomeBack = getDiplomeRandomSampleGenerator();

        coachExpert.addDiplome(diplomeBack);
        assertThat(coachExpert.getDiplomes()).containsOnly(diplomeBack);

        coachExpert.removeDiplome(diplomeBack);
        assertThat(coachExpert.getDiplomes()).doesNotContain(diplomeBack);

        coachExpert.diplomes(new HashSet<>(Set.of(diplomeBack)));
        assertThat(coachExpert.getDiplomes()).containsOnly(diplomeBack);

        coachExpert.setDiplomes(new HashSet<>());
        assertThat(coachExpert.getDiplomes()).doesNotContain(diplomeBack);
    }
}
