package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.CoachExpertTestSamples.*;
import static fr.coaching.maseance.domain.DisponibiliteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisponibiliteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disponibilite.class);
        Disponibilite disponibilite1 = getDisponibiliteSample1();
        Disponibilite disponibilite2 = new Disponibilite();
        assertThat(disponibilite1).isNotEqualTo(disponibilite2);

        disponibilite2.setId(disponibilite1.getId());
        assertThat(disponibilite1).isEqualTo(disponibilite2);

        disponibilite2 = getDisponibiliteSample2();
        assertThat(disponibilite1).isNotEqualTo(disponibilite2);
    }

    @Test
    void coachExpertTest() {
        Disponibilite disponibilite = getDisponibiliteRandomSampleGenerator();
        CoachExpert coachExpertBack = getCoachExpertRandomSampleGenerator();

        disponibilite.setCoachExpert(coachExpertBack);
        assertThat(disponibilite.getCoachExpert()).isEqualTo(coachExpertBack);

        disponibilite.coachExpert(null);
        assertThat(disponibilite.getCoachExpert()).isNull();
    }
}
