package capdb.design.service;

import capdb.design.domain.*;
import capdb.design.dto.course.DateCourseRequest;
import capdb.design.dto.course.DateCourseResponse;
import capdb.design.dto.course.DateCourseUpdateRequest;
import capdb.design.dto.courseplace.CoursePlaceUpdateDto;
import capdb.design.dto.courseplace.DateCourseDetailResponse;
import capdb.design.dto.courseplace.DateCoursePlaceResponse;
import capdb.design.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class DateCourseService {

    private final DateCourseRepository courseRepo;
    private final UserRepository userRepo;
    private final PlaceRepository placeRepo;

    /** CREATE **/
    public Long createDateCourse(Long userId, DateCourseRequest req) {
        User user = userRepo.findById(userId).orElseThrow();
        DateCourse c = DateCourse.builder()
                .user(user)
                .title(req.title())
                .dateRegion(req.dateRegion())
                .date(req.date())
                .createdAt(LocalDateTime.now())
                .build();
        return courseRepo.save(c).getId();
    }


    /** READ **/
    @Transactional(readOnly = true)
    public List<DateCourseResponse> readListByUser(Long userId) {
        List<DateCourseResponse> dtoList = new ArrayList<>();
        for (DateCourse c : courseRepo.findByUserId(userId)) dtoList.add(toDto(c));
        return dtoList;
    }

    @Transactional(readOnly = true)
    public DateCourseDetailResponse readCourseWithPlaces(Long userId, Long courseId) {

        // ① 코스 조회 & (선택) 소유 사용자 확인
        DateCourse course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스 없음"));


        // ② 코스 DTO 변환
        DateCourseResponse courseDto = toDto(course);

        // ③ 장소 목록 DTO 변환
        List<DateCoursePlaceResponse> placeDtos = new ArrayList<>();
        course.getPlaces().stream()
                .sorted(Comparator.comparingInt(DateCoursePlace::getOrderIndex))
                .forEach(p -> placeDtos.add(toPlaceDto(p)));

        // ④ 통합 DTO로 반환
        return new DateCourseDetailResponse(courseDto, placeDtos);
    }

    /** UPDATE 코스 정보(title, dateRegion, date)  **/
    public void updateDateCourse(Long courseId, DateCourseUpdateRequest req) {

        DateCourse  dateCourse= courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스 없음"));

        /*데이트 코스 기본정보 수정*/
        dateCourse.update(req.title(), req.dateRegion(), req.date());

        /*저장한 장소 초기화*/
        dateCourse.getPlaces().clear();

        /*저장된 장소 업데이트*/
        updateDateCoursePlace(req, dateCourse); //저장된 장소 업데이트

    }

    private void updateDateCoursePlace(DateCourseUpdateRequest req, DateCourse dateCourse) {
        for (CoursePlaceUpdateDto p : req.places()) {
            Place place = placeRepo.findById(p.placeId()).orElseThrow();

            DateCoursePlace entry = DateCoursePlace.builder()
                    .dateCourse(dateCourse)
                    .place(place)
                    .orderIndex(p.orderIndex())
                    .duration(p.duration())
                    .memo(p.memo())
                    .build();

            dateCourse.getPlaces().add(entry);
        }
    }

    /** DELETE **/
    public void delete(Long id) { courseRepo.deleteById(id); }


    /** Dto 변환 **/
    private DateCourseResponse toDto(DateCourse c) {
        return new DateCourseResponse(
                c.getId(), c.getTitle(), c.getDateRegion(),
                c.getDate(), c.getCreatedAt());
    }

    private DateCoursePlaceResponse toPlaceDto(DateCoursePlace e){
        return new DateCoursePlaceResponse(
                e.getId(),
                e.getPlace().getId(),
                e.getPlace().getPlaceName(),
                e.getOrderIndex(), e.getDuration(), e.getMemo()
        );
    }


}

