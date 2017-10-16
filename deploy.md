## Deploying to maven central
1. Add the account details for OSSRH to `~/.gradle/gradle properties`:
  * `ossrhUser = username` (not email address)
  * `ossrhPassword = password`
1. Update the project version in [`build.gradle`](build.gradle)
1. Run `./gradlew uploadShadow`
1. Go to  https://oss.sonatype.org/#stagingRepositories and promote the artifact 

### Problems
* The `api-client-jersey` jar doesn't depend on `api-client`,
 because the shadow plugin doesn't support depending on another shadow jar.
