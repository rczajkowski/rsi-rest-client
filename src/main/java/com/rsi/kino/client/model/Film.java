package com.rsi.kino.client.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Film implements Serializable {

	public Film(String name, String pathToImage, Set<LocalDateTime> schedule, String director, List<String> cast) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.pathToImage = pathToImage;
		this.schedule = schedule;
		this.director = director;
		this.cast = cast;
	}

	private UUID id;

	private String name;

	private String pathToImage;

	private Set<LocalDateTime> schedule;

	private String director;

	private List<String> cast;
}
