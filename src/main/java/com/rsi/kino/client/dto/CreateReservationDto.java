package com.rsi.kino.client.dto;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateReservationDto {

	private UUID filmId;

	@NotNull
	private LocalDateTime date;

	@NotEmpty
	private List<Integer> seats;


}
