/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dan
 */
public class SelectionPanelController implements Initializable {
    
    @FXML
    private Button btn_crear_nuevo;
    @FXML
    private Button btn_cargar;
    @FXML
    private Button btn_salir;
    @FXML
    private Button btn_parameros;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btn_action_crear_nuevo(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("CreadorPanel.fxml"));
        } catch (IOException e) {
            System.err.println("Error reading: " + e);
            return;
        }
        Stage panel_preguntas = new Stage();
        Scene scene = new Scene(root);
        panel_preguntas.setResizable(false);
        panel_preguntas.setScene(scene);
        panel_preguntas.setTitle("Creador de horarios");
        panel_preguntas.show();
        ((Stage)btn_salir.getScene().getWindow()).close();
    }

    @FXML
    private void btn_action_cargar(ActionEvent event) {
        {
        Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("CargarSalvadoPanel.fxml"));
            } catch (IOException e) {
                System.err.println("Error reading: " + e);
                return;
            }
            Stage cargar_salvado_stage = new Stage();
            Scene scene = new Scene(root);
            cargar_salvado_stage.setResizable(false);
            cargar_salvado_stage.setScene(scene);
            cargar_salvado_stage.setTitle("Seleccionar horario guardado");
            cargar_salvado_stage.showAndWait();
        }
        
        var salvado = PanelsRegister.cargar_salvado_output;
        PanelsRegister.cargar_salvado_output = null;
        if (salvado == null) 
            return;
        PanelsRegister.creador_panel_input = salvado;
        
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("CreadorPanel.fxml"));
        } catch (IOException e) {
            System.err.println("Error reading: " + e);
            return;
        }
        Stage panel_creador = new Stage();
        Scene scene = new Scene(root);
        panel_creador.setResizable(false);
        panel_creador.setScene(scene);
        panel_creador.setTitle("Seleccionar horario guardado");
        panel_creador.show();
        ((Stage)btn_salir.getScene().getWindow()).close();
    }
    
    @FXML
    private void action_btn_parametros(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("ParametrosSemanaPanel.fxml"));
        } catch (IOException e) {
            System.err.println("Error reading: " + e);
            return;
        }
        Stage panel_parametros = new Stage();
        Scene scene = new Scene(root);
        panel_parametros.setResizable(false);
        panel_parametros.setScene(scene);
        panel_parametros.setTitle("Parámetros de semana");
        panel_parametros.show();
    }
    
    @FXML
    private void action_btn_salir(ActionEvent event) {
        System.exit(0);
    }
    
}
