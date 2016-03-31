package portfwd;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

//This is the intial commit
public class Controller {
    @FXML
    private TextField srcIP;
    @FXML
    private TextField srcPort;
    @FXML
    private TextField destIP;
    @FXML
    private TextField destPort;


    protected InetSocketAddress tempSource;
    protected InetSocketAddress tempDest;

    Helper helper = new Helper();
    private HashMap<InetSocketAddress,InetSocketAddress> HostPairs = new HashMap<InetSocketAddress, InetSocketAddress>();

    private boolean isSourceInputValid()
    {
        boolean b = false;
        if(!(srcIP.getText() == null || srcIP.getText().length() == 0) && !(srcPort.getText() == null || srcPort.getText().length() == 0))
        {
            try
            {
                InetSocketAddress test = new InetSocketAddress(srcIP.getText(), Integer.parseInt(srcPort.getText()));
                b = true;

            } catch(Exception e){

        }

        }

        return b;
    }

    private boolean isDestInputValid()
    {
        boolean b = false;
        if(!(destIP.getText() == null || destIP.getText().length() == 0) && !(destPort.getText() == null || destPort.getText().length() == 0))
        {
            try
            {
                InetSocketAddress test = new InetSocketAddress(destIP.getText(), Integer.parseInt(destPort.getText()));
                b = true;

            } catch(Exception e){

            }

        }

        return b;
    }


    public void routeButtonClicked()
    {
        System.out.println("Route button clicked ! ");


        if(isSourceInputValid() == true && isDestInputValid() == true)
        {
            tempSource = new InetSocketAddress(srcIP.getText(), Integer.parseInt(srcPort.getText()));
            tempDest = new InetSocketAddress(destIP.getText(), Integer.parseInt(destPort.getText()));

            helper.addHost(HostPairs,tempSource,tempDest);

            Iterator it = HostPairs.entrySet().iterator();

            while (it.hasNext())
            {
                Map.Entry pair = (Map.Entry)it.next();
                InetSocketAddress prntSrc = (InetSocketAddress) pair.getKey();
                InetSocketAddress prntDest = (InetSocketAddress) pair.getValue();

                System.out.println(prntSrc.getHostName() + "  " + prntSrc.getPort());
                System.out.println(prntDest.getHostName() + "  " + prntDest.getPort());

            }

        }

        else
        {
            System.out.println("Invalid input!");
        }

    }
}
