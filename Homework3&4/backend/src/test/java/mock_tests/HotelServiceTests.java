package mock_tests;

import mk.ukim.finki.tech_prototype.Model.Exception.InvalidArgumentsException;
import mk.ukim.finki.tech_prototype.Model.Hotel;
import mk.ukim.finki.tech_prototype.Repository.HotelRepository;
import mk.ukim.finki.tech_prototype.Service.Impl.HotelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class HotelServiceTests {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPost() {
        Double x = 1.0;
        Double y = 1.0;
        String name = "Test Hotel";

        when(hotelRepository.save(any())).thenReturn(new Hotel());

        hotelService.post(x, y, name, "city", "street", "1", "website.com", "12345", 5);

        verify(hotelRepository).save(any());
    }

    @Test
    public void testFindAll() {
        when(hotelRepository.findAll()).thenReturn(Collections.singletonList(new Hotel()));

        hotelService.findAll();

        verify(hotelRepository).findAll();
    }


    @Test
    public void testEditNotFound() {
        Long id = 1L;
        when(hotelRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvalidArgumentsException.class, () -> {
            hotelService.edit(id, 1.0, 1.0, "name", "city", "street", "1", "website.com", "12345", 5, "desc", "path/to/image");
        });


        verify(hotelRepository).findById(id);
        verify(hotelRepository, times(0)).save(any());
    }

    @Test
    public void testFindByName() {
        String name = "Test Hotel";

        when(hotelRepository.findHotelByNameIgnoreCase(name)).thenReturn(Optional.of(new Hotel()));

        hotelService.findByName(name);

        verify(hotelRepository).findHotelByNameIgnoreCase(name);
    }

    @Test
    public void testFindByCity() {
        String city = "Test City";

        when(hotelRepository.findHotelsByCityIgnoreCase(city)).thenReturn(Collections.singletonList(new Hotel()));

        hotelService.findByCity(city);

        verify(hotelRepository).findHotelsByCityIgnoreCase(city);
    }
    @Test
    public void testFindByStars() {
        int stars = 5;
        List<Hotel> expectedHotels = Arrays.asList(new Hotel(), new Hotel());
        when(hotelRepository.findHotelsByStars(stars)).thenReturn(expectedHotels);
        hotelService.findByStars(stars);

        verify(hotelRepository).findHotelsByStars(stars);
    }
    @Test
    public void testFindByStarsNoResults() {
        int stars = 3;
        when(hotelRepository.findHotelsByStars(stars)).thenReturn(Collections.emptyList());

       hotelService.findByStars(stars);

        verify(hotelRepository).findHotelsByStars(stars);
    }

}
