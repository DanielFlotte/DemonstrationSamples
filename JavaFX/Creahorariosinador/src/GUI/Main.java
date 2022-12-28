/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package GUI;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ResourceBundle;
/**
 *
 * @author dan
 */
public class Main extends Application {

    /**
     * @param args the command line arguments
     */
    
    public static final String file_path = getFilePath();
    public static final String saves_ext = ".ina";
    
    public static void main(String[] args) {
        System.out.println(file_path);
        launch(args);
    }
    
    @Override
    public void start(Stage stage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("SelectionPanel.fxml"));
            //root = FXMLLoader.load(getClass().getResource("CreadorPanel.fxml"));
        } catch (IOException e) {
            System.err.println("Error reading " + e);
            return;
        }
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Panel de selección");
        stage.show();
    }
    
    private static String getFilePath() {
        String file = null;
        try {
            var file_undecoded = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            file = URLDecoder.decode(file_undecoded, Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        if (file.substring(file.length() - 4, file.length()).equals(".jar")) {
            int last_char = -1;
            for (int i = file.length() - 1; i > 0; i--) {
                if (file.charAt(i) == '/' || file.charAt(i) == '\\') {
                    last_char = i;
                    break;
                }
            }
            file = file.substring(0, last_char + 1);
        } else {
            file += "/";
        }
        return file;
    }
    
}