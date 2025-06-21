package Ejercicios;

import java.util.*;

class BTreeestudiante {
    private static final int T = 4;  // Orden 4
    private BNode root;

    public BTreeestudiante() {
        root = new BNode(true);
    }

    // Nodo del B-Tree
    private static class BNode {
        List<RegistroEstudiante> keys;
        List<BNode> children;
        boolean isLeaf;

        BNode(boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.keys = new ArrayList<>();
            this.children = new ArrayList<>();
        }
    }

    // Insertar un RegistroEstudiante
    public void insert(RegistroEstudiante estudiante) {
        BNode root = this.root;
        if (root.keys.size() == (2 * T - 1)) {
            BNode newNode = new BNode(false);
            newNode.children.add(this.root);
            split(newNode, 0);
            this.root = newNode;
        }
        insertNonFull(this.root, estudiante);
    }

    private void insertNonFull(BNode node, RegistroEstudiante estudiante) {
        int i = node.keys.size() - 1;
        if (node.isLeaf) {
            while (i >= 0 && estudiante.compareTo(node.keys.get(i)) < 0) {
                i--;
            }
            node.keys.add(i + 1, estudiante);
        } else {
            while (i >= 0 && estudiante.compareTo(node.keys.get(i)) < 0) {
                i--;
            }
            i++;
            BNode child = node.children.get(i);
            if (child.keys.size() == (2 * T - 1)) {
                split(node, i);
                if (estudiante.compareTo(node.keys.get(i)) > 0) {
                    i++;
                }
            }
            insertNonFull(node.children.get(i), estudiante);
        }
    }

    private void split(BNode parent, int i) {
        BNode fullNode = parent.children.get(i);
        BNode newNode = new BNode(fullNode.isLeaf);
        parent.keys.add(i, fullNode.keys.get(T - 1));
        parent.children.add(i + 1, newNode);

        for (int j = T; j < fullNode.keys.size(); j++) {
            newNode.keys.add(fullNode.keys.get(j));
        }
        fullNode.keys.subList(T - 1, fullNode.keys.size()).clear();

        if (!fullNode.isLeaf) {
            for (int j = T; j < fullNode.children.size(); j++) {
                newNode.children.add(fullNode.children.get(j));
            }
            fullNode.children.subList(T, fullNode.children.size()).clear();
        }
    }

    // Buscar RegistroEstudiante por código
    public RegistroEstudiante buscar(int codigo) {
        return buscarRec(root, codigo);
    }

    private RegistroEstudiante buscarRec(BNode node, int codigo) {
        int i = 0;
        while (i < node.keys.size() && codigo > node.keys.get(i).getCodigo()) {
            i++;
        }

        if (i < node.keys.size() && node.keys.get(i).getCodigo() == codigo) {
            return node.keys.get(i);
        }

        if (node.isLeaf) {
            return null;
        }

        return buscarRec(node.children.get(i), codigo);
    }

    // Eliminar RegistroEstudiante
    public void eliminar(int codigo) {
        eliminarRec(root, codigo);
    }

    private void eliminarRec(BNode node, int codigo) {
        int i = 0;
        while (i < node.keys.size() && codigo > node.keys.get(i).getCodigo()) {
            i++;
        }

        if (i < node.keys.size() && node.keys.get(i).getCodigo() == codigo) {
            node.keys.remove(i);
        } else {
            if (node.isLeaf) {
                System.out.println("No encontrado");
            } else {
                eliminarRec(node.children.get(i), codigo);
            }
        }
    }

    // Buscar nombre del estudiante por código
    public String buscarNombre(int codigo) {
        RegistroEstudiante estudiante = buscar(codigo);
        if (estudiante != null) {
            return estudiante.getNombre();
        } else {
            return "No encontrado";
        }
    }

    // Mostrar el árbol
    public void mostrar() {
        mostrarRec(root, "", true);
    }

    private void mostrarRec(BNode node, String indent, boolean last) {
        System.out.println(indent + "+- " + node.keys);
        indent += (last ? "   " : "|  ");

        if (!node.isLeaf) {
            for (int i = 0; i < node.children.size(); i++) {
                mostrarRec(node.children.get(i), indent, i == node.children.size() - 1);
            }
        }
    }
}
