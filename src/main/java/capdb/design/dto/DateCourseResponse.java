package capdb.design.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DateCourseResponse(Long id, String title, String dateRegion,
                                 LocalDate date, LocalDateTime createdAt) {
}
