package org.webonise.ameya.scratchpad.Image;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class ImageViewPanel extends Application {
    public static int INITIAL_HEIGHT=768;
    public static int INITIAL_WIDTH=1024;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(INITIAL_HEIGHT);
        stage.setWidth(INITIAL_WIDTH);
        stage.setTitle("OpenGL Image View");

        final OpenGlPanel openGlPanel = new OpenGlPanel();
        openGlPanel.initialize();

        StackPane pane = new StackPane();
        pane.getChildren().add(openGlPanel);
        stage.setScene(new Scene(pane, INITIAL_HEIGHT, INITIAL_WIDTH));
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                openGlPanel.pauseImagePanelView();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
