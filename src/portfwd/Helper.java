package portfwd; /**
 * Created by Rizwan Ahmed on 15/03/2016.
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.util.Map;
import java.util.TreeMap;

public class Helper {

    public Map createMap() {
        Map<Object, Object> map = new TreeMap<Object, Object>();
        return map;
    }

    public void addObject(TreeMap map, Object key, Object value) {
        map.put(key, value);
    }

    public void removeObject(TreeMap map, Object key) {
        map.remove(key);
    }

    protected ServerSocketChannel makeNonBlockingServerSocketChannnel() throws IOException {

        //Opening up a server Socket Channel
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //Set to non-blocking
        ssChannel.configureBlocking(false);
        return ssChannel;
    }

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
}
