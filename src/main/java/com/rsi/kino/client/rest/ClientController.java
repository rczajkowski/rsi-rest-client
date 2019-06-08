package com.rsi.kino.client.rest;

import com.rsi.kino.client.domain.cinema.Kino;
import com.rsi.kino.client.dto.CreateReservationDto;
import com.rsi.kino.client.exception.ObjectNotFoundException;
import com.rsi.kino.client.model.Film;
import com.rsi.kino.client.model.Reservation;
import com.rsi.kino.client.rest.resources.FilmResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/films", produces = "application/hal+json")
public class ClientController {

	private final Kino kinoClient;

	public ClientController(Kino kinoClient) {
		this.kinoClient = kinoClient;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping
	public ResponseEntity<Resources<FilmResource>> getAllFilms(){
		List<FilmResource> films = kinoClient.getAllFilms().stream().map(FilmResource::new).collect(Collectors.toList());
		final Resources<FilmResource> resources = new Resources<>(films);
		String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
		resources.add(new Link(uriString, "self"));
		ResponseEntity r = ResponseEntity.ok(resources);
		System.out.println(r);
		return r;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/{filmId}")
	public ResponseEntity<FilmResource> getById(@PathVariable("filmId")UUID fimlId){
		return kinoClient.getAllFilms()
				.stream()
				.filter(f -> f.getId().equals(fimlId))
				.findFirst()
				.map(FilmResource::new)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new ObjenesisException("Nie znaleziono filmu o podanym id"));
	}
}
