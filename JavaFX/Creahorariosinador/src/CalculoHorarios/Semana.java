/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CalculoHorarios;

import java.io.IOException;

/**
 *
 * @author dan
 */
public class Semana {
    private Dia[] dias = new Dia[Declarations.parametros.dias_por_semana];
    
    public Semana() {
        for (int i = 0; i < dias.length; i++) {
            dias[i] = new Dia();
        }
    }
    
    public Semana(Dia[] dias) {
        this.dias = dias;
    }
    
    public Semana(Semana other) {
        for (int i = 0; i < dias.length; i++) {
            dias[i] = new Dia(other.dias[i]);
        }
    }
    
    public Semana sumar(Semana other) {
        for (int i = 0; i < dias.length; i++)
            this.dias[i].sumar(other.dias[i]);
        
        return this;
    }
    
    public Dia get(int index) {
        return dias[index];
    }
    
    public int getHorasLibres() {
        int result = 0;
        for (Dia dia : dias)
            result += dia.getHorasLibres();
        return result;
    }
    
    public String getDigits() {
        var result = new StringBuilder();
        for (var dia : dias) {
            for (int i = 0; i < Declarations.parametros.horas_por_dia; i++) 
                result.append(asDigit(dia.get(i)));
        }
        return result.toString();
    }
    
    public static Semana readFromDigits(String digits) throws IOException {
        if (digits.length() != Declarations.parametros.dias_por_semana * Declarations.parametros.horas_por_dia)
            throw new IOException("Largo incorrecto de string: " + digits.length()
            + "(" + digits + ")");
        var semana = new Semana();
        int dias_it = 0;
        int horas_it = 0;
        for (int i = 0; i < digits.length(); i++) {
            semana.dias[dias_it].set(horas_it++, asBoolean(digits.charAt(i)));
            if (horas_it == Declarations.parametros.horas_por_dia) {
                dias_it++;
                horas_it = 0;
            }
        }
        return semana;
    }
    
    private static String asDigit(boolean b) {
        if (b)
            return "1";
        else 
            return "0";
    }
    
    private static boolean asBoolean(char c) throws IOException {
        switch (c) {
            case '1' -> {
                return true;
            }
            case '0' -> {
                return false;
            }
            default -> throw new IOException("Valor en String no esperado: '" + c + "'");
        }
    }
}
