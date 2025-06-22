package capdb.design.dto;

import java.time.LocalTime;

public record DateCoursePlaceRequest(Long placeId, Integer  orderIndex,
                                     LocalTime duration, String memo) {
}
