package com.javanauta.agendadortarefas.infrastructure.controller;

import com.javanauta.agendadortarefas.infrastructure.business.TarefaService;
import com.javanauta.agendadortarefas.infrastructure.business.dto.TarefasDTO;
import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    //Método GET para periodo de data (eventos)
    @GetMapping("/eventos")
    public ResponseEntity<List<TarefasDTO>> buscaListaDeTarefasPorPeriodo(
            @RequestParam @DateTimeFormat(iso =  DateTimeFormat.ISO.DATE_TIME)LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso =  DateTimeFormat.ISO.DATE_TIME)LocalDateTime dataFinal){

        return ResponseEntity.ok(tarefaService.buscarTarefasAgendadasPorPeriodo(dataInicial, dataFinal));
    }

    //Método GET por email de usuario
    @GetMapping
    public ResponseEntity<List<TarefasDTO>> buscaListaDeTarefasPorEmail(
            @RequestHeader("Authorization") String token){

        return ResponseEntity.ok(tarefaService.buscarTarefasPorEmail(token));
    }

    //método para deletar DELETE tarefa por id
    @DeleteMapping
    public ResponseEntity<Void> deletarTarefaPorId(@RequestParam("id") String id){
        tarefaService.deletarTarefaPorId(id);
        return ResponseEntity.ok().build();
    }

    //método para alterar status PATCH da tarefa por id
    @PatchMapping
    public ResponseEntity<TarefasDTO> alteraStatusNotificacao(@RequestParam("status") StatusNotificacaoEnum status,
                                                              @RequestParam("id") String id){
        return ResponseEntity.ok(tarefaService.alterarStatus(status, id));
    }

    //método de update de tarefas PUT por id
    @PutMapping
    public ResponseEntity<TarefasDTO> updateTarefas(@RequestBody TarefasDTO tarefasDTO,
                                                    @RequestParam("id") String id){
        return ResponseEntity.ok(tarefaService.updateTarefas(tarefasDTO, id));
    }

}
