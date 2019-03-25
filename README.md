# Special Topics Build Tools Lab

## Objective

For this lab, you will learn a little more about Maven and Gradle, by converting a Maven project to a Gradle project.  The project itself is pretty simple, but has been constructed to demonstrate some common use cases where build tools can help you accomplish your goal.  The code maps strings from "Upper Underscore" case to "Lower Camel" case, leveraging the [Guava CaseFormat](https://github.com/google/guava/wiki/StringsExplained#caseformat) library.  The final artifact is a docker image that can be run and accepts an input string, converts it, and spits the output to `stdout`.

## Getting Started:

1. Copy the starter code from here into a new, private repository in your personal GitHub account using [these instructions](https://github.com/jeff-anderson-cscc/submitting-assignments-lab#copy-the-starter-code-into-a-new-private-repository-in-your-personal-github-account) substituting this repository URL ``https://github.com/jschmersal-cscc/special-topics-labs-build-tools`` for the one referenced in that document.  When adding a collaborator, be sure to add me ("jschmersal-cscc").
2. Create a new branch for your code changes as described in [these instructions](https://github.com/jeff-anderson-cscc/submitting-assignments-lab#before-you-start-coding)


## Completing the Assignment

1. The goal of this assignment is to migrate the existing Maven build infrastructure for this project to use Gradle instead.
    1. You _should_ be able to complete this assignment without having to change anything besides build scripts (but you are allowed to change other code if it makes your job easier).  Your finished project should not have a `pom.xml` file, and instead should have (at least) a `build.gradle` build script in its place.  You should include instructions for me on how you want me to build your project.  It would be handy if you replaced this README.md with that information.
    1. You _must not_ remove or change any of the tests in the [src/test](src/test) directory.  If you want to add extra tests, by all means do so.  Remember though that the point of this lab isn't to fix or modify the software, but rather to migrate from one build system to another.
    1. As you are migrating, you will notice a few things that Maven is handling for you (that will be critical parts of migrating to Gradle):
        1.  The first thing Maven is doing is helping manage dependencies.  If you look at the [pom.xml](pom.xml) you will see the `<dependencies>` section.  The inclusion of Guava is part of that.  If you were to [comment out](https://stackoverflow.com/questions/2757396/how-do-i-comment-out-a-block-of-tags-in-xml) the Guava dependency (I suggest you try it) and run `mvn clean compile` you will see that the code doesn't even compile.  Your first step in migrating to Gradle should likely be to get your code to compile with your Gradle build script.
        1.  The second thing Maven is doing is helping run unit tests. If you run `mvn test` you will see output similar to the below snippet.  Your next step should be to get your tests running in your Gradle build.
```
[INFO] --- maven-surefire-plugin:2.10:test (default-test) @ constant-to-camel ---
[INFO] Surefire report directory: /home/jts25/repos/special-topics-build-tools/target/surefire-reports

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running edu.cscc.topics.tools.build.ConstantToCamelTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.074 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
``` 
        1.  Another thing Maven is doing is building a so-called executable "Uber jar", using the Maven Assembly plugin (again, reference the [pom.xml](pom.xml)).  This helps application developers in two ways:
            1. When you have multiple dependent jars, sometimes classpath management and application distribution can be problematic.  For someone to use your library they need access to your maven metadata to know what to download (or you need to publish that list), and they need to set the [Java classpath](https://en.wikipedia.org/wiki/Classpath_(Java)) to include all of the jars necessary to run your application.  Java has some packaging formats to help with this, but they are somewhat niche usecases.  The assembly plugin has a goal that will take your application code and all of its dependencies, and shove them into a single jar file.   
            1. Another problem with running java programs sometimes is getting the command right to actually start your program from the command line.  Java has [a facility](https://docs.oracle.com/javase/tutorial/deployment/jar/run.html) to simplify the process for end users so they can just run your jar with a simple invocation (`java -jar <your-jar>.jar`).  The assembly plugin helps facilitate making your jar executable by letting you specify in your plugin configuration the class that contains your `main()` method you want as the entry point to your application. 
        1. Your next step should be augment your gradle build to get your jar built as an "uber" jar that is executable using `java -jar`.  You can easily test if this is working by running `java -jar <your-gradle-built-jar>.jar DOES_THIS_WORK` from the command line (expected output would be "doesThisWork").
        1. Maven is also useful in this case for building a docker image to make running our application portable.  When you look in the [pom.xml](pom.xml) you will see references to the `docker-maven-plugin`.  The `docker-maven-plugin` builds the docker image using the specified [Dockerfile](src/main/docker/Dockerfile) along with the artifact produced by the build (`constant-to-camel-1.0-SNAPSHOT.jar`).  You'll want to enhance your Gradle build to do the same.
        1. Finally, Maven is helping us run the integration tests with the `maven-failsafe-plugin`.  You should add this functionality to your gradle build script.  When the integration tests pass, you're done! 


## Hints
1. There is lots of good information on the internet, including Gradle's web site.  I encourage you to get familiar with Gradle tooling by trying out various tutorials (in particular you might find this one helpfule: https://guides.gradle.org/building-java-libraries/).  Please don't use an auto-conversion tool (if you find one that works).  The goal here is for you to learn something.
1. There are plenty of Gradle plugins available.  You'll want to look through a few for your use cases (e.g. interacting with Docker).  I'd suggest to use something that seems pretty straightforward and has good examples documented.  For the "uber jar" (sometimes called a "fat jar") you can decide if you want to use a plugin or just use the built-in jar task, with appropriate configuration.
1. The place you're most likely to change a non-build script is when you're building your `Dockerfile`.  The existing [Dockerfile](src/main/docker/Dockerfile) has the jar name explicitly coded, and depending on how you do things your gradle-produced artifact might not follow the same naming conventions.  If they don't match, you'll likely see something along the lines of this when you try to run your docker:
```Error: Unable to access jarfile /opt/constant-to-camel-1.0-SNAPSHOT.jar```

## Submitting Your Work

1. Create a pull request for your branch using [these instructions](https://github.com/jeff-anderson-cscc/submitting-assignments-lab#once-you-are-ready-to-submit-your-work-for-grading)
1. Submit the assignment in Blackboard as described in [these instructions](https://github.com/jeff-anderson-cscc/submitting-assignments-lab#once-your-pull-request-is-created-and-i-am-added-as-a-reviewer)

__NOTE: I will provide feedback via. comments in your pull request.__
If you need to amend your work after you issue your initial pull request:

1. Commit your updates
1. Push your changes to gitHub
1. Verify the new commits were automatically added to your open pull request
