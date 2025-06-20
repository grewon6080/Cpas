package capdb.design.domain;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "datecourse_legs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateCourseLeg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transportType;   // 이동 수단(예: 도보, 버스)
    private int distanceM;          // 거리(미터)
    private int durationMinutes;    // 소요 시간(분 단위)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datecourse_id")
    private DateCourse dateCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_place_id")
    private Place fromPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_place_id")
    private Place toPlace;

    public void update(String transportType, int distanceM, int durationMinutes) {
        this.transportType = transportType;
        this.distanceM = distanceM;
        this.durationMinutes = durationMinutes;
    }
}
