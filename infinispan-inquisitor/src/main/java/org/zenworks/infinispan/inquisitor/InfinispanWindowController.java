package org.zenworks.infinispan.inquisitor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.infinispan.client.hotrod.ServerStatistics;
import org.zenworks.gui.GuiUtils;

import java.util.Map;

import static org.zenworks.gui.GuiUtils.*;

public class InfinispanWindowController {

    @FXML
    private Button queryButton;

    @FXML
    private ComboBox<String> ipanHost;

    @FXML
    private TextArea ipanContent;

    @FXML
    private Button connectToIpan;

    @FXML
    private ComboBox<String> ipanKey;

    @FXML
    private Button addKeyWithContent;

    @FXML
    private Button refreshButton;

    @FXML
    private Label statLabel;

    InfinispanAdapter infinispanConnection;

    @FXML
    void initialize() {
        statLabel.setText("Not connected.");
    }

    @FXML
    void onConnectToIpan(ActionEvent event) {
        infinispanConnection = new InfinispanAdapter(ipanHost.getEditor().getText());
        enableControls(queryButton, ipanKey, refreshButton);
        onRefreshStats(null);
    }

    @FXML
    void onQueryKey(ActionEvent event) {
       if (infinispanConnection != null) {
           ipanContent.setText(infinispanConnection.getKey(ipanKey.getEditor().getText()).toString());
           if (!ipanKey.getEditor().getText().isEmpty()) {
               ipanKey.getItems().add(ipanKey.getEditor().getText());
           }
       }
    }

    @FXML
    void onRefreshStats(ActionEvent event) {
       if (infinispanConnection != null) {
           StringBuilder infinispanStatus = new StringBuilder();
           Map<String,String> props = infinispanConnection.getStats();
           infinispanStatus.append("Infinispan happily serving: ");
           infinispanStatus.append(props.get(ServerStatistics.CURRENT_NR_OF_ENTRIES));
           infinispanStatus.append(" number of entries. Hits/misses/remove-hits/remove-misses: ");
           infinispanStatus.append(props.get(ServerStatistics.HITS));
           infinispanStatus.append("/");
           infinispanStatus.append(props.get(ServerStatistics.MISSES));
           infinispanStatus.append("/");
           infinispanStatus.append(props.get(ServerStatistics.REMOVE_HITS));
           infinispanStatus.append("/");
           infinispanStatus.append(props.get(ServerStatistics.REMOVE_MISSES));
           infinispanStatus.append(" Retrieval/Store ratio: ");
           infinispanStatus.append(props.get(ServerStatistics.RETRIEVALS));
           infinispanStatus.append("/");
           infinispanStatus.append(props.get(ServerStatistics.STORES));
           infinispanStatus.append(". Server uptime: ");
           infinispanStatus.append(props.get(ServerStatistics.TIME_SINCE_START));
           infinispanStatus.append(" seconds.");
           statLabel.setText(infinispanStatus.toString());
       }

    }

}
