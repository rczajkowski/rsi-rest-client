package com.rsi.kino.client.rest;


import com.rsi.kino.client.KinoClient;
import com.rsi.kino.client.dto.CreateReservationDto;
import com.rsi.kino.client.serve_kino.DocumentException_Exception;
import com.rsi.kino.client.serve_kino.FileNotFoundException_Exception;
import com.rsi.kino.client.serve_kino.Film;
import com.rsi.kino.client.serve_kino.ObjectNotFoundException_Exception;
import com.rsi.kino.client.serve_kino.Reservation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/films")
public class ClientController {

	private final KinoClient kinoClient;

	public ClientController(KinoClient kinoClient) {
		this.kinoClient = kinoClient;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping
	public ResponseEntity<List<Film>> getAllFilms(){
		return ResponseEntity.ok(kinoClient.getAllFilms());
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/reservation/user")
	public ResponseEntity<List<Reservation>> getAllUserReservations() {
		return ResponseEntity.ok(kinoClient.getAllReservations(kinoClient.getUserId()));
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/{filmId}/reservation")
	public ResponseEntity<String> makeReservation(@PathVariable("filmId") String filmId, @RequestBody CreateReservationDto reservationDto)
			throws DocumentException_Exception, FileNotFoundException_Exception {
		return ResponseEntity.ok(kinoClient.makeReservation(filmId, reservationDto.getDate(), reservationDto.getSeats()));
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping("/resignation")
	public ResponseEntity<String> resignation(@RequestParam String tokenResignation) throws ObjectNotFoundException_Exception {
		return ResponseEntity.ok(kinoClient.resignation(tokenResignation));
	}


	@RequestMapping(value = "/pdf/{reservationId}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<InputStreamResource> downloadPDFFile(@PathVariable("reservationId") UUID reservationId)
			throws IOException {


		File pdfFile = new File("/home/rafau/IdeaProjects/rsi/ps6/auth/client/src/main/resources/reservation-" + reservationId +".pdf");
		InputStream targetStream = new FileInputStream(pdfFile);

		return ResponseEntity
				.ok()
				.contentLength(pdfFile.length())
				.contentType(
						MediaType.parseMediaType("application/pdf"))
				.body(new InputStreamResource(targetStream));
	}


	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping("/reservation/{reservationId}")
	public ResponseEntity<String> updateReservation(@PathVariable("reservationId") UUID reservationId, @RequestBody CreateReservationDto dto)
			throws ObjectNotFoundException_Exception, DocumentException_Exception, FileNotFoundException_Exception {
		return ResponseEntity.ok(kinoClient.updateReservation(reservationId.toString(), dto.getSeats()));
	}
}
