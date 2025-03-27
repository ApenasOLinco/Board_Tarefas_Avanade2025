package com.apenasolinco.board_tarefas_avanade_2025.service;

import static com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnKind.CANCELED;
import static com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnKind.FINAL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.apenasolinco.board_tarefas_avanade_2025.dto.BoardColumnInfoDTO;
import com.apenasolinco.board_tarefas_avanade_2025.dto.CardDetailsDTO;
import com.apenasolinco.board_tarefas_avanade_2025.exception.BlockedCardException;
import com.apenasolinco.board_tarefas_avanade_2025.exception.EntityNotFoundException;
import com.apenasolinco.board_tarefas_avanade_2025.exception.FinishedCardException;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.dao.BlockDAO;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.dao.CardDAO;
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

			throwIfBlocked(dto, "O card de id %s está bloqueado. Não é possível movê-lo.".formatted(cardId));

			// @formatter:off
			var currentColumn = boardColumnInfo.stream()
				.filter(
					bc -> bc.id().equals(dto.columnId())
				)
				.findFirst()
			.orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board."));
			// @formatter:on

			if (currentColumn.kind().equals(FINAL)) {
				throw new FinishedCardException("O card informado já foi finalizado");
			}

			// @formatter:off
			var nextColumn = boardColumnInfo.stream()
				.filter(
					bc -> bc.order() == currentColumn.order() + 1
				)
				.findFirst()
			.orElseThrow(() -> new IllegalStateException("O card informado está na coluna de cancelamento."));
			// @formatter:on

			dao.moveToColumn(nextColumn.id(), cardId);
			connection.commit();
		} catch (SQLException ex) {
			connection.rollback();
			throw ex;
		}
	}

	public void cancelCard(final Long cardId, final Long cancelColumnId,
			final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
		try {
			var dao = new CardDAO(connection);
			var optional = dao.findById(cardId);
			var dto = optional.orElseThrow(
					() -> new EntityNotFoundException("O card de id %s não foi encontrado.".formatted(cardId)));

			throwIfBlocked(dto,
					"O card de id %s está bloqueado. É necessário desbloqueá-lo para poder cancelá-lo.".formatted(cardId));

			// @formatter:off
			var currentColumn = boardColumnsInfo.stream()
				.filter(bc -> bc.id().equals(dto.columnId()))
				.findFirst()
			.orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board."));
			// @formatter:on

			if (cancelColumnId.equals(currentColumn.id()))
				throw new IllegalStateException("O card informado já está cancelado!");
			else if (currentColumn.kind().equals(FINAL))
				throw new IllegalStateException("O card informado já está finalizado. Não é possível cancelá-lo.");

			dao.moveToColumn(cancelColumnId, cardId);
			connection.commit();
		} catch (SQLException ex) {
			connection.rollback();
			throw ex;
		}
	}

	public void block(final Long id, final String reason, final List<BoardColumnInfoDTO> boardColumnsInfo)
			throws SQLException {
		try {
			var dao = new CardDAO(connection);
			var optional = dao.findById(id);
			var dto = optional.orElseThrow(
					() -> new EntityNotFoundException("O card de id %s não foi encontrado.".formatted(id)));

			throwIfBlocked(dto, "O card de id %s já está bloqueado.".formatted(id));

			// @formatter:off
			var currentColumn = boardColumnsInfo.stream()
				.filter(bc -> bc.id().equals(dto.columnId()))
				.findFirst()
			.orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board."));
			// @formatter:on

			if (currentColumn.kind().equals(CANCELED) || currentColumn.kind().equals(FINAL)) {
				throw new IllegalStateException("O card informado pertence a uma coluna %s. Não é possível bloqueá-lo."
						.formatted(currentColumn.kind()));
			}
			
			var blockDAO = new BlockDAO(connection);
			blockDAO.block(id, reason);

			connection.commit();
		} catch (SQLException ex) {
			connection.rollback();
			throw ex;
		}
	}

	private void throwIfBlocked(CardDetailsDTO dto, String message) throws BlockedCardException {
		if (dto.blocked()) {
			throw new BlockedCardException(message);
		}
	}
}
