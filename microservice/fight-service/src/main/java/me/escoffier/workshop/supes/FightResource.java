package me.escoffier.workshop.supes;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Random;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class FightResource {

    private static final Logger LOGGER = Logger.getLogger(FightResource.class);
    @Inject @RestClient HeroServiceClient heroClient;
    @Inject @RestClient VillainServiceClient villainClient;

    @GET
    @Counted("fight-service.fight.invocations")
    @Timed("fight-service.fight.time")
    @Path("/fight")
    public Fight fight() {
        return fight(
                getRandomHero(),
                getRandomVillain()
        );
    }

    private Hero getRandomHero() {
        final Hero hero = heroClient.findRandom();
        LOGGER.debug("Found random hero " + hero);
        return hero;
    }

    private Villain getRandomVillain() {
        Villain villain = villainClient.findRandom();
        LOGGER.debug("Found random villain " + villain);
        return villain;
    }

    private final Random random = new Random();

    private Fight fight(Hero hero, Villain villain) {
        int heroAdjust = random.nextInt(20);
        int villainAdjust = random.nextInt(20);

        if ((hero.level + heroAdjust) >= (villain.level + villainAdjust)) {
            return new Fight(hero, villain, hero.name);
        } else {
            return new Fight(hero, villain, villain.name);
        }
    }

}
