package portfwd;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.InetSocketAddress;

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


        if(isSourceInputValid() == true && isDestInputValid() == true){
        tempSource = new InetSocketAddress(srcIP.getText(), Integer.parseInt(srcPort.getText()));
        tempDest = new InetSocketAddress(destIP.getText(), Integer.parseInt(destPort.getText()));
        }

        else
        {
            System.out.println("Invalid input!");
        }

    }
}
