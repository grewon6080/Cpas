package capdb.design.dto.courseplace;

import capdb.design.dto.course.DateCourseResponse;

import java.util.List;

public record DateCourseDetailResponse(DateCourseResponse course,
                                       List<DateCoursePlaceResponse> places) {
}
