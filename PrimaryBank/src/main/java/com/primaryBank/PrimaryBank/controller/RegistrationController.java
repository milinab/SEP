package com.primaryBank.PrimaryBank.controller;

import com.primaryBank.PrimaryBank.dto.ClientDto;
import com.primaryBank.PrimaryBank.model.Client;
import com.primaryBank.PrimaryBank.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registration")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/addClient")
    public Client addClient(@RequestBody ClientDto dto) throws Exception {
        return registrationService.addClient(dto);
    }
}
