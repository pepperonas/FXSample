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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.net.ssl.HttpsURLConnection;

import io.celox.dialog.DialogAbout;
import io.celox.model.Todo;
import io.celox.utils.Database;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * @author Martin Pfeffer
 * <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class MainController implements Initializable {

    @SuppressWarnings("unused")
    private static final String TAG = "MainController";

    private static final boolean CREATE_DB = false;
    private static final boolean EMBEDDED_DB = true;

    private static final String USER_AGENT = "Mozilla/5.0";

    @FXML
    Label label;
    @FXML
    JFXButton button_store;
    @FXML
    JFXButton button_read;

    private Application mApp;

    void setApp(Application app) {
        this.mApp = app;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResourceBundle bundle = ResourceBundle.getBundle("UIResources", new Locale("de"));
        System.out.println("Testing resources... '" + bundle.getString("greeting") + "'");
        String greeting = bundle.getString("greeting");

        label.setText(greeting);

        if (CREATE_DB) {
            createDatabase();
        }
    }

    private void createDatabase() {
        Connection connection = Database.getConnection(EMBEDDED_DB);
        try {
            if (!EMBEDDED_DB) {
                connection.createStatement().execute("CREATE DATABASE samplefx");
            }
            connection.createStatement().execute("CREATE TABLE samplefx.TODOS " +
                    "(" +
                    "id INT NOT NULL PRIMARY KEY, " +
                    "title VARCHAR(150) NOT NULL, " +
                    "completed INT NOT NULL" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onBtnStore(ActionEvent actionEvent) {
        try {
            sendGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendGet() throws Exception {
        String url = "https://jsonplaceholder.typicode.com/todos";

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("Sending 'GET' request: " + url);
        System.out.println("Response: " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append("\n");
        }
        in.close();

        if (!response.toString().isEmpty()) {
            processJsonString(response);
        }
    }

    private void processJsonString(StringBuilder response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response.toString());
        List<Todo> todoList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            todoList.add(new Todo(
                    jsonObject.getInt("id"),
                    jsonObject.getString("title"),
                    jsonObject.getBoolean("completed")));
        }

        Connection connection = Database.getConnection(EMBEDDED_DB);
        try {
            Statement statement = connection.createStatement();
            for (Todo todo : todoList) {
                System.out.println(todo.toString());
                String sql = "INSERT INTO samplefx.TODOS (id, title, completed)" +
                        "VALUES (" + todo.getId() + ",'" + todo.getTitle() + "', " + (todo.isCompleted() ? "0" : "1") + ")";
                statement.execute(sql);
                System.out.println(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendPost() throws Exception {
        String url = "https://posttestserver.com/post.php?";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "dir=example&num=12345";

        con.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(con.getOutputStream());
        dataOutputStream.writeBytes(urlParameters);
        dataOutputStream.flush();
        dataOutputStream.close();

        int responseCode = con.getResponseCode();
        System.out.println("Sending 'POST': " + url);
        System.out.println("Post parameters: " + urlParameters);
        System.out.println("Response: " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append("\n");
        }
        in.close();

        System.out.println(response.toString());
    }

    public void onBtnRead(ActionEvent actionEvent) {
        Connection connection = Database.getConnection(EMBEDDED_DB);

        List<Todo> todos = new ArrayList<>();

        String query = "SELECT id, title, completed FROM samplefx.TODOS";

        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = statement.executeQuery(query)) {

            rs.last();
            int rowCount = rs.getRow();
            rs.first();
            if (rowCount > 0) {
                do {
                    todos.add(new Todo(
                            rs.getInt("id"),
                            rs.getString("title"),
                            (rs.getInt("completed") == 1)));
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Todo todo : todos) {
            System.out.println("===> " + todo.toString());
        }
    }

    public void onMenuAbout(ActionEvent actionEvent) {
        if (mApp != null) {
            new DialogAbout(mApp);
        }
    }

    public void onBtnPost(ActionEvent actionEvent) {
        try {
            sendPost();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
