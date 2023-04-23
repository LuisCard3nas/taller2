/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.services;

import cl.ucn.disc.pa.bibliotech.model.Libro;
import cl.ucn.disc.pa.bibliotech.model.Socio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.princeton.cs.stdlib.StdIn;
import edu.princeton.cs.stdlib.StdOut;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The Sistema.
 *
 * @author Programacion Avanzada.
 */
public final class Sistema {

    /**
     * Procesador de JSON.
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * The list of Socios.
     */
    private Socio[] socios;

    /**
     * The list of Libros.
     */
    private Libro[] libros;

    /**
     * Socio en el sistema.
     */
    private Socio socio;

    /**
     * The Sistema.
     */
    public Sistema() throws IOException {

        // no hay socio logeado.
        this.socios = new Socio[0];
        this.libros = new Libro[0];
        this.socio = null;

        // carga de los socios y libros.
        try {
            this.cargarInformacion();
        } catch (FileNotFoundException ex) {
            // no se encontraron datos, se agregar los por defecto.

            // creo un socio
            this.socios = Utils.append(this.socios, new Socio("John", "Doe", "john.doe@ucn.cl", 1, "john123"));

            // creo un libro y lo agrego al arreglo de libros.
            this.libros = Utils.append(this.libros, new Libro("1491910771", "Head First Java: A Brain-Friendly Guide", " Kathy Sierra", "Programming Languages"));

            // creo otro libro y lo agrego al arreglo de libros.
            this.libros = Utils.append(this.libros, new Libro("14919107721", "Effective Java", "Joshua Bloch", "Programming Languages"));

        } finally {
            // guardo la informacion.
            this.guardarInformacion();
        }

    }

    /**
     * Activa (inicia sesion) de un socio en el sistema.
     *
     * @param numeroDeSocio a utilizar.
     * @param contrasenia   a validar.
     */
    public void iniciarSession(final int numeroDeSocio, final String contrasenia) {


        // El numero de socio siempre es positivo.
        if (numeroDeSocio <= 0) {
            throw new IllegalArgumentException("El numero de socio no es valido!");
        }

        //buscar el socio dado su numero.
        int aux = -1;
        for (int i = 0; i < socios.length; i++) {
            Socio a = this.socios[i];
            if (a.getNumeroDeSocio() == numeroDeSocio) {
                aux = i;
                break;
            }
        }
        if (aux == -1) {
            throw new IllegalArgumentException("No se ha encontrado el numero de socio");
        }

        // verificar su clave.
        this.socio = socios[numeroDeSocio - 1];
        if (!contrasenia.equals(socio.getContrasenia())) {
            throw new IllegalArgumentException("La contraseña es incorrecta");
        }

        // ATRIBUTO SOCIO A SOCIO ENCONTRADO
        this.socio = socios[numeroDeSocio - 1];


    }

    /**
     * Cierra la session del Socio.
     */
    public void cerrarSession() {
        this.socio = null;
    }

    /**
     * Metodo que mueve un libro de los disponibles y lo ingresa a un Socio.
     *
     * @param isbn del libro a prestar.
     */
    public void realizarPrestamoLibro(final String isbn) throws IOException {
        // el socio debe estar activo.
        if (this.socio == null) {
            throw new IllegalArgumentException("Socio no se ha logeado!");
        }


        // busco el libro.
        Libro libro = this.buscarLibro(isbn);

        // si no lo encontre, lo informo.
        if (libro == null) {
            throw new IllegalArgumentException("Libro con isbn " + isbn + " no existe o no se encuentra disponible.");
        }

        // agrego el libro al socio.
        this.socio.agregarLibro(libro);


        // se actualiza la informacion de los archivos
        this.guardarInformacion();

    }

