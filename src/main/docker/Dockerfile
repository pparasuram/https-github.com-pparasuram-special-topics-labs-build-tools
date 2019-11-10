# We need to build upon a base image that has java, thus openjdk
FROM openjdk:8u181

# The docker-maven-plugin puts your artifact in maven/ when you configure it with <appendAssemblyId>false</appendAssemblyId>.
# This line puts it in /opt/ in the image we're building
ADD ./build/libs/constant-to-camel-1.0-SNAPSHOT.jar /opt/

# When the docker is run, execute /user/bin/java -jar /opt/constant-to-camel-1.0-SNAPSHOT.jar
ENTRYPOINT ["/usr/bin/java", "-jar", "/opt/constant-to-camel-1.0-SNAPSHOT.jar"]

# The default argument when running a container is "THIS_IS_A_TEST_CONSTANT_NAME"
CMD ["THIS_IS_A_TEST_CONSTANT_NAME"]
