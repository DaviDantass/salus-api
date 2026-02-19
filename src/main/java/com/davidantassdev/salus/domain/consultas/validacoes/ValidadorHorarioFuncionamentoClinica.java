package com.davidantassdev.salus.domain.consultas.validacoes;

import com.davidantassdev.salus.domain.ValidacaoException;
import com.davidantassdev.salus.domain.consultas.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoDeConsulta {
    public void validar(DadosAgendamentoConsulta dados) {
        var  dataConsulta= dados.data();
        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaAberturaDaClinica = dataConsulta.getHour() < 7;
        var DepoisDoEncerramentoDaClinica = dataConsulta.getHour() > 18;
        if (domingo || antesDaAberturaDaClinica || DepoisDoEncerramentoDaClinica) {
            throw new ValidacaoException("Consulta dora do horario de funcionamento da clinica");
        }
    }
}
