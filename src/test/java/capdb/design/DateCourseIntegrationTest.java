package capdb.design;

import capdb.design.domain.*;
import capdb.design.repository.DateCourseLegRepository;
import capdb.design.repository.DateCoursePlaceRepository;
import capdb.design.repository.DateCourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DateCourseIntegrationTest {

    @Autowired
    DateCourseRepository dateCourseRepository;
    @Autowired
    DateCoursePlaceRepository dateCoursePlaceRepository;
    @Autowired
    DateCourseLegRepository dateCourseLegRepository;
    @Autowired User userRepository;
    @Autowired Place placeRepository;

    @Test
    void crudTest() {
        // 1. 사용자, 장소 데이터 생성 및 저장
        User user = User.builder().username("홍길동").build();
  //      userRepository.save(user);

        Place placeA = Place.builder().placeName("서울역").build();
        Place placeB = Place.builder().placeName("시청역").build();
        Place placeC = Place.builder().placeName("명동역").build();
    //    placeRepository.saveAll(List.of(placeA, placeB, placeC));

        // 2. 코스 생성
        DateCourse course = DateCourse.builder()
                .title("서울 데이트코스")
                .dateRegion("서울특별시")
                .date(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        // 3. 장소(코스에 속한 장소) 추가
        DateCoursePlace cp1 = DateCoursePlace.builder()
                .orderIndex(1).durationMinutes(30).place(placeA).dateCourse(course).build();
        DateCoursePlace cp2 = DateCoursePlace.builder()
                .orderIndex(2).durationMinutes(40).place(placeB).dateCourse(course).build();
        DateCoursePlace cp3 = DateCoursePlace.builder()
                .orderIndex(3).durationMinutes(60).place(placeC).dateCourse(course).build();

        course.getPlaces().addAll(List.of(cp1, cp2, cp3));

        // 4. 이동경로(legs) 추가
        DateCourseLeg leg1 = DateCourseLeg.builder()
                .dateCourse(course)
                .fromPlace(placeA).toPlace(placeB)
                .transportType("지하철").distanceM(1500).durationMinutes(5)
                .build();
        DateCourseLeg leg2 = DateCourseLeg.builder()
                .dateCourse(course)
                .fromPlace(placeB).toPlace(placeC)
                .transportType("도보").distanceM(800).durationMinutes(10)
                .build();

        course.getLegs().addAll(List.of(leg1, leg2));

        // 5. 코스 저장 (cascade=ALL 덕분에 places/legs도 같이 저장됨)
        dateCourseRepository.save(course);

        // 6. 검증 - 코스, 장소, legs 정상 저장 및 조회
        DateCourse loaded = dateCourseRepository.findById(course.getId()).orElseThrow();

        // 엔티티 컬렉션 직접 접근 (N+1 발생 가능, 테스트에선 OK)
        assertThat(loaded.getPlaces()).hasSize(3);
        assertThat(loaded.getLegs()).hasSize(2);
        assertThat(loaded.getPlaces().get(0).getPlace().getPlaceName()).isEqualTo("서울역");
        assertThat(loaded.getLegs().get(0).getTransportType()).isEqualTo("지하철");

        // 커스텀 리포지토리 메서드로도 조회
        List<DateCoursePlace> places = dateCoursePlaceRepository.findByDateCourseIdOrderByOrderIndex(course.getId());
        List<DateCourseLeg> legs = dateCourseLegRepository.findByDateCourseId(course.getId());

        assertThat(places).hasSize(3);
        assertThat(legs).hasSize(2);

        System.out.println("코스 제목: " + loaded.getTitle());
        System.out.println("장소 1: " + places.get(0).getPlace().getPlaceName() + " (체류 " + places.get(0).getDurationMinutes() + "분)");
        System.out.println("첫 이동수단: " + legs.get(0).getTransportType() + " (거리 " + legs.get(0).getDistanceM() + "m)");
    }
}

