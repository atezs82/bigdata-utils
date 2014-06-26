package org.zenworks.redis.buddy;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.TimerTask;

import javax.management.timer.Timer;
import javax.security.auth.Refreshable;
import javax.swing.event.HyperlinkEvent.EventType;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class MainWindowController implements Initializable {
	
	private RedisAdapter ownAdapter;
	
	@FXML
    private Button filterBtn;

    @FXML
    private CheckBox fetchKeys;

    @FXML
    private Button refreshRedisView;

    @FXML
    private TextField refreshInterval;

    @FXML
    private TextField filterContent;

    @FXML
    private GridPane redisView;

    @FXML
    private Button addSelectedKey;

    @FXML
    private Button connectToRedis;

    @FXML
    private Button startStopRefresh;

    @FXML
    private ListView<String> redisKeys;

    @FXML
    private ComboBox<String> redisConnectString;

    @FXML
    private CheckBox fetchValues;
    
    @FXML
    private TextField specificKey;
    
    private Timeline refreshTask = null;
    
    List<String> watchList;
    private String filterExpression = "*";
    private int MAX_STRING_LENGTH = 125;
    
	public void initialize(URL arg0, ResourceBundle arg1) {
		for (String favConnect:Config.redisConnectString) {
	        redisConnectString.getItems().add(favConnect);
	 	}
		
		if (Config.redisConnectString.length>0) {
			redisConnectString.getEditor().replaceSelection(Config.redisConnectString[0]);
			redisConnectString.getSelectionModel().selectFirst();
	 		onConnectToRedis(null);
		}
		
		redisKeys.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		watchList = new ArrayList<String>();
		
	}
	
    @FXML
    void onRefresh(ActionEvent event) {
    	refreshRedisKeyList();
    	refreshRedisWatchList();
    }
    
    @FXML
    void onDelete(ActionEvent event) {
    	for (String item:redisKeys.getSelectionModel().getSelectedItems()) {
    		ownAdapter.delete(item);
    	}
    	refreshRedisKeyList();
    	refreshRedisWatchList();
    }

    @FXML
    void onFlushAll(ActionEvent event) {
    	ownAdapter.flushAll();
    	refreshRedisKeyList();
    	refreshRedisWatchList();
    }

    @FXML
    void onStartRefresh(ActionEvent event) {
    	if (refreshTask != null) {
    		refreshTask.stop();
    		startStopRefresh.setText("Start refresh");
    		refreshTask=null;
    	} else {
	    	int duration=1000;
	    	try {
	    		duration = Integer.valueOf(refreshInterval.getText());    		
	    	} catch (NumberFormatException exc) {    		
	    	}
	    	refreshTask = new Timeline(new KeyFrame(Duration.millis(duration), new EventHandler<ActionEvent>() {
	
					public void handle(ActionEvent arg0) {
						if (fetchKeys.isSelected()) {
							refreshRedisKeyList();
						}
						if (fetchValues.isSelected()) {
							refreshRedisWatchList();
						}
					}
	    			
	    		}));
			refreshTask.setCycleCount(Timeline.INDEFINITE);
			refreshTask.play();
			startStopRefresh.setText("Stop refresh");
    	}
    	
    }

    @FXML
    void onFilterKeys(ActionEvent event) {
    	filterExpression = (filterContent.getText().equals("")?"*":filterContent.getText());
    	refreshRedisKeyList();
    }

    @FXML
    void onAddRedisKey(ActionEvent event) {
    	
    	
    	for (String item:redisKeys.getSelectionModel().getSelectedItems()) {
    		if (!watchList.contains(item)) {
    			watchList.add(item);
    	    	refreshRedisWatchList();	
    		}
    	}
    	
    }

    @FXML
    void onConnectToRedis(ActionEvent event) {
       ownAdapter = new RedisAdapter(redisConnectString.getEditor().getText().split(":")[0], Integer.valueOf(redisConnectString.getEditor().getText().split(":")[1]));
       refreshRedisKeyList();       
    }
    
    @FXML
    void onAddspecific(ActionEvent event) {
    	if (!watchList.contains(specificKey.getText())) {
    		watchList.add(specificKey.getText());
        	refreshRedisWatchList();	
    	}    	
    }

    @FXML
    void onImport(ActionEvent event) {

    }

    @FXML
    void onExport(ActionEvent event) {

    }
    
    private void refreshRedisKeyList() {
		 redisKeys.getItems().clear();
		 for (String key:ownAdapter.getKeys(filterExpression)) {
		   redisKeys.getItems().add(key);		   
		 }    	
    }
    
    private void refreshRedisWatchList() {
    	redisView.getChildren().clear();
    	redisView.getRowConstraints().clear();
    	redisView.getColumnConstraints().clear();
    	   	
    	int rowIndex = 0;
    	for (String key:watchList) {
    		String keyType = ownAdapter.getType(key); 
    		switch(keyType) {
	    		case "string":
	    			redisView.addRow(rowIndex, createRedisKeyLabel(key), createRedisKeyTypeLabel(ownAdapter.getType(key)), createStringKeyNode(ownAdapter.getStringKey(key)), createRemoveButton(rowIndex));
	        		break;
	    		case "list":
	    			redisView.addRow(rowIndex, createRedisKeyLabel(key), createRedisKeyTypeLabel(ownAdapter.getType(key)), createListKeyNode(ownAdapter.getListContents(key)),createRemoveButton(rowIndex));
	        		break;
	    		case "hash":
	    			redisView.addRow(rowIndex, createRedisKeyLabel(key), createRedisKeyTypeLabel(ownAdapter.getType(key)), createHashKeyNode(ownAdapter.getHashKey(key)),createRemoveButton(rowIndex));
	        		break;
	    		case "none":
	    			redisView.addRow(rowIndex, createRedisKeyLabel(key), createRedisKeyTypeLabel("<not existing>"), new Label("-"),createRemoveButton(rowIndex));
	    			
	    	}
			rowIndex = rowIndex + 1;
    	}
    	redisView.getRowConstraints().add(new RowConstraints(20));
    	
    	ColumnConstraints colConstraints = new ColumnConstraints();
    	colConstraints.setHgrow(Priority.ALWAYS);
    	colConstraints.setFillWidth(true);    	
    	redisView.getColumnConstraints().addAll(new ColumnConstraints(150),new ColumnConstraints(100), colConstraints, new ColumnConstraints(20));
    	
    }
    
    private Node createRemoveButton(final int key) {
		Button removeButton = new Button("X");
		removeButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				watchList.remove(key);
				refreshRedisWatchList();
			}
		});
		return removeButton;
	}

	private Node createHashKeyNode(Map<String, String> hashKey) {
    	String result = "";
    	for (Entry<String,String> e:hashKey.entrySet()) {
           result = result + "[ "+e.getKey()+" -> "+e.getValue() + " ]";    	
    	}
    	return new Label(trimToLength(result));    	
    }
	
	private Node createStringKeyNode(String value) {
		return new Label(trimToLength(value));
	}

	private String trimToLength(String value) {
		return value.length()<=MAX_STRING_LENGTH?value:value.substring(0, MAX_STRING_LENGTH);
	}

	private Node createListKeyNode(List<String> listContents) {
		String result = "";
		for (String s:listContents) {
			result=result+"["+s+"]";
		}
		return new Label(trimToLength(result));
	}

	private Node createRedisKeyTypeLabel(final String type) {
    	Label result = new Label(type);    	
    	return result;	
    }
    
    private Node createRedisKeyLabel(final String key) {
    	Label result = new Label(key);    	
    	return result;
    }   

}
