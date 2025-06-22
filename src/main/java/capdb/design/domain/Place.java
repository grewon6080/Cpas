package capdb.design.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="places")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Place {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  placeName;
    private String  address;
    private Integer regionCode;

    private String  placeId;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    private String categoryGroupCode;
    private String placePhoneNumb;
    private String placeUrl;

}

