/*
 * Copyright (c) 2017 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.celox.main;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import io.celox.dialog.DialogAbout;
import io.celox.utils.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Martin Pfeffer
 * <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class Main extends Application {

    @SuppressWarnings("unused")
    private static final String TAG = "Main";

    private static final boolean IS_CONTROLLER_CONFIG = true;

    @Override
    public void start(Stage primaryStage) throws IOException {
        if (IS_CONTROLLER_CONFIG) {
            makeConfigController(primaryStage);
        } else {
            makeConfigNoController(primaryStage);
        }
    }

    private void makeConfigController(Stage primaryStage) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("UIResources", new Locale("de"));
        System.out.println("testing lang... '" + bundle.getString("greeting") + "'");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"), bundle);
        Parent root = fxmlLoader.load();
        MainController controller = fxmlLoader.getController();
        controller.setApp(this);
        Scene scene = initScene(primaryStage, root);
        scene.getStylesheets().add("styles/styles.css");
        Utils.closeOnEscAndExit(root, scene);
    }

    private Scene initScene(Stage primaryStage, Parent root) {
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        Utils.closeOnEscAndExit(root, scene);

        return scene;
    }

    private void makeConfigNoController(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");

        BorderPane root = new BorderPane();

        // top
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem miAbout = new MenuItem("About");
        miAbout.setOnAction(t -> new DialogAbout(Main.this));
        menuFile.getItems().addAll(miAbout);
        menuBar.getMenus().addAll(menuFile);
        root.setTop(menuBar);

        // center
        JFXButton button = new JFXButton("OK");

        Label label = new Label();
        button.setOnAction(event -> label.setText("Clicked!"));
        VBox vBox = new VBox(15, button, label);
        vBox.setAlignment(Pos.CENTER);
        root.setCenter(vBox);

        initScene(primaryStage, root);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
