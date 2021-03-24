# MonPoké Kotlin

This is an approach of the MonPoke coding exercise done in Kotlin, using JUnit for testing and Gradle for
the build system.

## Building

To build an output jar, run `./gradlew build` (or `gradlew build` on Windows), and it can be in the
`build/libs` folder with the name `MonPoke-1.0.0.jar`. To run it against the sample input/output, you can
run `java -jar build/libs/MonPoke-1.0.0.jar < sample_input.txt | diff - sample_output.txt`.

## Tests

The test suite can be found under the `src/test/kotlin` directory. To run the tests from the command line,
run `./gradlew test`.
