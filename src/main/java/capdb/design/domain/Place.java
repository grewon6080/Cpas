package capdb.design.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "place")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Long 고려

    @Column
    private String region;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String address;
}
