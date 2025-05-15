/**
 * Clase que representa la configuración de una partida
 * @author Tu Nombre
 * @author Tu Número
 */
public class Configuracion {
    private boolean requiereContacto;
    private boolean largoVariado;
    private int cantidadBandas;
    private int cantidadTableros;
    
    // Constructor por defecto
    public Configuracion() {
        this.requiereContacto = false;
        this.largoVariado = false;
        this.cantidadBandas = 10;
        this.cantidadTableros = 1;
    }
    
    // Constructor con parámetros
    public Configuracion(boolean requiereContacto, boolean largoVariado, int cantidadBandas, int cantidadTableros) {
        this.requiereContacto = requiereContacto;
        this.largoVariado = largoVariado;
        this.cantidadBandas = cantidadBandas;
        this.cantidadTableros = cantidadTableros;
    }
    
    public boolean isRequiereContacto() {
        return requiereContacto;
    }
    
    public boolean isLargoVariado() {
        return largoVariado;
    }
    
    public int getCantidadBandas() {
        return cantidadBandas;
    }
    
    public int getCantidadTableros() {
        return cantidadTableros;
    }
}
