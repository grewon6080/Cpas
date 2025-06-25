package capdb.design.dto.course;

import capdb.design.dto.courseplace.CoursePlaceUpdateDto;

import java.time.LocalDate;
import java.util.List;

public record DateCourseUpdateRequest(String title, String dateRegion,
                                      LocalDate date, List<CoursePlaceUpdateDto> places) {
}
