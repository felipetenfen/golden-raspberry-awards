package com.outsera.awards.controller;

import com.outsera.awards.model.ProducersIntervalsModel;
import com.outsera.awards.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Awards")
@RestController
@RequestMapping("/awards")
@RequiredArgsConstructor
public class AwardsController {
    private final MovieService movieService;

    @Operation(description = "Retorna os produtores com menor e maior intervalos entre dois prêmios consecutivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requisição realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado", content = @Content(mediaType = ""))
    })
    @GetMapping("/producers")
    public ResponseEntity<ProducersIntervalsModel> getAwardsProducers() {
        return ResponseEntity.ok(movieService.getProducersWithAwardIntervals());
    }
}
