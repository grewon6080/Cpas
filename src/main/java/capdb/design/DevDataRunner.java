package capdb.design;

import capdb.design.domain.*;
import capdb.design.dto.course.*;
import capdb.design.dto.courseplace.*;
import capdb.design.repository.*;
import capdb.design.service.DateCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DevDataRunner implements CommandLineRunner {

    private final UserRepository       userRepo;
    private final PlaceRepository      placeRepo;
    private final DateCourseService    courseService;

    @Override
    @Transactional
    public void run(String... args) {

        log.info("====== DevDataRunner (orderIndex 테스트) START ======");

        /* 1. User & Place 더미 -------------------------------------- */
        User user = userRepo.save(
                User.builder().userName("Runner").email("runner@demo.com")
                        .age(28).gender("F").provider("local").password("pw").build());

        Place p1 = savePlace("서울역",   "P1", 37.5547, 126.9706);
        Place p2 = savePlace("시청역",   "P2", 37.5651, 126.9753);
        Place p3 = savePlace("남산타워", "P3", 37.5512, 126.9882);

        /* 2. 코스 생성 ---------------------------------------------- */
        Long courseId = courseService.createDateCourse(
                user.getId(),
                new DateCourseRequest("초기 코스", "서울", LocalDate.of(2026, 3, 10)));
        log.info("CREATE  courseId={}", courseId);

        /* 3. 장소 3개(1,2,3) 등록 ----------------------------------- */
        courseService.updateDateCourse(courseId,
                new DateCourseUpdateRequest(
                        "초기 코스", "서울",
                        LocalDate.of(2026, 3, 10),
                        List.of(
                                new CoursePlaceUpdateDto(p1.getId(), 1, LocalTime.of(1, 0), "A"),
                                new CoursePlaceUpdateDto(p2.getId(), 2, LocalTime.of(1, 0), "B"),
                                new CoursePlaceUpdateDto(p3.getId(), 3, LocalTime.of(1, 0), "C")
                        )));

        log.info("INSERT  places → {}",
                courseService.readCourseWithPlaces(user.getId(), courseId).places());

        /* 4. 순서 재정렬 : 남산타워 1 → 서울역 2 → 시청역 3 ----------- */
        courseService.updateDateCourse(courseId,
                new DateCourseUpdateRequest(
                        "순서변경 코스", "서울특별시",
                        LocalDate.of(2026, 4, 1),
                        List.of(
                                new CoursePlaceUpdateDto(p3.getId(), 1, LocalTime.of(1, 0), "C"),
                                new CoursePlaceUpdateDto(p1.getId(), 2, LocalTime.of(1, 0), "A"),
                                new CoursePlaceUpdateDto(p2.getId(), 3, LocalTime.of(1, 0), "B")
                        )));

        log.info("REORDER places → {}",
                courseService.readCourseWithPlaces(user.getId(), courseId).places());

        log.info("====== DevDataRunner END ======");
    }

    /* util: Place 저장 */
    private Place savePlace(String name, String pid, double lat, double lon) {
        return placeRepo.save(
                Place.builder()
                        .placeName(name).address("서울")
                        .regionCode(1).placeId(pid)
                        .latitude(new BigDecimal(lat))
                        .longitude(new BigDecimal(lon))
                        .build());
    }
}
