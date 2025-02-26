package fr.coaching.maseance.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FactureTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Facture getFactureSample1() {
        return new Facture().id(1L);
    }

    public static Facture getFactureSample2() {
        return new Facture().id(2L);
    }

    public static Facture getFactureRandomSampleGenerator() {
        return new Facture().id(longCount.incrementAndGet());
    }
}
