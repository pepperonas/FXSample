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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Martin Pfeffer
 * <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class DialogChart {

    public DialogChart() {
        Stage dialogStage = new Stage(StageStyle.UTILITY);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setWidth(1000);
        dialogStage.setHeight(500);

        BorderPane borderPane = new BorderPane();

        borderPane.setPadding(new Insets(15));
        borderPane.setPrefWidth(Integer.MAX_VALUE);
        borderPane.setPrefHeight(Integer.MAX_VALUE);

        Scene scene = new Scene(borderPane);
        dialogStage.setScene(scene);
        dialogStage.show();
        Utils.closeOnEscAndExit(borderPane, scene);

        // Top
        Label textTitle = new Label("Dialog");
        textTitle.setStyle("-fx-font-size: 18px;");
        borderPane.setTop(textTitle);

        VBox vBox = new VBox();

        borderPane.setCenter(vBox);
    }
}
