# microservice

This project represents a fighting game between "heroes" and "villains", it is composed of 3 services, each of which has
2 replicas.

## Services
### hero-service
- Does not have an external IP address and can only be accessed by other consumers in the same cluster.
- Is connected to the `supes-hero` database which contains only one table named `hero`.
- Exposes a get endpoint `/heroes/random` which returns a random hero from the database.

### villain-service
- Does not have an external IP address and can only be accessed by other consumers in the same cluster.
- Is connected to the `supes-villain` database which contains only one table named `villain`.
- Exposes a get endpoint `/villains/random` which returns a random villain from the database.

### fight-service
- Has an external IP address, meaning that it is possible to interact with it.
- Interacts with both the `hero-service` and the `villain-service` by using a REST client.
- During its interaction with the other services, if a timeout occurred (no response for 500 ms), it retries 4 times 
before falling back to a default, predefined result.
- Exposes a get endpoint `/fight` which simulates the victor of a fight between a random hero and a random villain.


## Build and deployment

To build and deploy the different services, open a new terminal at the project's root and configure it to use the 
Minikube Docker daemon with:

```
# On Linux/Mac
eval $(minikube docker-env)

# On windows
minikube docker-env | Invoke-Expression
```

Then run the following instructions:

```
# To build and deploy the hero-service
cd hero-service; mvn clean package; docker build -f src/main/docker/Dockerfile.jvm -t workshop/hero-service:latest .; kubectl delete deployment hero-service; kubectl apply -f kubernetes/; cd ..

# To build and deploy the villain-service
cd villain-service; mvn clean package; docker build -f src/main/docker/Dockerfile.jvm -t workshop/villain-service:latest .; kubectl delete deployment villain-service; kubectl apply -f kubernetes/; cd ..

# To build and deploy the fight-service
cd fight-service; mvn clean package; docker build -f src/main/docker/Dockerfile.jvm -t workshop/fight-service:latest .; kubectl delete deployment fight-service; kubectl apply -f kubernetes/; cd ..

# To get the address of the fight-service
minikube service fight-service --url
```

# Chaos

A simple example of chaos engineering, it is a command-line tool that connects to the kubernetes cluster to delete every
10 seconds a running pod. It takes a parameter `--deplyment` which corresponds to the names of the pods to delete separated
by a space.

```
# To build
mvn package

# To run
java -jar target/quarkus-app/quarkus-run.jar --deployment='hero-service villain-service fight-service'
```