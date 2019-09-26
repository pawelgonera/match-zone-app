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

    @GetMapping("/personal/{id}")
    public ResponseEntity<PersonalDetails> getPersonalDetailsById(@PathVariable(value = "id") Long personalDetailsId) throws ResourceNotFoundException {

        PersonalDetails personalDetails = personalDetailsService.getPersonalDetailsById(personalDetailsId);

        return ResponseEntity.ok().body(personalDetails);
    }

    @PutMapping("/personal/{id}")
    public ResponseEntity<?> updatePersonalDetails(@PathVariable(value = "id") Long userId, @Valid @RequestBody PersonalDetails personalDetails) throws ResourceNotFoundException {

        PersonalDetails updatedPersonalDetails =  personalDetailsService.updatePersonalDetails(userId, personalDetails);

        return ResponseEntity.ok(updatedPersonalDetails);
    }
}
