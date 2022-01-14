package me.escoffier.workshop.supes;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeoutException;

@RegisterRestClient(configKey = "hero-service")
@Produces(MediaType.APPLICATION_JSON)
public interface HeroServiceClient {

    @Retry(retryOn = TimeoutException.class,
            maxRetries = 4, maxDuration = 10,
            durationUnit = ChronoUnit.SECONDS)
    @Timeout(value = 1, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getFallbackHero")
    @Path("/heroes/random")
    @GET
    Hero findRandom();

    default Hero getFallbackHero() {
        final Hero hero = new Hero();
        hero.name = "Luffy";
        return hero;
    }
}
