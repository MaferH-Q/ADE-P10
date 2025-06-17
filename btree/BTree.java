package btree;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Excepciones.ItemNoFound;

public class BTree<E extends Comparable<E>> {
    private BNode<E> root;      // Nodo raíz del árbol
    private int orden;          // Orden del árbol (número máximo de hijos por nodo)
    private boolean up;         // Bandera para indicar si hubo una división que subió una clave
    private BNode<E> nDes;      // Nodo que se genera como resultado de una división

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    // ✔️ Inserta una clave en el árbol. Si hay división desde la raíz, se crea una nueva raíz.
    public void insert(E cl) {
        up = false;
        E mediana = push(this.root, cl);
        if (up) {  // Se promovió una mediana desde abajo: se crea nueva raíz
            BNode<E> pnew = new BNode<>(this.orden);
            pnew.count = 1;
            pnew.keys.set(0, mediana);
            pnew.childs.set(0, this.root);
            pnew.childs.set(1, nDes);
            this.root = pnew;
        }
    }

    // ✔️ Inserta recursivamente. Si se necesita dividir, lo hace y propaga hacia arriba.
    private E push(BNode<E> current, E cl) {
        int pos[] = new int[1];
        E mediana;

        if (current == null) {
            up = true;  // Llegamos al lugar donde insertar
            nDes = null;
            return cl;
        } else {
            boolean fl = current.searchNode(cl, pos);
            if (fl) {
                System.out.println("Item duplicado\n");
                up = false;
                return null;
            }

            mediana = push(current.childs.get(pos[0]), cl);

            if (up) {
                if (current.nodeFull(this.orden - 1))
                    mediana = dividedNode(current, mediana, pos[0]);
                else {
                    up = false;
                    putNode(current, mediana, nDes, pos[0]);
                }
            }
            return mediana;
        }
    }

    // ✔️ Inserta una clave en un nodo que no está lleno
    private void putNode(BNode<E> current, E cl, BNode<E> rd, int k) {
        for (int i = current.count - 1; i >= k; i--) {
            current.keys.set(i + 1, current.keys.get(i));
            current.childs.set(i + 2, current.childs.get(i + 1));
        }
        current.keys.set(k, cl);
        current.childs.set(k + 1, rd);
        current.count++;
    }

    // ✔️ Divide un nodo lleno y retorna la clave mediana que subirá
    private E dividedNode(BNode<E> current, E cl, int k) {
        BNode<E> rd = nDes;
        int i, posMdna = (k <= this.orden / 2) ? this.orden / 2 : this.orden / 2 + 1;

        nDes = new BNode<>(this.orden);
        for (i = posMdna; i < this.orden - 1; i++) {
            nDes.keys.set(i - posMdna, current.keys.get(i));
            nDes.childs.set(i - posMdna + 1, current.childs.get(i + 1));
        }

        nDes.count = (this.orden - 1) - posMdna;
        current.count = posMdna;

        if (k <= this.orden / 2)
            putNode(current, cl, rd, k);
        else
            putNode(nDes, cl, rd, k - posMdna);

        E median = current.keys.get(current.count - 1);
        nDes.childs.set(0, current.childs.get(current.count));
        current.count--;

        return median;
    }

    // ✔️ Requerido en 3.3: Devuelve todo el árbol en el formato solicitado
    @Override
    public String toString() {
        String s = "";
        if (isEmpty())
            s += "BTree is empty...";
        else
            s = writeTree(this.root, null);
        return s;
    }

