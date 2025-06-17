package btree;
import Excepciones.ItemNoFound;
import java.io.File;
import java.util.Scanner;

@SuppressWarnings("unused")
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BTree<Integer> btree = null;
        int orden;

        // ================================
        // Inicio: Selección de inicialización
        // ================================
        System.out.println("====== CONSTRUCCIÓN INICIAL ======");
        System.out.println("1. Crear árbol vacío");
        System.out.println("2. Cargar árbol desde archivo (Ejercicio 03)");
        System.out.print("Seleccione una opción: ");
        int inicial = sc.nextInt();
        sc.nextLine(); // limpiar buffer

        if (inicial == 1) {
            System.out.print("Ingrese el orden del Árbol B: ");
            orden = sc.nextInt();
            sc.nextLine();
            btree = new BTree<>(orden);
        } else if (inicial == 2) {
            System.out.print("Ingrese la ruta del archivo arbolB.txt: ");
            String ruta = sc.nextLine();
            try {
                btree = BTree.building_Btree(ruta); // Ejercicio 03
                System.out.println("✔ Árbol cargado exitosamente desde archivo.");
            } catch (ItemNoFound e) {
                System.out.println("❌ Error: " + e.getMessage());
                return;
            }
        } else {
            System.out.println("❌ Opción inválida.");
            return;
        }

        // ======================
        // Menú interactivo
        // ======================
        int opcion;
        do {
            System.out.println("\n====== MENÚ ÁRBOL B ======");
            System.out.println("1. Insertar clave");
            System.out.println("2. Mostrar árbol (formato figura 10.14)");
            System.out.println("3. Verificar si está vacío");
            System.out.println("4. Buscar clave (Ejercicio 01)");
            System.out.println("5. Eliminar clave (Ejercicio 02)");
            System.out.println("6. Insertar claves para construir figura 10.13 (demo)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1: // Insertar clave
                    System.out.print("Ingrese la clave a insertar: ");
                    int claveInsertar = sc.nextInt();
                    btree.insert(claveInsertar);
                    System.out.println("→ Clave insertada.");
                    break;

                case 2: // Mostrar árbol
                    System.out.println("\n==== ÁRBOL B ====");
                    System.out.println(btree);
                    break;

                case 3: // Verificar vacío
                    System.out.println(btree.isEmpty() ? "El árbol está vacío." : "El árbol NO está vacío.");
                    break;

                case 4: // Ejercicio 01 - Buscar
                    System.out.print("Ingrese la clave a buscar: ");
                    int claveBuscar = sc.nextInt();
                    boolean existe = btree.search(claveBuscar);
                    if (!existe)
                        System.out.println("La clave " + claveBuscar + " no se encuentra en el árbol.");
                    break;

                case 5: // Ejercicio 02 - Eliminar
                    System.out.print("Ingrese la clave a eliminar: ");
                    int claveEliminar = sc.nextInt();
                    btree.remove(claveEliminar);
                    System.out.println("→ Clave eliminada (si existía).");
                    break;

                case 6: // Cargar claves de la figura 10.13
                    int[] claves = {
                        12, 25, 80, 142, 20, 150, 176, 206, 297,
                        300,
                        380, 395, 412,
                        430, 451,
                        480,
                        493, 506,
                        520,
                        521, 600
                    };
                    for (int clave : claves) {
                        btree.insert(clave);
                    }
                    System.out.println("✔ Árbol B cargado con las claves de la figura 10.13.");
                    break;

                case 0:
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida. Intente otra vez.");
            }
        } while (opcion != 0);

        sc.close();
    }
}
