package com.apenasolinco.board_tarefas_avanade_2025.persistence.converter;

import static java.time.ZoneOffset.UTC;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocalDateTimeConverter {

	public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
		return Objects.isNull(timestamp) ? null : LocalDateTime.ofInstant(timestamp.toInstant(), UTC);
	}

}
