package capdb.design.dto.course;

import java.time.LocalDate;

public record DateCourseRequest(String title, String dateRegion, LocalDate date) {
}
