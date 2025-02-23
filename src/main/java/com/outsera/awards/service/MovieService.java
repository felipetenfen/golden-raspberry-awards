package com.outsera.awards.service;

import com.outsera.awards.model.*;
import com.outsera.awards.repository.MovieRepository;
import com.outsera.awards.repository.ProducerRepository;
import com.outsera.awards.repository.StudioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final StudioRepository studioRepository;
    private final ProducerRepository producerRepository;

    @Transactional
    public void saveMovie(MovieModel movie) {
        Set<StudioModel> studiosUpdated = new HashSet<>();

        for (StudioModel studio : movie.getStudios()) {
            Optional<StudioModel> existingStudio = studioRepository.findByName(studio.getName());

            if (existingStudio.isPresent()) {
                studiosUpdated.add(existingStudio.get());
            } else {
                studioRepository.save(studio);
                studiosUpdated.add(studio);
            }
        }

        movie.setStudios(studiosUpdated);

        Set<ProducerModel> producersUpdated = new HashSet<>();

        for (ProducerModel producer : movie.getProducers()) {
            Optional<ProducerModel> existingProducer = producerRepository.findByName(producer.getName());

            if (existingProducer.isPresent()) {
                producersUpdated.add(existingProducer.get());
            } else {
                producerRepository.save(producer);
                producersUpdated.add(producer);
            }
        }

        movie.setProducers(producersUpdated);

        movieRepository.save(movie);
    }

    public ProducersIntervalsModel getProducersWithAwardIntervals() {
        List<ProducerModel> producers = producerRepository.findAll();

        Map<Long, List<Integer>> producerAwards = new HashMap<>();

        for (ProducerModel producer : producers) {
            List<Integer> winningYears = producer.getMovies().stream()
                    .filter(MovieModel::isWinner)
                    .map(MovieModel::getYear)
                    .toList();

            if (winningYears.size() > 1) {
                producerAwards.computeIfAbsent(producer.getId(), k -> new ArrayList<>())
                        .addAll(winningYears);
            }
        }


        int maxInterval = 0;
        int minInterval = Integer.MAX_VALUE;
        List<ProducerIntervalModel> minIntervalProducers = new ArrayList<>();
        List<ProducerIntervalModel> maxIntervalProducers = new ArrayList<>();

        for (Map.Entry<Long, List<Integer>> entry : producerAwards.entrySet()) {
            List<Integer> years = entry.getValue();
            Collections.sort(years);

            for (int i = 1; i < years.size(); i++) {
                int interval = years.get(i) - years.get(i - 1);

                ProducerIntervalModel producerIntervalModel = createProducerIntervalModel(
                        producers, entry.getKey(), interval, years.get(i - 1), years.get(i));

                if (producerIntervalModel == null) {
                    continue;
                }

                if (interval <= minInterval) {
                    if (interval < minInterval) {
                        minIntervalProducers.clear();
                    }

                    minInterval = interval;
                    minIntervalProducers.add(producerIntervalModel);
                }

                if (interval >= maxInterval) {
                    if (interval > maxInterval) {
                        maxIntervalProducers.clear();
                    }

                    maxInterval = interval;
                    maxIntervalProducers.add(producerIntervalModel);
                }
            }
        }

        minIntervalProducers.sort(Comparator.comparingInt(ProducerIntervalModel::getPreviousWin));
        maxIntervalProducers.sort(Comparator.comparingInt(ProducerIntervalModel::getPreviousWin));

        return new ProducersIntervalsModel(minIntervalProducers, maxIntervalProducers);
    }

    private ProducerIntervalModel createProducerIntervalModel(
            List<ProducerModel> producers, Long key, int interval, int previousWin, int followingWin) {
        ProducerModel producerModel = producers.stream()
                .filter(p -> p.getId() == key)
                .findFirst()
                .orElse(null);

        if (producerModel == null) {
            return null;
        }

        return new ProducerIntervalModel(producerModel.getName(), interval, previousWin, followingWin);
    }
}
