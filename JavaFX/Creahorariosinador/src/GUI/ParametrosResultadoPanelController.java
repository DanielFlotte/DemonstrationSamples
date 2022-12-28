/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import CalculoHorarios.Evaluator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dan
 */
public class ParametrosResultadoPanelController implements Initializable {

    @FXML
    private TextField max_materias_label;
    @FXML
    private TextField max_results_label;
    @FXML
    private TextField obligatorias_label;
    @FXML
    private Button btn_aceptar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        max_materias_label.setText(String.valueOf(Evaluator.def_max_materias));
        max_results_label.setText(String.valueOf(Evaluator.def_max_results));
        obligatorias_label.setText(String.valueOf(Evaluator.def_obligatorias));
    }    

    @FXML
    private void action_btn_aceptar(ActionEvent event) {
        int new_max_materia;
        int new_max_results;
        int new_obligatorias;
        try {
            new_max_materia = Integer.parseInt(max_materias_label.getText());
            new_max_results = Integer.parseInt(max_results_label.getText());
            new_obligatorias = Integer.parseInt(obligatorias_label.getText());
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Valores incorrector", ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.show();
            return;
        }
        
        Evaluator.def_max_materias = new_max_materia;
        Evaluator.def_max_results = new_max_results;
        Evaluator.def_obligatorias = new_obligatorias;
        ((Stage)btn_aceptar.getScene().getWindow()).close();
    }
    
}
