/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CalculoHorarios;

import CalculoHorarios.ConjuntoHorarios;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author dan
 */
public class Evaluator {
        
    public static int def_max_materias = 99;
    public static int def_max_results = 50;
    public static int def_obligatorias = 1;
    
    private final int max_materias;
    private final int max_results;
    private final int obligatorias;
    
    private Set<Result> results = new TreeSet<>();
    
    public Evaluator() {
        this.max_materias = def_max_materias;
        this.max_results = def_max_results;
        this.obligatorias = def_obligatorias;
    }
    
    public Evaluator(int max_results, int obligatorias, int max_materias) {
        this.max_materias = max_materias;
        this.max_results = max_results;
        this.obligatorias = obligatorias;
    }
    
    public Set<Result> evaluate(ConjuntoHorarios[] conjuntos) {
        int[] result = new int[conjuntos.length];
        Arrays.fill(result, Declarations.null_pos);
        evaluate(conjuntos, result, 0, new Semana());
        return results;
    }
    
    public void evaluate(ConjuntoHorarios[] conjuntos, int[] rutas, int iterator, Semana mapping) {
        if (iterator == conjuntos.length) {//Then evaluate:
            long eval = 0;
            //Materias evaluadas?
            int materias_tomadas = 0;
            for (var i : rutas)
                if (i != Declarations.null_pos)
                    ++materias_tomadas;
            
            if (materias_tomadas > max_materias)
                return;
            
            eval += materias_tomadas * 1000;
            
            //Horas libres?
            var horas_libres = mapping.getHorasLibres();
            eval -= horas_libres;
            
            //No more eval
            report(new Result(rutas, eval, materias_tomadas, horas_libres));
            return;
        }
        
        ConjuntoHorarios conjunto_actual = conjuntos[iterator];
        for (int i = 0, posibilidades = conjunto_actual.size(); i < posibilidades; i++) {
            rutas[iterator] = i;
            //System.out.println("MAPPING IS NULL " + (mapping == null));
            Semana next_mapping = new Semana(mapping);
            try {
                next_mapping.sumar(conjunto_actual.get(i));
            } catch (RuntimeException e) {
                continue;
            }
            this.evaluate(conjuntos, rutas, iterator + 1, next_mapping);
        }
        
        if (iterator >= obligatorias) {
            rutas[iterator] = Declarations.null_pos;
            this.evaluate(conjuntos, rutas, iterator + 1, mapping);
        }
    }
    
    private void report(Result result) {
        if (results.size() < max_results) {
            results.add(result);
            return;
        }
        //else is full
        
        Result lowest = results.iterator().next();
        if (lowest.menorQue(result)) {
            removeLastOfResults();
            results.add(result);
        }
    }
    
    private void removeLastOfResults() {
        var it = results.iterator();
        it.next();
        it.remove();
    }
    
}
