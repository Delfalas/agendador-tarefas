package com.javanauta.agendadortarefas.infrastructure.business.mapper;

import com.javanauta.agendadortarefas.infrastructure.business.dto.TarefasDTO;
import com.javanauta.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TarefaConverter {

    TarefasEntity paraTarefasEntity(TarefasDTO tarefasDTO);

    TarefasDTO paraTarefasDTO(TarefasEntity tarefasEntity);

    //conversor de Listas de Tarefas
    List<TarefasEntity> paraListTarefasEntity(List<TarefasDTO> listTarefasDTO);

    List<TarefasDTO> paraListTarefasDTO(List<TarefasEntity> listTarefasEntity);
}
