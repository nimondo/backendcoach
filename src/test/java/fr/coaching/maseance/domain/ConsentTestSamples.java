package fr.coaching.maseance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConsentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Consent getConsentSample1() {
        return new Consent().id(1L).email("email1");
    }

    public static Consent getConsentSample2() {
        return new Consent().id(2L).email("email2");
    }

    public static Consent getConsentRandomSampleGenerator() {
        return new Consent().id(longCount.incrementAndGet()).email(UUID.randomUUID().toString());
    }
}
