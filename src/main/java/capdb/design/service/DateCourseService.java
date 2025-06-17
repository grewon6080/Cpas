package capdb.design.service;

import capdb.design.domain.DateCourse;
import capdb.design.domain.DateCourseLeg;
import capdb.design.domain.DateCoursePlace;
import capdb.design.dto.CourseDetailResponse;
import capdb.design.repository.DateCourseLegRepository;
import capdb.design.repository.DateCoursePlaceRepository;
import capdb.design.repository.DateCourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DateCourseService {

    private final DateCourseRepository dateCourseRepository;
    private final DateCoursePlaceRepository dateCoursePlaceRepository;
    private final DateCourseLegRepository dateCourseLegRepository;

    // 1. 단일 코스 상세조회
    public CourseDetailResponse getCourseDetail(Long courseId) {
        // 코스, 장소, legs 전부 페치 조인 or 쿼리로 한 번에 조회 (성능 최적화)
        DateCourse course = dateCourseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("코스 없음"));

        // 장소 목록
        List<DateCoursePlace> places = dateCoursePlaceRepository
                .findByDateCourseIdOrderByOrderIndex(courseId);

        // 이동 경로 목록
        List<DateCourseLeg> legs = dateCourseLegRepository
                .findByDateCourseId(courseId);

        // DTO 변환
        return CourseDetailResponse.of(course, places, legs);
    }

//    // 2. (추가) 코스 목록 조회 (사용자별)
//    public List<CourseSimpleResponse> getCoursesByUser(Long userId) {
//        return dateCourseRepository.findByUserId(userId)
//                .stream()
//                .map(CourseSimpleResponse::from)
//                .toList();
//    }
}
