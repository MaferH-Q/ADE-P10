package btree;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BTree bTree = null;

        while (true) {
            // Menú principal
            System.out.println("\n1. Crear un nuevo árbol B");
            System.out.println("2. Cargar un árbol desde un archivo");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    // Crear un nuevo árbol B
                    System.out.print("Ingrese el orden del árbol B: ");
                    int orden = scanner.nextInt();
                    bTree = new BTree(orden);
                    System.out.println("Árbol B de orden " + orden + " creado exitosamente.");
                    treeMenu(bTree); // Ir al menú de operaciones del árbol
                    break;
                case 2:
                    // Cargar un árbol desde un archivo
                    System.out.print("Ingrese el orden del árbol B: ");
                    orden = scanner.nextInt();
                    bTree = loadTreeFromFile(orden); // Implementar carga desde archivo
                    System.out.println("Árbol B cargado exitosamente.");
                    treeMenu(bTree); // Ir al menú de operaciones del árbol
                    break;
                case 3:
                    // Salir
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción no válida. Por favor, intente nuevamente.");
            }
        }
    }

    // Menú para operaciones del árbol
    public static void treeMenu(BTree bTree) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Insertar claves en el árbol");
            System.out.println("2. Mostrar el árbol actual");
            System.out.println("3. Buscar una clave");
            System.out.println("4. Eliminar una clave");
            System.out.println("5. Guardar el árbol en un archivo");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    // Insertar claves
                    System.out.print("Ingrese las claves separadas por espacio: ");
                    scanner.nextLine();  // Limpiar buffer
                    String entrada = scanner.nextLine();
                    String[] claves = entrada.split(" ");
                    for (String clave : claves) {
                        bTree.insert(Integer.parseInt(clave));  // Insertar las claves
                    }
                    System.out.println("Las claves han sido insertadas.");
                    break;
                case 2:
                    // Mostrar el árbol
                    System.out.println("Mostrando el árbol:");
                    bTree.printTree(bTree.root, 0);
                    break;
                case 3:
                    // Buscar una clave
                    System.out.print("Ingrese la clave a buscar: ");
                    int keyToSearch = scanner.nextInt();
                    if (bTree.search(bTree.root, keyToSearch)) {
                        System.out.println("Clave encontrada.");
                    } else {
                        System.out.println("Clave no encontrada.");
                    }
                    break;
                case 4:
                    // Eliminar una clave
                    System.out.print("Ingrese la clave a eliminar: ");
                    int keyToDelete = scanner.nextInt();
                    bTree.delete(bTree.root, keyToDelete); // Eliminar la clave
                    System.out.println("Clave eliminada.");
                    break;
                case 5:
                    // Guardar el árbol en un archivo
                    System.out.println("Guardando el árbol en un archivo...");
                    // Implementar guardar el árbol en un archivo
                    break;
                case 6:
                    // Salir
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción no válida. Por favor, intente nuevamente.");
            }
        }
    }

    // Cargar el árbol desde un archivo (Ejemplo, puedes implementarlo)
    public static BTree loadTreeFromFile(int order) {
        return new BTree(order); // Implementar carga desde archivo
    }
}