    /**
     * Obtiene un String que representa el listado completo de libros disponibles.
     *
     * @return the String con la informacion de los libros disponibles.
     */
    public String obtegerCatalogoLibros() {

        StringBuilder sb = new StringBuilder();
        for (Libro libro : this.libros) {
            sb.append("Titulo    : ").append(libro.getTitulo()).append("\n");
            sb.append("Autor     : ").append(libro.getAutor()).append("\n");
            sb.append("ISBN      : ").append(libro.getIsbn()).append("\n");
            sb.append("Categoria : ").append(libro.getCategoria()).append("\n");
            sb.append("Calificacion : ").append(libro.getCalificacion()).append("\n"); //Se agregó Calificacion para verlo en el menu de libros
            sb.append("Disponibilidad : ").append(libro.getDisponibilidad()).append("\n");//Se agregó Disponibilidad
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Metodo que busca un libro en los libros disponibles.
     *
     * @param isbn a buscar.
     * @return el libro o null si no fue encontrado.l
     */
    private Libro buscarLibro(final String isbn) {
        // recorro el arreglo de libros.
        for (Libro libro : this.libros) {
            // si lo encontre, retorno el libro.
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        // no lo encontre, retorno null.
        return null;
    }

    /**
     * Lee los archivos libros.json y socios.json.
     *
     * @throws FileNotFoundException si alguno de los archivos no se encuentra.
     */
    private void cargarInformacion() throws FileNotFoundException {

        // trato de leer los socios y los libros desde el archivo.
        this.socios = GSON.fromJson(new FileReader("socios.json"), Socio[].class);
        this.libros = GSON.fromJson(new FileReader("libros.json"), Libro[].class);
    }

    /**
     * Guarda los arreglos libros y socios en los archivos libros.json y socios.json.
     *
     * @throws IOException en caso de algun error.
     */
    private void guardarInformacion() throws IOException {

        // guardo los socios.
        try (FileWriter writer = new FileWriter("socios.json")) {
            GSON.toJson(this.socios, writer);
        }

        // guardo los libros.
        try (FileWriter writer = new FileWriter("libros.json")) {
            GSON.toJson(this.libros, writer);
        }

    }

    public String obtenerDatosSocioLogeado() {
        if (this.socio == null) {
            throw new IllegalArgumentException("No hay un Socio logeado");
        }

        return "Nombre: " + this.socio.getNombreCompleto() + "\n"
                + "Correo Electronico: " + this.socio.getCorreoElectronico();
    }

    public void calificacionDeLibros(String ISBNaCalificar, double cantidadDeEstrellas) {


        int aux1 = 0;
        //logica validacion ISBN
        while (aux1 == 0) {
            int aux = -1;
            for (int i = 0; i < libros.length; i++) {
                Libro a = this.libros[i];
                if (a.getIsbn().equals(ISBNaCalificar)) {
                    aux = i;
                    aux1 = 1;
                    //tras encontrar el ISBN del libro obtenemos su cantidad de estrellas y cantidad de validaciones
                    //para actualizarlas con la nueva información
                    double cantidadDeEstrellasTotales = this.libros[i].getCantidadTotalEstrellas();
                    cantidadDeEstrellasTotales += cantidadDeEstrellas;

                    double auxCantidadValoraciones = this.libros[i].getCantidadValoraciones();
                    auxCantidadValoraciones++;

                    double auxCalificacion = (double) cantidadDeEstrellasTotales / (double) auxCantidadValoraciones;

                    //sets para actualizar los valores editados
                    this.libros[i].setCalificacion(auxCalificacion);
                    this.libros[i].setCantidadValoraciones(auxCantidadValoraciones);
                    this.libros[i].setCantidadTotalEstrellas(cantidadDeEstrellasTotales);

                    break;

                }
            }
            // Ciclos para asegurarse que el usuario ingrese los datos de manera correcta
            if (aux == -1) {
                StdOut.print("Ingrese un ISBN valido, vuelva a intentarlo: ");
                ISBNaCalificar = StdIn.readString();
                StdOut.println("Ingrese la cantidad de estrellas con la que desea calificar el libro : ");
                cantidadDeEstrellas = StdIn.readDouble();
                //Validacion de calificacion 0 al 5
                boolean condicion = true;
                if (cantidadDeEstrellas <= 5 && cantidadDeEstrellas >= 0) {
                    condicion = false;
                }
                while (condicion) {
                    StdOut.println("Ingrese una cantidad validad (0 al 5)");
                    cantidadDeEstrellas = StdIn.readDouble();

                    if (cantidadDeEstrellas <= 5 && cantidadDeEstrellas >= 0) {
                        condicion = false;
                    }

                }
            }
        }
    }

    // // este subprograma recibe el nuevo correo leido en el main para cambiarlo por el nuevo correo del socio que legeo anteriormente
    public void cambiarCorreo(String correoNuevo) {
        this.socio.setCorreoElectronico(correoNuevo);
    }


    // este subprograma recibe la nueva contraseña leida en el main para cambiarla por la contraseña del socio que legeo anteriormente
    public void cambiarContrasenia(String contraseniaNueva) {
        this.socio.setContrasenia(contraseniaNueva);
    }


    public void validacionCantidadEstrellas(double cantEstrellas) {
        //utilizamos este subprograma para verfiicar el dato de entrada que proviene desde el main 
        // y ver si esta dentro de los requisitos en este caso entre 0 a 5
        boolean condicion = true;
        if (cantEstrellas <= 5 && cantEstrellas >= 0) {
            condicion = false;
        }
        while (condicion) {
            StdOut.println("Ingrese una cantidad validad (0 al 5)");
            cantEstrellas = StdIn.readDouble();
            if (cantEstrellas <= 5 && cantEstrellas >= 0) {
                condicion = false;
            }
        }

    }

    public void disponibilidadLibro(String ISBNaBuscar) {
        /* Este subprograma se utiliza para ver y cambiar la disponibilidad de los libros, se declaro en una variable de tipo
        boolean para identifacarlos, "true" significa disponible y "false" no disponible
         */
        String auxISBNaGuardarEnVectoLibrosDelUsusuario = "";
        boolean auxDisponibilidad = true;
        int aux1 = 0;
        while (aux1 == 0) {
            int aux = -1;
            // Un buscador entre el valor dado por el usuario (ISBNaBuscar) y el valor del arreglo//
            for (int i = 0; i < libros.length; i++) {
                Libro a = this.libros[i];
                if (a.getIsbn().equals(ISBNaBuscar)) {
                    aux = i;
                    aux1 = 1;
                    auxDisponibilidad = this.libros[i].getDisponibilidad();
                    /*Condicionales para ver el estado del libro en la carpeta libros.json y si el usuario pide el libro con
                    el IBSN este cambia el estado de disponibilidad de dicho libro a un "false" o sea, no disponible
                     */
                    if (auxDisponibilidad) {
                        auxDisponibilidad = false;
                        StdOut.println("[*] Libro disponible,Agregado con exito [*]");
                    } else {
                        StdOut.println("[*] El libro el cual desea solicitar no se encuentra disponible [*]");
                    }
                    this.libros[i].setDisponibilidad(auxDisponibilidad);
                    auxISBNaGuardarEnVectoLibrosDelUsusuario = this.libros[i].getIsbn();

                }
            }
            if (aux == -1) {
                StdOut.print("Ingrese un ISBN valido, vuelva a intentarlo: ");
                ISBNaBuscar = StdIn.readString();
            }
        }
    }
}
