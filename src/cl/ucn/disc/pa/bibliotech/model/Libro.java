/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.model;

/**
 * Clase que representa un Libro.
 *
 * @author Programacion Avanzada.
 */
public final class Libro {

    /**
     * The ISBN.
     */
    private String isbn;

    /**
     * The Titulo.
     */
    private String titulo;

    /**
     * The Author.
     */
    private String autor;

    /**
     * The Categoria
     */
    private String categoria;
    /**
     * The Disponibilidad
     */
    private boolean disponibilidad;


    // TODO: Agregar la calificacion.
    private double calificacion;

    private double cantidadTotalEstrellas;

    private double cantidadValoraciones;

    /**
     * The Constructor.
     *
     * @param isbn      del libro.
     * @param titulo    del libro.
     * @param autor     del libro
     * @param categoria del libro.
     */
    public Libro(final String isbn, final String titulo, final String autor, final String categoria) {

        // TODO: agregar validacion de ISBN
        if (isbn == null || isbn.length() == 0) {
            throw new IllegalArgumentException("ISBN no valido");
        }
        // validacion del titulo
        if (titulo == null || titulo.length() == 0) {
            throw new IllegalArgumentException("Titulo no valido!");
        }
        this.titulo = titulo;
        // TODO: Agregar validacion
        if (autor == null || autor.length() == 0) {
            throw new IllegalArgumentException("Autor no valido!");
        }
        this.autor = autor;
        // TODO: Agregar validacion
        if (categoria == null || categoria.length() == 0) {
            throw new IllegalArgumentException("Categoria no valida!");
        }
        this.categoria = categoria;
    }

    /**
     * @return the ISBN.
     */
    public String getIsbn() {
        return this.isbn;
    }

    /**
     * @return the titulo.
     */
    public String getTitulo() {
        return this.titulo;
    }

    /**
     * @return the autor.
     */
    public String getAutor() {
        return this.autor;
    }

    /**
     * @return the categoria.
     */
    public String getCategoria() {
        return this.categoria;
    }

    /**
     * @return the calificacion
     */


    // serie de gets y sets , que seran usados en la parte de sistemas para obtener todo tipo de calculos
    // sobre la valoracion del libro y las reseñas que reciben del mismo.

    //disponibilidad que sera utilizado para ver que libro se puede arrendar y cual no
    public double getCalificacion() {
        return this.calificacion;
    }

    public boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public double getCantidadTotalEstrellas() {
        return cantidadTotalEstrellas;
    }

    public void setCantidadTotalEstrellas(double cantidadTotalEstrellas) {
        this.cantidadTotalEstrellas = cantidadTotalEstrellas;
    }

    public double getCantidadValoraciones() {
        return cantidadValoraciones;
    }

    public void setCantidadValoraciones(double cantidadValoraciones) {
        this.cantidadValoraciones = cantidadValoraciones;
    }
}
