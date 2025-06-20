package capdb.design;

import capdb.design.domain.*;
import capdb.design.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DevDataRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final DateCourseRepository dateCourseRepository;
    private final DateCoursePlaceRepository dateCoursePlaceRepository;
    private final DateCourseLegRepository dateCourseLegRepository;

    @Override
    public void run(String... args) {
        System.out.println("==== [DevDataRunner: H2 데이터 테스트 시작] ====");

        // 1. User 생성/저장
        User user = User.builder()
                .userName("테스트유저")
                .email("test@example.com")
                .provider("google")
                .password("pw1234")
                .build();
        userRepository.save(user);

        // 2. Place 3개 생성/저장
        Place a = Place.builder().placeName("서울역").address("서울 중구").latitude(37.555).longitude(126.970).build();
        Place b = Place.builder().placeName("명동성당").address("서울 중구").latitude(37.563).longitude(126.987).build();
        Place c = Place.builder().placeName("청계천").address("서울 종로구").latitude(37.570).longitude(126.977).build();
        placeRepository.saveAll(List.of(a, b, c));

        // 3. DateCourse 생성 (연관관계 설정)
        DateCourse course = DateCourse.builder()
                .title("서울 한바퀴 코스")
                .dateRegion("서울")
                .date(LocalDate.of(2025, 7, 1))
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        // 4. DateCoursePlace 생성 및 course에 추가
        DateCoursePlace cp1 = DateCoursePlace.builder()
                .orderIndex(1).durationMinutes(40).place(a).dateCourse(course).build();
        DateCoursePlace cp2 = DateCoursePlace.builder()
                .orderIndex(2).durationMinutes(30).place(b).dateCourse(course).build();
        DateCoursePlace cp3 = DateCoursePlace.builder()
                .orderIndex(3).durationMinutes(50).place(c).dateCourse(course).build();
        course.getPlaces().addAll(List.of(cp1, cp2, cp3));

        // 5. 이동구간 생성 및 course에 추가
        DateCourseLeg leg1 = DateCourseLeg.builder()
                .dateCourse(course)
                .fromPlace(a).toPlace(b)
                .transportType("지하철").distanceM(1200).durationMinutes(8)
                .build();
        DateCourseLeg leg2 = DateCourseLeg.builder()
                .dateCourse(course)
                .fromPlace(b).toPlace(c)
                .transportType("도보").distanceM(700).durationMinutes(12)
                .build();
        course.getLegs().addAll(List.of(leg1, leg2));

        // 6. 전체 코스 저장 (cascade=ALL, places/legs 자동 저장)
        dateCourseRepository.save(course);

        // 7. 검증 - 데이터 정상 저장/조회 여부
        DateCourse loaded = dateCourseRepository.findById(course.getId()).orElseThrow();

        // 엔티티 값 콘솔로 출력
        System.out.println("코스 제목: " + loaded.getTitle());
        System.out.println("유저명: " + loaded.getUser().getUserName());
        System.out.println("== 장소 목록 ==");
        loaded.getPlaces().forEach(p ->
                System.out.println("장소: " + p.getPlace().getPlaceName() + " (" + p.getDurationMinutes() + "분)")
        );
        System.out.println("== 이동 경로 ==");
        loaded.getLegs().forEach(l ->
                System.out.println("이동: " + l.getFromPlace().getPlaceName() + " → " + l.getToPlace().getPlaceName() +
                        " [" + l.getTransportType() + ", " + l.getDistanceM() + "m, " + l.getDurationMinutes() + "분]")
        );

        // 커스텀 리포지토리 메서드로 조회도 확인
        List<DateCoursePlace> loadedPlaces = dateCoursePlaceRepository.findByDateCourseIdOrderByOrderIndex(course.getId());
        List<DateCourseLeg> loadedLegs = dateCourseLegRepository.findByDateCourseId(course.getId());

        System.out.println("== 커스텀 리포지토리로 조회한 장소 ==");
        loadedPlaces.forEach(p ->
                System.out.println("장소: " + p.getPlace().getPlaceName() + " (" + p.getDurationMinutes() + "분)")
        );
        System.out.println("== 커스텀 리포지토리로 조회한 이동 경로 ==");
        loadedLegs.forEach(l ->
                System.out.println("이동: " + l.getFromPlace().getPlaceName() + " → " + l.getToPlace().getPlaceName() +
                        " [" + l.getTransportType() + ", " + l.getDistanceM() + "m]")
        );

        System.out.println("==== [DevDataRunner: 테스트 종료] ====");
    }
}
