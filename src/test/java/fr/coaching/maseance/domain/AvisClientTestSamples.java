package fr.coaching.maseance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AvisClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AvisClient getAvisClientSample1() {
        return new AvisClient().id(1L).note(1).descriptionAvis("descriptionAvis1");
    }

    public static AvisClient getAvisClientSample2() {
        return new AvisClient().id(2L).note(2).descriptionAvis("descriptionAvis2");
    }

    public static AvisClient getAvisClientRandomSampleGenerator() {
        return new AvisClient()
            .id(longCount.incrementAndGet())
            .note(intCount.incrementAndGet())
            .descriptionAvis(UUID.randomUUID().toString());
    }
}
