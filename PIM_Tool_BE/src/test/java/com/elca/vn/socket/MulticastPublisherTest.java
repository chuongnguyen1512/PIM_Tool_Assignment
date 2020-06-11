package com.elca.vn.socket;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Objects;

@RunWith(MockitoJUnitRunner.class)
public class MulticastPublisherTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MulticastPublisherTest.class);

    private String host = "230.0.0.0";
    private int port = 8085;
    private MulticastPublisher multicastPublisher;

    private MulticastSocket socket = null;
    private byte[] buf = new byte[256];
    private InetAddress group = null;

    @Before
    public void init() throws IOException {
        multicastPublisher = new MulticastPublisher(host, port);
        socket = new MulticastSocket(port);
        group = InetAddress.getByName(host);
        socket.joinGroup(group);
    }

    @After
    public void tearDown() throws IOException {
        if (Objects.nonNull(socket) && !socket.isClosed()) {
            socket.leaveGroup(group);
            socket.close();
        }
    }

    @Test
    public void shouldConsumeSuccessfullyAfterMulticasting() throws IOException {
        String message = "Hello message";

        //Send message
        LOGGER.info("Sending message: {}", message);
        multicastPublisher.sendSocketMessage(message);

        // Receive message
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(packet.getData(), 0, packet.getLength());

        LOGGER.info("Received message: {}", received);
        Assert.assertEquals(received, message);
    }
}
