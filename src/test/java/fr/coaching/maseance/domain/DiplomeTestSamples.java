package fr.coaching.maseance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DiplomeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Diplome getDiplomeSample1() {
        return new Diplome().id(1L).libelle("libelle1").nbAnneesEtudePostBac(1);
    }

    public static Diplome getDiplomeSample2() {
        return new Diplome().id(2L).libelle("libelle2").nbAnneesEtudePostBac(2);
    }

    public static Diplome getDiplomeRandomSampleGenerator() {
        return new Diplome()
            .id(longCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .nbAnneesEtudePostBac(intCount.incrementAndGet());
    }
}
