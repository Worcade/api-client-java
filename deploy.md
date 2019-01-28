## Deploying to maven central
1. Add the account details for OSSRH to `~/.gradle/gradle properties`:
    * `ossrhUser = username` (not email address)
    * `ossrhPassword = password`
1. Update the project version in [`build.gradle`](build.gradle) to a non-SNAPSHOT version
1. Run `./gradlew publish`
1. Go to https://oss.sonatype.org/#stagingRepositories and find the networcade repository (on the bottom)
1. Close the repo and wait until the Activity tab shows "Repository closed"
1. Release the artifacts
1. Wait. Could be less than a minute, could be more than an hour.
