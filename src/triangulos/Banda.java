package triangulos;

import java.util.ArrayList;
import java.util.List;

public class Banda {
    private Punto inicio;
    private Punto fin;
    private List<Punto> puntos;
    private boolean esBlanco;
    
    // Constructor simplificado que recibe una lista de puntos
    public Banda(List<Punto> puntos, boolean esBlanco) {
        if (puntos == null || puntos.isEmpty()) {
            throw new IllegalArgumentException("La lista de puntos no puede estar vacía");
        }
        this.puntos = new ArrayList<>(puntos);
        this.inicio = puntos.get(0);
        this.fin = puntos.get(puntos.size() - 1);
        this.esBlanco = esBlanco;
    }
    
    public Punto getInicio() {
        return inicio;
    }
    
    public Punto getFin() {
        return fin;
    }
    
    public List<Punto> getPuntos() {
        return puntos;
    }
    
    public boolean esBlanco() {
        return esBlanco;
    }
    
    // Método para verificar si la banda pasa por un punto específico
    public boolean pasaPorPunto(Punto punto) {
        if (punto == null) return false;
        
        for (Punto p : puntos) {
            if (p.equals(punto)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Banda: ");
        for (int i = 0; i < puntos.size(); i++) {
            sb.append(puntos.get(i));
            if (i < puntos.size() - 1) {
                sb.append(" → ");
            }
        }
        return sb.toString();
    }
}
