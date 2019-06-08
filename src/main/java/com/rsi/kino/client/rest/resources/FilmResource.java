package com.rsi.kino.client.rest.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.rsi.kino.client.model.Film;
import com.rsi.kino.client.rest.ClientController;
import java.util.UUID;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class FilmResource extends ResourceSupport {
	private final Film film;

	@JsonCreator
	public FilmResource(Film film) {
		this.film = film;
		final UUID id = film.getId();
		add(linkTo(ClientController.class).withRel("films"));
		add(linkTo(methodOn(ClientController.class).getById(id)).withSelfRel());
	}
}
