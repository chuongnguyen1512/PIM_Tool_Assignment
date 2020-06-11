package com.elca.vn.socket;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver {

    private String multicastHost;
    private int multicastPort;

    public MulticastReceiver(String multicastHost, int multicastPort) {
        this.multicastHost = multicastHost;
        this.multicastPort = multicastPort;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MulticastReceiver.class);
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];

    /**
     * Receive UDP multicast message
     *
     * @param reloadData function to execute reload gui data
     * @throws Exception
     */
    public void receiveMessage(ReloadData reloadData) throws Exception {
        InetAddress group = null;
        try {
            socket = new MulticastSocket(multicastPort);
            group = InetAddress.getByName(multicastHost);
            socket.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                if (StringUtils.isNotBlank(received)) {
                    LOGGER.info("Receiving multicast socket data {}", received);
                    reloadData.execute(received);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Cannot receive multicast message", e);
        } finally {
            socket.leaveGroup(group);
            socket.close();
        }
    }

    public interface ReloadData {
        void execute(String eventMessage);
    }
}