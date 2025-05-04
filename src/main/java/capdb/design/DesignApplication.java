package capdb.design;

import capdb.design.domain.*;
import capdb.design.repository.*;
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
	public CommandLineRunner initDatabase(
			UsersRepository usersRepository,
			PlaceRepository placeRepository,
			DateCourseRepository dateCourseRepository,
			DateCoursePlaceRepository dateCoursePlaceRepository,
			BookMarkRepository bookMarkRepository,
			ReviewRepository reviewRepository,
			ReviewPlaceRepository reviewPlaceRepository
	) {
		return args -> {
			// 초기화 (삭제 순서: 자식 → 부모)
			reviewPlaceRepository.deleteAll();
			reviewRepository.deleteAll();
			bookMarkRepository.deleteAll();
			dateCoursePlaceRepository.deleteAll();
			dateCourseRepository.deleteAll();
			placeRepository.deleteAll();
			usersRepository.deleteAll();

			//  사용자
			Users user = usersRepository.save(Users.builder()
					.username("friend")
					.provider("kakao")
					.profileImg("friend.png")
					.build());

			//  장소 2개
			Place p1 = placeRepository.save(Place.builder()
					.region("서울 강남구")
					.placeName("코엑스")
					.address("서울 강남구 삼성로 513")
					.build());

			Place p2 = placeRepository.save(Place.builder()
					.region("서울 용산구")
					.placeName("아이파크몰")
					.address("서울 용산구 한강대로 23길")
					.build());

			//  데이트 코스
			DateCourse course = dateCourseRepository.save(DateCourse.builder()
					.user(user)
					.title("서울 데이트 코스")
					.date(LocalDate.now())
					.build());

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

			//  BookMark (user가 p1을 북마크)
			bookMarkRepository.save(BookMark.builder()
					.user(user)
					.place(p1)
					.build());

			//  Review
			Review review = reviewRepository.save(Review.builder()
					.user(user)
					.title("핵심 서울 데이트 후기")
					.date(LocalDate.now())
					.content("코엑스는 쇼핑도 좋고, 아이파크몰은 야경 최고!")
					.cost("2만원")
					.likes("10")
					.imgUrl("date.png")
					.build());

			//  ReviewPlace (Review와 Place 연결)
			reviewPlaceRepository.saveAll(List.of(
					ReviewPlace.builder()
							.review(review)
							.place(p1)
							.placeOrder(1)
							.comment("코엑스는 넓고 쾌적해요")
							.build(),
					ReviewPlace.builder()
							.review(review)
							.place(p2)
							.placeOrder(2)
							.comment("아이파크몰은 식당가가 훌륭해요")
							.build()
			));
		};
	}


}
