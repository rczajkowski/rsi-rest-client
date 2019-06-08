package com.rsi.kino.client.rest.resources;

import com.rsi.kino.client.exception.ObjectNotFoundException;
import com.rsi.kino.client.model.Reservation;
import com.rsi.kino.client.rest.ClientController;
import com.rsi.kino.client.rest.ReservationController;
import java.io.IOException;
import java.util.UUID;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class ReservationResource extends ResourceSupport {

	private final Reservation reservation;

	public ReservationResource(Reservation reservation) throws Exception {
		this.reservation = reservation;
		final UUID id = reservation.getId();
		add(linkTo(ReservationController.class).withRel("reservations"));
		add(linkTo(methodOn(ReservationController.class).getById(id)).withSelfRel());
		add(linkTo(methodOn(ClientController.class).getById(reservation.getFilm().getId())).withRel("film"));
		add(linkTo(methodOn(ReservationController.class).downloadPDFFile(reservation.getId())).withRel("downloadPdf"));
		add(linkTo(methodOn(ReservationController.class).resignation(reservation.getResignationToken())).withRel("resignation"));
	}
}
