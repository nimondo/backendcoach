package fr.coaching.maseance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ThemeExpertiseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ThemeExpertise getThemeExpertiseSample1() {
        return new ThemeExpertise().id(1L).libelleExpertise("libelleExpertise1").urlPhoto("urlPhoto1");
    }

    public static ThemeExpertise getThemeExpertiseSample2() {
        return new ThemeExpertise().id(2L).libelleExpertise("libelleExpertise2").urlPhoto("urlPhoto2");
    }

    public static ThemeExpertise getThemeExpertiseRandomSampleGenerator() {
        return new ThemeExpertise()
            .id(longCount.incrementAndGet())
            .libelleExpertise(UUID.randomUUID().toString())
            .urlPhoto(UUID.randomUUID().toString());
    }
}
