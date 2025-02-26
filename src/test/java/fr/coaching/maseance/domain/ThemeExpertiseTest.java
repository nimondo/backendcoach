package fr.coaching.maseance.domain;

import static fr.coaching.maseance.domain.ThemeExpertiseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.coaching.maseance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ThemeExpertiseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThemeExpertise.class);
        ThemeExpertise themeExpertise1 = getThemeExpertiseSample1();
        ThemeExpertise themeExpertise2 = new ThemeExpertise();
        assertThat(themeExpertise1).isNotEqualTo(themeExpertise2);

        themeExpertise2.setId(themeExpertise1.getId());
        assertThat(themeExpertise1).isEqualTo(themeExpertise2);

        themeExpertise2 = getThemeExpertiseSample2();
        assertThat(themeExpertise1).isNotEqualTo(themeExpertise2);
    }
}
