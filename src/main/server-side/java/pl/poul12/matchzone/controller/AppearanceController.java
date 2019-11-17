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

    @GetMapping("/appearance/{username}")
    public ResponseEntity<Appearance> getAppearance(@PathVariable(value = "username") String username) {

        Appearance appearance = appearanceService.getAppearance(username);

        return ResponseEntity.ok().body(appearance);
    }

    @PutMapping("/appearance/{username}")
    public ResponseEntity<?> updateAppearance(@PathVariable(value = "username") String username, @Valid @RequestBody Appearance appearance) {

        Appearance appearanceDetails = appearanceService.updateAppearance(username, appearance);

        return ResponseEntity.ok(appearanceDetails);
    }
}
