package capdb.design.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "date_course_place")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateCoursePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 코스에 포함된 장소인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_course_id", nullable = false)
    private DateCourse dateCourse;

    //저장된 장소
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "place_order")
    private Integer placeOrder;
}
