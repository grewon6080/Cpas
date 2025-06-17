package capdb.design.dto;

import capdb.design.domain.DateCourse;
import capdb.design.domain.DateCourseLeg;
import capdb.design.domain.DateCoursePlace;

import java.time.LocalDateTime;
import java.util.List;

public record CourseDetailResponse(
        Long courseId,
        String title,
        LocalDateTime createdAt,
        List<PlaceInfo> places,
        List<LegInfo> legs
) {
    public static CourseDetailResponse of(DateCourse course, List<DateCoursePlace> places, List<DateCourseLeg> legs) {
        return new CourseDetailResponse(
                course.getId(),
                course.getTitle(),
                course.getCreatedAt(),
                places.stream().map(PlaceInfo::from).toList(),
                legs.stream().map(LegInfo::from).toList()
        );
    }

    public record PlaceInfo(Long placeId, String placeName, int orderIndex, int durationMinutes) {
        public static PlaceInfo from(DateCoursePlace p) {
            return new PlaceInfo(
                    p.getPlace().getId(),
                    p.getPlace().getPlaceName(),
                    p.getOrderIndex(),
                    p.getDurationMinutes()
            );
        }
    }
    public record LegInfo(Long fromPlaceId, Long toPlaceId, String transportType, int distanceM) {
        public static LegInfo from(DateCourseLeg l) {
            return new LegInfo(
                    l.getFromPlace().getId(),
                    l.getToPlace().getId(),
                    l.getTransportType(),
                    l.getDistanceM()
            );
        }
    }
}
