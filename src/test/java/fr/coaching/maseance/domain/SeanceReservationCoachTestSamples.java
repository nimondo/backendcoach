package fr.coaching.maseance.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SeanceReservationCoachTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SeanceReservationCoach getSeanceReservationCoachSample1() {
        return new SeanceReservationCoach().id(1L);
    }

    public static SeanceReservationCoach getSeanceReservationCoachSample2() {
        return new SeanceReservationCoach().id(2L);
    }

    public static SeanceReservationCoach getSeanceReservationCoachRandomSampleGenerator() {
        return new SeanceReservationCoach().id(longCount.incrementAndGet());
    }
}
