package com.davidantassdev.salus.controller;

import com.davidantassdev.salus.domain.consultas.AgendaDeConsultas;
import com.davidantassdev.salus.domain.consultas.DadosAgendamentoConsulta;
import com.davidantassdev.salus.domain.consultas.DadosDetalhamentoConsulta;
import com.davidantassdev.salus.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @MockBean
    private AgendaDeConsultas agendaDeConsultas;


    @Test
    @DisplayName("400 - Deve devolver HTTP 400 quando dados inválidos")
    @WithMockUser
    void agendar_cenario1() throws Exception {

        var response = mvc.perform(post("/consultas"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    @DisplayName("200 - Deve devolver HTTP 200 e JSON do detalhamento quando dados válidos")
    @WithMockUser
    void agendar_cenario2() throws Exception {

        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;

        var dadosDetalhamento = new DadosDetalhamentoConsulta(
                null,
                2L,
                5L,
                data
        );

        when(agendaDeConsultas.agendar(any()))
                .thenReturn(dadosDetalhamento);

        var response = mvc.perform(
                        post("/consultas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        dadosAgendamentoConsultaJson.write(
                                                new DadosAgendamentoConsulta(
                                                        2L,
                                                        5L,
                                                        data,
                                                        especialidade
                                                )
                                        ).getJson()
                                )
                )
                .andReturn()
                .getResponse();

        // Verifica status
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());

        // Verifica JSON retornado
        assertThat(response.getContentAsString())
                .isEqualTo(
                        dadosDetalhamentoConsultaJson
                                .write(dadosDetalhamento)
                                .getJson()
                );
    }
}