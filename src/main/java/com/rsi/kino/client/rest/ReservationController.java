package com.rsi.kino.client.rest;

import com.itextpdf.text.DocumentException;
import com.rsi.kino.client.domain.cinema.Kino;
import com.rsi.kino.client.dto.CreateReservationDto;
import com.rsi.kino.client.exception.LambdaExceptionWrapper;
import com.rsi.kino.client.exception.ObjectAlreadyExistsException;
import com.rsi.kino.client.exception.ObjectNotFoundException;
import com.rsi.kino.client.model.Reservation;
import com.rsi.kino.client.rest.resources.FilmResource;
import com.rsi.kino.client.rest.resources.ReservationResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.Link;
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
@RequestMapping(value = "/reservations", produces = "application/hal+json")
public class ReservationController {

	private final Kino kinoClient;

	public ReservationController(Kino kinoClient) {
		this.kinoClient = kinoClient;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping
	public ResponseEntity<Resources<ReservationResource>> getAllUserReservations() throws Exception {
		List<ReservationResource> reservations = kinoClient.getAllUserReservations(kinoClient.getUserId()).stream()
				.map(LambdaExceptionWrapper.rethrowFunction(ReservationResource::new))
				.collect(Collectors.toList());
		final Resources<ReservationResource> resources = new Resources<>(reservations);
		String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
		resources.add(new Link(uriString, "self"));
		ResponseEntity r = ResponseEntity.ok(resources);
		return ResponseEntity.ok(resources);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping
	public ResponseEntity<Reservation> makeReservation(@RequestBody CreateReservationDto reservationDto)
			throws FileNotFoundException, ObjectNotFoundException, ObjectAlreadyExistsException, DocumentException {
		return ResponseEntity.ok(kinoClient.makeReseravation(reservationDto));
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping
	public ResponseEntity<Reservation> resignation(@RequestParam UUID tokenResignation) throws ObjectNotFoundException {
		return ResponseEntity.ok(kinoClient.resignation(tokenResignation));
	}


	@RequestMapping(value = "/{reservationId}/pdf", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<InputStreamResource> downloadPDFFile(@PathVariable("reservationId") UUID reservationId)
			throws IOException {
		File pdfFile = new File("/home/rafau/IdeaProjects/rsi/ps6/auth/client/src/main/resources/reservation-" + reservationId +".pdf");
		InputStream targetStream = new FileInputStream(pdfFile);

		return ResponseEntity
				.ok()
				.contentLength(pdfFile.length())
				.contentType(MediaType.parseMediaType("application/pdf"))
				.body(new InputStreamResource(targetStream));
	}

	@GetMapping(value = "/{reservationId}")
	public ResponseEntity<ReservationResource> getById(@PathVariable("reservationId") UUID reservationId) throws Exception {

		return kinoClient.getAllUserReservations(kinoClient.getUserId())
				.stream()
				.filter(r -> r.getId().equals(reservationId))
				.findFirst()
				.map(LambdaExceptionWrapper.rethrowFunction(ReservationResource::new))
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new ObjenesisException("Nie znaleziono filmu o podanym id"));
	}


	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping("/{reservationId}")
	public ResponseEntity<Reservation> updateReservation(@PathVariable("reservationId") UUID reservationId, @RequestBody CreateReservationDto dto)
			throws ObjectNotFoundException, ObjectAlreadyExistsException, DocumentException, FileNotFoundException {
		return ResponseEntity.ok(kinoClient.updateReservation(reservationId, dto.getSeats()));
	}
}
