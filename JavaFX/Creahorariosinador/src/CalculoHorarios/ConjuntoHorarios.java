/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CalculoHorarios;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dan
 */
public class ConjuntoHorarios {
    public String name;
    private List<Semana> conjunto = new ArrayList<>();
    
    public ConjuntoHorarios(String name) {
        this.name = name;
    }
    
    public void push_back(Semana semana) {
        conjunto.add(semana);
    }
    
    public Semana get(int index) {
        return conjunto.get(index);
    }
    
    public int size() {
        return conjunto.size();
    }
    
    public boolean isEmpty() {
        return conjunto.isEmpty();
    }
}
