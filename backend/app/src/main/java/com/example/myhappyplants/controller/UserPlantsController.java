package com.example.myhappyplants.controller;

import com.example.myhappyplants.dto.UserPlantDTO;
import com.example.myhappyplants.service.UserPlantsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owned")
public class UserPlantsController {
    private final UserPlantsService userPlantsService;

    public UserPlantsController(UserPlantsService userPlantsService) {this.userPlantsService = userPlantsService;}

//TODO - write all the bodies in ResponseEntity().status(HtttpStatus.OK).body(userPlantsService.{METHODNAME()});
    //TODO - the ? (Optional) can also throw other HttpStatuses, like not found etc. Helps inform the response.
    @GetMapping("/all")
    public ResponseEntity<?> getAllOwnedLibrary() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOwnedLibrary(@PathVariable String id) {
        return null;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOwnedLibrary(@RequestBody UserPlantDTO UserPlantDTO) {
        return null;
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateOwnedLibrary(@PathVariable String id, @RequestBody UserPlantDTO UserPlantDTO) {
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOwnedLibrary(@PathVariable String id) {
        return null;
    }
}