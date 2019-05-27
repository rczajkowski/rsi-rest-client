/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.kino.client;

import com.rsi.kino.client.serve_kino.DocumentException_Exception;
import com.rsi.kino.client.serve_kino.FileNotFoundException_Exception;
import com.rsi.kino.client.serve_kino.Film;
import com.rsi.kino.client.serve_kino.Kino;
import com.rsi.kino.client.serve_kino.KinoService;
import com.rsi.kino.client.serve_kino.ObjectNotFoundException_Exception;
import com.rsi.kino.client.serve_kino.Reservation;
import java.awt.Image;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;


/**
 *
 * @author Tomek
 */
@Component
public class KinoClient {

    private UUID userId = UUID.randomUUID();

    Kino port;

    public KinoClient() {
        port = new KinoService().getKinoPort();
    }

    public UUID getUserId() {
        return userId;
    }

    public List<Film> getAllFilms() {
        List<Film> filmy = port.getAllFilms();
        return filmy;
    }

    public Image getPicture(String filmId) throws ObjectNotFoundException_Exception {
        return port.getImage(filmId);
    }

    public String makeReservation(String filmId, long date, List<Integer> seats)
            throws DocumentException_Exception, FileNotFoundException_Exception {
        return port.makeReseravation(userId.toString(), seats, filmId, date);
    }

    public String resignation(String tokenResignation) throws ObjectNotFoundException_Exception {
        return port.resignation(tokenResignation);
    }

    public String updateReservation(String reservationId, List<Integer> seats)
            throws ObjectNotFoundException_Exception, DocumentException_Exception, FileNotFoundException_Exception {
        return port.updateReservation(reservationId, seats);
    }

    public List<Reservation> getAllReservations(UUID userId) {
        return port.getAllUserReservations(userId.toString());
    }
}
