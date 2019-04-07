package ru.digitalsuperhero.dshapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.digitalsuperhero.dshapi.dao.AdminRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Admin;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;

import java.util.Base64;

@RestController
@RequestMapping(path = "/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private AdminRepository adminRepository;

    public AdminController(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    @RequestMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Admin> loginAdmin(@RequestBody Admin admin) {
        Admin foundAdmin = adminRepository.findByUsername(admin.getUsername());
        byte[] foundContractorDecodedBytes = Base64.getDecoder().decode(foundAdmin.getPassword());
        String decodedFounderPassword = new String(foundContractorDecodedBytes);
        if (foundAdmin != null && admin.getPassword().equals(decodedFounderPassword)) {
            foundAdmin.setSignedIn(true);
            return new ResponseEntity<>(foundAdmin, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(admin, HttpStatus.NOT_FOUND);
        }
    }
}
