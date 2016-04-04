package portfwd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Helper
{
    private final int BUFFERSIZE = 2048;

    /**
     * @param map
     * @param key
     * @param value
     */
    protected void addHost(HashMap map, int key, InetSocketAddress value)
    {

        map.put(key, value);
    }

    /**
     * @param map
     * @param key
     */
    protected void removeHost(HashMap map, int key)
    {
        map.remove(key);
    }

    /**
     * To create a new ServerSocketChannel
     *
     * @return A ServerSocketChannel configured with Non Blocking option
     * @throws IOException
     */
    protected ServerSocketChannel makeNonBlockingServerSocketChannnel() throws IOException, SocketException {

        //Opening up a server Socket Channel
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //Set to non-blocking
        ssChannel.configureBlocking(false);
        //Set the options of this channel
        //In this case we will set it to SO_LINGER and SO_REUSEADDR
        //ssChannel.setOption(StandardSocketOptions.SO_LINGER, null);
        ssChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

        return ssChannel;
    }

    /**
     * BindSSChannel is used to bind the ServerSocket returned by the the Bind() of ServerSocketChannel to the port
     *
     * @param ssc  The ServerSocketChannel to Bind
     * @param port The Listening port to Bind the ServerSocket
     * @return True if binding was successful, False if binding was unsuccessful
     */
    protected boolean bindServerSocketChannel(ServerSocketChannel ssc, int port) {
        try {
            ServerSocket srvsock = ssc.socket();
            InetSocketAddress isa = new InetSocketAddress(port);
            srvsock.bind(isa);
        } catch (IOException io) {
            System.out.println(io.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Creates a New Selector
     *
     * @return Selector
     * @throws IOException
     */
    protected Selector createSelector() throws IOException {
        Selector selector = Selector.open();
        return selector;
    }


    /**
     * Registers a selector for the ACCEPT, READ, or WRITE
     *
     * @param ssc
     * @param selector
     * @param option   Operation Set Bit (1=OP_READ, 2=OP_WRITE, 3=OP_ACCEPT, 4=OP_CONNECT)
     * @return True on Success and False on Failure
     * @throws ClosedSelectorException
     * @throws IllegalBlockingModeException
     * @throws CancelledKeyException
     * @throws IllegalArgumentException
     * @throws ClosedChannelException
     */
    protected boolean registerSelector(ServerSocketChannel ssc, Selector selector, int option) throws ClosedSelectorException
        , IllegalBlockingModeException, CancelledKeyException, IllegalArgumentException, ClosedChannelException {
        switch (option) {
            case 1:
                ssc.register(selector, SelectionKey.OP_READ);
                System.out.println();
                return true;
            case 2:
                ssc.register(selector, SelectionKey.OP_WRITE);
                System.out.println();
                return true;
            case 3:
                ssc.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println();
                return true;
            case 4:
                ssc.register(selector, SelectionKey.OP_CONNECT);
                System.out.println();
                return true;
            default:
                return false;
        }
    }

    /**
     * Registers a selector for the ACCEPT, READ, or WRITE
     *
     * @param sc
     * @param selector
     * @param option   Operation Set Bit (1=OP_READ, 2=OP_WRITE, 3=OP_CONNECT)
     * @return True on Success and False on Failure
     * @throws ClosedSelectorException
     * @throws IllegalBlockingModeException
     * @throws CancelledKeyException
     * @throws IllegalArgumentException
     * @throws ClosedChannelException
     */
    protected boolean registerSocketChannel(SocketChannel sc, Selector selector, int option) throws ClosedSelectorException
        , IllegalBlockingModeException, CancelledKeyException, IllegalArgumentException, ClosedChannelException {
        switch (option) {
            case 1:
                sc.register(selector, SelectionKey.OP_READ);
                System.out.println();
                return true;
            case 2:
                sc.register(selector, SelectionKey.OP_WRITE);
                System.out.println();
                return true;
            case 3:
                sc.register(selector, SelectionKey.OP_CONNECT);
                System.out.println();
                return true;
            default:
                return false;
        }
    }

    /**
     * Creates a Socket Channel that is set in the non-blocking mode
     *
     * @return A Socket Channel
     * @throws IOException
     */
    protected SocketChannel makeNonBlockingSocketChannnel() throws IOException {
        SocketChannel sockChannel = SocketChannel.open();
        sockChannel.configureBlocking(false);
        //sockChannel.setOption(StandardSocketOptions.SO_LINGER, true);//Set the option to SO_LINGER
        sockChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);//Set the option to SO_REUSEADDR
        return sockChannel;
    }

    protected void setSocketChannelNonBlocking(SocketChannel sc) {
        try {
            sc.configureBlocking(false);
            // sc.setOption(StandardSocketOptions.SO_LINGER, null);//Set the option to SO_LINGER
            sc.setOption(StandardSocketOptions.SO_REUSEADDR, true);//Set the option to SO_REUSEADDR
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    /**
     * Connects the Socket Channel to the provided address
     *
     * @param sChannel The Socket Channel
     * @param addr     The Inet Address
     * @return True on success and False on Failure
     */
    protected boolean connectSocketChannel(SocketChannel sChannel, InetSocketAddress addr) {
        try {
            sChannel.connect(addr);
        } catch (IOException io) {
            io.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Closes the Socket Channel
     *
     * @param sChannel The Socket Channel to close
     * @return True on success and False on failure
     */
    protected boolean closeSocketChannel(SocketChannel sChannel) {
        try {
            sChannel.close();
        } catch (IOException io) {
            io.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Reads the Socket Channel into a ByteBuffer
     *
     * @param sChannel Socket Channel To read from
     * @return ByteBuffer
     */
    protected ByteBuffer readSocketChannel(SocketChannel sChannel) {
        ByteBuffer buf = ByteBuffer.allocate(1048);
        try {
            int bytesRead = sChannel.read(buf);
        } catch (IOException io) {
            io.printStackTrace();
        }

        return buf;
    }

    protected int select(Selector selector) {
        int num = 0;
        try {
            num = selector.select();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return num;
    }

    protected SocketChannel makeForwardingSocket(SocketChannel sc, int port, String hostIP, Selector selector) {
        InetSocketAddress address = new InetSocketAddress(hostIP, port);
        SocketChannel socChannel = null;
        try {
            socChannel = SocketChannel.open();
            //Connect to the forwarding host
            socChannel.connect(address);
            //register the socket for OP_READ
            registerSocketChannel(socChannel, selector, 1);

        } catch (IOException io) {
            io.printStackTrace();
            try {
                sc.close();
                socChannel.close();
            } catch (IOException io2) {
                io2.printStackTrace();
            }
        }
        return socChannel;
    }

    protected boolean processData(SocketChannel recieveChannel, SocketChannel forwardSocketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFERSIZE);
        int bytesRead;
        try {
            recieveChannel.read(buffer);
            buffer.flip();

            //if no data close socket
            if(buffer.limit() == 0)
            {
                return false;
            }

            forwardSocketChannel.write(buffer);
            System.out.println("Processed Data and sent to the corresponding socket....");
        } catch (IOException io) {
            io.printStackTrace();
        }
        return true;
    }

}