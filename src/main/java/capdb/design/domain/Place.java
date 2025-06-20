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
    private Long id;

    private String placeName;

    private String address;

    private String placeId; // 외부 API(카카오/네이버 등) 연동용 고유값

    private Double latitude;

    private Double longitude;

    private String regionCode;

    public void update(String placeName, String address, String placeId, Double latitude, Double longitude, String regionCode) {
        this.placeName = placeName;
        this.address = address;
        this.placeId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.regionCode = regionCode;
    }
}
