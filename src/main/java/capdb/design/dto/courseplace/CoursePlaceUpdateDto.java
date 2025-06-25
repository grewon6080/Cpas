package capdb.design.dto.courseplace;

import java.time.LocalTime;

public record CoursePlaceUpdateDto(Long placeId,Integer orderIndex, LocalTime duration, String memo) {
}
