package fr.coaching.maseance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CoachExpertTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CoachExpert getCoachExpertSample1() {
        return new CoachExpert()
            .id(1L)
            .nom("nom1")
            .prenom("prenom1")
            .adresseEmail("adresseEmail1")
            .numeroTelephone("numeroTelephone1")
            .ville("ville1")
            .codePostal(1)
            .numeroEtNomVoie("numeroEtNomVoie1")
            .tarifActuel(1L)
            .urlPhotoProfil("urlPhotoProfil1")
            .bio("bio1");
    }

    public static CoachExpert getCoachExpertSample2() {
        return new CoachExpert()
            .id(2L)
            .nom("nom2")
            .prenom("prenom2")
            .adresseEmail("adresseEmail2")
            .numeroTelephone("numeroTelephone2")
            .ville("ville2")
            .codePostal(2)
            .numeroEtNomVoie("numeroEtNomVoie2")
            .tarifActuel(2L)
            .urlPhotoProfil("urlPhotoProfil2")
            .bio("bio2");
    }

    public static CoachExpert getCoachExpertRandomSampleGenerator() {
        return new CoachExpert()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .adresseEmail(UUID.randomUUID().toString())
            .numeroTelephone(UUID.randomUUID().toString())
            .ville(UUID.randomUUID().toString())
            .codePostal(intCount.incrementAndGet())
            .numeroEtNomVoie(UUID.randomUUID().toString())
            .tarifActuel(longCount.incrementAndGet())
            .urlPhotoProfil(UUID.randomUUID().toString())
            .bio(UUID.randomUUID().toString());
    }
}
