package sum25.se.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer airportId;
    @Column(columnDefinition = "NVARCHAR(100)")
    private String airportName;
    private String code;
    @Column(columnDefinition = "NVARCHAR(100)")
    private String location;
    @OneToMany(mappedBy = "airport")
    private List<Plane> planes;

}
