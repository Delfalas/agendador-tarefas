package com.javanauta.agendadortarefas.infrastructure.controller;

import com.javanauta.agendadortarefas.infrastructure.business.TarefaService;
import com.javanauta.agendadortarefas.infrastructure.business.dto.TarefasDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    // 🔹 Cadastro unitário (continua igual)
    @PostMapping
    public ResponseEntity<TarefasDTO> salvarTarefas(
            @RequestHeader("Authorization") String token,
            @RequestBody TarefasDTO tarefasDTO) {

        return ResponseEntity.ok(tarefaService.gravarTarefas(token, tarefasDTO));
    }

    // 🔹 Cadastro em lote
    @PostMapping("/lote")
    public ResponseEntity<List<TarefasDTO>> salvarTarefasEmLote(
            @RequestHeader("Authorization") String token,
            @RequestBody List<TarefasDTO> tarefasDTOList) {

        return ResponseEntity.ok(
                tarefaService.gravarTarefasEmLote(token, tarefasDTOList)
        );
    }
}
