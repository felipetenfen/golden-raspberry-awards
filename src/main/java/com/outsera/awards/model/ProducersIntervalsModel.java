package com.outsera.awards.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProducersIntervalsModel {
    private List<ProducerIntervalModel> min;
    private List<ProducerIntervalModel> max;
}
