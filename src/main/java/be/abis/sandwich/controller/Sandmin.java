package be.abis.sandwich.controller;

import be.abis.sandwich.exception.ApiException;
import be.abis.sandwich.model.Sandwich;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class Sandmin {
    @Autowired
    private RestTemplate rt;

    private static final String uri_sandwichapi_mgmt = "http://localhost:8080/api/mgmt";
    private static final String uri_sandwichapi_user = "http://localhost:8080/api";

    @PostMapping(path = "/mgmt/sandwich")
//    @RolesAllowed("AbisAdmin")
    public void addSandwich(@RequestBody Sandwich sandwich) {
        System.out.println("[FRONT][Controller] ADDSANDWICH");
        rt.postForObject(uri_sandwichapi_mgmt + "/sandwich", sandwich, Void.class);
        return;
    }

    @PatchMapping(path = "/mgmt/sandwich", consumes = {MediaType.APPLICATION_JSON_VALUE})
//    @RolesAllowed("AbisAdmin")
    public void updateSandwichPrice(Sandwich sandwich) {
        System.out.println("[FRONT][Controller] UPDATESANDWICHPRICE");
        rt.patchForObject(uri_sandwichapi_mgmt + "/sandwich", sandwich, Void.class);
        return;
    }

    @DeleteMapping(path = "/mgmt/sandwich/{id}")
//    @RolesAllowed("AbisAdmin")
    public void deleteSandwich(@PathVariable("id") int id) {
        System.out.println("[FRONT][Controller] DELETESANDWICH");

        rt.delete(uri_sandwichapi_mgmt + "/sandwich/" + id, id);
        return;
    }

    @GetMapping(path = "/sandwiches/all")
    public ResponseEntity<?> getAllSandwiches() {
        System.out.println("[FRONT][Controller] GETALLSANDWICHES");

        URI uri = UriComponentsBuilder
                .fromUri(URI.create(uri_sandwichapi_user + "/sandwiches/all"))
                .build()
                .toUri();

        ResponseEntity<List<Sandwich>> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            responseEntity = rt.exchange(uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Sandwich>>() {});
            return responseEntity;
        } catch (HttpClientErrorException hcee) {
            System.out.println("[FRONT] API sent an error");
            return new ResponseEntity<>(hcee.getMessage(), responseEntity.getStatusCode());
        }
    }

    @GetMapping(path = "/sandwich/{id}")
    public ResponseEntity<?> getSandwichById(@PathVariable("id") int id) {
        System.out.println("[FRONT][Controller] GETSANDWICHBYID");

        URI uri = UriComponentsBuilder
                .fromUri(URI.create(uri_sandwichapi_user + "/sandwich/" + id))
                .build()
                .toUri();

        ResponseEntity<Sandwich> apiResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            apiResponseEntity = rt.exchange(uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Sandwich>() {});
            return apiResponseEntity;
        } catch (HttpClientErrorException hcee) {
            System.out.println("[FRONT] API sent an error");
            return new ResponseEntity<>(hcee.getMessage(), apiResponseEntity.getStatusCode());
        }
    }

    @GetMapping(path = "/sandwich")
    public ResponseEntity<?> getSandwichByName(@RequestParam("name") String name) {
        System.out.println("[FRONT][Controller] GETSANDWICHBYNAME");

        URI uri = UriComponentsBuilder
                .fromUri(URI.create(uri_sandwichapi_user + "/sandwich"))
                .queryParam("name", name)
                .build()
                .toUri();

        ResponseEntity<Sandwich> apiResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            apiResponseEntity = rt.exchange(uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Sandwich>() {});
            return apiResponseEntity;
        } catch (HttpClientErrorException hcee) {
            System.out.println("[FRONT] API sent an error");
            return new ResponseEntity<>(hcee.getMessage(), apiResponseEntity.getStatusCode());
        }
    }

    @GetMapping(path = "/sandwiches")
    public ResponseEntity<?> getAllSandwichesByCategory(@RequestParam("category") String category) {
        System.out.println("[FRONT][Controller] GETSANDWICHESBYCATEGORY");

        URI uri = UriComponentsBuilder
                .fromUri(URI.create(uri_sandwichapi_user + "/sandwiches"))
                .queryParam("category", category)
                .build()
                .toUri();

        ResponseEntity<List<Sandwich>> apiResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            apiResponseEntity = rt.exchange(uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Sandwich>>() {});
            return apiResponseEntity;
        } catch (HttpClientErrorException hcee) {
            System.out.println("[FRONT] API sent an error");
            return new ResponseEntity<>(hcee.getMessage(), apiResponseEntity.getStatusCode());
        }
    }
}
