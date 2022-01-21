package me.escoffier.workshop;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.jboss.logging.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import javax.inject.Inject;
import java.util.List;
import java.util.Random;

@Command(name = "chaos", mixinStandardHelpOptions = true)
public class ChaosCommand implements Runnable {

    @CommandLine.Option(
        names = {"--deployment", "-d"}, required = true, description = "The targeted deployment")
    String deploymentParameter;

    @Inject KubernetesClient kubernetes;
    @Inject Logger logger;

    @Override
    public void run() {
        logger.infof("Connected to %s, current namespace is %s", kubernetes.getMasterUrl(), kubernetes.getNamespace());
        String[] deploymentsToKill;
        if (deploymentParameter.contains(" ")) deploymentsToKill = deploymentParameter.split(" ");
        else deploymentsToKill = new String[] {deploymentParameter};

        logger.info("Current deployments: ");
        List<Deployment> deployments = kubernetes.apps().deployments().list().getItems();
        for (Deployment dep : deployments) {
            logger.infof("Deployment %s", dep.getMetadata().getName());
        }

        Pod toDelete;
        logger.infof("Deployments to kill %s: ", deploymentParameter);
        final Random random = new Random();
        while (true) {
            toDelete = null;
            // Picking a random pod to kill.
            final String deploymentToKill = deploymentsToKill[random.nextInt(deploymentsToKill.length)];

            // Iterating through the pods looking for the
            for (Pod pod : kubernetes.pods().list().getItems()) {
                // If the current pod isn't running, skip.
                if (!pod.getStatus().getPhase().equals("Running")) continue;

                // If the name of the current pod corresponds, store it and leave the loop.
                if (pod.getMetadata().getName().contains(deploymentToKill)) {
                    toDelete = pod;
                    break;
                }
            }

            // Delete the pod if found.
            if (toDelete != null) {
                logger.infof("Deleting Pod %s - %s ...", toDelete.getMetadata().getName(), toDelete.getStatus().getPhase());
                if (kubernetes.pods().delete(toDelete)) {
                    logger.infof("Deleted Pod %s", toDelete.getMetadata().getName());
                }
            }

            // Sleeping for 10s
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException ignore) {
                logger.error("Interruption exception");
            }
        }
    }
}
