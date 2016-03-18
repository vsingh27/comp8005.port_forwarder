package portfwd; /**
 * Created by Rizwan Ahmed on 15/03/2016.
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.StandardSocketOptions;
import java.nio.channels.*;
import java.util.Map;
import java.util.TreeMap;

public class Helper {
    /**
     * @return
     */
    protected Map createMap() {
        Map<Object, Object> map = new TreeMap<Object, Object>();
        return map;
    }

    /**
     * @param map
     * @param key
     * @param value
     */
    protected void addObject(TreeMap map, Object key, Object value) {
        map.put(key, value);
    }

    /**
     * @param map
     * @param key
     */
    protected void removeObject(TreeMap map, Object key) {
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
        ssChannel.setOption(StandardSocketOptions.SO_LINGER, null);
        ssChannel.setOption(StandardSocketOptions.SO_REUSEADDR, null);

        return ssChannel;
    }

    /**
     * BindSSChannel is used to bind the ServerSocket returned by the the Bind() of ServerSocketChannel to the port
     *
     * @param ssc  The ServerSocketChannel to Bind
     * @param port The Listening port to Bind the ServerSocket
     * @return True if binding was successful, False if binding was unsuccessful
     */
    protected boolean BindSSChannel(ServerSocketChannel ssc, int port) {
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
    protected Selector CreateSelector() throws IOException {
        Selector selector = Selector.open();
        return selector;
    }

    /**
     * Registers a selector for the ACCEPT, READ, or WRITE
     *
     * @param ssc
     * @param selector
     * @param option   Operation Set Bit (1=OP_READ, 2=OP_WRITE, 3=OP_ACCEPT, 4=OP_CONNECT)
     * @return
     */
    protected boolean RegisterSelector(ServerSocketChannel ssc, Selector selector, int option) {
        try {

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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
