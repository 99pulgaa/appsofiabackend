package com.gestion.clientes.controller;

import com.gestion.clientes.exception.ResourceNotFoundException;
import com.gestion.clientes.modelo.Cliente;
import com.gestion.clientes.repository.ClienteRepository;
import com.gestion.clientes.service.ClienteService;
import com.gestion.clientes.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private ClienteRepository clienteRepository;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(@RequestBody Cliente cliente) {
        List<Cliente> clientes = clienteRepository.findAll();

        for(Cliente c : clientes){
            String email = cliente.getEmail();
            if(c.getEmail().equals(email)){
                emailService.sendEmail(email, "Recupera tu contraseña", createMessage(c));
                return ResponseEntity.ok("Se envio el correo");
            }
        }

        return ResponseEntity.of(Optional.empty());
    }

    private String createMessage(Cliente cliente){
        return new StringBuilder()
                .append("Hola ").append(cliente.getNombre()).append(",")
                .append("\nAqui puedes recuperar tu contraseña:\n")
                .append("http://localhost:3000/RecuperarPass?id=")
                .append(cliente.getId())
                .toString();
    }
}
