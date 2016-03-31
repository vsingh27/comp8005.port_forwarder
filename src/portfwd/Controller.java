package portfwd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.net.InetSocketAddress;
import java.util.*;

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
    @FXML
    private TableView<Pair<InetSocketAddress,InetSocketAddress>> PairTable;
    @FXML
    private TableColumn SourceHostname;
    @FXML
    private TableColumn SourcePortNum;
    @FXML
    private TableColumn DestHostname;
    @FXML
    private TableColumn DestPortNum;


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

                System.out.println(prntSrc.getHostName() + "  " + prntSrc.getPort() + "  " + prntDest.getHostName() + "  " + prntDest.getPort());

            }

        }

        else
        {
            System.out.println("Invalid input!");
        }

        srcIP.clear();
        srcPort.clear();
        destIP.clear();
        destPort.clear();

        updateTable();

    }

    public void updateTable()
    {
        PairTable.setEditable(true);

        ObservableList<Pair<InetSocketAddress,InetSocketAddress>> data1 = FXCollections.observableArrayList();


        //ObservableList<ForwardPair> data = FXCollections.observableArrayList();
        //ForwardPair temp = new ForwardPair();

        Iterator iter = HostPairs.entrySet().iterator();

        while (iter.hasNext())
        {
            Map.Entry pair = (Map.Entry)iter.next();

            InetSocketAddress tmpSrc = (InetSocketAddress) pair.getKey();
            InetSocketAddress tmpDest = (InetSocketAddress) pair.getValue();

            Pair dataVal = new Pair(tmpSrc,tmpDest);

            data1.add(dataVal);

            //temp.setSrcIP(tmpSrc.getHostName());
            //temp.setSrcPort(tmpSrc.getPort());
            //temp.setDestIP(tmpDest.getHostName());
            //temp.setDestPort(tmpDest.getPort());

            //data.add(temp);

        }

        PairTable.setItems(data1);

    }

    public void StartButtonClicked()
    {

    }

}
