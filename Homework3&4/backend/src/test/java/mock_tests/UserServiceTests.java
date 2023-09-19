package mock_tests;

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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {
    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        User expectedUser = new User("bojan", "Bojan", "Trpeski", "Test1234!", Role.ROLE_USER);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        userServiceImpl = new UserServiceImpl(userRepository, passwordEncoder, locationRepository);


    }

    @Test
    public void successRegister() {
        UserDTO userDTO = new UserDTO("bojan", "Test1234!", "Test1234!",
                "Bojan", "Trpeski", Role.ROLE_USER);
        Optional<User> result = userServiceImpl.register(userDTO);
        verify(userRepository).save(any(User.class));

    }

    @Test
    public void invalidArgumentsRegister() {
        UserDTO userDTO = new UserDTO("", "Test1234!", "Test1234!",
                "Bojan", "Trpeski", Role.ROLE_USER);
        assertThrows(InvalidArgumentsException.class, () -> {
            userServiceImpl.register(userDTO);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void invalidArgumentsRegisterNull() {
        UserDTO userDTO = new UserDTO(null, "Test1234!", "Test1234!",
                "Bojan", "Trpeski", Role.ROLE_USER);
        assertThrows(InvalidArgumentsException.class, () -> {
            userServiceImpl.register(userDTO);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void passwordsDoNotMatch() {
        UserDTO userDTO = new UserDTO("bojan", "Test1234!", "Test",
                "Bojan", "Trpeski", Role.ROLE_USER);
        assertThrows(PasswordsDoNotMatchException.class, () -> {
            userServiceImpl.register(userDTO);

        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void usernameAlreadyExists() {
        UserDTO userDTO = new UserDTO("bojan", "Test1234!", "Test1234!",
                "Bojan", "Trpeski", Role.ROLE_USER);
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(new User()));
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userServiceImpl.register(userDTO);
        });
        verify(userRepository).findByUsername(userDTO.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void getUserWithValidUsername() {
        String validUsername = "bojan";
        User expectedUser = new User();
        when(userRepository.findByUsername(validUsername)).thenReturn(Optional.of(expectedUser));
        Optional<User> result = userServiceImpl.getUser(validUsername);
        verify(userRepository).findByUsername(validUsername);

    }

    @Test
    public void getUserWithInvalidUsername() {
        String invalidUsername = "unknownUser";
        when(userRepository.findByUsername(invalidUsername)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userServiceImpl.getUser(invalidUsername);
        });
        verify(userRepository).findByUsername(invalidUsername);

    }

    @Test
    public void authorizePendingAdminWithInvalidUsername() {
        String invalidUsername = "unknownUser";
        when(userRepository.findByUsername(invalidUsername)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userServiceImpl.authorizePendingAdmin(invalidUsername);
        });
        verify(userRepository).findByUsername(invalidUsername);

    }

    @Test
    public void authorizePendingAdminWithInvalidRole() {
        String username = "User";
        User user = new User();
        user.setRole(Role.ROLE_USER);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(InvalidArgumentsException.class, () -> {
            userServiceImpl.authorizePendingAdmin(username);
        });
        verify(userRepository).findByUsername(username);

    }

    @Test
    public void authorizePendingAdminWithValidRole() {
        String validUsername = "pendingAdmin";
        User expectedUser = new User();
        expectedUser.setRole(Role.ROLE_PENDING_ADMIN);
        when(userRepository.findByUsername(validUsername)).thenReturn(Optional.of(expectedUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        userServiceImpl.authorizePendingAdmin(validUsername);
        verify(userRepository).findByUsername(validUsername);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void findAllPendingAdmins() {
        User pendingAdmin1 = new User("user1", "B", "T", "password", Role.ROLE_PENDING_ADMIN);
        User pendingAdmin2 = new User("user2", "BT", "BT", "password", Role.ROLE_PENDING_ADMIN);

        when(userRepository.findAllByRole(Role.ROLE_PENDING_ADMIN)).thenReturn(Arrays.asList(pendingAdmin1, pendingAdmin2));
        userServiceImpl.findAllPendingAdmins();
        verify(userRepository).findAllByRole(Role.ROLE_PENDING_ADMIN);

    }

    @Test
    public void addToVisitedUserNotFound() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userServiceImpl.addToVisited("username", 1L);
        });
        verify(userRepository).findByUsername("username");


    }

    @Test
    public void addToVisitedLocationNotFound() {
        User user = new User();
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LocationNotFoundException.class, () -> {
            userServiceImpl.addToVisited("username", 1L);
        });
        verify(userRepository).findByUsername("username");
        verify(locationRepository).findById(1L);
    }


}
