package com.davidantassdev.salus.domain.consultas;

import com.davidantassdev.salus.domain.ValidacaoException;
import com.davidantassdev.salus.domain.consultas.validacoes.ValidadorAgendamentoDeConsulta;
import com.davidantassdev.salus.domain.consultas.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
import com.davidantassdev.salus.domain.medico.Medico;
import com.davidantassdev.salus.domain.medico.MedicoRepository;
import com.davidantassdev.salus.domain.paciente.PacienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AgendaDeConsultas {
    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;

    @Autowired
    private List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
        log.info("Scheduling appointment for patient: {}, doctor: {}, date: {}",
                dados.idPaciente(), dados.idMedico(), dados.data());

        if (!pacienteRepository.existsById(dados.idPaciente())) {
            log.warn("Patient not found: {}", dados.idPaciente());
            throw new ValidacaoException("Id do paciente informado não existe!");
        }

        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
            log.warn("Doctor not found: {}", dados.idMedico());
            throw new ValidacaoException("Id do médico informado não existe!");
        }

        validadores.forEach(v -> v.validar(dados));

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);
        if (medico == null) {
            log.warn("No available doctor found for specialty: {} on date: {}", dados.especialidade(), dados.data());
            throw new ValidacaoException("Não existe médico disponível nessa data!");
        }

        var consulta = new Consulta(null, medico, paciente, dados.data(), null);
        consultaRepository.save(consulta);

        log.info("Appointment scheduled successfully. ID: {}", consulta.getId());
        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null) {
            return medicoRepository.getReferenceById(dados.idMedico());
        }
        if (dados.especialidade() == null) {
            throw new ValidacaoException("especialidade é obrigatoria quando medico especifico nao for escolhido");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        log.info("Canceling appointment: {}, reason: {}", dados.idConsulta(), dados.motivo());

        if (!consultaRepository.existsById(dados.idConsulta())) {
            log.warn("Appointment not found: {}", dados.idConsulta());
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());

        log.info("Appointment canceled successfully. ID: {}", dados.idConsulta());
    }
}
