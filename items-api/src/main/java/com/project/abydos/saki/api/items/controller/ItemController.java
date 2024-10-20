package com.project.abydos.saki.api.items.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class ItemController {

    @GetMapping("/items/{item_id}")
    public ResponseEntity<String> getItemDetail(
            @Validated @PathVariable("item_id") Long itemId
    ) {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }

}
