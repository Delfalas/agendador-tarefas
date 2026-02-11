package com.javanauta.agendadortarefas.infrastructure.business;

import com.javanauta.agendadortarefas.infrastructure.business.dto.TarefasDTO;
import com.javanauta.agendadortarefas.infrastructure.business.mapper.TarefaConverter;
import com.javanauta.agendadortarefas.infrastructure.business.mapper.TarefaUpdateConverter;
import com.javanauta.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.javanauta.agendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.javanauta.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefasRepository tarefasRepository;
    private final TarefaConverter tarefaConverter;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateConverter tarefaUpdateConverter;

    public TarefasDTO gravarTarefas(String token, TarefasDTO tarefasDTO){
        //Extrair o email do token e setar email
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        tarefasDTO.setDataCriacao(LocalDateTime.now());
        tarefasDTO.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        tarefasDTO.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefasEntity(tarefasDTO);

        // grava e salva a tarefa entity e converte novamente para DTO
        return tarefaConverter.paraTarefasDTO(tarefasRepository.save(entity));
    }

    public List<TarefasDTO> gravarTarefasEmLote(String token, List<TarefasDTO> tarefasDTOList) {

        String email = jwtUtil.extrairEmailToken(token.substring(7));

        List<TarefasEntity> entidades = tarefasDTOList.stream()
                .map(dto -> {
                    dto.setDataCriacao(LocalDateTime.now());
                    dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
                    dto.setEmailUsuario(email);
                    return tarefaConverter.paraTarefasEntity(dto);
                })
                .toList();

        List<TarefasEntity> salvas = tarefasRepository.saveAll(entidades);

        return salvas.stream()
                .map(tarefaConverter::paraTarefasDTO)
                .toList();
    }

    //método para buscar GET por periodo
    public List<TarefasDTO> buscarTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal){
        return tarefaConverter.paraListTarefasDTO(tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    //método para buscar GET por email de usuario
    public List<TarefasDTO> buscarTarefasPorEmail(String token){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        return tarefaConverter.paraListTarefasDTO(tarefasRepository.findByEmailUsuario(email));
    }

    //método para deletar DELETE tarefa por id
    public void deletarTarefaPorId(String id){
        try {
            tarefasRepository.deleteById(id);
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(" Tarefa não encontrada! " + id, e.getCause());
        }
    }

    //método para alterar status PATCH da tarefa por id
    public TarefasDTO alterarStatus(StatusNotificacaoEnum status, String id){
        try {
            TarefasEntity entity = tarefasRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada! " + id));
            entity.setStatusNotificacaoEnum(status);
            return tarefaConverter.paraTarefasDTO(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa! " + e.getCause());
        }
    }

    //método de update de tarefas PUT por id
    public TarefasDTO updateTarefas(TarefasDTO tarefasDTO, String id){
        try {
            TarefasEntity entity = tarefasRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada! " + id));
            tarefaUpdateConverter.updateTarefas(tarefasDTO, entity);
            return tarefaConverter.paraTarefasDTO(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa! " + e.getCause());
        }
    }

}
