package mk.ukim.finki.tech_prototype.Spring_Boot_tests;

import mk.ukim.finki.tech_prototype.Model.AlpineHut;
import mk.ukim.finki.tech_prototype.Model.DTO.AlpineHutDTO;
import mk.ukim.finki.tech_prototype.Model.DTO.LoginRequest;
import mk.ukim.finki.tech_prototype.Model.DTO.UserInfoResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlpineHutSpringBootTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port + "/alpinehut";
    }

    @Test
    public void testGetAllAlpineHuts() {
        ResponseEntity<List> response = restTemplate.getForEntity(getRootUrl(), List.class);
        assertNotNull("Not null", response.getBody());
    }

    @Test
    public void testGetAlpineHutById() {
        int id = 62;
        ResponseEntity<AlpineHut> response = restTemplate.getForEntity(getRootUrl() + "/" + id, AlpineHut.class);
        assertNotNull("Null", response.getBody());
    }

    @Test
    public void testGetAlpineHutByName() {
        String name = "Bödeli";
        ResponseEntity<AlpineHut> response = restTemplate.getForEntity(getRootUrl() + "/name?name=" + name, AlpineHut.class);
        assertNotNull("Null", response.getBody());
    }

    @Test
    public void testGetAlpineHutByContains() {
        String name = "Gros";
        ResponseEntity<List> response = restTemplate.getForEntity(getRootUrl() + "/cname?name=" + name, List.class);
        assertNotNull("Null", response.getBody());
    }

    @Test
    public void testGetAlpineHutByCity() {
        String city = "La Roche FR";
        ResponseEntity<List> response = restTemplate.getForEntity(getRootUrl() + "/city?city=" + city, List.class);
        assertNotNull("Null", response.getBody());
    }

    @Test
    public void testSaveAlpineHut() {
        AlpineHutDTO alpineHutDTO = new AlpineHutDTO(1.0, 2.0, "test", "test", "test", "tets", "test", "test", 123);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, login());

        HttpEntity<AlpineHutDTO> requestEntity = new HttpEntity<>(alpineHutDTO, headers);

        ResponseEntity<AlpineHut> response = restTemplate.exchange(
                getRootUrl() + "/add",
                HttpMethod.POST,
                requestEntity,
                AlpineHut.class
        );
        assertNotNull("Null", response.getBody());
    }

    @Test
    public void testEditAlpineHut() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, login());

        AlpineHutDTO alpineHutDTO = new AlpineHutDTO(6.99390513195248, 47.1647119534148, "La Jonquille", "Mont Soleil", "Mont Soleil", "159", "opis", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSlgTR19MWZ-Wln2Z_lE4JR7PHAQcwpmVNiUPXeEsTOA&s", 1254);


        HttpEntity<AlpineHutDTO> requestEntity = new HttpEntity<>(alpineHutDTO, headers);

        ResponseEntity<AlpineHut> response = restTemplate.exchange(
                getRootUrl() + "/edit/62",
                HttpMethod.POST,
                requestEntity,
                AlpineHut.class
        );
        assertNotNull("Null", response.getBody());
    }

    private String login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("JDoe");
        loginRequest.setPassword("Password@123");

        ResponseEntity<UserInfoResponse> response = restTemplate.postForEntity("http://localhost:8080/api/auth/signin", loginRequest, UserInfoResponse.class);
        return response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0).split(";")[0];
    }

}