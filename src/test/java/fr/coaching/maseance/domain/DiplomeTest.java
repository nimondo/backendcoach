package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.CoachExpertTestSamples.*;
import static fr.coaching.maseance.domain.DiplomeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DiplomeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Diplome.class);
        Diplome diplome1 = getDiplomeSample1();
        Diplome diplome2 = new Diplome();
        assertThat(diplome1).isNotEqualTo(diplome2);

        diplome2.setId(diplome1.getId());
        assertThat(diplome1).isEqualTo(diplome2);

        diplome2 = getDiplomeSample2();
        assertThat(diplome1).isNotEqualTo(diplome2);
    }

    @Test
    void coachExpertTest() {
        Diplome diplome = getDiplomeRandomSampleGenerator();
        CoachExpert coachExpertBack = getCoachExpertRandomSampleGenerator();

        diplome.addCoachExpert(coachExpertBack);
        assertThat(diplome.getCoachExperts()).containsOnly(coachExpertBack);
        assertThat(coachExpertBack.getDiplomes()).containsOnly(diplome);

        diplome.removeCoachExpert(coachExpertBack);
        assertThat(diplome.getCoachExperts()).doesNotContain(coachExpertBack);
        assertThat(coachExpertBack.getDiplomes()).doesNotContain(diplome);

        diplome.coachExperts(new HashSet<>(Set.of(coachExpertBack)));
        assertThat(diplome.getCoachExperts()).containsOnly(coachExpertBack);
        assertThat(coachExpertBack.getDiplomes()).containsOnly(diplome);

        diplome.setCoachExperts(new HashSet<>());
        assertThat(diplome.getCoachExperts()).doesNotContain(coachExpertBack);
        assertThat(coachExpertBack.getDiplomes()).doesNotContain(diplome);
    }
}
