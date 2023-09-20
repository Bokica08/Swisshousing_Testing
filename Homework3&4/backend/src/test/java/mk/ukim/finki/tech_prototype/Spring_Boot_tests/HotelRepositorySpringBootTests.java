package mk.ukim.finki.tech_prototype.Spring_Boot_tests;
import mk.ukim.finki.tech_prototype.Model.Hotel;
import mk.ukim.finki.tech_prototype.Repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@DataJpaTest
public class HotelRepositorySpringBootTests {

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel testHotel;

    @BeforeEach
    public void setUp() {
        testHotel = new Hotel(1.0, 1.0, "Test Hotel", "Test City", "Test Street", "1A", "http://test.com", "0123456789", 5);
        hotelRepository.save(testHotel);
    }

    @Test
    public void findHotelsByNameContainingIgnoreCase() {
        List<Hotel> result = hotelRepository.findHotelsByNameContainingIgnoreCase("test");
        assertTrue(result.size() > 0);
        assertTrue(result.stream().anyMatch(hotel -> "Test Hotel".equalsIgnoreCase(hotel.getName())));
    }

    @Test
    public void findHotelByNameIgnoreCase() {
        Optional<Hotel> result = hotelRepository.findHotelByNameIgnoreCase("test hotel");
        assertTrue(result.isPresent());
        assertEquals("Test Hotel", result.get().getName());
    }

    @Test
    public void findHotelsByCityIgnoreCase() {
        List<Hotel> result = hotelRepository.findHotelsByCityIgnoreCase("test city");
        assertTrue(result.size() > 0);
        assertTrue(result.stream().anyMatch(hotel -> "Test City".equalsIgnoreCase(hotel.getCity())));
    }

    @Test
    public void findHotelsByStars_shouldReturnHotels() {
        List<Hotel> result = hotelRepository.findHotelsByStars(5);
        assertTrue(result.size() > 0);
        assertTrue(result.stream().anyMatch(hotel -> hotel.getStars() == 5));
    }
}
