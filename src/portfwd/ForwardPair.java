package portfwd;

/**
 * Created by Rizwan Ahmed on 2016-03-30.
 */
public class ForwardPair {
    public String srcIP;
    public int srcPort;
    public String destIP;
    public int destPort;

    public ForwardPair(String srcName, int srcP, String destName, int destP)
    {
        this.srcIP = srcName;
        this.srcPort = srcP;
        this.destIP = destName;
        this.destPort = destP;
    }

    public String getSrcIP() {
        return srcIP;
    }

    public void setSrcIP(String srcIP) {
        this.srcIP = srcIP;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(int srcPort) {
        this.srcPort = srcPort;
    }

    public String getDestIP() {
        return destIP;
    }

    public void setDestIP(String destIP) {
        this.destIP = destIP;
    }

    public int getDestPort() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }
}
