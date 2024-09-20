# H______b_____ - B______ C_____ C________
A simplified e-commerce API with a single endpoint that performs a
checkout action. The single endpoint takes a list of watches and return the total cost.

# Running
Simply navigate to the project path and run the following command to start the server locally.
```
./gradlew bootRun
```
## DockerHub
Additionally, a docker images for the master branch are available at [DockerHub](https://hub.docker.com/r/joaofolino/hbbcc).
Use the following to pull it:
```
docker pull joaofolino/hbbcc
```
# Tests
Simply navigate to the project path and run the following command to start the server locally.
```
./gradlew test
```

# Approach
This application was developed from an endpoint description that encapsulated a specific business logic.
1. I started by describing tests to ensure the requirements on the business logic would be met;
2. Worked my way down do the data layer, to ensure I would have coverage for reasonably generic persistence of the necessary data structures;
3. Worked my way up from the business layer, to cover the expected requests.
4. Built the application from the bottom up, layer by layer.
5. Started the application and checked that everything was actually working as intended.
6. Finished documenting it.
7. Reviewed the requirements and made some additional changes to tests, storage and build.
8. Added CI/CD for master branch

# Improvements
Additional functionality that was not added but would be nice to have:
- I'm happy with the unit tests but would like to have had included more integration tests
- SWAGGER endpoints, for ease of integration
- Finish extreme case coverage, with more understanding of what the limits should look like
- Caching layer, to avoid direct access to a database