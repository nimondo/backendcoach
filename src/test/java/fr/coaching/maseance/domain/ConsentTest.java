package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.ConsentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consent.class);
        Consent consent1 = getConsentSample1();
        Consent consent2 = new Consent();
        assertThat(consent1).isNotEqualTo(consent2);

        consent2.setId(consent1.getId());
        assertThat(consent1).isEqualTo(consent2);

        consent2 = getConsentSample2();
        assertThat(consent1).isNotEqualTo(consent2);
    }
}
