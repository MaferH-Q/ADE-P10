package Excepciones;

public class ItemNoFound extends RuntimeException {
    public ItemNoFound(String mensaje) {
        super(mensaje);
    }
}