package mock_tests;

import mk.ukim.finki.tech_prototype.Model.Enumeration.Role;
import mk.ukim.finki.tech_prototype.Model.Exception.InvalidArgumentsException;
import mk.ukim.finki.tech_prototype.Model.Location;
import mk.ukim.finki.tech_prototype.Model.Review;
import mk.ukim.finki.tech_prototype.Model.User;
import mk.ukim.finki.tech_prototype.Repository.LocationRepository;
import mk.ukim.finki.tech_prototype.Repository.ReviewRepository;
import mk.ukim.finki.tech_prototype.Repository.UserRepository;
import mk.ukim.finki.tech_prototype.Service.Impl.ReviewServiceImp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)

public class ReviewServiceTests {
    @InjectMocks
    private ReviewServiceImp reviewServiceImp;
    @Mock
    private  ReviewRepository reviewRepository;
    @Mock
    private  LocationRepository locationRepository;
    @Mock
    private  UserRepository userRepository;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        User expectedUser = new User("bojan", "Bojan", "Trpeski", "Test1234!", Role.ROLE_USER);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
    }
    @After
    public void tearDown() {
        reset(reviewRepository, locationRepository, userRepository);
    }

    @Test
    public void postLocationNotFound() {
        String text = "Great Place!";
        String username = "johnDoe";
        Long locationId = 1L;
        int grade = 5;

        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        assertThrows(InvalidArgumentsException.class, () -> {
            reviewServiceImp.post(text, username, locationId, grade);
        });

        verify(userRepository, times(0)).findByUsername(username);
        verify(reviewRepository, times(0)).save(any());
    }
    @Test
    public void postUserNotFound() {
        String text = "Great Place!";
        String username = "johnDoe";
        Long locationId = 1L;
        int grade = 5;

        Location mockLocation = new Location();

        when(locationRepository.findById(locationId)).thenReturn(Optional.of(mockLocation));
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(InvalidArgumentsException.class, () -> {
            reviewServiceImp.post(text, username, locationId, grade);
        });

        verify(reviewRepository, times(0)).save(any());
    }
    @Test
    public void editReviewUserNotFound() {
        Long reviewId = 1L;
        String newText = "New Text";
        String username = "user";
        Long locationId = 2L;
        int grade = 5;

        Review mockReview = new Review(newText, new User(), new Location(), grade);
        mockReview.setReviewId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(mockReview));
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(InvalidArgumentsException.class, () -> {
            reviewServiceImp.edit(reviewId, newText, username, locationId, grade);
        });


    }
    @Test
    public void editReviewReviewNotFound() {
        Long reviewId = 1L;
        String newText = "New Text";
        String username = "user";
        Long locationId = 2L;
        int grade = 5;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(InvalidArgumentsException.class, () -> {
            reviewServiceImp.edit(reviewId, newText, username, locationId, grade);
        });
        verify(userRepository, times(0)).findByUsername(username);

    }
    @Test
    public void editReviewLocationNotFound() {
        Long reviewId = 1L;
        String newText = "New Text";
        String username = "user";
        Long locationId = 2L;
        int grade = 5;

        Review mockReview = new Review(newText, new User(), new Location(), grade);
        mockReview.setReviewId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(mockReview));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        assertThrows(InvalidArgumentsException.class, () -> {
            reviewServiceImp.edit(reviewId, newText, username, locationId, grade);
        });
    }



}
