package portfwd;

/**
 * Created by Rizwan Ahmed on 2016-03-30.
 */
public class HostAddress {
    public String ipAddress;
    public int portNum;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNum() {
        return portNum;
    }

    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }
}
