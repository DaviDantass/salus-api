package com.davidantassdev.salus.controller;

import com.davidantassdev.salus.domain.usuario.DadosCadastroUsuario;
import com.davidantassdev.salus.domain.usuario.Usuario;
import com.davidantassdev.salus.domain.usuario.UsuarioRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroUsuario> dadosCadastroUsuarioJson;

    @MockBean
    private UsuarioRepository repository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("400 - Should return HTTP 400 when login is missing")
    void cadastrar_cenario1() throws Exception {
        var response = mvc
                .perform(post("/usuarios"))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("201 - Should return HTTP 201 when user is registered successfully")
    void cadastrar_cenario2() throws Exception {
        var dados = new DadosCadastroUsuario("testuser", "senha123");

        when(passwordEncoder.encode("senha123")).thenReturn("encodedPassword");
        when(repository.save(any())).thenReturn(new Usuario(1L, "testuser", "encodedPassword"));

        var response = mvc
                .perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroUsuarioJson.write(dados).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED.value());
    }
}
