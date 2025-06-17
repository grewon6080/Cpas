package capdb.design.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "datecourse_places")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateCoursePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int orderIndex;         // 장소 순서
    private int durationMinutes;    // 체류 시간(분 단위)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datecourse_id")
    private DateCourse dateCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;
}
