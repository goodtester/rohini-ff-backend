package com.rohini.fastfoodapp.controller;

import com.rohini.fastfoodapp.model.Company;
import com.rohini.fastfoodapp.model.Response;
import com.rohini.fastfoodapp.service.CompanyServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Map;

/**
 * @author Rohini
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private final CompanyServiceImp serviceImp;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<Response> saveCompany(@RequestBody @Valid Company company) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(Instant.now())
                        .data(Map.of("company", serviceImp.create(company)))
                        .message("Create company")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping()
    public ResponseEntity<Response> listCompanies() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(Instant.now())
                        .data(Map.of("company", serviceImp.list()))
                        .message("List companies")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> updateCompany(@PathVariable("id") Long id, @RequestBody @Valid Company company) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(Instant.now())
                        .data(Map.of("company", serviceImp.update(id, company)))
                        .message("Updating company")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> deleteCompany(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(Instant.now())
                        .data(Map.of("company", serviceImp.delete(id)))
                        .message("Delete company")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }
}
