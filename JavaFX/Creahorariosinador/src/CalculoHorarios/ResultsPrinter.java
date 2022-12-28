/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CalculoHorarios;

import CalculoHorarios.ConjuntoHorarios;
import java.util.Set;

/**
 *
 * @author dan
 */
public class ResultsPrinter {
    
    public static String print(ConjuntoHorarios[] conjuntos, Set<Result> results) {
        var builder = new StringBuilder();
        print(builder, conjuntos);
        print(builder, results);
        System.out.println("");
        print(builder, conjuntos, results);
        return builder.toString();
    }
    
    private static void setOnMat(String[][] sm, Semana toadd, String nombre) {
        for (int i = 0; i < Declarations.parametros.horas_por_dia; i++) {
            for (int j = 0; j < Declarations.parametros.dias_por_semana; j++) {
                //out.append(i + " - " + j);
                if (toadd.get(j).get(i) == Declarations.ocupado)
                    sm[i][j] = nombre;
            }
        }
    }
    
    private static String setStrLen(String str, int print_len) {
        if (str.length() > print_len)
            return str.substring(0, print_len);
        return str + makeString(print_len - str.length(), ' ');
    }
    
    private static String makeString(int len, char c) {
        var lines = new char[len];
        for (int i = 0; i < lines.length; ++i)
                lines[i] = c;
        return new String(lines);
    }
    
    private static void print(StringBuilder out, ConjuntoHorarios[] conjuntos, int[] ruta) {
        var horario_final = new String[Declarations.parametros.horas_por_dia][Declarations.parametros.dias_por_semana];
        String empty_place = makeString(Declarations.max_name_len - 3, '-');
        for (int i = 0; i < horario_final.length; i++) {
            for (int j = 0; j < horario_final[i].length; j++) {
                horario_final[i][j] = empty_place;
            }
        }
        
        final int conjunto_len = conjuntos.length;
        for (int conjunto_it = 0; conjunto_it < conjunto_len; conjunto_it++) {
            int opcion_de_materia = ruta[conjunto_it];
            if (opcion_de_materia == Declarations.null_pos)
                continue;
            Semana next_semana = conjuntos[conjunto_it].get(ruta[conjunto_it]);
            setOnMat(horario_final, next_semana, conjuntos[conjunto_it].name);
        }
        
        out.append(setStrLen("", 8));
        for (int i = 0; i < Declarations.parametros.dias_por_semana; i++) 
            out.append(setStrLen(Declarations.getNombreDia(i), Declarations.max_name_len));
        out.append("").append("\n");
        
        for (int i = 0; i < Declarations.parametros.horas_por_dia; i++) {
            out.append(setStrLen(String.valueOf(i + Declarations.parametros.primera_hora) + ":00", 8));
            for (int j = 0; j < Declarations.parametros.dias_por_semana; j++) {
                out.append(setStrLen(horario_final[i][j], Declarations.max_name_len));
            }
            out.append("").append("\n");
        }
    }
    
    private static void print(StringBuilder out, ConjuntoHorarios[] conjunto, Set<Result> resultados) {
        int result_count = resultados.size();
        for (var i : resultados) {
            out.append("opcion " + result_count + ": ").append("\n");
            print(out, conjunto, i.ruta);
            out.append("Materias tomadas: " + i.materias_tomadas).append("\n");
            out.append("Horas libres: " + i.horas_libres).append("\n");
            //out.append("Eval: ").append(i.eval).append("\n");
            out.append("").append("\n");
            --result_count;
        }
    }
    
    private static void print(StringBuilder out, Set<Result> results) {
        int result_counter = results.size();
        var results_arr = new Result[results.size()];
        results.toArray(results_arr);
        for (var i : results_arr) {
            out.append(result_counter + ".- ");
            for (var j : i.ruta) {
                if (j == Declarations.null_pos)
                    out.append("-" + " ");
                else
                    out.append(j + " ");
            }
            out.append("\n");
            --result_counter;
        }
    }
    
    private static void print(StringBuilder out, ConjuntoHorarios[] conjuntos) {
        for (var i : conjuntos)
            out.append(i.name + " ");
        out.append("\n");
    }
}
