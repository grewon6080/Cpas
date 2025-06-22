package capdb.design.domain;

import jakarta.persistence.*;
import lombok.*;

import java.awt.image.ImagingOpException;
import java.time.LocalTime;

@Entity
@Table(name = "datecourse_places")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateCoursePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datecourse_id")
    private DateCourse dateCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    private LocalTime duration;   //체류 시간
    private Integer   orderIndex; //장소 순서
    private String    memo;



    public void updateDuration(LocalTime duration){
        this.duration = duration;
    }

    public void updateOrder(Integer orderIndex){
        this.orderIndex = orderIndex;
    }

    public void updateMemo(String memo){
        this.memo = memo;
    }

}
