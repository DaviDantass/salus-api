package com.davidantassdev.salus.controller;

import com.davidantassdev.salus.domain.endereco.DadosEndereco;
import com.davidantassdev.salus.domain.paciente.DadosCadastroPaciente;
import com.davidantassdev.salus.domain.paciente.DadosListagemPaciente;
import com.davidantassdev.salus.domain.paciente.Paciente;
import com.davidantassdev.salus.domain.paciente.PacienteRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class PacienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroPaciente> dadosCadastroPacienteJson;

    @MockBean
    private PacienteRepository repository;

    @Test
    @DisplayName("400 - Should return HTTP 400 when data is invalid")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {
        var response = mvc
                .perform(post("/pacientes"))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("200 - Should return HTTP 200 when data is valid")
    @WithMockUser
    void cadastrar_cenario2() throws Exception {
        var dadosCadastro = new DadosCadastroPaciente(
                "João Silva",
                "joao@example.com",
                "61999999999",
                "123.456.789-01",
                dadosEndereco());

        when(repository.save(any())).thenReturn(new Paciente(dadosCadastro));

        var response = mvc
                .perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroPacienteJson.write(dadosCadastro).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "Rua Test",
                "Bairro Test",
                "12345678",
                "Brasilia",
                "DF",
                null,
                null);
    }
}
