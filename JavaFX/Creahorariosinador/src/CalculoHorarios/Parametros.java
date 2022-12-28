/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CalculoHorarios;

/**
 *
 * @author dan
 */
public class Parametros {
    public final int horas_por_dia;
    public final int primera_hora;
    public final int ultima_hora;
    public final int dias_por_semana;
    
    public Parametros(int horas_por_dia, int primera_hora, int dias_por_semana) {
        this.horas_por_dia = horas_por_dia;
        this.primera_hora = primera_hora;
        this.ultima_hora = primera_hora + horas_por_dia - 1;
        this.dias_por_semana = dias_por_semana;
    }
}
