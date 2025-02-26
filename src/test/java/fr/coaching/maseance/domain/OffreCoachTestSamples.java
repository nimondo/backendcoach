package fr.coaching.maseance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OffreCoachTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OffreCoach getOffreCoachSample1() {
        return new OffreCoach()
            .id(1L)
            .synthese("synthese1")
            .descriptionDetaillee("descriptionDetaillee1")
            .tarif(1L)
            .duree(1)
            .descriptionDiplome("descriptionDiplome1");
    }

    public static OffreCoach getOffreCoachSample2() {
        return new OffreCoach()
            .id(2L)
            .synthese("synthese2")
            .descriptionDetaillee("descriptionDetaillee2")
            .tarif(2L)
            .duree(2)
            .descriptionDiplome("descriptionDiplome2");
    }

    public static OffreCoach getOffreCoachRandomSampleGenerator() {
        return new OffreCoach()
            .id(longCount.incrementAndGet())
            .synthese(UUID.randomUUID().toString())
            .descriptionDetaillee(UUID.randomUUID().toString())
            .tarif(longCount.incrementAndGet())
            .duree(intCount.incrementAndGet())
            .descriptionDiplome(UUID.randomUUID().toString());
    }
}
