package triangulos;

public class Configuracion {
    private boolean requiereContacto;
    private int largoBanda; // Cambiado de boolean a int
    private int cantidadBandas;
    private int cantidadTableros;
    
    // Constructor por defecto
    public Configuracion() {
        this.requiereContacto = false;
        this.largoBanda = 1; // Por defecto, largo 1
        this.cantidadBandas = 10;
        this.cantidadTableros = 1;
    }
    
    // Constructor con parámetros
    public Configuracion(boolean requiereContacto, int largoBanda, int cantidadBandas, int cantidadTableros) {
        this.requiereContacto = requiereContacto;
        this.largoBanda = largoBanda;
        this.cantidadBandas = cantidadBandas;
        this.cantidadTableros = cantidadTableros;
    }
    
    public boolean isRequiereContacto() {
        return requiereContacto;
    }
    
    public int getLargoBanda() { // Cambiado el método getter
        return largoBanda;
    }
    
    public int getCantidadBandas() {
        return cantidadBandas;
    }
    
    public int getCantidadTableros() {
        return cantidadTableros;
    }
}
