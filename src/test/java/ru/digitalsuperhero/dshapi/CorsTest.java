package ru.digitalsuperhero.dshapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import ru.digitalsuperhero.dshapi.dao.AdminRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Admin;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

import java.net.URI;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorsTest {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void corsWithAnnotation() throws Exception {
        ResponseEntity<Admin> entity = this.restTemplate.exchange(
                RequestEntity
                        .post(uri("/login/admin"))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .body(new Admin("network", "network")),
                Admin.class);

        assertEquals(HttpStatus.ACCEPTED, entity.getStatusCode());
        assertEquals("http://localhost:3000", entity.getHeaders().getAccessControlAllowOrigin());
    }

    @Test
    public void corsCustomer() throws Exception {
        ResponseEntity<Customer> entity = this.restTemplate.exchange(
                RequestEntity
                        .post(uri("/register/customer"))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .body(new Customer("test3", "test3@gmail.com", "+79991234567", "test")),
                Customer.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("http://localhost:3000", entity.getHeaders().getAccessControlAllowOrigin());
    }

    private URI uri(String path) {
        return restTemplate.getRestTemplate().getUriTemplateHandler().expand(path);
    }
}