package fr.coaching.maseance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client()
            .id(1L)
            .nom("nom1")
            .prenom("prenom1")
            .adresseEmail("adresseEmail1")
            .numeroTelephone("numeroTelephone1")
            .ville("ville1")
            .codePostal(1)
            .numeroEtNomVoie("numeroEtNomVoie1")
            .urlPhotoProfil("urlPhotoProfil1");
    }

    public static Client getClientSample2() {
        return new Client()
            .id(2L)
            .nom("nom2")
            .prenom("prenom2")
            .adresseEmail("adresseEmail2")
            .numeroTelephone("numeroTelephone2")
            .ville("ville2")
            .codePostal(2)
            .numeroEtNomVoie("numeroEtNomVoie2")
            .urlPhotoProfil("urlPhotoProfil2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .adresseEmail(UUID.randomUUID().toString())
            .numeroTelephone(UUID.randomUUID().toString())
            .ville(UUID.randomUUID().toString())
            .codePostal(intCount.incrementAndGet())
            .numeroEtNomVoie(UUID.randomUUID().toString())
            .urlPhotoProfil(UUID.randomUUID().toString());
    }
}
