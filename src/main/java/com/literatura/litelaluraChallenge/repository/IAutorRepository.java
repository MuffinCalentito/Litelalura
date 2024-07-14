package com.literatura.litelaluraChallenge.repository;
import com.literatura.litelaluraChallenge.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAutorRepository extends JpaRepository<Autor,Long> {

    List<Autor> findAll();


    List<Autor> findByFechaNacimientoLessThanOrFechaFallecimientoGreaterThanEqual(int year, int year1);

    Optional<Autor> findFirstByNombreContainsIgnoreCase(String escritor);
}
