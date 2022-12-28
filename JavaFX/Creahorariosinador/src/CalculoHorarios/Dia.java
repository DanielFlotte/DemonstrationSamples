/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CalculoHorarios;

/**
 *
 * @author dan
 */
public class Dia {
    private boolean[] horas = new boolean[Declarations.parametros.horas_por_dia];
    
    public Dia() {}
    
    public Dia(int begin, int end) {
        if (begin < Declarations.parametros.primera_hora || (end - 1) > Declarations.parametros.ultima_hora)
            throw new RuntimeException("Dia incorrecto de " + begin + " a " + end);
        
        begin -= Declarations.parametros.primera_hora;
        end -= Declarations.parametros.primera_hora;
        
        for (; begin < end; begin++) {
            horas[begin] = Declarations.ocupado;
        }
    }
    
    public Dia(int begin) {
        this(begin, begin + 1);
    }
    
    public Dia sumar(Dia other) {
        for (int i = 0; i < horas.length; i++) {
            if (this.horas[i] == Declarations.ocupado && other.horas[i] == Declarations.ocupado)
                throw new RuntimeException("Error de suma"); //cambiar?
            
            this.horas[i] = this.horas[i] != other.horas[i];
        }
        return this;
    }
    
    public Dia(Dia other) {
        for (int i = 0; i < horas.length; i++) {
            this.horas[i] = other.horas[i];
        }
    }
    
    public boolean get(int index) {
        return horas[index];
    }
    
    public void set(int index, boolean value) {
        horas[index] = value;
    }
    
    public int getHorasLibres() {
        int counter = 0;
        
        int sub_counter = 0;
        boolean has_hit_first = false;
	for (boolean i : horas) {
            if (i == Declarations.ocupado) {
                has_hit_first = true;
                counter += sub_counter;
                sub_counter = 0;
                continue;
            }

            //else...
            if (has_hit_first)
                ++sub_counter;
	}

	return counter;
    }
}
