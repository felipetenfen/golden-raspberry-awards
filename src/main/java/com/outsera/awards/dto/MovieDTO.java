package com.outsera.awards.dto;

import java.util.Set;

public record MovieDTO(
        int year,
        String title,
        boolean winner,
        Set<StudioDTO> studios,
        Set<ProducerDTO> producers) {
}
