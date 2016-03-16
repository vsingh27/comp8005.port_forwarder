package portfwd; /**
 * Created by Rizwan Ahmed on 15/03/2016.
 */
import java.util.*;
public class Helper {

    public Map createMap()
    {
        Map<Object, Object> map = new HashMap<Object,Object>();
        return map;
    }

    public void addObjects(HashMap map,Object key, Object value)
    {
        map.put(key, value);

    }
}
