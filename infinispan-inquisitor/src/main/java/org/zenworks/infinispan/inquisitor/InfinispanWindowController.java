package org.zenworks.infinispan.inquisitor;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.infinispan.client.hotrod.ServerStatistics;
import org.zenworks.gui.DialogBox;
import org.zenworks.gui.GuiUtils;

import java.util.Date;
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

    @FXML
    private Label numOfEntries;

    @FXML
    private Label cacheMisses;

    @FXML
    private Label cacheRemovalMisses;

    @FXML
    private Label cacheStores;

    @FXML
    private Label cacheRemovalHits;

    @FXML
    private Label cacheHits;

    @FXML
    private Label serverUptime;

    @FXML
    private TextField refreshStatInterval;

    @FXML
    private Label cacheRetrievals;

    @FXML
    private Button refreshStatButton;

    InfinispanAdapter infinispanConnection;

    private Timeline refreshTask = null;

    @FXML
    void initialize() {
        onRefreshStats(null);
    }

    @FXML
    void onConnectToIpan(ActionEvent event) {
        infinispanConnection = new InfinispanAdapter(ipanHost.getEditor().getText());
        enableControls(queryButton, ipanKey, refreshButton,ipanContent);
        onRefreshStats(null);
        infinispanConnection.setKey("Toth","Attila");
        infinispanConnection.setKey("Toth2","Attila2");
        infinispanConnection.setKey("Toth33","Attila33");
    }

    @FXML
    void onQueryKey() {
        String queryKey = ipanKey.getEditor().getText();
        Object value = infinispanConnection.getKey(queryKey);
        if (value==null) {
            DialogBox.showMessageDialog("Key `"+queryKey+"` could not be found in Infinispan.");
        } else {
            ipanContent.setText((String) value);
        }
        if (!ipanKey.getItems().contains(queryKey)) {
           ipanKey.getItems().add(queryKey);
        }
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
    void onRefreshStatIval(ActionEvent event) {

        if (refreshTask == null) {

            int duration = Integer.valueOf(refreshStatInterval.getText());
            refreshTask = new Timeline(new KeyFrame(Duration.millis(duration), new EventHandler<ActionEvent>() {

                public void handle(ActionEvent arg0) {
                   onRefreshStats(null);
                }
            }));
            refreshTask.setCycleCount(Timeline.INDEFINITE);
            refreshTask.play();
            refreshButton.setText("STOP");
        } else {
            refreshTask.stop();
            refreshTask = null;
            refreshButton.setText("Start");
        }

    }

    @FXML
    void onRefreshStats(ActionEvent event) {
       if (infinispanConnection != null) {
           Map<String,String> props = infinispanConnection.getStats();
           numOfEntries.setText("Infinispan happily serving: " + props.get(ServerStatistics.CURRENT_NR_OF_ENTRIES) + " number of entries.");
           cacheHits.setText("Cache hits: " + props.get(ServerStatistics.HITS));
           cacheMisses.setText("Cache misses: " + props.get(ServerStatistics.MISSES));
           cacheRemovalHits.setText("Remove hits: " + props.get(ServerStatistics.REMOVE_HITS));
           cacheRemovalMisses.setText("Remove misses: " + props.get(ServerStatistics.REMOVE_MISSES));
           cacheRetrievals.setText("Retrievals: "+props.get(ServerStatistics.RETRIEVALS));
           cacheStores.setText("Stores: "+props.get(ServerStatistics.STORES));
           serverUptime.setText("Server uptime: " + props.get(ServerStatistics.TIME_SINCE_START));
           statLabel.setText("Connected to Infinispan at "+infinispanConnection.getHost()+". Stats refreshed at " + new Date().toString());
       } else {
           numOfEntries.setText("Infinispan happily serving: ? number of entries.");
           cacheHits.setText("Cache hits: ?");
           cacheMisses.setText("Cache misses: ?");
           cacheRemovalHits.setText("Remove hits: ?");
           cacheRemovalMisses.setText("Remove misses: ?");
           cacheRetrievals.setText("Retrievals: ?");
           cacheStores.setText("Stores: ?");
           serverUptime.setText("Server uptime: ?");
           statLabel.setText("Not connected to Infinispan. Stats refreshed at " + new Date().toString());
       }

    }

}
