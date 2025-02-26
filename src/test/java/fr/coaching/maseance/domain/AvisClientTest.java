package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.AvisClientTestSamples.*;
import static fr.coaching.maseance.domain.ClientTestSamples.*;
import static fr.coaching.maseance.domain.CoachExpertTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvisClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AvisClient.class);
        AvisClient avisClient1 = getAvisClientSample1();
        AvisClient avisClient2 = new AvisClient();
        assertThat(avisClient1).isNotEqualTo(avisClient2);

        avisClient2.setId(avisClient1.getId());
        assertThat(avisClient1).isEqualTo(avisClient2);

        avisClient2 = getAvisClientSample2();
        assertThat(avisClient1).isNotEqualTo(avisClient2);
    }

    @Test
    void clientTest() {
        AvisClient avisClient = getAvisClientRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        avisClient.setClient(clientBack);
        assertThat(avisClient.getClient()).isEqualTo(clientBack);

        avisClient.client(null);
        assertThat(avisClient.getClient()).isNull();
    }

    @Test
    void coachExpertTest() {
        AvisClient avisClient = getAvisClientRandomSampleGenerator();
        CoachExpert coachExpertBack = getCoachExpertRandomSampleGenerator();

        avisClient.setCoachExpert(coachExpertBack);
        assertThat(avisClient.getCoachExpert()).isEqualTo(coachExpertBack);

        avisClient.coachExpert(null);
        assertThat(avisClient.getCoachExpert()).isNull();
    }
}
