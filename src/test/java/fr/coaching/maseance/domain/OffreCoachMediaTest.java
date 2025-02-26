package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.OffreCoachMediaTestSamples.*;
import static fr.coaching.maseance.domain.OffreCoachTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OffreCoachMediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OffreCoachMedia.class);
        OffreCoachMedia offreCoachMedia1 = getOffreCoachMediaSample1();
        OffreCoachMedia offreCoachMedia2 = new OffreCoachMedia();
        assertThat(offreCoachMedia1).isNotEqualTo(offreCoachMedia2);

        offreCoachMedia2.setId(offreCoachMedia1.getId());
        assertThat(offreCoachMedia1).isEqualTo(offreCoachMedia2);

        offreCoachMedia2 = getOffreCoachMediaSample2();
        assertThat(offreCoachMedia1).isNotEqualTo(offreCoachMedia2);
    }

    @Test
    void offreCoachTest() {
        OffreCoachMedia offreCoachMedia = getOffreCoachMediaRandomSampleGenerator();
        OffreCoach offreCoachBack = getOffreCoachRandomSampleGenerator();

        offreCoachMedia.setOffreCoach(offreCoachBack);
        assertThat(offreCoachMedia.getOffreCoach()).isEqualTo(offreCoachBack);

        offreCoachMedia.offreCoach(null);
        assertThat(offreCoachMedia.getOffreCoach()).isNull();
    }
}
