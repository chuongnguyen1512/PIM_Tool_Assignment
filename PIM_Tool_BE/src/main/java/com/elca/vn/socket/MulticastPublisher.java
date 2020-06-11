package com.elca.vn.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Service
public class MulticastPublisher implements SocketPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MulticastPublisher.class);

    private final String multicastInetHost;
    private final int multicastInetPort;

    @Autowired
    public MulticastPublisher(@Value("${multicast.inet.host}") String multicastInetHost,
                              @Value("${multicast.inet.port}") int multicastInetPort) {
        this.multicastInetHost = multicastInetHost;
        this.multicastInetPort = multicastInetPort;
    }

    @Override
    public void sendSocketMessage(String message) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress group = InetAddress.getByName(multicastInetHost);
            byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, multicastInetPort);
            socket.send(packet);
        } catch (Exception e) {
            LOGGER.error("There is an exception when multicast message {} with inet info {}:{}",
                    message, multicastInetHost, multicastInetPort, e);
        }
    }
}