package com.davidantassdev.salus.controller;

import com.davidantassdev.salus.domain.consultas.AgendaDeConsultas;
import com.davidantassdev.salus.domain.consultas.DadosAgendamentoConsulta;
import com.davidantassdev.salus.domain.consultas.DadosCancelamentoConsulta;
import com.davidantassdev.salus.domain.consultas.DadosDetalhamentoConsulta;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consulta")
public class ConsultaController {

    @Autowired
    private AgendaDeConsultas agenda;

    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoConsulta dados) {
        var dto = agenda.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity cancelar(@RequestBody @Valid DadosCancelamentoConsulta dados) {
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}
