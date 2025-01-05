package com.tamer.open_api_generator;

import com.tamer.api.v1.PetApi;
import com.tamer.api.v1.resources.Pet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class PetController implements PetApi {
    @Override
    public ResponseEntity<Pet> addPet(Pet pet) {
        return null;
    }
}
