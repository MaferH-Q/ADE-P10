package btree;
import java.util.ArrayList;

public class BNode<E> {
    // Lista de claves (ordenadas) del nodo
    protected ArrayList<E> keys;

    // Lista de punteros a los hijos del nodo
    protected ArrayList<BNode<E>> childs;

    // Cantidad de claves que actualmente tiene el nodo
    protected int count;

    // ✔️ Requisito: Atributo único que identifica cada nodo
    protected int idNode;

    // ✔️ Requisito: Contador estático que se incrementa con cada nuevo nodo creado
    private static int nextId = 0;

    // ✔️ Constructor: crea un nodo con capacidad para n-1 claves y n hijos
    public BNode(int n) {
        this.keys = new ArrayList<>(n);           // claves máximas = n-1, se inicializa con n por seguridad
        this.childs = new ArrayList<>(n + 1);     // hijos máximos = n

        this.count = 0;                           // inicialmente sin claves
        this.idNode = nextId++;                   // ✔️ Asignación única del idNode

        // Inicializa la lista de claves con null para evitar errores de índice en set()
        for (int i = 0; i < n - 1; i++) {
            this.keys.add(null);
        }

        // Inicializa la lista de hijos con null
        for (int i = 0; i < n; i++) {
            this.childs.add(null);
        }
    }

    // ✔️ Método requerido: verifica si el nodo ya tiene el máximo número de claves
    public boolean nodeFull(int maxKeys) {
        return count == maxKeys;
    }

    // ✔️ Método requerido: verifica si el nodo está vacío (sin claves)
    public boolean nodeEmpty() {
        return count == 0;
    }

    // ✔️ Método requerido: búsqueda secuencial dentro del nodo
    // Devuelve true si la clave está y false si no; también indica por qué hijo descender
    @SuppressWarnings("unchecked")
    public boolean searchNode(E key, int[] pos) {
        int i = 0;
        // Compara la clave con las existentes en orden
        while (i < count && ((Comparable<E>) key).compareTo(keys.get(i)) > 0) {
            i++;
        }

        pos[0] = i;  // guarda la posición en la que se encontró o por donde debe bajar

        if (i < count && ((Comparable<E>) key).compareTo(keys.get(i)) == 0) {
            return true;  // clave encontrada
        } else {
            return false; // no encontrada, debe bajar por hijo i
        }
    }

    // ✔️ Método requerido: devuelve una representación textual del nodo
    // Muestra el idNode y todas las claves actuales
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nodo ").append(idNode).append(": ");
        for (int i = 0; i < count; i++) {
            sb.append(keys.get(i)).append(" ");
        }
        return sb.toString();
    }
}
