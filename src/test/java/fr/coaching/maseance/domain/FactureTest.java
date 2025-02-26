package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.FactureTestSamples.*;
import static fr.coaching.maseance.domain.PaiementTestSamples.*;
import static fr.coaching.maseance.domain.SeanceReservationCoachTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facture.class);
        Facture facture1 = getFactureSample1();
        Facture facture2 = new Facture();
        assertThat(facture1).isNotEqualTo(facture2);

        facture2.setId(facture1.getId());
        assertThat(facture1).isEqualTo(facture2);

        facture2 = getFactureSample2();
        assertThat(facture1).isNotEqualTo(facture2);
    }

    @Test
    void paiementTest() {
        Facture facture = getFactureRandomSampleGenerator();
        Paiement paiementBack = getPaiementRandomSampleGenerator();

        facture.setPaiement(paiementBack);
        assertThat(facture.getPaiement()).isEqualTo(paiementBack);

        facture.paiement(null);
        assertThat(facture.getPaiement()).isNull();
    }

    @Test
    void seanceReservationCoachTest() {
        Facture facture = getFactureRandomSampleGenerator();
        SeanceReservationCoach seanceReservationCoachBack = getSeanceReservationCoachRandomSampleGenerator();

        facture.setSeanceReservationCoach(seanceReservationCoachBack);
        assertThat(facture.getSeanceReservationCoach()).isEqualTo(seanceReservationCoachBack);
        assertThat(seanceReservationCoachBack.getFacture()).isEqualTo(facture);

        facture.seanceReservationCoach(null);
        assertThat(facture.getSeanceReservationCoach()).isNull();
        assertThat(seanceReservationCoachBack.getFacture()).isNull();
    }
}
