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
public class TestExample {
    public static void _main_() {
        // TODO code application logic here
        int max_results = 20;
        int obligatorias = 0;
        int max_materias = Integer.MAX_VALUE;
        var conjuntos = getHorariosPrueba();
        var result = new Evaluator(max_results, obligatorias, max_materias).evaluate(conjuntos);
        print(conjuntos);
        print(result);
        System.out.println("");
        print(conjuntos, result);
    }
    
    static void setOnMat(String[][] sm, Semana toadd, String nombre) {
        for (int i = 0; i < Declarations.parametros.horas_por_dia; i++) {
            for (int j = 0; j < Declarations.parametros.dias_por_semana; j++) {
                //System.out.println(i + " - " + j);
                if (toadd.get(j).get(i) == Declarations.ocupado)
                    sm[i][j] = nombre;
            }
        }
    }
    
    static String setStrLen(String str, int print_len) {
        if (str.length() > print_len)
            return str.substring(0, print_len);
        return str + makeString(print_len - str.length(), ' ');
    }
    
    static String makeString(int len, char c) {
        var lines = new char[len];
        for (int i = 0; i < lines.length; ++i)
                lines[i] = c;
        return new String(lines);
    }
    
    static void print(ConjuntoHorarios[] conjuntos, int[] ruta) {
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
        
        System.out.print(setStrLen("", 8));//???
        for (int i = 0; i < Declarations.parametros.dias_por_semana; i++) 
            System.out.print(setStrLen(Declarations.getNombreDia(i), Declarations.max_name_len));
        System.out.println("");
        
        for (int i = 0; i < Declarations.parametros.horas_por_dia; i++) {
            System.out.print(setStrLen(String.valueOf(i + Declarations.parametros.primera_hora) + ":00", 8));
            for (int j = 0; j < Declarations.parametros.dias_por_semana; j++) {
                System.out.print(setStrLen(horario_final[i][j], Declarations.max_name_len));
            }
            System.out.println("");
        }
    }
    
    static void print(ConjuntoHorarios[] conjunto, Set<Result> resultados) {
        int result_count = resultados.size();
        for (var i : resultados) {
            System.out.println("opcion " + result_count + ": ");
            print(conjunto, i.ruta);
            System.out.println("Materias tomadas: " + i.materias_tomadas);
            System.out.println("Horas libres: " + i.horas_libres);
            System.out.println("Eval: " + i.eval);
            System.out.println("");
            --result_count;
        }
    }
    
    static void print(Set<Result> results) {
        int result_counter = results.size();
        var results_arr = new Result[results.size()];
        results.toArray(results_arr);
        for (var i : results_arr) {
            System.out.print(result_counter + ".- ");
            for (var j : i.ruta) {
                if (j == Declarations.null_pos)
                    System.out.print("-" + " ");
                else
                    System.out.print(j + " ");
            }
            System.out.println("");
            --result_counter;
        }
    }
    
    static void print(ConjuntoHorarios[] conjuntos) {
        for (var i : conjuntos)
            System.out.print(i.name + " ");
        System.out.println("");
    }
    
    static ConjuntoHorarios[] getHorariosPrueba() {
        ConjuntoHorarios[] conjuntos = new ConjuntoHorarios[4];
        {
            var conjunto = new ConjuntoHorarios("Historia");
            conjunto.push_back(makeSemana(new int[]{8, 8, 9, 9, 11}));
            conjunto.push_back(makeSemana(new int[]{9, 14, 14, 8, 8}));
            conjunto.push_back(makeSemana(new int[]{10, 8, 15, 8, 9}));
            conjuntos[0] = conjunto;
        }
        {
            var conjunto = new ConjuntoHorarios("Progra");
            conjunto.push_back(makeSemana(new int[]{9, 10, 10, 10, 8}));
            conjunto.push_back(makeSemana(new int[]{13, 11, 11, 9, 7}));
            conjunto.push_back(makeSemana(new int[]{14, 11, 11, 8, 9}));
            conjuntos[1] = conjunto;
        }
        {
            var conjunto = new ConjuntoHorarios("Psicología");
            conjunto.push_back(makeSemana(new int[]{10, 9, 11, 11, 9}));
            conjunto.push_back(makeSemana(new int[]{9, 14, 14, 8, 8}));
            conjunto.push_back(makeSemana(new int[]{10, 8, 15, 8, 9}));
            conjuntos[2] = conjunto;
        }
        {
            var conjunto = new ConjuntoHorarios("Costura");
            conjunto.push_back(makeSemana(new int[]{11, 11, 8, 8, 10}));
            conjunto.push_back(makeSemana(new int[]{12, 12, 12, 10, 10}));
            conjunto.push_back(makeSemana(new int[]{8, 8, 9, 14, 9}));
            conjuntos[3] = conjunto;
        }
        
        return conjuntos;
    }
    
    static Semana makeSemana(int[] horas) {
        var dias = new Dia[5];
        for (int i = 0; i < dias.length; ++i)
            dias[i] = new Dia(horas[i]);
        return new Semana(dias);
    }
    
}
