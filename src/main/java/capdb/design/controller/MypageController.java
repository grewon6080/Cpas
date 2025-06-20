package capdb.design.controller;

import capdb.design.domain.DateCourse;
import capdb.design.domain.DateCourseLeg;
import capdb.design.domain.DateCoursePlace;
import capdb.design.dto.DateCourseResponse;
import capdb.design.repository.DateCourseLegRepository;
import capdb.design.repository.DateCoursePlaceRepository;
import capdb.design.repository.DateCourseRepository;
import capdb.design.service.DateCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final DateCourseService courseService;
    private final DateCourseRepository dateCourseRepo;
    private final DateCoursePlaceRepository placeRepo;
    private final DateCourseLegRepository legRepo;

    // 1. 나의 데이트 코스 목록 (마이페이지)
    @GetMapping("/{userId}/datecourse")
    public List<DateCourseResponse> myCourses(@PathVariable Long userId) {
        return courseService.getCourses(userId);
    }

    // 2. 특정 코스 상세 정보 (수정버튼 눌렀을 때)
    @GetMapping("/searchdatecourse/{courseId}")
    public SearchDateCourseDto searchDateCourse(@PathVariable Long courseId) {
        DateCourse course = dateCourseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스 없음"));
        List<DateCoursePlace> places = placeRepo.findByDateCourseIdOrderByOrderIndex(courseId);
        List<DateCourseLeg> legs = legRepo.findByDateCourseId(courseId);

        // 직접 DTO로 가공해서 반환
        return new SearchDateCourseDto(course, places, legs);
    }
        // 마이페이지 목록 페이지 보여주기
        @GetMapping("/{userId}001/datecourse")
        public String showCourseListPage(@PathVariable Long userId, Model model) {
            model.addAttribute("userId", userId); // JS에서 userId 쓰고 싶을 때
            return "datecourse"; // src/main/resources/static/datecourse.html
        }

        // 상세/수정 페이지
        @GetMapping("/searchdatecourse")
        public String showSearchCoursePage() {
            return "searchdatecourse"; // src/main/resources/static/searchdatecourse.html
        }

    // DTO 정의
    public static class SearchDateCourseDto {
        public Long courseId;
        public String title;
        public String dateRegion;
        public LocalDate date;
        public List<PlaceInfo> places;
        public List<LegInfo> legs;

        public SearchDateCourseDto(DateCourse course, List<DateCoursePlace> places, List<DateCourseLeg> legs) {
            this.courseId = course.getId();
            this.title = course.getTitle();
            this.dateRegion = course.getDateRegion();
            this.date = course.getDate();
            this.places = new ArrayList<>();
            for (DateCoursePlace p : places) {
                this.places.add(new PlaceInfo(
                        p.getPlace().getId(),
                        p.getPlace().getPlaceName(),
                        p.getOrderIndex(),
                        p.getDurationMinutes()
                ));
            }
            this.legs = new ArrayList<>();
            for (DateCourseLeg l : legs) {
                this.legs.add(new LegInfo(
                        l.getFromPlace().getId(),
                        l.getToPlace().getId(),
                        l.getTransportType(),
                        l.getDistanceM(),
                        l.getDurationMinutes()
                ));
            }
        }
    }
    public static class PlaceInfo {
        public Long placeId;
        public String placeName;
        public int orderIndex;
        public int durationMinutes;
        public PlaceInfo(Long placeId, String placeName, int orderIndex, int durationMinutes) {
            this.placeId = placeId;
            this.placeName = placeName;
            this.orderIndex = orderIndex;
            this.durationMinutes = durationMinutes;
        }
    }
    public static class LegInfo {
        public Long fromPlaceId;
        public Long toPlaceId;
        public String transportType;
        public int distanceM;
        public int durationMinutes;
        public LegInfo(Long fromPlaceId, Long toPlaceId, String transportType, int distanceM, int durationMinutes) {
            this.fromPlaceId = fromPlaceId;
            this.toPlaceId = toPlaceId;
            this.transportType = transportType;
            this.distanceM = distanceM;
            this.durationMinutes = durationMinutes;
        }
    }
}
