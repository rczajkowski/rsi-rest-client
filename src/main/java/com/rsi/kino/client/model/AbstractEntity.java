package com.rsi.kino.client.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "id")
public abstract class AbstractEntity implements Serializable {

	private UUID id;

	public AbstractEntity() {

	}

	public UUID getId() {
		return id;
	}

	@Override
	public String toString() {
		return "AbstractEntity{" + "id=" + id + '}';
	}
}
