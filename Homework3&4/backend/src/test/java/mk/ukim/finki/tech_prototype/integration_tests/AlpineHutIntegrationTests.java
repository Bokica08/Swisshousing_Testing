package mk.ukim.finki.tech_prototype.integration_tests;

import mk.ukim.finki.tech_prototype.Model.AlpineHut;
import mk.ukim.finki.tech_prototype.Model.DTO.AlpineHutDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlpineHutIntegrationTests {

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
        assertNotNull("Not null",response.getBody());
    }

    @Test
    public void testGetAlpineHutById() {
        Long id = 127L;
        ResponseEntity<AlpineHut> response = restTemplate.getForEntity(getRootUrl() + "/" + id, AlpineHut.class);
        assertNotNull("Null",response.getBody());
    }

    @Test
    public void testGetAlpineHutByName() {
        String name = "Bödeli";
        ResponseEntity<AlpineHut> response = restTemplate.getForEntity(getRootUrl() + "/name?name=" + name, AlpineHut.class);
        assertNotNull("Null",response.getBody());
    }

    @Test
    public void testGetAlpineHutByContains() {
        String name = "Gros";
        ResponseEntity<List> response = restTemplate.getForEntity(getRootUrl() + "/cname?name=" + name, List.class);
        assertNotNull("Null",response.getBody());
    }

    @Test
    public void testGetAlpineHutByCity() {
        String city = "La Roche FR";
        ResponseEntity<List> response = restTemplate.getForEntity(getRootUrl() + "/city?city=" + city, List.class);
        assertNotNull("Null",response.getBody());
    }

    @Test
    public void testSaveAlpineHut() {
        AlpineHutDTO alpineHutDTO = new AlpineHutDTO(1.0,2.0,"test","test","test","tets","test","test",123);
        ResponseEntity<AlpineHut> response = restTemplate.postForEntity(getRootUrl() + "/add", alpineHutDTO, AlpineHut.class);
        assertNotNull("Null",response.getBody());
    }

}