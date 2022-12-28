/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CalculoHorarios;

/**
 *
 * @author dan
 */
public class Declarations {
    
    public final static boolean libre = false;
    public final static boolean ocupado = !libre;
    
    public static Parametros parametros = new Parametros(14, 7, 6);
    
    public final static int null_pos = -1;
//    public final static int horas_por_dia = 14;
//    public final static int primera_hora = 7;
//    public final static int ultima_hora = primera_hora + horas_por_dia - 1;
//    public final static int dias_por_semana = 6;
//    
    public final static long MIN_EVAL = Long.MIN_VALUE;
    
    public final static int max_name_len = 12;
    
    public static String getNombreDia(int cual) {
        return switch (cual) {
            case 0 -> "Lunes";
            case 1 -> "Martes";
            case 2 -> "Mierc";
            case 3 -> "Jueves";
            case 4 -> "Viernes";
            case 5 -> "Sabado";
            case 6 -> "Domingo";
            default -> "???";
        };
    }
}


