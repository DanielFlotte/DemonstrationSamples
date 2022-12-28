/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CalculoHorarios;

import java.util.List;

/**
 *
 * @author dan
 */
public class Result implements Comparable<Result> {
    public int[] ruta;
    long eval = Declarations.MIN_EVAL;
    int materias_tomadas;
    int horas_libres;
    
    public Result(int[] ruta, long eval, int materias_tomadas, int horas_libres) {
        this.ruta = new int[ruta.length];
        for (int i = 0; i < ruta.length; i++) { //system.arraycopy?
            this.ruta[i] = ruta[i];
        }
        this.eval = eval;
        this.materias_tomadas = materias_tomadas;
        this.horas_libres = horas_libres;
    }
    
    public boolean menorQue(Result other) {
        if (eval == other.eval)
            return rutasComp(ruta, other.ruta);
        return eval < other.eval;
    }
    
    private static boolean rutasComp(int[] r1, int[] r2) {
        assert r1.length == r2.length;
        for (int i = 0; i < r1.length; i++) {
            if (!(r1[i] < r2[i]))
                return false;
        }
        return true;
    }

    @Override
    public int compareTo(Result other) { //Eficiencia mejorable al cambiar la segunda comparación
        if (this.eval != other.eval)
            return Long.valueOf(eval).compareTo(other.eval);
        
        if (this.ruta.length != other.ruta.length) {
            System.err.println("Error en las rutas");
            System.exit(-1);
        }
        
//        for (int i = 0; i < this.ruta.length; i++) {
//            if (this.ruta[i] > other.ruta[i]) return 1;
//            if (this.ruta[i] < other.ruta[i]) return -1;
//        }
//        return 0;
        return 1;
    }
}
