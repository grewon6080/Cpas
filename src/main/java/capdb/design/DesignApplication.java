package capdb.design;

import capdb.design.domain.DateCourse;
import capdb.design.domain.DateCoursePlace;
import capdb.design.domain.Place;
import capdb.design.domain.Users;
import capdb.design.repository.DateCoursePlaceRepository;
import capdb.design.repository.DateCourseRepository;
import capdb.design.repository.PlaceRepository;
import capdb.design.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class DesignApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesignApplication.class, args);
	}

	@Bean
	public CommandLineRunner sampleData(
			UsersRepository usersRepository,
			PlaceRepository placeRepository,
			DateCourseRepository dateCourseRepository,
			DateCoursePlaceRepository dateCoursePlaceRepository
	) {
		return args -> {
			// 사용자
			Users user = usersRepository.save(Users.builder()
					.username("friend")
					.provider("kakao")
					.profileImg("friend.png")
					.build());

			// 장소 2개
			Place p1 = placeRepository.save(Place.builder()
					.region("서울 강남구")
					.placeName("코엑스")
					.address("서울 강남구 삼성로 513")
					.build());

			Place p2 = placeRepository.save(Place.builder()
					.region("서울 용산구")
					.placeName("용산 아이파크몰")
					.address("서울 용산구 한강대로23길 55")
					.build());

			// 데이트 코스
			DateCourse course = dateCourseRepository.save(DateCourse.builder()
					.user(user)
					.title("서울 핵심 데이트 코스")
					.date(LocalDate.now())
					.build());

			// 코스에 장소 추가
			dateCoursePlaceRepository.saveAll(List.of(
					DateCoursePlace.builder()
							.dateCourse(course)
							.place(p1)
							.placeOrder(1)
							.build(),
					DateCoursePlace.builder()
							.dateCourse(course)
							.place(p2)
							.placeOrder(2)
							.build()
			));
		};
	}

}
