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

package io.celox.dialog;

import io.celox.utils.Utils;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Martin Pfeffer <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class DialogAbout {

    private static final String TAG = "DialogAbout";

    @SuppressWarnings("UnusedAssignment")
    public DialogAbout(Application app) {
        Stage dialogStage = new Stage(StageStyle.UTILITY);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setWidth(400);
        dialogStage.setHeight(220);
        dialogStage.setResizable(false);

        BorderPane borderPane = new BorderPane();

        borderPane.setPadding(new Insets(15));
        borderPane.setPrefWidth(Integer.MAX_VALUE);
        borderPane.setPrefHeight(Integer.MAX_VALUE);

        Scene scene = new Scene(borderPane);
        dialogStage.setScene(scene);
        dialogStage.show();
        Utils.closeOnEscAndExit(borderPane, scene);

        // Top
        Label textTitle = new Label("ABOUT");
        textTitle.setStyle("-fx-font-size: 18px;");

        HBox hBoxTop = new HBox(0);
        hBoxTop.getChildren().addAll(textTitle);
        borderPane.setTop(hBoxTop);

        // Center
        Label textMsg = new Label("Created by Martin Pfeffer (2015-2017)");
        textMsg.setStyle("-fx-font-size: 14px;");

        GridPane grid = new GridPane();
        grid.setHgap(10);

        int row = 0;
        Hyperlink linkWebsite = new Hyperlink("https://celox.io");
        grid.add(new Text("Website:"), 0, row);
        grid.add(linkWebsite, 1, row++);

        Hyperlink linkGithub = new Hyperlink("https://github.com/pepperonas");
        grid.add(new Text("Github:"), 0, row);
        grid.add(linkGithub, 1, row++);

        Hyperlink linkMail = new Hyperlink("martin.pfeffer@celox.io");
        grid.add(new Text("Mail:"), 0, row);
        grid.add(linkMail, 1, row++);

        setClickHandler(app, linkWebsite, linkGithub, linkMail);
        styleHyperlink(linkWebsite, linkGithub, linkMail);
        grid.setStyle("-fx-font-size: 14px;");

        HBox hBoxInputPane = new HBox(10);
        hBoxInputPane.setAlignment(Pos.CENTER);

        VBox vBoxCenter = new VBox(10);
        vBoxCenter.setPadding(new Insets(15, 0, 15, 0));

        VBox vBoxCenterSub = new VBox(0, grid);
        vBoxCenter.getChildren().addAll(textMsg, vBoxCenterSub);
        borderPane.setCenter(vBoxCenter);

        // Bottom
        HBox hBoxBottom = new HBox();
        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hBoxBottom.getChildren().addAll(spacer);
        borderPane.setBottom(hBoxBottom);
    }

    private void setClickHandler(Application app, Hyperlink... hyperlinks) {
        for (Hyperlink hyperlink : hyperlinks) {
            hyperlink.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
                if (!hyperlink.getText().contains("@")) {
                    app.getHostServices().showDocument(hyperlink.getText());
                } else if (hyperlink.getText().equals("martin.pfeffer@celox.io")) {
                    app.getHostServices().showDocument("mailto:" + hyperlink.getText());
                }
            });
        }
    }

    private void styleHyperlink(Hyperlink... hyperlinks) {
        for (Hyperlink hyperlink : hyperlinks) {
            hyperlink.setStyle("-fx-border-color: transparent; -fx-text-fill: #009688");
        }
    }
}
