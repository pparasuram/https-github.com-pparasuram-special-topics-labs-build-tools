package edu.cscc.topics.tools.build;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ConstantToCamelIT {
    @Test
    public void replacesDefaultJavaStyleConstantWithCamelCase() throws InterruptedException {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        List<Image> images = dockerClient.listImagesCmd().withImageNameFilter("edu.cscc.topics.tools/constant-to-camel").exec();
        assertTrue( "Couldn't find image named 'edu.cscc.topics.tools/constant-to-camel'", images.size() > 0);
        Image image = images.get(0);
        CreateContainerResponse container = dockerClient.createContainerCmd(image.getId())
                .withTty(true)
                .withAttachStdin(true)
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        LogContainerCmd logContainerCmd = dockerClient.logContainerCmd(container.getId())
                .withStdOut(true)
                .withFollowStream(true)
                .withTailAll();

        StringBuilder stringBuilder = new StringBuilder();
        logContainerCmd.exec(new LogContainerResultCallback() {
                @Override
                public void onNext(Frame item) {
                    stringBuilder.append(new String(item.getPayload()));
                }
            }).awaitCompletion();

        dockerClient.removeContainerCmd(container.getId()).exec();

        assertTrue("expected '" + stringBuilder.toString() + "' to start with 'thisIsATestConstantName'",
                stringBuilder.toString().startsWith("thisIsATestConstantName"));
    }

    @Test
    public void replacesJavaStyleConstantWithCamelCase() throws InterruptedException {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        Image image = dockerClient.listImagesCmd().withImageNameFilter("edu.cscc.topics.tools/constant-to-camel").exec().get(0);
        CreateContainerResponse container = dockerClient.createContainerCmd(image.getId())
                .withTty(true)
                .withAttachStdin(true)
                .withCmd("CONVERT_ME_TO_CAMEL_CASE")
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        LogContainerCmd logContainerCmd = dockerClient.logContainerCmd(container.getId())
                .withStdOut(true)
                .withFollowStream(true)
                .withTailAll();

        StringBuilder stringBuilder = new StringBuilder();
        logContainerCmd.exec(new LogContainerResultCallback() {
            @Override
            public void onNext(Frame item) {
                stringBuilder.append(new String(item.getPayload()));
            }
        }).awaitCompletion();

        dockerClient.removeContainerCmd(container.getId()).exec();

        assertTrue(stringBuilder.toString().startsWith("convertMeToCamelCase"));
    }
}
