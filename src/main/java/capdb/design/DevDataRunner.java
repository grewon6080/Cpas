package capdb.design;

import capdb.design.domain.*;
import capdb.design.dto.*;
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
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DevDataRunner implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PlaceRepository placeRepo;
    private final DateCourseRepository courseRepo;
    private final DateCourseService courseService;

    @Override
    @Transactional
    public void run(String... args) {

        log.info("==== DevDataRunner 시작 ====");

        /* 1. User, Place 더미 데이터 */
        User user = userRepo.save(
                User.builder().userName("테스터").email("tester@demo.com")
                        .age(30).gender("M").provider("local").password("pw").build());

        Place p1 = placeRepo.save(
                Place.builder().placeName("서울역").address("서울 중구").regionCode(1)
                        .placeId("AA1").latitude(new BigDecimal("37.5547"))
                        .longitude(new BigDecimal("126.9706")).build());
        Place p2 = placeRepo.save(
                Place.builder().placeName("시청역").address("서울 중구").regionCode(1)
                        .placeId("AA2").latitude(new BigDecimal("37.5651"))
                        .longitude(new BigDecimal("126.9753")).build());
        Place p3 = placeRepo.save(
                Place.builder().placeName("남산타워").address("서울 중구").regionCode(1)
                        .placeId("AA3").latitude(new BigDecimal("37.5512"))
                        .longitude(new BigDecimal("126.9882")).build());

        /* 2. 코스 생성 */
        Long courseId = courseService.createDateCourse(
                user.getId(),
                new DateCourseRequest("봄나들이", "서울", LocalDate.of(2026, 3, 20)));
        log.info("코스 생성 ID={}", courseId);

        /* 3. 장소 3개 추가 */
        DateCoursePlaceResponse a = courseService.addPlace(courseId,
                new DateCoursePlaceRequest(p1.getId(), 1, LocalTime.of(1, 0), "기차역 구경"));
        DateCoursePlaceResponse b = courseService.addPlace(courseId,
                new DateCoursePlaceRequest(p2.getId(), 2, LocalTime.of(2, 0), "광장 산책"));
        DateCoursePlaceResponse c = courseService.addPlace(courseId,
                new DateCoursePlaceRequest(p3.getId(), 3, LocalTime.of(1, 30), "전망대 방문"));
        log.info("장소 추가 결과: {}, {}, {}", a, b, c);

        /* 4. 코스 목록 조회 */
        log.info("목록 조회: {}", courseService.readListByUser(user.getId()));

        /* 5. 코스 기본 정보 수정 */
        courseService.updateDateCourse(courseId,
                new DateCourseRequest("봄 서울투어", "서울특별시", LocalDate.of(2026, 4, 1)));
        log.info("수정 후 코스: {}", courseService.readListByUser(user.getId()));

        /* 6. orderIndex 재배치 (시청역을 1번, 서울역을 2번, 남산타워 3번) */
        DateCourse course = courseRepo.findById(courseId).orElseThrow();
        List<OrderIndexDto> newOrders = Arrays.asList(
                new OrderIndexDto(3),
                new OrderIndexDto(2),
                new OrderIndexDto(1)
        );
        log.info("순서 변경 후 Places: {}", course.getPlaces().get(0).getOrderIndex());

        courseService.updateOrderIndexes(course, newOrders);
        log.info("순서 변경 후 Places: {}", course.getPlaces().get(0).getOrderIndex());

        /* 7. 코스 삭제 */
        courseService.delete(courseId);
        log.info("삭제 후 코스 목록: {}", courseService.readListByUser(user.getId()));

        log.info("==== DevDataRunner 종료 ====");
    }
}
