package mk.ukim.finki.MainService.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampSiteDTO {
    private Double x;
    private Double y;
    private String name;
    private String city;
    private String street;
    private String houseNumber;
    private String description;
    private String imagePath;
    private String website;
    private String phoneNumber;
}
