package com.apenasolinco.board_tarefas_avanade_2025.dto;

import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnKind;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKind kind) {

}
