package com.outsera.awards.service;

import com.outsera.awards.dto.MovieDTO;
import com.outsera.awards.dto.ProducerDTO;
import com.outsera.awards.dto.StudioDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CsvService {
    private static final Logger LOGGER = Logger.getLogger(CsvService.class.getName());

    public List<MovieDTO> readCsv(String filePath) {
        List<MovieDTO> movies = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            String[] dataLine;
            reader.readLine(); // skip header

            if ((line = reader.readLine()) == null) {
                throw new IOException("File is empty");
            }

            do {
                dataLine = line.split(";");
                Set<StudioDTO> studios = new HashSet<>();

                for (String studio : dataLine[2].split(",")) {
                    studios.add(new StudioDTO(studio.trim()));
                }

                Set<ProducerDTO> producers = getProducers(dataLine[3].split(","));

                movies.add(new MovieDTO(
                        Integer.parseInt(dataLine[0]),
                        dataLine[1],
                        dataLine.length > 4 && "yes".equals(dataLine[4]),
                        studios,
                        producers));

            } while ((line = reader.readLine()) != null);

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found: " + filePath, e);
            throw new RuntimeException("File not found: " + filePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: " + filePath, e);
            throw new RuntimeException("Error reading file: " + filePath, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred", e);
            throw new RuntimeException(e);
        }

        return movies;
    }

    private static Set<ProducerDTO> getProducers(String[] producersArray) {
        Set<ProducerDTO> producers = new HashSet<>();

        for (String producer : producersArray) {
            if (producer.contains(" and ")) {
                String[] splitProducers = producer.split(" and ");
                if (!splitProducers[0].trim().isEmpty()) {
                    producers.add(new ProducerDTO(splitProducers[0].trim()));
                }

                producers.add(new ProducerDTO(splitProducers[1].trim()));
                continue;
            }

            producers.add(new ProducerDTO(producer.trim()));
        }

        return producers;
    }
}
