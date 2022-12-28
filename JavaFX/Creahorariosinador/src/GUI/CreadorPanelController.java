/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import CalculoHorarios.Evaluator;
import CalculoHorarios.ResultsPrinter;
import CalculoHorarios.Semana;
import CalculoHorarios.ConjuntoHorarios;
import CalculoHorarios.Declarations;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author dan
 */
public class CreadorPanelController implements Initializable {

    @FXML
    private VBox vbox_materias;
    @FXML
    private Button btn_nueva_materia;
    @FXML
    private VBox vbox_opciones;
    @FXML
    private Button btn_nueva_opcion;
    @FXML
    private Button btn_calcular;
    @FXML
    private Button btn_guardar;
    @FXML
    private Button btn_parametros;
    @FXML
    private Button btn_delete;
    @FXML
    private ScrollPane scrollpane_materias;
    @FXML
    private ScrollPane scrollpane_opciones;
    
    private ToggleGroup materias_toggle_group = new ToggleGroup();
    private Map<String, Map<String, Semana>> opciones_materias = new HashMap<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_nueva_opcion.setDisable(true);
        btn_delete.setDisable(true);
        if (PanelsRegister.creador_panel_input != null) {
            opciones_materias = PanelsRegister.creador_panel_input;
            PanelsRegister.creador_panel_input = null;
            for (var entry : opciones_materias.entrySet()) {
                var button = generateMateriasButton(entry.getKey());
                vbox_materias.getChildren().add(button);
            }
        }
    }

    @FXML
    private void action_btn_nueva_materia(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.getDialogPane().setContentText("Nombre de la materia:");
        dialog.setHeaderText(null);
        dialog.setTitle("Insertar nombre de materia");
        var answer = dialog.showAndWait();
        if (!answer.isPresent())
            return;
        String nueva_materia = dialog.getEditor().getText();
        if (opciones_materias.containsKey(nueva_materia)) {
            Alert alert = new Alert(AlertType.ERROR, "Nombre de materia ya existente", ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.show();
            return;
        }
        if (nueva_materia.isEmpty()) 
            return;
        
        var next = generateMateriasButton(nueva_materia);
        vbox_materias.getChildren().add(next);
        opciones_materias.put(nueva_materia, new TreeMap<>());//Ordenar si no son tal cual
    }

    @FXML
    private void action_btn_nueva_opcion(ActionEvent event) {
        var nombres_prohibidos = new HashSet<String>();
        for (var entry : opciones_materias.get(getBotonAprensado().getText()).entrySet()) {
            nombres_prohibidos.add(entry.getKey());
        }
        PanelsRegister.editor_semana_input_1 = nombres_prohibidos;
        
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("EditorSemanaPanel.fxml"));
        } catch (IOException e) {
            System.err.println("Error reading: " + e);
            return;
        }
        Stage panel_preguntas = new Stage();
        Scene scene = new Scene(root);
        panel_preguntas.setResizable(false);
        panel_preguntas.setScene(scene);
        panel_preguntas.setTitle("Crear opción");
        panel_preguntas.showAndWait();
        if (PanelsRegister.editor_semana_output_1 == null) 
            return;
        
        var opcion = PanelsRegister.editor_semana_output_1;
        PanelsRegister.editor_semana_output_1 = null;
        
        var materia_selected = getBotonAprensado();
        opciones_materias.get(materia_selected.getText()).put(opcion.nombre, opcion.horarios);
        
        var next = generateOpcionesButton(opcion.nombre);
        vbox_opciones.getChildren().add(next);
    }

    @FXML
    private void action_btn_calcular(ActionEvent event) {
        //Obtener semanas
        var conjuntos_list = new ArrayList<ConjuntoHorarios>();
        for (var materia_entry : opciones_materias.entrySet()) {
            var conjunto = new ConjuntoHorarios(materia_entry.getKey());
            for (var opcion_entry : materia_entry.getValue().entrySet())
                conjunto.push_back(opcion_entry.getValue());
            conjuntos_list.add(conjunto);
        }
        var conjuntos = conjuntos_list.toArray(new ConjuntoHorarios[0]);
        var results = new Evaluator().evaluate(conjuntos);
        
        var printed_results = ResultsPrinter.print(conjuntos, results);
        PanelsRegister.results_panel_input = printed_results;
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("ResultsPanel.fxml"));
        } catch (IOException e) {
            System.err.println("Error reading: " + e);
            return;
        }
        Stage panel_preguntas = new Stage();
        Scene scene = new Scene(root);
        panel_preguntas.setResizable(false);
        panel_preguntas.setScene(scene);
        panel_preguntas.setTitle("Resultados");
        panel_preguntas.show();
        PanelsRegister.results_panel_input = null;
    }
    
    @FXML
    private void action_btn_guardar(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.getDialogPane().setContentText("El archivo se guardará en " + 
                Main.file_path + "\nInserte nombre del archivo: ");
        dialog.setHeaderText(null);
        dialog.setTitle("Inserte nombre de archivo");
        var answer = dialog.showAndWait();
        if (!answer.isPresent())
            return;
        String nombre_archivo = Main.file_path + dialog.getEditor().getText() + Main.saves_ext;
        if (new File(nombre_archivo).exists()) {
            Alert alert = new Alert(AlertType.ERROR, "Archivo ya existente", ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.show();
            return;
        }
        
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(nombre_archivo);
        } catch (FileNotFoundException e) {
            System.out.println("Error al iniciar PrintWriter: ");
            e.printStackTrace();
            System.exit(-1);
        }
        
        writer.println("max_materias " + Evaluator.def_max_materias);
        writer.println("max_results " + Evaluator.def_max_results);
        writer.println("obligatorias " + Evaluator.def_obligatorias);
        
        writer.println("horas_por_dia " + Declarations.parametros.horas_por_dia);
        writer.println("dias_por_semana " + Declarations.parametros.dias_por_semana);
        writer.println("primera_hora " + Declarations.parametros.primera_hora);
        
        for (var materias_entry : opciones_materias.entrySet()) {
            writer.println(materias_entry.getKey() + " {");
            for (var opcion_entry : materias_entry.getValue().entrySet()) {
                writer.println(opcion_entry.getKey());
                writer.println(opcion_entry.getValue().getDigits());
            }
            writer.println("}");
        }
        
        writer.close();
    }
    
    @FXML
    private void action_btn_parametros(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("ParametrosResultadoPanel.fxml"));
        } catch (IOException e) {
            System.err.println("Error reading: " + e);
            return;
        }
        Stage panel_preguntas = new Stage();
        Scene scene = new Scene(root);
        panel_preguntas.setResizable(false);
        panel_preguntas.setScene(scene);
        panel_preguntas.setTitle("Ajustar parámetros");
        panel_preguntas.show();
    }
    
    @FXML
    private void action_btn_delete(ActionEvent event){
        var aprensado = getBotonAprensado();
        vbox_materias.getChildren().remove(aprensado);
        vbox_opciones.getChildren().clear();
        System.out.println(opciones_materias.size());
        opciones_materias.remove(aprensado.getText());
        System.out.println(opciones_materias.size());
        btn_delete.setDisable(true);
    }
    
    private void actionBotonDeMateria(ActionEvent event) {
        var button = (ToggleButton)event.getSource();
        if (!button.isSelected()) {
            btn_delete.setDisable(true);
            desabilitarSegundoPanel();
            vbox_opciones.getChildren().clear();
            return;
        }
        btn_nueva_opcion.setDisable(false);
        btn_delete.setDisable(false);
        vbox_opciones.getChildren().clear();
        for (var entry : opciones_materias.get(button.getText()).entrySet())
            vbox_opciones.getChildren().add(generateOpcionesButton(entry.getKey()));
    }
    
    private void actionBotonDeOpcion(ActionEvent event) {
        var boton = (Button)event.getSource();
        var materia_selected = getBotonAprensado();
        var map_options = opciones_materias.get(materia_selected.getText());
        var semana = map_options.get(boton.getText());
        var nombre = boton.getText();
        PanelsRegister.editor_semana_input_2 = new OpcionClase(nombre, semana);
        var nombres_prohibidos = new HashSet<String>();
        for (var entry : map_options.entrySet()) {
            if (!entry.getKey().equals(nombre))
                nombres_prohibidos.add(entry.getKey());
        }
        PanelsRegister.editor_semana_input_1 = nombres_prohibidos;
        PanelsRegister.editor_semana_input_3 = true;
        
        // Se corre la ventana
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("EditorSemanaPanel.fxml"));
        } catch (IOException e) {
            System.err.println("Error reading: " + e);
            return;
        }
        Stage panel_preguntas = new Stage();
        Scene scene = new Scene(root);
        panel_preguntas.setResizable(false);
        panel_preguntas.setScene(scene);
        panel_preguntas.setTitle("Editar opción");
        panel_preguntas.initStyle(StageStyle.DECORATED);
        panel_preguntas.showAndWait();
        //
        
        if (PanelsRegister.editor_semana_output_2) { //if eliminar
            map_options.remove(nombre);
            vbox_opciones.getChildren().remove(boton);
            PanelsRegister.editor_semana_output_2 = false;
            return;
        }
        
        var resultado = PanelsRegister.editor_semana_output_1;
        PanelsRegister.editor_semana_output_1 = null;
        
        if (resultado == null)
            return;
        
        if (!resultado.nombre.equals(nombre)) {
            map_options.remove(nombre);
            boton.setText(resultado.nombre);
        }
        map_options.put(resultado.nombre, resultado.horarios);
    }
    
    private void desabilitarSegundoPanel() {
        btn_nueva_opcion.setDisable(true);
        vbox_opciones.getChildren().clear();
    }
    
    public ToggleButton generateMateriasButton(String text) {
        var button = new ToggleButton();
        button.setText(text);
        button.setPrefWidth(Double.MAX_VALUE);
        button.setToggleGroup(materias_toggle_group);
        button.getStyleClass().add("standar_toggle_button");
        button.getStyleClass().add("rounded_button");
        button.setOnAction((ActionEvent event) -> {
            actionBotonDeMateria(event);
        });
        return button;
    }
    
    public Button generateOpcionesButton(String text) {
        var button = new Button();
        button.setText(text);
        button.setPrefWidth(Double.MAX_VALUE);
        button.getStyleClass().add("standar_button");
        button.getStyleClass().add("rounded_button");
        button.setOnAction((ActionEvent event) -> {
            actionBotonDeOpcion(event);
        });
        return button;
    }
    
    private ToggleButton getBotonAprensado() {
        return (ToggleButton)materias_toggle_group.getSelectedToggle();
    }
}
