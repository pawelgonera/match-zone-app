package pl.poul12.matchzone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.service.PersonalDetailsService;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class PersonalDetailsController {

    private PersonalDetailsService personalDetailsService;

    public PersonalDetailsController(PersonalDetailsService personalDetailsService) {
        this.personalDetailsService = personalDetailsService;
    }

    @GetMapping("/personal/{username}")
    public ResponseEntity<PersonalDetails> getPersonalDetails(@PathVariable(value = "username") String username) {

        PersonalDetails personalDetails = personalDetailsService.getPersonalDetails(username);

        return ResponseEntity.ok().body(personalDetails);
    }

    @PutMapping("/personal/{username}")
    public ResponseEntity<?> updatePersonalDetails(@PathVariable(value = "username") String username, @Valid @RequestBody PersonalDetails personalDetails) {

        PersonalDetails updatedPersonalDetails =  personalDetailsService.updatePersonalDetails(username, personalDetails);

        return ResponseEntity.ok(updatedPersonalDetails);
    }
}
