package mk.ukim.finki.LocationsService.Service;

import mk.ukim.finki.LocationsService.Model.DTO.HotelDTO;
import mk.ukim.finki.LocationsService.Model.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelService {
    Optional<Hotel> post(Double x, Double y, String name, String city, String street, String houseNumber, String website, String phoneNumber, int stars);
    Optional<Hotel> post(HotelDTO hotelDTO);
    List<Hotel> findAll();
    Optional<Hotel> findById(Long id);
    Optional<Hotel> edit(Long id, Double x, Double y, String name, String city, String street, String houseNumber, String website, String phoneNumber, int stars, String description, String imagePath);
    Optional<Hotel> edit(Long id, HotelDTO hotelDTO);
    Optional<Hotel> findByName(String name);
    List<Hotel> findAllContainingName(String name);
    List<Hotel> findByCity(String city);
    List<Hotel> findByStars(int stars);
}