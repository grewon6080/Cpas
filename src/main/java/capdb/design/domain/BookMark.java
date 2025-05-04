package capdb.design.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookmark")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //Long 고려

    //북마크 저장자
    @ManyToOne(fetch = FetchType.LAZY) //성능 최적화 기법
    @JoinColumn(name = "user_id", nullable = false) //외래키
    private Users user;

    //저장된 장소
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false) //외래키
    private Place place;
}
