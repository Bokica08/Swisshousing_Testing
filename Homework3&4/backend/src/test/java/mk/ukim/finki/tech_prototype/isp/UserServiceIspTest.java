package mk.ukim.finki.tech_prototype.isp;

import mk.ukim.finki.tech_prototype.Model.DTO.UserDTO;
import mk.ukim.finki.tech_prototype.Model.Enumeration.Role;
import mk.ukim.finki.tech_prototype.Model.Exception.InvalidArgumentsException;
import mk.ukim.finki.tech_prototype.Model.Exception.LocationNotFoundException;
import mk.ukim.finki.tech_prototype.Model.Exception.PasswordsDoNotMatchException;
import mk.ukim.finki.tech_prototype.Model.Exception.UsernameAlreadyExistsException;
import mk.ukim.finki.tech_prototype.Model.Location;
import mk.ukim.finki.tech_prototype.Model.User;
import mk.ukim.finki.tech_prototype.Repository.LocationRepository;
import mk.ukim.finki.tech_prototype.Repository.UserRepository;
import mk.ukim.finki.tech_prototype.Service.Impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceIspTest {
    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private UserRepository userRepository;

    private User registerUser;
    private User admin;
    private User pendingAdmin;
    private User userFavourites;
    private User userVisited;
    private Location location = new Location(1.0, 1.0, "Location", "Skopje", "Ulica", "12a");

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        location.setLocationId(1L);
        registerUser = new User("dare431", "Darko", "Sasanski", passwordEncoder.encode("Password@123"), Role.ROLE_USER);
        admin = new User("admin", "Admin", "Admin", passwordEncoder.encode("Admin123!"), Role.ROLE_ADMIN);
        admin.getFavourites().add(location);
        admin.getVisited().add(location);
        when(userRepository.save(admin)).thenReturn(admin);
        pendingAdmin = new User("pendingAdmin", "Pending", "Admin", passwordEncoder.encode("Admin123!"), Role.ROLE_PENDING_ADMIN);
        userFavourites = new User("andrejT", "Andrej", "Todorovski", passwordEncoder.encode("Password@123"), Role.ROLE_USER);
        when(userRepository.save(userFavourites)).thenReturn(userFavourites);
        userVisited = new User("aleksej", "Aleksej", "Ivanovski", passwordEncoder.encode("Password@123"), Role.ROLE_USER);
        when(userRepository.save(userVisited)).thenReturn(userVisited);
        when(userRepository.save(registerUser)).thenReturn(registerUser);
        User pendingAdminMadeAdmin = new User("pendingAdmin", "Pending", "Admin", passwordEncoder.encode("Admin123!"), Role.ROLE_ADMIN);
        when(userRepository.save(pendingAdminMadeAdmin)).thenReturn(pendingAdminMadeAdmin);
        userServiceImpl = new UserServiceImpl(userRepository, passwordEncoder, locationRepository);
    }

    // Testing of loadUserByUsername(String username)

    // Test 1 F F, username is not empty and user is found
    @Test
    public void loadUserByUsernameTest1() {
        String username = "admin";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.ofNullable(admin));
        UserDetails user = userServiceImpl.loadUserByUsername(username);
        Assertions.assertEquals(user.getUsername(), admin.getUsername());
        verify(userRepository).findByUsername(username);

    }

    // Test 2 T T, username is empty and user is not found
    @Test
    public void loadUserByUsernameTest2() {
        String username = "";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userServiceImpl.loadUserByUsername(username);
        });
        verify(userRepository).findByUsername(username);
    }

    // Test 3 F T, username is not empty and user is not found
    @Test
    public void loadUserByUsernameTest3() {
        String username = "bojan08";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userServiceImpl.loadUserByUsername(username);
        });
        verify(userRepository).findByUsername(username);
    }

    // Testing of registerUser(UserDTO userDTO)

    // Test 1 F F T F
    @Test
    public void registerUserTest1() {
        UserDTO userDTO = new UserDTO("dare431", "Password@123", "Password@123", "Darko", "Sasanski", Role.ROLE_USER);
        Optional<User> result = userServiceImpl.register(userDTO);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get().getUsername(), registerUser.getUsername());
        verify(userRepository).save(registerUser);
    }

    // Test 2 T F T F
    @Test
    public void registerUserTest2() {
        UserDTO userDTO = new UserDTO(null, "Password@123", "Password@123", "Darko", "Sasanski", Role.ROLE_USER);
        Assertions.assertThrows(InvalidArgumentsException.class, () -> {
            userServiceImpl.register(userDTO);
        });
    }

    // Test 3 F T T F
    @Test
    public void registerUserTest3() {
        UserDTO userDTO = new UserDTO("dare431", null, "Password@123", "Darko", "Sasanski", Role.ROLE_USER);
        Assertions.assertThrows(InvalidArgumentsException.class, () -> {
            userServiceImpl.register(userDTO);
        });
    }

    // Test 4 F F F F
    @Test
    public void registerUserTest4() {
        UserDTO userDTO = new UserDTO("dare431", "Password@123", "Password@12", "Darko", "Sasanski", Role.ROLE_USER);
        Assertions.assertThrows(PasswordsDoNotMatchException.class, () -> {
            userServiceImpl.register(userDTO);
        });
    }

    // Test 5 T F T T
    @Test
    public void registerUserTest5() {
        UserDTO userDTO = new UserDTO("admin", "Password@123", "Password@123", "Darko", "Sasanski", Role.ROLE_USER);
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(admin));
        Assertions.assertThrows(UsernameAlreadyExistsException.class, () -> {
            userServiceImpl.register(userDTO);
        });
        verify(userRepository).findByUsername(userDTO.getUsername());
    }

    // Testing of pendingAdmin(String username)

    // Test 1 T F F
    @Test
    public void pendingAdminTest1() {
        String username = "pendingAdmin";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(pendingAdmin));
        Optional<User> result = userServiceImpl.authorizePendingAdmin(username);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get().getUsername(), pendingAdmin.getUsername());
        Assertions.assertEquals(result.get().getRole(), Role.ROLE_ADMIN);
        verify(userRepository).findByUsername(username);
    }

    // Test 2 F T F
    @Test
    public void pendingAdminTest2() {
        String username = "";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userServiceImpl.authorizePendingAdmin(username);
        });
        verify(userRepository).findByUsername(username);
    }

    // Test 3 F T F
    @Test
    public void pendingAdminTest3() {
        String username = "bojan08";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userServiceImpl.authorizePendingAdmin(username);
        });
        verify(userRepository).findByUsername(username);
    }

    // Test 4 F F T
    @Test
    public void pendingAdminTest4() {
        String username = "dare431";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(registerUser));
        assertThrows(InvalidArgumentsException.class, () -> {
            userServiceImpl.authorizePendingAdmin(username);
        });
        verify(userRepository).findByUsername(username);
    }

    // Testing of addToFavourites(String username, Long locationId)

    // Test 1 F T F
    @Test
    public void addToFavouritesTest1() {
        String username = "andrejT";
        Long locationId = 1L;
        int numberOfFavourites = userFavourites.getFavourites().size();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userFavourites));
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        Optional<User> result = userServiceImpl.addToFavourites(username, locationId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get().getUsername(), userFavourites.getUsername());
        Assertions.assertEquals(result.get().getFavourites().size(), numberOfFavourites + 1);
        verify(userRepository).findByUsername(username);
        verify(locationRepository).findById(locationId);
    }

    // Test 2 T T F
    @Test
    public void addToFavouritesTest2() {
        String username = "";
        Long locationId = 1L;
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userServiceImpl.addToFavourites(username, locationId);
        });
        verify(userRepository).findByUsername(username);
    }

    // Test 3 F F F
    @Test
    public void addToFavouritesTest3() {
        String username = "andrejT";
        Long locationId = -1L;
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userFavourites));
        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());
        assertThrows(LocationNotFoundException.class, () -> {
            userServiceImpl.addToFavourites(username, locationId);
        });
        verify(userRepository).findByUsername(username);
        verify(locationRepository).findById(locationId);
    }

    // Test 4 F T T
    @Test
    public void addToFavouritesTest4() {
        String username = "admin";
        Long locationId = 1L;
        int numberOfFavourites = admin.getFavourites().size();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(admin));
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        Optional<User> result = userServiceImpl.addToFavourites(username, locationId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get().getUsername(), admin.getUsername());
        Assertions.assertEquals(result.get().getFavourites().size(), numberOfFavourites);
        verify(userRepository).findByUsername(username);
        verify(locationRepository).findById(locationId);
    }

    // Testing of addToVisited(String username, Long locationId)

    // Test 1 F T F
    @Test
    public void addToVisitedTest1() {
        String username = "aleksej";
        Long locationId = 1L;
        int numberOfVisited = userVisited.getVisited().size();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userVisited));
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        Optional<User> result = userServiceImpl.addToVisited(username, locationId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get().getUsername(), userVisited.getUsername());
        Assertions.assertEquals(result.get().getVisited().size(), numberOfVisited + 1);
        verify(userRepository).findByUsername(username);
        verify(locationRepository).findById(locationId);
    }

    // Test 2 T T F
    @Test
    public void addToVisitedTest2() {
        String username = "";
        Long locationId = 1L;
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userServiceImpl.addToVisited(username, locationId);
        });
        verify(userRepository).findByUsername(username);
    }

    // Test 3 F F F
    @Test
    public void addToVisitedTest3() {
        String username = "andrejT";
        Long locationId = -1L;
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userFavourites));
        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());
        assertThrows(LocationNotFoundException.class, () -> {
            userServiceImpl.addToVisited(username, locationId);
        });
        verify(userRepository).findByUsername(username);
        verify(locationRepository).findById(locationId);
    }

    // Test 4 F T T
    @Test
    public void addToVisitedTest4() {
        String username = "admin";
        Long locationId = 1L;
        int numberOfFavourites = admin.getVisited().size();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(admin));
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        Optional<User> result = userServiceImpl.addToVisited(username, locationId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get().getUsername(), admin.getUsername());
        Assertions.assertEquals(result.get().getFavourites().size(), numberOfFavourites);
        verify(userRepository).findByUsername(username);
        verify(locationRepository).findById(locationId);
    }


}
