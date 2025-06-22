package capdb.design.dto;

import java.time.LocalTime;

public record DateCoursePlaceResponse(Long id, Long placeId,
                                      String placeName, Integer orderIndex,
                                      LocalTime duration, String memo) {

}
