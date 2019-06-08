package com.rsi.kino.client.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Reservation implements Serializable {


	public Reservation(String name, Film film, List<Integer> seatsNo) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.film = film;
		this.seatsNo = seatsNo;
		this.resignationToken = UUID.randomUUID();
	}
	private UUID id;

	private UUID userId;

	private String name;

	private Film film;

	private List<Integer> seatsNo;

	private UUID resignationToken;

	private LocalDateTime date;

}
