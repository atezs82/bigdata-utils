package org.zenworks.storm.stewer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.zenworks.common.cli.CliExecutor;
import org.zenworks.common.cli.CliExecutorException;
import org.zenworks.common.cli.ExecutorFactory;

import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ResourceBundle;


/**
 * Created by atezs82 on 8/9/14.
 */
// TODO start stop watching logs
// CONNECT TO TO STORM DIR, DISPLAY LOGS, START WATCHING THEM
// STOP SCROLLING TO THE END IF SCROLLBAR IS NOT THERE
// START STOP STORM COMPONENTS
// STORM START SCRIPT, STORM STOP SCRIPT INVOCATION
// REFRESH STORM TOPOLOGY (storm list) ON A TIMELY BASIS

public class MainWindowController implements Initializable {

    @FXML
    private Label statLabel;

    @FXML
    private TextArea stormDir;

    @FXML
    private Button startLogWatch;

    @FXML
    private GridPane logGrid;

    private Timeline refreshTask = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        logGrid.addRow(1,createBorderPane("Hello"),createBorderPane("Hello2"));
        logGrid.addRow(2,createBorderPane("Hello3"),createBorderPane("Hello4"));

    }


    @FXML
    void onStartLogWatch(ActionEvent event) {
        CliExecutor exec = ExecutorFactory.getLocalExecutor();

        try {
            // String listLogsCommand = "bash -c \'for i in %s/*; do echo $i; done\'";
            String listLogsCommand = "bash -x -c \"ls -d " + stormDir.getText() + " \"";
            String command = String.format(listLogsCommand,stormDir.getText());

            System.out.println(command);

            String files = exec.executeCommand(command);
            System.out.println("Files:`"+files+"`");
            for (String file : files.split("\n")) {
                System.out.println(stormDir.getText() + "/" + file);
            }
        } catch (CliExecutorException e) {
            e.printStackTrace();
        }
    }


    private void createBackgroundTask() {

        refreshTask = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {

            private SecureRandom random = new SecureRandom();

            @Override
            public void handle(ActionEvent actionEvent) {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {



                    }
                });
            }
        }));

        refreshTask.setCycleCount(Timeline.INDEFINITE);
        refreshTask.play();


    }

    private BorderPane createBorderPane(final String fileName) {

        BorderPane bPane = new BorderPane();
        final TextArea content = new TextArea();

        CliExecutor exec = ExecutorFactory.getLocalExecutor();

        try {
            content.setText(exec.executeCommand("ls /"));
        } catch (CliExecutorException e) {

        }

        bPane.setTop(new Label(fileName));

        bPane.setCenter(content);
        return bPane;

    }
}
