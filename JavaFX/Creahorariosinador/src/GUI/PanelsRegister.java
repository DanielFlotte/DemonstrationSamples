/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import CalculoHorarios.Semana;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dan
 */
public class PanelsRegister {
    public static OpcionClase editor_semana_output_1 = null;
    public static boolean editor_semana_output_2 = false;
    public static Map<String, Map<String, Semana>> cargar_salvado_output = null;
    
    public static Set<String> editor_semana_input_1 = null; //nombres prohibidos
    public static OpcionClase editor_semana_input_2 = null;
    public static boolean editor_semana_input_3 = false;
    public static String results_panel_input = null;
    public static Map<String, Map<String, Semana>> creador_panel_input = null;
}