    // ✔️ Método auxiliar recursivo para mostrar el árbol como en la figura 10.14
    private String writeTree(BNode<E> current, BNode<E> parent) {
        StringBuilder sb = new StringBuilder();

        sb.append("Id.Nodo: ").append(current.idNode).append("\n");

        sb.append("Claves Nodo: ");
        for (int i = 0; i < current.count; i++) {
            sb.append(current.keys.get(i));
            if (i < current.count - 1) sb.append(", ");
        }
        sb.append("\n");

        sb.append("Id.Padre: ");
        sb.append(parent != null ? parent.idNode : "--");
        sb.append("\n");

        sb.append("Id.Hijos: ");
        boolean tieneHijos = false;
        for (int i = 0; i <= current.count; i++) {
            BNode<E> child = current.childs.get(i);
            if (child != null) {
                sb.append(child.idNode);
                if (i < current.count && current.childs.get(i + 1) != null) sb.append(", ");
                tieneHijos = true;
            }
        }
        if (!tieneHijos) sb.append("--");
        sb.append("\n\n");

        for (int i = 0; i <= current.count; i++) {
            BNode<E> child = current.childs.get(i);
            if (child != null) sb.append(writeTree(child, current));
        }

        return sb.toString();
    }
    // Ejercicios: 01 - Método de búsqueda de clave en el árbol B
public boolean search(E cl) {
    // Si el árbol está vacío, retorna false
    if (this.root == null) {
        System.out.println("El árbol está vacío.");
        return false;
    }

    // Llama al método recursivo de búsqueda desde la raíz
    return searchRecursive(this.root, cl);
}

// Ejercicios: 01 - Método recursivo de búsqueda
private boolean searchRecursive(BNode<E> current, E cl) {
    int pos[] = new int[1];

    // Busca la clave en el nodo actual
    boolean found = current.searchNode(cl, pos);

    if (found) {
        // ✔️ Si se encuentra, muestra el mensaje con el id del nodo y la posición
        System.out.println(cl + " se encuentra en el nodo " + current.idNode + " en la posición " + pos[0]);
        return true;
    } else {
        // Si ya no hay hijos por dónde bajar, la clave no existe
        if (current.childs.get(pos[0]) == null) {
            return false;
        } else {
            // Recurre al hijo correspondiente
            return searchRecursive(current.childs.get(pos[0]), cl);
        }
    }
}

// Ejercicios: 02 - Método principal de eliminación
public void remove(E cl) {
    if (this.root == null) {
        System.out.println("El árbol está vacío.");
        return;
    }

    removeRecursive(this.root, cl);

    // Si la raíz queda vacía, subir único hijo como nueva raíz
    if (this.root.count == 0 && this.root.childs.get(0) != null) {
        this.root = this.root.childs.get(0);
    }

    // Si raíz vacía sin hijos, el árbol se vuelve nulo
    if (this.root.count == 0) {
        this.root = null;
    }
}
// Ejercicios: 02 - Método recursivo de eliminación
private void removeRecursive(BNode<E> node, E cl) {
    int[] pos = new int[1];
    boolean found = node.searchNode(cl, pos);

    if (found) {
        // Si la clave está en una hoja → eliminar directamente
        if (node.childs.get(0) == null) {
            removeFromLeaf(node, pos[0]);
        } else {
            // Reemplazar con predecesor y eliminar desde subárbol izquierdo
            BNode<E> predNode = node.childs.get(pos[0]);
            while (predNode.childs.get(predNode.count) != null) {
                predNode = predNode.childs.get(predNode.count);
            }
            E pred = predNode.keys.get(predNode.count - 1);
            node.keys.set(pos[0], pred);
            removeRecursive(node.childs.get(pos[0]), pred);
        }
    } else {
        // No se encuentra en el nodo actual, bajar por el hijo correspondiente
        BNode<E> child = node.childs.get(pos[0]);
        if (child == null) {
            System.out.println("La clave no existe.");
            return;
        }

        // Antes de bajar, asegurarse de que el hijo tiene más que el mínimo
        if (child.count == (orden - 1) / 2) {
            fixChild(node, pos[0]);
        }

        removeRecursive(node.childs.get(pos[0]), cl);
    }
}
// Ejercicios: 02 - Eliminar clave directamente desde una hoja
private void removeFromLeaf(BNode<E> node, int pos) {
    for (int i = pos; i < node.count - 1; i++) {
        node.keys.set(i, node.keys.get(i + 1));
    }
    node.keys.set(node.count - 1, null);
    node.count--;
}
// Ejercicios: 02 - Intenta redistribuir o fusionar si el hijo tiene el mínimo
private void fixChild(BNode<E> parent, int idx) {
    BNode<E> child = parent.childs.get(idx);

    // Redistribuir con hermano izquierdo
    if (idx > 0 && parent.childs.get(idx - 1).count > (orden - 1) / 2) {
        BNode<E> left = parent.childs.get(idx - 1);

        // Mover clave del padre al hijo
        for (int i = child.count; i > 0; i--) {
            child.keys.set(i, child.keys.get(i - 1));
        }
        for (int i = child.count + 1; i > 0; i--) {
            child.childs.set(i, child.childs.get(i - 1));
        }

        child.keys.set(0, parent.keys.get(idx - 1));
        child.childs.set(0, left.childs.get(left.count));
        parent.keys.set(idx - 1, left.keys.get(left.count - 1));

        left.keys.set(left.count - 1, null);
        left.childs.set(left.count, null);
        left.count++;
        child.count++;
        left.count--;
    }

    // Redistribuir con hermano derecho
    else if (idx < parent.count && parent.childs.get(idx + 1).count > (orden - 1) / 2) {
        BNode<E> right = parent.childs.get(idx + 1);

        child.keys.set(child.count, parent.keys.get(idx));
        child.childs.set(child.count + 1, right.childs.get(0));
        parent.keys.set(idx, right.keys.get(0));

        for (int i = 0; i < right.count - 1; i++) {
            right.keys.set(i, right.keys.get(i + 1));
        }
        for (int i = 0; i < right.count; i++) {
            right.childs.set(i, right.childs.get(i + 1));
        }

        right.keys.set(right.count - 1, null);
        right.childs.set(right.count, null);
        right.count--;
        child.count++;
    }

    // Si no se puede redistribuir → fusionar
    else {
        if (idx > 0) {
            merge(parent, idx - 1);
        } else {
            merge(parent, idx);
        }
    }
}
// Ejercicios: 02 - Fusiona dos hijos y ajusta claves del padre
private void merge(BNode<E> parent, int idx) {
    BNode<E> left = parent.childs.get(idx);
    BNode<E> right = parent.childs.get(idx + 1);

    left.keys.set(left.count++, parent.keys.get(idx));

    for (int i = 0; i < right.count; i++) {
        left.keys.set(left.count++, right.keys.get(i));
    }

    for (int i = 0; i <= right.count; i++) {
        left.childs.set(left.count + i, right.childs.get(i));
    }

    for (int i = idx; i < parent.count - 1; i++) {
        parent.keys.set(i, parent.keys.get(i + 1));
        parent.childs.set(i + 1, parent.childs.get(i + 2));
    }

    parent.keys.set(parent.count - 1, null);
    parent.childs.set(parent.count, null);
    parent.count--;
}
// Ejercicios: 03 - Construcción del BTree desde un archivo plano
public static BTree<Integer> building_Btree(String path) {
    try {
        Scanner sc = new Scanner(new File(path));
        int orden = Integer.parseInt(sc.nextLine().trim());

        BTree<Integer> btree = new BTree<>(orden);
        Map<Integer, BNode<Integer>> nodos = new HashMap<>();
        Map<Integer, Integer> niveles = new HashMap<>();
        Map<Integer, List<Integer>> hijosPorNivel = new HashMap<>();

        while (sc.hasNextLine()) {
            String linea = sc.nextLine().trim();
            if (linea.isEmpty()) continue;

            String[] partes = linea.split(",");
            int nivel = Integer.parseInt(partes[0]);
            int id = Integer.parseInt(partes[1]);

            BNode<Integer> nodo = new BNode<>(orden);
            nodo.idNode = id;

            for (int i = 2; i < partes.length; i++) {
                nodo.keys.set(i - 2, Integer.parseInt(partes[i]));
                nodo.count++;
            }

            nodos.put(id, nodo);
            niveles.put(id, nivel);

            hijosPorNivel.computeIfAbsent(nivel, k -> new ArrayList<>()).add(id);
        }

            // Conectar hijos a padres en orden (nivel por nivel)
            for (int nivel = hijosPorNivel.size() - 2; nivel >= 0; nivel--) {
                List<Integer> padres = hijosPorNivel.get(nivel);
                List<Integer> hijos = hijosPorNivel.get(nivel + 1);

                int idxHijo = 0;
                for (int idPadre : padres) {
                    BNode<Integer> padre = nodos.get(idPadre);

                    for (int i = 0; i <= padre.count && idxHijo < hijos.size(); i++) {
                        int idHijo = hijos.get(idxHijo);
                        BNode<Integer> hijo = nodos.get(idHijo);
                        padre.childs.set(i, hijo);
                        idxHijo++;
                    }
                }
            }

        // Obtener el nodo con nivel más alto para asignar como raíz
        int nivelRaiz = hijosPorNivel.keySet().stream().max(Integer::compareTo).get();
        List<Integer> nodosNivelMax = hijosPorNivel.get(nivelRaiz);
        int idRaiz = nodosNivelMax.get(nodosNivelMax.size() - 1); // Último es la raíz
        btree.root = nodos.get(idRaiz);

        sc.close();
        // Verificar propiedades del árbol
        if (!verifyBTree(btree.root, orden)) {
            throw new Excepciones.ItemNoFound("El árbol no cumple con las propiedades de un BTree.");
        }

        return btree;

    } catch (Exception e) {
        throw new Excepciones.ItemNoFound("Error al construir el árbol: " + e.getMessage());
    }
}
// Ejercicios: 03 - Verifica que cada nodo cumpla con propiedades de BTree
private static boolean verifyBTree(BNode<Integer> node, int orden) {
    if (node == null) return true;

    int maxClaves = orden - 1;
    int minClaves;

    // ✔ Si es la raíz, permitir desde 1 clave (aunque sea menor al mínimo)
    boolean esRaiz = true;
    for (int i = 0; i <= node.count; i++) {
        if (node.childs.get(i) != null) {
            esRaiz = false;
            break;
        }
    }
    if (esRaiz) {
        minClaves = 1;
    } else {
        minClaves = (int) Math.ceil((orden - 1) / 2.0);
    }

    if (node.count < minClaves || node.count > maxClaves) return false;

    // Verificar recursivamente a los hijos
    for (int i = 0; i <= node.count; i++) {
        if (node.childs.get(i) != null && !verifyBTree(node.childs.get(i), orden)) {
            return false;
        }
    }

    return true;
}
}