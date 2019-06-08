package com.rsi.kino.client.exception;

import com.rsi.kino.client.model.Film;
import com.rsi.kino.client.model.Reservation;
import java.util.List;

public class ObjectAlreadyExistsException extends Exception {

	public ObjectAlreadyExistsException( String message) {
		super(message);
	}

	public ObjectAlreadyExistsException(Reservation reservation, List<Integer> seats) {
		super("Miejsce na film " + reservation.getFilm().getName() + " o godzinie " +  reservation.getDate().toString() +  " o numerze "
				+ reservation.getSeatsNo().stream().filter(seats::contains).findFirst().orElse(0) + " jest już zajęte");
	}
}
