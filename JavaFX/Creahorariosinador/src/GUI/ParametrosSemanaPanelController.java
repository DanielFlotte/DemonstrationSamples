/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import CalculoHorarios.Declarations;
import CalculoHorarios.Parametros;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author dan
 */
public class ParametrosSemanaPanelController implements Initializable {

    @FXML
    private TextField tf_horas_por_dia;
    @FXML
    private TextField tf_dias_por_semana;
    @FXML
    private TextField tf_primera_hora;
    @FXML
    private Button btn_aceptar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tf_horas_por_dia.setText(String.valueOf(Declarations.parametros.horas_por_dia));
        tf_dias_por_semana.setText(String.valueOf(Declarations.parametros.dias_por_semana));
        tf_primera_hora.setText(String.valueOf(Declarations.parametros.primera_hora));
    }    

    @FXML
    private void action_btn_aceptar(ActionEvent event) {
        int horas_por_dia;
        int dias_por_semana;
        int primera_hora;
        try {
            horas_por_dia = Integer.parseInt(tf_horas_por_dia.getText());
            dias_por_semana = Integer.parseInt(tf_dias_por_semana.getText());
            primera_hora = Integer.parseInt(tf_primera_hora.getText());
            if (primera_hora + horas_por_dia > 24) 
                throw new RuntimeException("Error en el número de horas por día en base a la primera hora del día");
            if (dias_por_semana > 7)
                throw new RuntimeException("Solo se permiten hasta 7 días por semana");
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR, "Error en el formato de los números", ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.show();
            return;
        } catch (RuntimeException e) {
            Alert alert = new Alert(AlertType.ERROR, e.toString(), ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.show();
            return;
        }
        
        Declarations.parametros = new Parametros(horas_por_dia, primera_hora, dias_por_semana);
        
        ((Stage)btn_aceptar.getScene().getWindow()).close();
    }
    
}
