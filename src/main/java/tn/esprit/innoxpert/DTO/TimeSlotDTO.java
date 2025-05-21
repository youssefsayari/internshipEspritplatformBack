package tn.esprit.innoxpert.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean available;
}