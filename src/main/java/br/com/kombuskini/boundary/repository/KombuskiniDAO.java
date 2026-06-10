package br.com.kombuskini.boundary.repository;

import br.com.kombuskini.entity.Kombuskini;

import java.util.List;
import java.util.Optional;

public interface KombuskiniDAO {
    Kombuskini save(Kombuskini kombuskini);
    void update(Kombuskini kombuskini);
    void delete(int id);
    Optional<Kombuskini> findById(int id);
    List<Kombuskini> findAll();
}
