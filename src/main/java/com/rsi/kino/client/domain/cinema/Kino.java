/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.kino.client.domain.cinema;

import com.itextpdf.text.DocumentException;
import com.rsi.kino.client.dto.CreateReservationDto;
import com.rsi.kino.client.exception.ObjectAlreadyExistsException;
import com.rsi.kino.client.exception.ObjectNotFoundException;
import com.rsi.kino.client.model.Film;
import com.rsi.kino.client.model.Reservation;
import com.rsi.kino.client.pdf_service.PdfService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class Kino {

    private PdfService pdfService;

    private UUID userId;

    private List<Film> films = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public Kino(PdfService pdfService) throws IOException {
        this.pdfService = pdfService;
        Film film = new Film("Nowy film", "/home/rafau/IdeaProjects/RSI/photo/avengers1.png",
                Stream.of(LocalDateTime.of(2019, Month.MAY, 20, 11, 15), LocalDateTime.of(2019, Month.MAY, 22, 21, 33))
                        .collect(Collectors.toSet()), "Andrzej Wajda", Arrays.asList("AAAA", "BBB"));
        this.films.add(film);
        System.out.println(film.getId());
        this.userId = UUID.randomUUID();
		System.out.println(films.get(0).getName());
    }

    public List<Film> getAllFilms() {
        return this.films;
    }

    public Reservation makeReseravation(CreateReservationDto createReservationDto)
            throws FileNotFoundException, DocumentException, ObjectNotFoundException, ObjectAlreadyExistsException {
    	UUID filmId = createReservationDto.getFilmId();
    	List<Integer> seats = createReservationDto.getSeats();
    	LocalDateTime date = createReservationDto.getDate();
        Film film = films.stream()
                .filter(film1 -> film1.getId().equals(filmId))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Nie znaleziono filmu o podanym id"));
        if (seats.stream().anyMatch(m -> m > 100 && m < 1)) {
            throw new IllegalArgumentException("Dozwolone miejsca sa od 1 do 100");
        }
        if (film.getSchedule().stream().noneMatch(s -> s.equals(date))) {
            throw new ObjectNotFoundException("Dla podanego filmu nie ma takiej daty");
        }

        Optional<Reservation> checkRes = reservations.stream()
                .filter(r -> r.getFilm().getId().equals(filmId) && r.getDate().equals(date) && seats.stream().anyMatch(m -> r.getSeatsNo().contains(m)))
                .findFirst();

        if (checkRes.isPresent()) {
            throw new ObjectAlreadyExistsException(checkRes.get(), seats);
        }

        Reservation reservation = new Reservation(film.getName(), film, seats);
        reservation.setDate(date);
        reservations.add(reservation);
        reservation.setUserId(this.userId);
        pdfService.generateReservationPdf(reservation);
        System.out.println("Rezerwacja mijesc o numerze: " + reservation.getSeatsNo()  + " na film pt: " + film.getName() + " o  godz " + date);
        return reservation;

    }

    public Reservation resignation(UUID resignationToken) throws ObjectNotFoundException {
        Reservation reservation = reservations.stream()
                .filter(r -> r.getResignationToken().equals(resignationToken))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Nie znaleziono podanej rezerwacji"));
        reservations.remove(reservation);
        return reservation;
    }

    public Reservation updateReservation(UUID reservationId, List<Integer> seats)
            throws ObjectNotFoundException, FileNotFoundException, DocumentException, ObjectAlreadyExistsException {
        Reservation reservation = reservations.stream().filter(r -> r.getId().equals(reservationId)).findFirst().orElseThrow(() -> new ObjectNotFoundException("Nie znaleziono podanej rezerwacji"));

        if (seats.stream().anyMatch(m -> m > 100 && m < 1)) {
            throw new IllegalArgumentException("Dozwolone miejsca sa od 1 do 100");
        }

        Optional<Reservation> checkRes = reservations.stream()
                .filter(r -> r.getFilm().getId().equals(reservation.getFilm().getId()) && r.getDate() == reservation.getDate() && seats.stream().anyMatch(m -> r.getSeatsNo().contains(m)) && !reservation.getId().equals(r.getId()))
                .findFirst();

        if (checkRes.isPresent()) {
            throw new ObjectAlreadyExistsException(checkRes.get(), seats);
        }

        reservations.remove(reservation);
        reservation.setSeatsNo(seats);
        reservations.add(reservation);
        pdfService.generateReservationPdf(reservation);

        return reservation;
    }

    public List<Reservation> getAllUserReservations(UUID userId) {
        return reservations.stream()
                .filter(r -> r.getUserId()
                .equals(userId))
                .collect(Collectors.toList());

    }

	public UUID getUserId() {
		return userId;
	}
}
