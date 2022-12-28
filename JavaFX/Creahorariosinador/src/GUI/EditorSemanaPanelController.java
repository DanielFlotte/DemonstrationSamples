/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import CalculoHorarios.Declarations;
import CalculoHorarios.Parametros;
import CalculoHorarios.Semana;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author dan
 */
public class EditorSemanaPanelController implements Initializable {

    @FXML
    private VBox vbox_de_seleccion;
    @FXML
    private VBox vbox_horas;
    @FXML
    private HBox hbox_dias;
    @FXML
    private TextField nombre_opcion;
    @FXML
    private Button btn_aceptar;
    @FXML
    private Button btn_borrar;
    
    private Set<String> nombres_prohibidos;
    private static final double max_size = 9999;
    private Parametros parametros = Declarations.parametros;
    
    private final String btn_style_pressed = "-fx-background-color: #D1F7FF; -fx-border-color: black;";
    private final String btn_style_unpressed = "-fx-background-color: #660066; -fx-border-color: black;";
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (!PanelsRegister.editor_semana_input_3) {
            btn_borrar.setVisible(false);
        }
        PanelsRegister.editor_semana_input_3 = false;
        PanelsRegister.editor_semana_output_1 = null;
        
        nombres_prohibidos = PanelsRegister.editor_semana_input_1;
        PanelsRegister.editor_semana_input_1 = null;
        
        for (int i = 0; i < Declarations.parametros.horas_por_dia; i++) {
            var next_vbox = new HBox();
            next_vbox.setPrefSize(max_size, max_size);
            for (int j = 0; j < parametros.dias_por_semana; j++) {
                var next_btn = new ToggleButton();
                next_btn.setText("");
                next_btn.setPrefSize(max_size, max_size);
                next_btn.getStyleClass().add("hora_toggle_button");
                next_vbox.getChildren().add(next_btn);
            }
            vbox_de_seleccion.getChildren().add(next_vbox);
            
            var hora_label = new Label();
            hora_label.setPrefSize(max_size, max_size);
            hora_label.setText(String.valueOf(i + parametros.primera_hora) + ":00");
            hora_label.setFont(Font.font("System", 18));
            hora_label.getStyleClass().add("labels");

            vbox_horas.getChildren().add(hora_label);
        }
        
        for (int i = 0; i < parametros.dias_por_semana; i++) {
            var dia_label = new Label();
            dia_label.setPrefSize(max_size, max_size);
            dia_label.setText(Declarations.getNombreDia(i));
            dia_label.setFont(Font.font("System", 22));
            dia_label.setAlignment(Pos.BOTTOM_CENTER);
            dia_label.getStyleClass().add("labels");
            hbox_dias.getChildren().add(dia_label);
        }
        
        var semana_precalculada = PanelsRegister.editor_semana_input_2;
        PanelsRegister.editor_semana_input_2 = null;
        if (semana_precalculada == null) {
            nombre_opcion.setText("Opción " + (nombres_prohibidos.size() + 1));
        } else {
            nombre_opcion.setText(semana_precalculada.nombre);
            for (int i = 0; i < parametros.dias_por_semana; i++) {
                for (int j = 0; j < parametros.horas_por_dia; j++) {
                    var hora = (HBox)vbox_de_seleccion.getChildren().get(j);
                    var button = (ToggleButton)hora.getChildren().get(i);
                    button.setSelected(semana_precalculada.horarios.get(i).get(j));
                }
            }
        }
    }
    
    @FXML
    private void action_btn_aceptar(ActionEvent event) {
        boolean button_pressed = false;
        for (Node i : vbox_de_seleccion.getChildren()) {
            var fila = (HBox)i;
            for (var j : fila.getChildren()) {
                var boton = (ToggleButton)j;
                if (boton.isSelected()) {
                    button_pressed = true;
                    break;
                }
            }
        }
        
        if (!button_pressed) {
            alert("Hora vacía");
            return;
        }
        
        if (nombre_opcion.getText().isEmpty()) {
            alert("Insertar nombre de materia");
            return;
        }
        
        if (nombres_prohibidos.contains(nombre_opcion.getText())) {
            alert("Ya existe una opción para ésta materia con éste nombre");
            return;
        }
        
        capturarDatos();
        
        ((Stage)btn_aceptar.getScene().getWindow()).close();
    }
    
    @FXML
    private void action_btn_borrar(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "¿De verdad desea borrar esta opción?", ButtonType.NO, ButtonType.YES);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        var result = alert.showAndWait();
        if (result.get() == ButtonType.NO)
            return;
        PanelsRegister.editor_semana_output_2 = true;
        ((Stage)btn_aceptar.getScene().getWindow()).close();
    }
    
    private void capturarDatos() {
        var semana = new Semana();
        for (int i = 0; i < parametros.dias_por_semana; i++) {
            for (int j = 0; j < parametros.horas_por_dia; j++) {
                var linea_horas = (HBox)vbox_de_seleccion.getChildren().get(j);
                var boton = (ToggleButton)linea_horas.getChildren().get(i);
                if (boton == null)
                    System.out.println("!!!!!!!!!");
                semana.get(i).set(j, boton.isSelected());
            }
        }
        
        var nombre = nombre_opcion.getText();
        PanelsRegister.editor_semana_output_1 = new OpcionClase(nombre, semana);
    }
    
    private void alert(String msg) {
        Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.show();
    }
    
}

class OpcionClase {
    String nombre;
    Semana horarios;
    public OpcionClase(String nombre, Semana horarios) {
        this.nombre = nombre;
        this.horarios = horarios;
    }
}
