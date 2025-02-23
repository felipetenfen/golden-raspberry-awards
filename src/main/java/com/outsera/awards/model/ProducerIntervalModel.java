package com.outsera.awards.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProducerIntervalModel {
    private String producer;
    private int interval;
    private int previousWin;
    private int followingWin;
}