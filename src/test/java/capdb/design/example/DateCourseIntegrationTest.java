package capdb.design.example;


import capdb.design.domain.DateCourse;
import capdb.design.domain.DateCoursePlace;
import capdb.design.domain.Place;
import capdb.design.domain.Users;

import capdb.design.repository.DateCoursePlaceRepository;
import capdb.design.repository.DateCourseRepository;
import capdb.design.repository.PlaceRepository;
import capdb.design.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DateCourseIntegrationTest {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private DateCourseRepository dateCourseRepository;

    @Autowired
    private DateCoursePlaceRepository dateCoursePlaceRepository;

    @BeforeEach
    void clearDatabase() {
        dateCoursePlaceRepository.deleteAll();
        dateCourseRepository.deleteAll();
        placeRepository.deleteAll();
        usersRepository.deleteAll();
    }


    @Test
    public void connectTest() {
        // 🟡 given: 테스트용 유저, 장소, 코스 생성
        Users user = usersRepository.save(Users.builder()
                .username("jangmon")
                .provider("kakao")
                .profileImg("jang.jpg")
                .build());

        Place place1 = placeRepository.save(Place.builder()
                .region("서울 종로구")
                .placeName("경복궁")
                .address("서울 종로구 사직로 161")
                .build());

        Place place2 = placeRepository.save(Place.builder()
                .region("서울 마포구")
                .placeName("홍대 거리")
                .address("서울 마포구 양화로")
                .build());

        DateCourse dateCourse = dateCourseRepository.save(DateCourse.builder()
                .user(user)
                .title("서울 데이트 코스")
                .date(LocalDate.now())
                .build());

        DateCoursePlace cp1 = dateCoursePlaceRepository.save(DateCoursePlace.builder()
                .dateCourse(dateCourse)
                .place(place1)
                .placeOrder(1)
                .build());

        DateCoursePlace cp2 = dateCoursePlaceRepository.save(DateCoursePlace.builder()
                .dateCourse(dateCourse)
                .place(place2)
                .placeOrder(2)
                .build());

        // 🔵 when: 저장된 데이터 조회
        List<DateCoursePlace> savedPlaces = dateCoursePlaceRepository.findAll();

        // 🔴 then: 검증
        assertThat(savedPlaces).hasSize(2);
        assertThat(savedPlaces.get(0).getDateCourse().getTitle()).isEqualTo("서울 데이트 코스");
        assertThat(savedPlaces.get(1).getPlace().getPlaceName()).isEqualTo("홍대 거리");
        assertThat(dateCourse.getUser().getUsername()).isEqualTo("jangmon");
    }
}