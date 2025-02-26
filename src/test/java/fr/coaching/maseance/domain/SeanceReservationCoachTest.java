package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.ClientTestSamples.*;
import static fr.coaching.maseance.domain.CoachExpertTestSamples.*;
import static fr.coaching.maseance.domain.FactureTestSamples.*;
import static fr.coaching.maseance.domain.OffreCoachTestSamples.*;
import static fr.coaching.maseance.domain.SeanceReservationCoachTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeanceReservationCoachTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeanceReservationCoach.class);
        SeanceReservationCoach seanceReservationCoach1 = getSeanceReservationCoachSample1();
        SeanceReservationCoach seanceReservationCoach2 = new SeanceReservationCoach();
        assertThat(seanceReservationCoach1).isNotEqualTo(seanceReservationCoach2);

        seanceReservationCoach2.setId(seanceReservationCoach1.getId());
        assertThat(seanceReservationCoach1).isEqualTo(seanceReservationCoach2);

        seanceReservationCoach2 = getSeanceReservationCoachSample2();
        assertThat(seanceReservationCoach1).isNotEqualTo(seanceReservationCoach2);
    }

    @Test
    void factureTest() {
        SeanceReservationCoach seanceReservationCoach = getSeanceReservationCoachRandomSampleGenerator();
        Facture factureBack = getFactureRandomSampleGenerator();

        seanceReservationCoach.setFacture(factureBack);
        assertThat(seanceReservationCoach.getFacture()).isEqualTo(factureBack);

        seanceReservationCoach.facture(null);
        assertThat(seanceReservationCoach.getFacture()).isNull();
    }

    @Test
    void coachExpertTest() {
        SeanceReservationCoach seanceReservationCoach = getSeanceReservationCoachRandomSampleGenerator();
        CoachExpert coachExpertBack = getCoachExpertRandomSampleGenerator();

        seanceReservationCoach.setCoachExpert(coachExpertBack);
        assertThat(seanceReservationCoach.getCoachExpert()).isEqualTo(coachExpertBack);

        seanceReservationCoach.coachExpert(null);
        assertThat(seanceReservationCoach.getCoachExpert()).isNull();
    }

    @Test
    void clientTest() {
        SeanceReservationCoach seanceReservationCoach = getSeanceReservationCoachRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        seanceReservationCoach.setClient(clientBack);
        assertThat(seanceReservationCoach.getClient()).isEqualTo(clientBack);

        seanceReservationCoach.client(null);
        assertThat(seanceReservationCoach.getClient()).isNull();
    }

    @Test
    void offreTest() {
        SeanceReservationCoach seanceReservationCoach = getSeanceReservationCoachRandomSampleGenerator();
        OffreCoach offreCoachBack = getOffreCoachRandomSampleGenerator();

        seanceReservationCoach.setOffre(offreCoachBack);
        assertThat(seanceReservationCoach.getOffre()).isEqualTo(offreCoachBack);

        seanceReservationCoach.offre(null);
        assertThat(seanceReservationCoach.getOffre()).isNull();
    }
}
