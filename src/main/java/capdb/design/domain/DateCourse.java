package capdb.design.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "date_course")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //Long 고려

    // 코스 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;
}
