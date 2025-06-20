package capdb.design.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "datecourses")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateCourse {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String dateRegion;

    private LocalDate date;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "dateCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DateCoursePlace> places = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "dateCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DateCourseLeg> legs = new ArrayList<>();

    public void update(String title, String dateRegion, LocalDate date) {
        this.title = title;
        this.dateRegion = dateRegion;
        this.date = date;
    }

}
