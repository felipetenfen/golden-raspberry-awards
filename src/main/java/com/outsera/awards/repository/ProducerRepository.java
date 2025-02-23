package com.outsera.awards.repository;

import com.outsera.awards.model.ProducerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<ProducerModel, Long> {

//    @Query("SELECT p.name, m.title, m.release_year, m.winner\n" +
//            "FROM producer p \n" +
//            "JOIN movie_producer mp on p.id = mp.producer_id\n" +
//            "JOIN movie m on mp.movie_id = m.id\n" +
//            "WHERE m.winner = true \n" +
//            "ORDER BY p.name, m.release_year desc")
//    @Query("SELECT p FROM producer p " +
//            "JOIN FETCH p.movies m " +
//            "WHERE m.winner = true " +
//            "ORDER BY p.name, m.releaseYear DESC")
//    List<ProducerModel> findProducersWithWinningMovies();

    Optional<ProducerModel> findByName(String name);
}
