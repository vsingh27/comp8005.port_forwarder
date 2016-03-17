package portfwd; /**
 * Created by Rizwan Ahmed on 15/03/2016.
 */

import java.io.IOException;
import java.net.InetSocketAddress;
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

    protected ServerSocketChannel makeNonBlockingServerSocketChannnel(InetSocketAddress address) throws IOException {

        //Opening up a server Socekt Channel
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //Set to non-blocking
        ssChannel.configureBlocking(false);
        return ssChannel;
    }
}
