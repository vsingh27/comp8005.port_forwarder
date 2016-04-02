package portfwd;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

/**
 * Created by vishavpreet.singh on 4/2/2016.
 */
public class ConnectionTracker {

    //protected HashMap<Integer, InetSocketAddress> portTracker;//To keep track of ports to the forwarding IP and Port
    protected HashMap<Integer, SocketChannel> socketTracker;//To track sockets; whcihc socket to forward data to

    ConnectionTracker()
    {
        //this.portTracker = new HashMap<Integer, InetSocketAddress>();
        this.socketTracker = new HashMap<Integer, SocketChannel>();
    }


    /*
    protected void addToPortTracker(int sourcePort, int destinationPort, String hostName)
    {
        //Check if the entry Already exists
        if(portTracker.get(sourcePort) == null)
        {
            InetSocketAddress addr = generateInetSockAddress(destinationPort, hostName);
            portTracker.put(sourcePort, addr);
        }
        else {
            //logger will go here
            System.out.println("Port Already Exists...");
        }
    }
    */
    protected InetSocketAddress generateInetSockAddress(int port, String hostName)
    {
        InetSocketAddress addr = new InetSocketAddress(hostName,port);
        return addr;
    }

    protected void populateSocketTracker(SocketChannel socChannel1, SocketChannel socChannel2)
    {
        socketTracker.put(socChannel1.hashCode(), socChannel2);
        socketTracker.put(socChannel2.hashCode(), socChannel1);
    }

    /**
     * Returns the corresponding socket
     * @param sockChannel
     * @return
     */
    protected SocketChannel getForwardingSocketFromSocketTracker(SocketChannel sockChannel)
    {
        return socketTracker.get(sockChannel.hashCode());
    }

}
