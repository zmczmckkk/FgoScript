package commons.entity;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Stop;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;

/**
 * @author RENZHEHAO
 * @version 1.0.0
 * @ClassName FxWindow.java
 * @Description javafx window
 * @createTime 2020年01月16日 05:24:00
 */
public class FxWindow extends Application {
    public static FxWindow fxWindow;
    private static File chosenDir;
    @Override
    public void start(Stage primaryStage) throws Exception {
        String dir = System.getenv("USERPROFILE");
        DirectoryChooser jc = new DirectoryChooser();
        jc.setTitle("请选择路径");
        Stage s = new Stage();
        chosenDir = jc.showDialog(new Stage());
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static File getChosenDir() {
        return chosenDir;
    }

    public static void main(String[] args) {
        launch();
        fxWindow = FxWindow.fxWindow;
    }
}
