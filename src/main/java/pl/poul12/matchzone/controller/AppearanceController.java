package pl.poul12.matchzone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Appearance;
import pl.poul12.matchzone.service.AppearanceService;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class AppearanceController {

    private AppearanceService appearanceService;

    public AppearanceController(AppearanceService appearanceService) {
        this.appearanceService = appearanceService;
    }

    @GetMapping("/appearance/{id}")
    public ResponseEntity<Appearance> getAppearanceById(@PathVariable(value = "id") Long appearanceId) throws ResourceNotFoundException {

        Appearance appearance = appearanceService.getAppearanceById(appearanceId);

        return ResponseEntity.ok().body(appearance);
    }

    @PutMapping("/appearance/{id}")
    public ResponseEntity<?> updateAppearance(@PathVariable(value = "id") Long userId, @Valid @RequestBody Appearance appearance) throws ResourceNotFoundException {

        Appearance appearanceDetails = appearanceService.updateAppearance(userId, appearance);

        return ResponseEntity.ok(appearanceDetails);
    }
}
