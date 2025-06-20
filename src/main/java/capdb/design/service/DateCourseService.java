package capdb.design.service;

import capdb.design.domain.DateCourse;
import capdb.design.domain.User;
import capdb.design.dto.DateCourseRequest;
import capdb.design.dto.DateCourseResponse;
import capdb.design.repository.DateCourseRepository;
import capdb.design.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DateCourseService {

    private final DateCourseRepository dateCourseRepo;
    private final UserRepository userRepo;

    // 생성
    public Long createCourse(Long userId, DateCourseRequest req) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        DateCourse course = DateCourse.builder()
                .title(req.title())
                .dateRegion(req.dateRegion())
                .date(req.date())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        dateCourseRepo.save(course);
        return course.getId();
    }

    // 사용자별 코스 목록 조회
    @Transactional(readOnly = true)
    public List<DateCourseResponse> getCourses(Long userId) {
        List<DateCourse> list = dateCourseRepo.findByUserId(userId);
        List<DateCourseResponse> result = new ArrayList<>();
        for (DateCourse c : list) {
            result.add(new DateCourseResponse(
                    c.getId(), c.getTitle(), c.getDateRegion(), c.getDate(), c.getCreatedAt()
            ));
        }
        return result;
    }

    // 수정 (엔티티 update 메서드 사용)
    public void updateCourse(Long courseId, DateCourseRequest req) {
        DateCourse c = dateCourseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스 없음"));
        c.update(req.title(), req.dateRegion(), req.date());
    }

    // 삭제
    public void deleteCourse(Long courseId) {
        dateCourseRepo.deleteById(courseId);
    }
}
