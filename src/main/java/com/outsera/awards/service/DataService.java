package com.outsera.awards.service;

import com.outsera.awards.dto.MovieDTO;
import com.outsera.awards.dto.ProducerDTO;
import com.outsera.awards.dto.StudioDTO;
import com.outsera.awards.model.MovieModel;
import com.outsera.awards.model.ProducerModel;
import com.outsera.awards.model.StudioModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataService {
    private final CsvService csvService;
    private final MovieService movieService;

    @Value("${spring.csv.file-path}")
    private String csvFilePath;

    @EventListener(ApplicationReadyEvent.class)
    public void persistDataOnStartup() {
        String csvFilePathEnv = System.getenv("CSV_FILE_PATH");

        if (csvFilePathEnv != null) {
            csvFilePath = csvFilePathEnv;
        }

        persistData(csvFilePath);
    }

    public void persistData(String filePath) {
        List<MovieDTO> movies = csvService.readCsv(filePath);

        for (MovieDTO movieDTO : movies) {
            MovieModel movieModel = new MovieModel();
            BeanUtils.copyProperties(movieDTO, movieModel);

            movieModel.setStudios(convertStudios(movieDTO.studios()));
            movieModel.setProducers(convertProducers(movieDTO.producers()));

            movieService.saveMovie(movieModel);
        }
    }

    private Set<StudioModel> convertStudios(Set<StudioDTO> studioDTOs) {
        return studioDTOs.stream()
                .map(dto -> {
                    StudioModel model = new StudioModel();
                    BeanUtils.copyProperties(dto, model);
                    return model;
                })
                .collect(Collectors.toSet());
    }

    private Set<ProducerModel> convertProducers(Set<ProducerDTO> producerDTOs) {
        return producerDTOs.stream()
                .map(dto -> {
                    ProducerModel model = new ProducerModel();
                    BeanUtils.copyProperties(dto, model);
                    return model;
                })
                .collect(Collectors.toSet());
    }
}
