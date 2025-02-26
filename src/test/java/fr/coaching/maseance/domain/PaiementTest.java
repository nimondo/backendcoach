package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.FactureTestSamples.*;
import static fr.coaching.maseance.domain.PaiementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaiementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paiement.class);
        Paiement paiement1 = getPaiementSample1();
        Paiement paiement2 = new Paiement();
        assertThat(paiement1).isNotEqualTo(paiement2);

        paiement2.setId(paiement1.getId());
        assertThat(paiement1).isEqualTo(paiement2);

        paiement2 = getPaiementSample2();
        assertThat(paiement1).isNotEqualTo(paiement2);
    }

    @Test
    void factureTest() {
        Paiement paiement = getPaiementRandomSampleGenerator();
        Facture factureBack = getFactureRandomSampleGenerator();

        paiement.setFacture(factureBack);
        assertThat(paiement.getFacture()).isEqualTo(factureBack);
        assertThat(factureBack.getPaiement()).isEqualTo(paiement);

        paiement.facture(null);
        assertThat(paiement.getFacture()).isNull();
        assertThat(factureBack.getPaiement()).isNull();
    }
}
