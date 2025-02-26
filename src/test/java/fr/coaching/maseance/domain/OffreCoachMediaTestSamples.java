package fr.coaching.maseance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OffreCoachMediaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OffreCoachMedia getOffreCoachMediaSample1() {
        return new OffreCoachMedia().id(1L).urlMedia("urlMedia1");
    }

    public static OffreCoachMedia getOffreCoachMediaSample2() {
        return new OffreCoachMedia().id(2L).urlMedia("urlMedia2");
    }

    public static OffreCoachMedia getOffreCoachMediaRandomSampleGenerator() {
        return new OffreCoachMedia().id(longCount.incrementAndGet()).urlMedia(UUID.randomUUID().toString());
    }
}
