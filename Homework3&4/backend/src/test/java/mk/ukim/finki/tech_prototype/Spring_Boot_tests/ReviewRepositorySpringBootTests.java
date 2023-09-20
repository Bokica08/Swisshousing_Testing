package mk.ukim.finki.tech_prototype.Spring_Boot_tests;
import mk.ukim.finki.tech_prototype.Model.Enumeration.Role;
import mk.ukim.finki.tech_prototype.Model.Review;
import mk.ukim.finki.tech_prototype.Model.Location;
import mk.ukim.finki.tech_prototype.Model.User;
import mk.ukim.finki.tech_prototype.Repository.LocationRepository;
import mk.ukim.finki.tech_prototype.Repository.ReviewRepository;
import mk.ukim.finki.tech_prototype.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@DataJpaTest
public class ReviewRepositorySpringBootTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    private Review testReview;
    private User testUser;
    private Location testLocation;

    @BeforeEach
    public void setUp() {
        testUser = new User("testUser", "Test", "User", "password123", Role.ROLE_USER);
        testUser = userRepository.save(testUser);

        testLocation = new Location(1.0, 1.0, "Test Location", "Test City", "Test Street", "1A");
        testLocation = locationRepository.save(testLocation);

        testReview = new Review("test",testUser,testLocation,3);
        testReview = reviewRepository.save(testReview);
    }

    @Test
    public void findReviewsByLocation() {
        List<Review> reviews = reviewRepository.findReviewsByLocation_LocationId(testLocation.getLocationId());
        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(testReview.getReviewId(), reviews.get(0).getReviewId());
    }

    @Test
    public void FindAllByReviewer() {
        List<Review> reviews = reviewRepository.findAllByReviewer(testUser);
        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(testReview.getReviewId(), reviews.get(0).getReviewId());
    }
}
