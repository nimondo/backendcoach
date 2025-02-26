package fr.coaching.maseance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SpecialiteExpertiseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SpecialiteExpertise getSpecialiteExpertiseSample1() {
        return new SpecialiteExpertise()
            .id(1L)
            .specialite("specialite1")
            .specialiteDescription("specialiteDescription1")
            .tarifMoyenHeure(1L)
            .dureeTarif("dureeTarif1")
            .urlPhoto("urlPhoto1");
    }

    public static SpecialiteExpertise getSpecialiteExpertiseSample2() {
        return new SpecialiteExpertise()
            .id(2L)
            .specialite("specialite2")
            .specialiteDescription("specialiteDescription2")
            .tarifMoyenHeure(2L)
            .dureeTarif("dureeTarif2")
            .urlPhoto("urlPhoto2");
    }

    public static SpecialiteExpertise getSpecialiteExpertiseRandomSampleGenerator() {
        return new SpecialiteExpertise()
            .id(longCount.incrementAndGet())
            .specialite(UUID.randomUUID().toString())
            .specialiteDescription(UUID.randomUUID().toString())
            .tarifMoyenHeure(longCount.incrementAndGet())
            .dureeTarif(UUID.randomUUID().toString())
            .urlPhoto(UUID.randomUUID().toString());
    }
}
