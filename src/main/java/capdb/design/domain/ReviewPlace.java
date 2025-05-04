package capdb.design.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_place")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //Long 고려

    // 리뷰 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    // 장소 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "place_order")
    private Integer placeOrder;

    @Column
    private String comment;
}
