/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author dan
 */
public class ResultsPanelController implements Initializable {

    @FXML
    private TextArea text_area;
    @FXML
    private Button guardar_btn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        text_area.setText(PanelsRegister.results_panel_input);
        text_area.setScrollTop(Double.MIN_VALUE);
        text_area.appendText("");
        //text_area.setFont(Font.loadFont("file:resources/SnakeGameDemoRegular.ttf", 120));
//        try {
//            text_area.setFont(Font.loadFont(ResultsPanelController.class.getResource("resources/SourceCodePro-Regular.ttf").toExternalForm(), 16));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }    

    @FXML
    private void action_guardar_btn(ActionEvent event) {
        PrintWriter writer;
        var file_name = nextFileAvailable();
        try {
            writer = new PrintWriter(file_name);
        } catch (FileNotFoundException e) {
            System.out.println("Error al iniciar PrintWriter: ");
            e.printStackTrace();
            return;
        }
        writer.print(text_area.getText());
        writer.close();
        
        Alert alert = new Alert(AlertType.INFORMATION, "Se ha guardado el archivo en " + file_name, ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle("Aviso");
        alert.show();
    }
    
    private String nextFileAvailable() {
        var jar_path = Main.file_path;
        for (int i = 1; true; i++) {
            String next_path_name = jar_path + "calculo_" + i + ".txt";
            if (!new File(next_path_name).exists())
                return next_path_name;
            
        }
    }
    
}
