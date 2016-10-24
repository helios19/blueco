Setup Instructions
--

The microservice article-service has been implemented using gradle wrapper. Hence, to build it, simply run the following command
in a terminal, at the root of the source folder to generate the runnable fat jar:

./gradlew clean build

NB: This command will run unit and integration tests before packaging
the fat jar. If you wish to speed up the build and discard them,
simply add this "-x test" option to the above command. Also, take note that,
given the list of dependencies libraries, the build process might vary from
1 to 4 mins depending on the local gradle repository and your network bandwidth.

Then, to run the application, simply type:

java -jar <SOURCE_FOLDER>/build/libs/article-service-1.0.jar

Alternatively, you could also use the bootRun gradle task to run the application (skipping the jar packaging altogether):

./gradlew bootRun


The endpoints will be available at the following URLs:

- GET http://localhost:8080/articles
- POST http://localhost:8080/articles
- GET http://localhost:8080/articles/{id}
- GET http://localhost:8080/tags/{tagName}/{date}


Requirement assumptions
--

Given the requirements the following list of assumptions have been made while designing the solution:

- The specification clearly mentioned two distinct resources (namely article and tag), however only one microservice
 (mainly related to article resource) has been implemented. This assumption has been made based on
 how the tag resource is being defined in the specs. It seems like a tag only exists through the article
 resource. In this case, it makes sense to provide both resource endpoints through the same microservice.
 Note, however, that if a tag was also related to other items (comment, video, text message), then it would
 have been advisable to harvest this resource into its own separate microservice.

- An additional URL endpoint has been implemented for the articles resource (GET /articles) in an effort to
provide an easy way to get a paginated and limited subset of articles (default limit set to 50).

- Even though no detailed technical requirements have been provided, the following technical points have been
considered while implementing the solution:

    - Discoverability and consistency provided by HATEAOS and HAL specification
    (only implemented for error handling and POST operation)
    - Vulnerability of the system through common XSS attacks
    - Performance and scalability of the system
    - Testability of the system from the start through BDD

Design and architecture decisions
--

The solution provided has been designed and implemented in a microservice architecture model,
although only one fat jar (covering both article and tag resources) has been produced
given the above assumptions. Yet, by approaching the design from a microservice perspective,
the system will benefit from it in the long run (loosely coupled on other services, providing
high scalability, flexibility on the technology changes and resilient to failures).

Hereafter is a shortlist of the other technical aspects characterizing the system:

**Microservice using Spring Boot**

Spring Boot being one of the most mature framework and currently leading the trend in microservice applications,
the decision has been made to use it along with Java given the wide support it has for this language.
However, note that Scala could also have been a good option as well (and was initially chosen
to implement the tag microservice if the latter had a less tied relationship
with article resource).

**HATEAOS**

HATEOAS along wit HAL specification have been used mainly for error messages and POST operation.

**Security**

A XSS filter (encoding and stripping out malicious code from request parameter) has been added to the system
and being used to check submitted request and header values.

**Performance**

A particular emphasis was place on the performance aspect of the system through
the use of NoSQL database - namely mongodb (the system is actually comprising an embedded version
of mongodb), caching strategies (using guava cache), Optimistic concurrency (ETag),
Pagination and result size limitation for GET operations.

**BDD and Testability**

The system has been coded entirely through BDD using spring-test and RESTAssured,
although Spock could have been utilized here (certainly preferred library for unit tests).


Miscellenaous
--

I have noticed a couple of minor typos in the requirements (maybe let intentionally) as per the following:

- In the specs, it can been seen: "The final endpoints, GET /tags/{tagName}/{date} will return ...",
while couple of lines below, another URL for the same tag resource is mentioned:
"The GET /tag/{tagName}/{date} endpoint should produce ...". I guess that's
a typo, but not 100% sure.

- Point 3. in the Deliverable section mentions the following: "... anything of interest about about the code ...",
which I guess is another minor typo.


Time taken for implementing the solution: roughly 1d