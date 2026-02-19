package com.davidantassdev.salus.domain.consultas.validacoes;

import com.davidantassdev.salus.domain.ValidacaoException;
import com.davidantassdev.salus.domain.consultas.DadosAgendamentoConsulta;
import com.davidantassdev.salus.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteAtivo implements ValidadorAgendamentoDeConsulta{
        @Autowired
        private PacienteRepository repository;

        public void validar(DadosAgendamentoConsulta dados){
            var pacienteAtivo = repository.findAtivoById(dados.idPaciente());
            if(!pacienteAtivo){
                throw new ValidacaoException("Consulta nao pode ser agendada com paciente exlcuido");
            }
        }

}
