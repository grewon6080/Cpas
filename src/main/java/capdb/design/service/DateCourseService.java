package capdb.design.service;

import capdb.design.domain.*;
import capdb.design.dto.*;
import capdb.design.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
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


    /** READ 목록 **/
    @Transactional(readOnly = true)
    public List<DateCourseResponse> readListByUser(Long userId) {
        List<DateCourse> courses = courseRepo.findByUserId(userId);
        List<DateCourseResponse> result = new ArrayList<>();
        for (DateCourse course : courses) {
            DateCourseResponse dto = toDto(course);
            result.add(dto);
        }
        return result;
    }


    /** UPDATE 코스 정보(title, dateRegion, date)  **/
    public void updateDateCourse(Long id, DateCourseRequest req) {
        courseRepo.findById(id).orElseThrow()
                .update(req.title(), req.dateRegion(), req.date());
    }

    /**UPDATE 장소 추가**/
    public DateCoursePlaceResponse addPlace(Long courseId, DateCoursePlaceRequest req) {
        DateCourse course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스 없음"));
        Place place = placeRepo.findById(req.placeId())
                .orElseThrow(() -> new IllegalArgumentException("장소 없음"));

        DateCoursePlace entry = DateCoursePlace.builder()
                .dateCourse(course)
                .place(place)
                .orderIndex(req.orderIndex())
                .duration(req.duration())
                .memo(req.memo())
                .build();
        course.getPlaces().add(entry);
        return toPlaceDto(entry);
    }

    /**UPDATE orderIndex **/
    public void updateOrderIndexes(DateCourse course, List<OrderIndexDto> orderList) {
        List<DateCoursePlace> places = course.getPlaces();
        for (int i = 0; i < places.size(); i++) {
            DateCoursePlace entry = places.get(i);
            entry.updateOrder(orderList.get(i).orderIndex());
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

