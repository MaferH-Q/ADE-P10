package Ejercicios;
import java.util.Scanner;

public class Mainestudiante {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BTreeestudiante arbol = new BTreeestudiante();

        // Menú interactivo
        boolean running = true;
        while (running) {
            System.out.println("\n--- Menú ---");
            System.out.println("1. Insertar estudiante");
            System.out.println("2. Buscar estudiante por código");
            System.out.println("3. Eliminar estudiante por código");
            System.out.println("4. Mostrar árbol");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            
            switch (opcion) {
                case 1:
                    // Insertar estudiante
                    System.out.print("Ingrese el código del estudiante: ");
                    int codigoInsertar = scanner.nextInt();
                    scanner.nextLine();  // Consumir la línea
                    System.out.print("Ingrese el nombre del estudiante: ");
                    String nombreInsertar = scanner.nextLine();
                    arbol.insert(new RegistroEstudiante(codigoInsertar, nombreInsertar));
                    System.out.println("Estudiante insertado correctamente.");
                    break;

                case 2:
                    // Buscar estudiante por código
                    System.out.print("Ingrese el código del estudiante a buscar: ");
                    int codigoBuscar = scanner.nextInt();
                    String nombreEstudiante = arbol.buscarNombre(codigoBuscar);
                    System.out.println("Estudiante encontrado: " + nombreEstudiante);
                    break;

                case 3:
                    // Eliminar estudiante por código
                    System.out.print("Ingrese el código del estudiante a eliminar: ");
                    int codigoEliminar = scanner.nextInt();
                    arbol.eliminar(codigoEliminar);
                    System.out.println("Estudiante eliminado.");
                    break;

                case 4:
                    // Mostrar árbol
                    System.out.println("Mostrando árbol:");
                    arbol.mostrar();
                    break;

                case 5:
                    // Salir
                    running = false;
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción inválida, por favor intente de nuevo.");
                    break;
            }
        }

        scanner.close();
    }
}