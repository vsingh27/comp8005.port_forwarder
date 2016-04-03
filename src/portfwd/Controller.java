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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
    private int sourcePort = 0;
    private int destinationPort = 0;
    private String destinationIP = null;



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
        run();

    }

    public void run() {
        try {
            ConnectionTracker tracker = new ConnectionTracker();
            Helper helper = new Helper();

            //Create A ServerSocketChannel To Listen on the given port
            ServerSocketChannel servSockChannel = helper.makeNonBlockingServerSocketChannnel();
            //Bind ther serverSocketChannel to the port
            helper.bindServerSocketChannel(servSockChannel, sourcePort);

            //create a selector
            Selector selector = helper.createSelector();
            //register ServerSocketChannel with this selector with OP_ACCPET
            boolean registerSelectorOK = helper.registerSelector(servSockChannel, selector, 3);
            System.out.println("Listening on PORT " + sourcePort);
            ConnectionTracker connectionTracker = new ConnectionTracker();

            while (true) {
                int num = helper.select(selector);

                //IF no activity loop back
                if (num == 0) {
                    continue;
                }

                //Get the Keys of the corresponding activity and process one by one
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    //If There are new Connections
                    if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                        System.out.println("Accepting New Connection...");

                        SocketChannel sourceSocketChannel = servSockChannel.accept();
                        //Set this socketChannel to Non Blocking
                        helper.setSocketChannelNonBlocking(sourceSocketChannel);
                        //Register this socket For OP_READ
                        helper.registerSocketChannel(sourceSocketChannel, selector, 1);
                        //Make the forward Socket
                        SocketChannel newSocChannel = helper.makeForwardingSocket(sourceSocketChannel, this.destinationPort, this.destinationIP, selector);
                        //Add the Forward Socket and the client socket to the MAP
                        connectionTracker.populateSocketTracker(sourceSocketChannel, newSocChannel);
                        System.out.println("Connection Established with Server...");
                    } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                        SocketChannel receiveChannel = null;

                        try {
                            //Process the incoming data
                            receiveChannel = (SocketChannel) key.channel();
                            SocketChannel forwardChannel = tracker.getForwardingSocketFromSocketTracker(receiveChannel);
                            boolean processDataOK = helper.processData(receiveChannel,forwardChannel);
                            if(!processDataOK)
                            {
                                key.cancel();
                                try{
                                    receiveChannel.close();
                                }catch (IOException io)
                                {
                                    System.out.println("Error closing socket...");
                                    io.printStackTrace();
                                }
                            }
                        } catch (Exception io) {

                        }
                    }
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

    }
}
