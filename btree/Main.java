package btree;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BTree bTree = null; // Initialize the tree as null

        while (true) {
            // Main Menu: Create or load a tree
            System.out.println("\n1. Crear un nuevo árbol B"); // "Create a new B-tree"
            System.out.println("2. Cargar un árbol desde un archivo"); // "Load a tree from a file"
            System.out.println("3. Salir"); // "Exit"
            System.out.print("Seleccione una opción: "); // "Select an option: "
            
            int opcion = scanner.nextInt(); // Get the selected option

            switch (opcion) {
                case 1:
                    // Create a new B-tree
                    System.out.print("Ingrese el orden del árbol B: ");  // "Enter the B-tree order: "
                    int orden = scanner.nextInt(); // Ask for the tree order
                    bTree = new BTree(orden); // Create the tree with the given order
                    System.out.println("Árbol B de orden " + orden + " creado exitosamente.");  // "B-tree of order X created successfully."
                    treeMenu(bTree); // Go to the tree's menu
                    break;
                case 2:
                    // Load a B-tree from a file
                    System.out.print("Ingrese el orden del árbol B: ");  // "Enter the B-tree order: "
                    orden = scanner.nextInt(); // Ask for the tree order
                    bTree = loadTreeFromFile(orden); // Implement file loading
                    System.out.println("Árbol B cargado exitosamente."); // "B-tree loaded successfully."
                    treeMenu(bTree); // Go to the tree's menu
                    break;
                case 3:
                    // Exit the program
                    System.out.println("Saliendo..."); // "Exiting..."
                    return; // End the program
                default:
                    System.out.println("Opción no válida. Por favor, intente nuevamente."); // "Invalid option. Please try again."
            }
        }
    }

    // Menu for tree operations after creation or loading
    public static void treeMenu(BTree bTree) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Insertar claves en el árbol"); // "Insert keys into the tree"
            System.out.println("2. Mostrar el árbol actual"); // "Show the current tree"
            System.out.println("3. Guardar el árbol en un archivo"); // "Save the tree to a file"
            System.out.println("4. Salir"); // "Exit"
            System.out.print("Seleccione una opción: "); // "Select an option: "
            
            int opcion = scanner.nextInt(); // Get the selected option

            switch (opcion) {
                case 1:
                    // Insert keys into the tree
                    System.out.print("Ingrese las claves separadas por espacio: "); // "Enter keys separated by space:"
                    scanner.nextLine();  // Clear the buffer
                    String entrada = scanner.nextLine();  // Read the keys
                    String[] clavesStr = entrada.split(" ");  // Split the input keys
                    for (String clave : clavesStr) {
                        try {
                            int key = Integer.parseInt(clave);  // Convert to integer
                            bTree.insert(key); // Insert the key into the tree
                        } catch (NumberFormatException e) {
                            System.out.println("La clave '" + clave + "' no es un número válido."); // "The key 'X' is not a valid number."
                        }
                    }
                    System.out.println("Las claves han sido insertadas."); // "The keys have been inserted."
                    break;
                case 2:
                    // Show the current tree
                    System.out.println("Mostrando el árbol:"); // "Showing the tree:"
                    bTree.printTree(bTree.root, 0); // Call the method to print the tree
                    break;
                case 3:
                    // Save the tree to a file
                    System.out.println("Guardando el árbol en un archivo..."); // "Saving the tree to a file..."
                    saveTreeToFile(bTree); // Implement file saving
                    break;
                case 4:
                    // Exit the tree menu
                    System.out.println("Saliendo del menú del árbol..."); // "Exiting the tree menu..."
                    return;
                default:
                    System.out.println("Opción no válida. Por favor, intente nuevamente."); // "Invalid option. Please try again."
            }
        }
    }

    // Method to load a tree from a file (you need to implement this part)
    public static BTree loadTreeFromFile(int order) {
        // This method should read the tree from a file and return it
        return new BTree(order); // For now, just returning a new B-tree
    }

    // Method to save a tree to a file (you need to implement this part)
    public static void saveTreeToFile(BTree bTree) {
        // This method should serialize the tree or write it to a file
    }
}
