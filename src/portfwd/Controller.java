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
    private TextField srcIP;
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



    protected InetSocketAddress tempSource;
    protected InetSocketAddress tempDest;

    Helper helper = new Helper();
    private HashMap<InetSocketAddress,InetSocketAddress> HostPairs = new HashMap<InetSocketAddress, InetSocketAddress>();

    private ObservableList<ForwardPair> TableData = FXCollections.observableArrayList();
    private ForwardPair tempFPO;

    private boolean isTableMade = false;



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

            tempFPO = new ForwardPair(tempSource.getHostName(),tempSource.getPort(),tempDest.getHostName(),tempDest.getPort());
            TableData.add(tempFPO);

            appConsole.appendText("\n Valid pair added!");


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
            appConsole.appendText("\nInvalid input!");
        }

        srcIP.clear();
        srcPort.clear();
        destIP.clear();
        destPort.clear();

        updateTable();

    }

    public void updateTable()
    {
        if (!isTableMade){
            PairTable.setEditable(true);
            TableColumn SourceHostname = new TableColumn("Src IP");
            SourceHostname.setCellValueFactory(new PropertyValueFactory<ForwardPair,String>("srcIP"));
            TableColumn SourcePortNum = new TableColumn("Src Port");
            SourcePortNum.setCellValueFactory(new PropertyValueFactory<ForwardPair,Integer>("srcPort"));

            TableColumn DestHostname = new TableColumn("Dest IP");
            DestHostname.setCellValueFactory(new PropertyValueFactory<ForwardPair,String>("destIP"));
            TableColumn DestPortNum = new TableColumn("Dest Port");
            DestPortNum.setCellValueFactory(new PropertyValueFactory<ForwardPair,Integer>("destPort"));


            PairTable.setItems(TableData);
            PairTable.getColumns().addAll(SourceHostname,SourcePortNum,DestHostname,DestPortNum);
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
