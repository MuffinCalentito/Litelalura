package com.literatura.litelaluraChallenge.models;
import com.literatura.litelaluraChallenge.dtos.Genero;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long libroId;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // Asegura que el autor se guarde automáticamente
    @JoinColumn(name = "autor_id")
    //@Transient
    private Autor autor;
    @Enumerated(EnumType.STRING)
    private Genero genero;
    private String idioma;
    private Long numDescargas;

    public Libro() {
    }

    public Libro(DatosLibro datosLibro) {
        this.libroId = datosLibro.libroId();
        this.titulo = datosLibro.titulo();
        // Si autor es una lista de autores (como parece en tu registro DatosLibro)
        if (datosLibro.autor() != null && !datosLibro.autor().isEmpty()) {
            this.autor = new Autor(datosLibro.autor().get(0)); // Toma el primer autor de la lista
        } else {
            this.autor = null; // o maneja el caso de que no haya autor
        }
        this.genero =  generoMod(datosLibro.genero());
        this.idioma = idiomaMod(datosLibro.idioma());
        this.numDescargas = datosLibro.cantidadDescargas();
    }

    public Libro(Libro libro) {
    }

    private Genero generoMod(List<String> generos) {
        if (generos == null || generos.isEmpty()) {
            return Genero.DESCONOCIDO;
        }
        Optional<String> firstGenero = generos.stream()
                .map(g -> {
                    int index = g.indexOf("--");
                    return index != -1 ? g.substring(index + 2).trim() : null;
                })
                .filter(Objects::nonNull)
                .findFirst();
        return firstGenero.map(Genero::fromString).orElse(Genero.DESCONOCIDO);
    }

    private String idiomaMod(List<String> idiomas) {
        if (idiomas == null || idiomas.isEmpty()) {
            return "Desconocido";
        }
        return idiomas.get(0);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutores() {
        return autor;
    }

    public void setAutores(Autor autores) {
        this.autor = autores;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Long getCantidadDescargas() {
        return numDescargas;
    }

    public void setCantidadDescargas(Long cantidadDescargas) {
        this.numDescargas = cantidadDescargas;
    }

    @Override
    public String toString() {
        return
                "  \nN° de libro guardado= " + id +
                "  \nIdentificación del libro= " + libroId +
                ", \nTitulo= '" + titulo + '\'' +
                ", \nAutores= " + (autor != null ? autor.getNombre() : "N/A")+
                ", \nGenero= " + genero +
                ", \nIdioma= " + idioma +
                ", \nCantidad de descargas= " + numDescargas;
    }
}
