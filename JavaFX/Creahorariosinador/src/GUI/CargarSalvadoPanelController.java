/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import CalculoHorarios.Declarations;
import CalculoHorarios.Evaluator;
import CalculoHorarios.Parametros;
import CalculoHorarios.Semana;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dan
 */
public class CargarSalvadoPanelController implements Initializable {

    @FXML
    private ScrollPane buttons_scrollpane;
    @FXML
    private Button btn_aceptar;
    @FXML
    private VBox vbox_botones;
    
    private ToggleGroup salvados_group = new ToggleGroup();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        var directory = new File(Main.file_path);
        File[] files = directory.listFiles();
        for (var f : files) {
            var name = f.getName();
            if (name.length() < 4 || !name.substring(name.length() - 4, name.length()).equals(Main.saves_ext))
                continue;
            
            var next_btn = new ToggleButton();
            next_btn.setPrefWidth(9999);
            next_btn.setText(name);
            next_btn.setToggleGroup(salvados_group);
            next_btn.getStyleClass().add("standar_toggle_button");
            next_btn.getStyleClass().add("rounded_button");
            vbox_botones.getChildren().add(next_btn);
        }
    }    

    @FXML
    private void action_btn_aceptar(ActionEvent event) {
        var boton = (ToggleButton)salvados_group.getSelectedToggle();
        if (boton == null) {
            Alert alert = new Alert(AlertType.WARNING, "Ningún archivo seleccionado", ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.show();
            return;
        }
        
        try {
            PanelsRegister.cargar_salvado_output = interpretarArchivo(Main.file_path + boton.getText());
        } catch (Exception e) {
            System.err.println("Error al leer el archivo: ");
            e.printStackTrace();
        }
        ((Stage)btn_aceptar.getScene().getWindow()).close();
    }
    
    private Map<String, Map<String, Semana>> interpretarArchivo(String path) throws Exception {
        Map<String, Map<String, Semana>> materias_y_opciones = new HashMap<>();
        String[] lines;
        
        lines = Files.readString(Paths.get(path)).split("\n");
//        for (int i = 0; i < lines.length; i++) {
//            System.out.println("Linea " + i + ": " + lines[i]);
//        }
        String[][] content = new String[lines.length][];
        for (int i = 0; i < lines.length; i++)
            content[i] = lines[i].split(" ");
        if (!content[0][0].equals("max_materias")) {
            throw new RuntimeException("Formato incorrecto");
        }
        
        Evaluator.def_max_materias = Integer.parseInt(content[0][1]);
        Evaluator.def_max_results = Integer.parseInt(content[1][1]);
        Evaluator.def_obligatorias = Integer.parseInt(content[2][1]);
        
        {
            int horas_por_dia = Integer.parseInt(content[3][1]);
            int dias_por_semana = Integer.parseInt(content[4][1]);
            int primera_hora = Integer.parseInt(content[5][1]);
            Declarations.parametros = new Parametros(horas_por_dia, primera_hora, dias_por_semana);
        }
        
        int next_beg = 2;
        for (;;) {
            var beg = nextStartOf(lines, next_beg);
            if (beg == -1)
                break;
            var end = nextEndOf(lines, beg);
            //System.out.println(beg + " - " + end);
            var name_materia = lines[beg].substring(0, lines[beg].length() - 2);
            materias_y_opciones.put(name_materia, new TreeMap<>());
            var opciones_map = materias_y_opciones.get(name_materia);
            for (int i = beg; i + 1 < end; i += 2) {
                var nombre_opcion = lines[i + 1];
                var semana = Semana.readFromDigits(lines[i + 2]);
                opciones_map.put(nombre_opcion, semana);
            }
            next_beg = end + 1;
        }
        
        return materias_y_opciones;
    }
    
    private int nextStartOf(String[] lines, int beg) {
        for (int i = beg; i < lines.length; i++) {
            if (lines[i].endsWith("{"))
                return i;
        }
        return -1;
    } 
    
    private int nextEndOf(String[] lines, int beg) {
        for (int i = beg; i < lines.length; i++) {
            if (lines[i].startsWith("}"))
                return i;
        }
        return -1;
    }
    
}
