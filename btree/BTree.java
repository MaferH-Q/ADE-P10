package btree;

import java.util.Arrays;

class BTree {
    BTreeNode root;
    int order;

    public BTree(int order) {
        this.order = order;
        root = new BTreeNode(order, true);
    }

    // Insertion method
    public void insert(int key) {
        if (root.numKeys == order - 1) {
            BTreeNode newRoot = new BTreeNode(order, false);
            newRoot.children[0] = root;
            splitChild(newRoot, 0, root);
            root = newRoot;
        }
        insertNonFull(root, key);
    }

   // Método para dividir un nodo cuando está lleno
private void splitChild(BTreeNode parent, int index, BTreeNode child) {
    int medianKey = child.keys[order / 2];  // Clave mediana que se mueve al padre
    BTreeNode newChild = new BTreeNode(order, child.isLeaf);  // Crear un nuevo nodo hijo
    int mid = order / 2;

    // Mover las claves del hijo al nuevo nodo
    for (int i = mid + 1; i < order - 1; i++) {
        newChild.keys[i - (mid + 1)] = child.keys[i]; // Mover la clave a la nueva posición
        child.keys[i] = 0; // Limpiar las claves viejas en el nodo original
    }

    // Si el nodo no es hoja, mover los hijos también
    if (!child.isLeaf) {
        for (int i = mid + 1; i < order; i++) {
            newChild.children[i - (mid + 1)] = child.children[i];
            child.children[i] = null;
        }
    }

    child.numKeys = mid; // El hijo original ahora tiene solo la mitad de las claves
    newChild.numKeys = order - mid - 1; // El nuevo hijo obtiene las otras claves

    // Mover la clave mediana al nodo padre
    for (int i = parent.numKeys; i > index; i--) {
        parent.children[i + 1] = parent.children[i];
    }
    parent.children[index + 1] = newChild; // El nuevo hijo es colocado en el padre
    for (int i = parent.numKeys - 1; i >= index; i--) {
        parent.keys[i + 1] = parent.keys[i];
    }
    parent.keys[index] = medianKey; // La clave mediana se mueve al padre
    parent.numKeys++; // Incrementamos el número de claves en el nodo padre

    // Limpiar los valores 0 en el nuevo nodo
    cleanNode(newChild);

    // Impresión para depuración
    System.out.println("División realizada: Nodo original con claves " + Arrays.toString(child.keys));
    System.out.println("Nuevo nodo con claves " + Arrays.toString(newChild.keys));
}
private void cleanNode(BTreeNode node) {
    // Elimina los valores `0` y ajusta el número de claves
    int newNumKeys = 0;
    for (int i = 0; i < node.numKeys; i++) {
        if (node.keys[i] != 0) {
            node.keys[newNumKeys++] = node.keys[i];  // Mover claves válidas hacia el principio
        }
    }
    node.numKeys = newNumKeys;  // Actualizar el número de claves válidas
}
  private void insertNonFull(BTreeNode node, int key) {
    int i = node.numKeys - 1;
    if (node.isLeaf) {
        // Insertando en el nodo hoja
        while (i >= 0 && key < node.keys[i]) {
            node.keys[i + 1] = node.keys[i];
            i--;
        }
        node.keys[i + 1] = key;
        node.numKeys++;
    } else {
        // Insertando en un nodo interno
        while (i >= 0 && key < node.keys[i]) {
            i--;
        }
        i++;

        // Verificar si el hijo está vacío
        if (node.children[i] == null) {
            node.children[i] = new BTreeNode(order, true);  // Inicializar el hijo si es nulo
        }

        // Si el hijo está lleno, dividirlo
        if (node.children[i].numKeys == order - 1) {
            splitChild(node, i, node.children[i]);
            if (key > node.keys[i]) {
                i++;
            }
        }
        insertNonFull(node.children[i], key);  // Llamada recursiva para insertar en el hijo adecuado
    }
}

// Búsqueda de una clave en el árbol B
public boolean search(BTreeNode node, int key) {
    int i = 0;
    // Buscar en el nodo la posición de la clave
    while (i < node.numKeys && key > node.keys[i]) {
        i++;
    }

    // Si encontramos la clave
    if (i < node.numKeys && key == node.keys[i]) {
        return true;  // La clave fue encontrada
    }

    // Si el nodo es una hoja, la clave no está en el árbol
    if (node.isLeaf) {
        return false;  // No está en este nodo y es una hoja
    }

    // Buscar en el hijo correspondiente
    return search(node.children[i], key);  // Llamada recursiva en el hijo
}
public void delete(BTreeNode node, int key) {
    int i = 0;
    // Buscar la posición de la clave a eliminar
    while (i < node.numKeys && key > node.keys[i]) {
        i++;
    }

    // Si encontramos la clave en el nodo
    if (i < node.numKeys && key == node.keys[i]) {
        // Si es un nodo hoja, simplemente eliminamos la clave
        if (node.isLeaf) {
            for (int j = i; j < node.numKeys - 1; j++) {
                node.keys[j] = node.keys[j + 1];
            }
            node.numKeys--;
        } else {
            // Si no es hoja, se realiza un proceso más complejo de eliminación
            deleteInternal(node, i, key);
        }
    } else if (!node.isLeaf) {
        // Si no encontramos la clave, seguimos buscando en los hijos
        delete(node.children[i], key);
    }
}

// Elimina una clave de un nodo interno (no hoja)
private void deleteInternal(BTreeNode node, int index, int key) {
    BTreeNode child = node.children[index];
    if (child.numKeys >= order / 2) {
        // Si el hijo tiene suficientes claves, simplemente lo eliminamos
        delete(child, key);
    } else {
        // Si el hijo tiene pocas claves, fusionamos o redistribuimos
        handleUnderflow(node, index, key);
    }
}

// Maneja el subdesbordamiento (underflow) en el árbol B
private void handleUnderflow(BTreeNode parent, int index, int key) {
    BTreeNode child = parent.children[index];
    BTreeNode sibling = null;

    // Verificar si el hermano izquierdo o derecho existe
    if (index > 0) {
        sibling = parent.children[index - 1]; // Hermano izquierdo
    }

    if (index < parent.numKeys && parent.children[index + 1] != null) {
        sibling = parent.children[index + 1]; // Hermano derecho
    }

    // Si el hermano tiene suficientes claves, redistribuir
    if (sibling != null && sibling.numKeys > order / 2) {
        redistributeKeys(parent, index, child, sibling);
    } else {
        // Si no hay suficientes claves, fusionar nodos
        mergeNodes(parent, index, child, sibling);
    }
}

// Redistribuir las claves entre el nodo y su hermano
private void redistributeKeys(BTreeNode parent, int index, BTreeNode child, BTreeNode sibling) {
    // Si el hermano está a la izquierda
    if (index > 0) {
        // Mover la clave del padre al hijo
        child.keys[child.numKeys] = parent.keys[index - 1];
        child.numKeys++;

        // Mover la clave del hermano al padre
        parent.keys[index - 1] = sibling.keys[sibling.numKeys - 1];
        sibling.numKeys--;
    } 
    // Si el hermano está a la derecha
    else {
        // Mover la clave del padre al hijo
        child.keys[child.numKeys] = parent.keys[index];
        child.numKeys++;

        // Mover la clave del hermano al padre
        parent.keys[index] = sibling.keys[0];
        sibling.numKeys--;
    }
}

private void mergeNodes(BTreeNode parent, int index, BTreeNode child, BTreeNode sibling) {
    // Paso 1: Mover la clave del padre al hijo fusionado
    child.keys[child.numKeys] = parent.keys[index];
    child.numKeys++;

    // Paso 2: Mover las claves del hermano al hijo fusionado
    int i = 0;
    while (i < sibling.numKeys) {
        child.keys[child.numKeys] = sibling.keys[i];
        child.numKeys++;
        i++;
    }

    // Paso 3: Mover los hijos del hermano al hijo fusionado (si no es una hoja)
    if (!sibling.isLeaf) {
        i = 0;
        while (i <= sibling.numKeys) {
            child.children[child.numKeys] = sibling.children[i];
            child.numKeys++;
            i++;
        }
    }

    // Paso 4: Eliminar la clave del nodo padre y mover las claves hacia la izquierda
    for ( i = index; i < parent.numKeys - 1; i++) {
        parent.keys[i] = parent.keys[i + 1]; // Desplazar las claves
        parent.children[i + 1] = parent.children[i + 2]; // Desplazar los punteros
    }

    // Reducir el número de claves en el nodo padre
    parent.numKeys--;

    // Limpiar el último puntero del nodo padre
    parent.children[parent.numKeys + 1] = null;
}



public void printTree(BTreeNode node, int level) {
    if (node != null) {
        // Imprimir el nivel actual y las claves dentro de corchetes
        System.out.print("Nivel " + level + ": [");
        for (int i = 0; i < node.numKeys; i++) {
            System.out.print(node.keys[i]);
            if (i < node.numKeys - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");

        // Recursión para imprimir los hijos, pero solo si el nodo no es una hoja
        if (!node.isLeaf) {
            for (int i = 0; i <= node.numKeys; i++) {
                printTree(node.children[i], level + 1);  // Llamada recursiva para los hijos
            }
        }
    }
}




}
