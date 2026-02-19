package com.davidantassdev.salus.domain.consultas.validacoes;

import com.davidantassdev.salus.domain.ValidacaoException;
import com.davidantassdev.salus.domain.consultas.DadosAgendamentoConsulta;
import com.davidantassdev.salus.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoDeConsulta {
    @Autowired
    private MedicoRepository medicoRepository;

    public void validar(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() == null) {
            return;
        }
        var medicoEstaAtivo = medicoRepository.findAtivoById(dados.idMedico());
        if (!medicoEstaAtivo){
            throw new ValidacaoException("Consulta nao pode ser agendada com medico inativo");
        }
    }
}
