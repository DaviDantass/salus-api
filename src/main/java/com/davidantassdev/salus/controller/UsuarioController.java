package com.davidantassdev.salus.controller;

import com.davidantassdev.salus.domain.usuario.DadosCadastroUsuario;
import com.davidantassdev.salus.domain.usuario.DadosDetalhamentoUsuario;
import com.davidantassdev.salus.domain.usuario.Usuario;
import com.davidantassdev.salus.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoUsuario> cadastrar(
            @RequestBody @Valid DadosCadastroUsuario dados,
            UriComponentsBuilder uriBuilder) {

        var senhaHash = passwordEncoder.encode(dados.senha());
        var usuario = new Usuario(null, dados.login(), senhaHash);

        repository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
    }
}
