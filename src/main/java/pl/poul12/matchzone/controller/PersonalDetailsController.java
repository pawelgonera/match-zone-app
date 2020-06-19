package pl.poul12.matchzone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.service.PersonalDetailsServiceImpl;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class PersonalDetailsController {

    private PersonalDetailsServiceImpl personalDetailsService;

    public PersonalDetailsController(PersonalDetailsServiceImpl personalDetailsService) {
        this.personalDetailsService = personalDetailsService;
    }

    @GetMapping("/personal/{id}")
    public ResponseEntity<PersonalDetails> getPersonalDetails(@PathVariable(value = "id") Long id) {

        PersonalDetails personalDetails = personalDetailsService.getPersonalDetails(id);

        return ResponseEntity.ok().body(personalDetails);
    }

    @PutMapping("/personal/{id}")
    public ResponseEntity<?> updatePersonalDetails(@PathVariable(value = "id") Long id, @Valid @RequestBody PersonalDetails personalDetails) {

        PersonalDetails updatedPersonalDetails =  personalDetailsService.updatePersonalDetails(id, personalDetails);

        return ResponseEntity.ok(updatedPersonalDetails);
    }

}
