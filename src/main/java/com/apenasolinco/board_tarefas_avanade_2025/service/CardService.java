package com.apenasolinco.board_tarefas_avanade_2025.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.apenasolinco.board_tarefas_avanade_2025.dto.BoardColumnInfoDTO;
import com.apenasolinco.board_tarefas_avanade_2025.exception.BlockedCardException;
import com.apenasolinco.board_tarefas_avanade_2025.exception.EntityNotFoundException;
import com.apenasolinco.board_tarefas_avanade_2025.exception.FinishedCardException;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.dao.CardDAO;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnKind;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.CardEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardService {

	private final Connection connection;

	public CardEntity insert(CardEntity entity) throws SQLException {
		try {
			var dao = new CardDAO(connection);
			dao.insert(entity);
			connection.commit();
			return entity;
		} catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
	}

	public void moveToNextColumn(final Long cardId, List<BoardColumnInfoDTO> boardColumnInfo) throws SQLException {
		try {
			var dao = new CardDAO(connection);
			var optional = dao.findById(cardId);
			var dto = optional.orElseThrow(
					() -> new EntityNotFoundException("O card de id %s não foi encontrado.".formatted(cardId)));

			if (dto.blocked()) {
				var message = "O card %s está bloqueado. É necessário desbloqueá-lo para poder movê-lo."
						.formatted(cardId);
				throw new BlockedCardException(message);
			}

			// @formatter:off
			var currentColumn = boardColumnInfo.stream()
				.filter(
					bc -> bc.id().equals(dto.columnId())
				)
				.findFirst()
			.orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board."));
			// @formatter:on

			if (currentColumn.kind().equals(BoardColumnKind.FINAL)) {
				throw new FinishedCardException("O card informado já foi finalizado");
			}

			// @formatter:off
			var nextColumn = boardColumnInfo.stream()
				.filter(
					bc -> bc.order() == currentColumn.order() + 1
				)
				.findFirst()
			.orElseThrow();
			// @formatter:on

			dao.moveToColumn(nextColumn.id(), cardId);
			connection.commit();
		} catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
	}
}
