package portfwd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import java.net.InetSocketAddress;
import java.util.*;

//This is the intial commit
public class Controller {
    @FXML
    private TextField srcPort;
    @FXML
    private TextField destIP;
    @FXML
    private TextField destPort;
    @FXML
    private TableView<ForwardPair> PairTable;
    @FXML
    private TextArea appConsole;



    protected int tempSrcPort;
    protected InetSocketAddress tempDest;

    Helper helper = new Helper();
    private HashMap<Integer ,InetSocketAddress> HostPairs = new HashMap<Integer, InetSocketAddress>();

    private ObservableList<ForwardPair> TableData = FXCollections.observableArrayList();
    private ForwardPair tempFPO;

    private boolean isTableMade = false;



    private boolean isSourceInputValid()
    {
        if(!(srcPort.getText() == null || srcPort.getText().length() == 0)) {

            return true;

        }
        else
        {
            return false;

        }
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
            tempSrcPort = Integer.parseInt(srcPort.getText());
            tempDest = new InetSocketAddress(destIP.getText(), Integer.parseInt(destPort.getText()));

            tempFPO = new ForwardPair(tempSrcPort,tempDest.getHostName(),tempDest.getPort());
            TableData.add(tempFPO);

            appConsole.appendText("\n Valid pair added!");


            helper.addHost(HostPairs,tempSrcPort,tempDest);

            Iterator it = HostPairs.entrySet().iterator();

            while (it.hasNext())
            {
                Map.Entry pair = (Map.Entry)it.next();

                int prntSrc = (Integer) pair.getKey();
                InetSocketAddress prntDest = (InetSocketAddress) pair.getValue();

                System.out.println("Forwarder's port: " + prntSrc + "  " + prntDest.getHostName() + "  " + prntDest.getPort());

            }

        }

        else
        {
            System.out.println("Invalid input!");
            appConsole.appendText("\nInvalid input!");
        }

        srcPort.clear();
        destIP.clear();
        destPort.clear();

        updateTable();

    }

    public void updateTable()
    {
        if (!isTableMade){
            PairTable.setEditable(true);
            TableColumn SourcePortNum = new TableColumn("Src Port");
            SourcePortNum.setCellValueFactory(new PropertyValueFactory<ForwardPair,Integer>("srcPort"));

            TableColumn DestHostname = new TableColumn("Dest IP");
            DestHostname.setCellValueFactory(new PropertyValueFactory<ForwardPair,String>("destIP"));
            TableColumn DestPortNum = new TableColumn("Dest Port");
            DestPortNum.setCellValueFactory(new PropertyValueFactory<ForwardPair,Integer>("destPort"));


            PairTable.setItems(TableData);
            PairTable.getColumns().addAll(SourcePortNum,DestHostname,DestPortNum);
            isTableMade = true;
        }

        else
            PairTable.setItems(TableData);

    }

    public void StartButtonClicked()
    {
        appConsole.appendText("\n Start button clicked!");

    }

}
