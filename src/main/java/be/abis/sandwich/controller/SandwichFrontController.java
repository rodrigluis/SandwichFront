package be.abis.sandwich.controller;

import be.abis.sandwich.exception.ApiException;
import be.abis.sandwich.model.Credentials;
import be.abis.sandwich.model.Sandwich;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static be.abis.sandwich.exception.ApiException.Type.NOT_ALLOWED;

//@RestController
@Controller
public class SandwichFrontController {
    @Autowired
    private RestTemplate rt;
    @Autowired
    private Credentials credentials;

    private static final String uri_sandwichapi_mgmt = "http://localhost:8080/api/mgmt";
    private static final String uri_sandwichapi_user = "http://localhost:8080/api";

    @GetMapping("/ui")
    public String home(Model model) {
        System.out.println("[FRONT][Controller] HOME form");

        model.addAttribute("message", "SANDWICH FRONTEND UI");
        return "home";
    }

    @GetMapping("/form")
    public String form(Model model) {
        System.out.println("[FRONT][Controller] FORM form");

        return "Form";
    }

    @RequestMapping(value = "/sand", method = RequestMethod.GET)
    public ModelAndView showForm() {
//        https://spring.io/guides/gs/handling-form-submission
        return new ModelAndView("form", "sandwich", new Sandwich());
    }

    @PostMapping(path = "/mgmt/new")
//    @RolesAllowed("AbisAdmin")
    public String submit(@ModelAttribute("sandwich") Sandwich sandwich,
                            BindingResult result, ModelMap form) {
        System.out.println("[FRONT][Controller] NEWSANDWICH");

        if (result.hasErrors()) {
            return "error";
        }
        form.addAttribute("name", sandwich.getName());
        form.addAttribute("description", sandwich.getDescription());
        form.addAttribute("id", sandwich.getId());
        form.addAttribute("category", sandwich.getCategory());
        form.addAttribute("basePrice", sandwich.getBasePrice());

        rt.postForObject(uri_sandwichapi_mgmt + "/sandwich", sandwich, Void.class);
        return "employeeView";
    }

    @PostMapping(path = "/mgmt/sandwich")
    @RolesAllowed("AbisAdmin")
    public void addSandwich(@RequestBody Sandwich sandwich) {
        System.out.println("[FRONT][Controller] ADDSANDWICH");
        if (credentials.getUserName() != null) {
            rt.getInterceptors().add(new BasicAuthenticationInterceptor(credentials.getUserName(), credentials.getUserPassword()));
            rt.postForObject(uri_sandwichapi_mgmt + "/sandwich", sandwich, Void.class);
            return;
        }
        throw new ApiException(NOT_ALLOWED);
    }

    @PatchMapping(path = "/mgmt/sandwich", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @RolesAllowed("AbisAdmin")
    public void updateSandwichPrice(@RequestBody Sandwich sandwich) {
        System.out.println("[FRONT][Controller] UPDATESANDWICHPRICE [" + sandwich.toString() + "]");

//        HttpHeaders headers = new HttpHeaders();
//        MediaType mediaType = new MediaType("application", "merge-patch+json");
//        headers.setContentType(mediaType);

        if (credentials.getUserName() != null) {
            HttpEntity<Sandwich> entity = new HttpEntity<>(sandwich);
            rt.getInterceptors().add(new BasicAuthenticationInterceptor(credentials.getUserName(), credentials.getUserPassword()));
//            rt.exchange(uri_sandwichapi_mgmt + "/sandwich", HttpMethod.PATCH, entity, Void.class);
            rt.patchForObject(uri_sandwichapi_mgmt + "/sandwich", sandwich, Void.class);
            return;
        }
        throw new ApiException(NOT_ALLOWED);
    }

    @DeleteMapping(path = "/mgmt/sandwich/{id}")
    @RolesAllowed("AbisAdmin")
    public void deleteSandwich(@PathVariable("id") int id) {
        System.out.println("[FRONT][Controller] DELETESANDWICH");

        if (credentials.getUserName() != null) {
            rt.getInterceptors().add(new BasicAuthenticationInterceptor(credentials.getUserName(), credentials.getUserPassword()));
            rt.delete(uri_sandwichapi_mgmt + "/sandwich/" + id, id);
            return;
        }
        throw new ApiException(NOT_ALLOWED);
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
