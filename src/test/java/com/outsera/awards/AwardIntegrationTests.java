package com.outsera.awards;

import com.outsera.awards.service.DataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AwardIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Deve retornar corretamente os produtores com os intervalos de prêmios")
	void shouldReturn200WhenGetRequestIsValid() throws Exception {
		ResultActions resultActions =
				mockMvc.perform(MockMvcRequestBuilders.get("/awards/producers"));

		resultActions
				// Valida o status da requisição
				.andExpect(MockMvcResultMatchers.status().isOk())

				// Valida o tipo de conteúdo da resposta
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))

				// Valida a estrutura e existência dos campos principais
				.andExpect(MockMvcResultMatchers.jsonPath("min").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("max").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("min").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("max").isArray())

				// Valida o primeiro item de "min"
				.andExpect(MockMvcResultMatchers.jsonPath("min[0].producer").value("Joel Silver"))
				.andExpect(MockMvcResultMatchers.jsonPath("min[0].interval").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("min[0].previousWin").value(1990))
				.andExpect(MockMvcResultMatchers.jsonPath("min[0].followingWin").value(1991))

				// Valida o primeiro item de "max"
				.andExpect(MockMvcResultMatchers.jsonPath("max[0].producer").value("Matthew Vaughn"))
				.andExpect(MockMvcResultMatchers.jsonPath("max[0].interval").value(13))
				.andExpect(MockMvcResultMatchers.jsonPath("max[0].previousWin").value(2002))
				.andExpect(MockMvcResultMatchers.jsonPath("max[0].followingWin").value(2015));
	}

	@Test
	@DisplayName("Deve retornar não encontrado ao buscar um endpoint inexistente")
	void shouldReturn404WhenResourceNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/awards/producers/999"))
				// Valida o status da requisição
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}
