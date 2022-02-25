package com.esm.epam.controller;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.View;
import com.esm.epam.exception.ControllerException;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.service.CRUDService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

import static com.esm.epam.validator.ControllerValidator.validateIntToBeUpdated;
import static com.esm.epam.validator.ControllerValidator.validateSortValues;


@RestController
@RequestMapping("/certificates")
@Validated
public class CertificateController {

    @Autowired
    public CRUDService<Certificate> certificateService;

    @GetMapping
    @JsonView(View.UI.class)
    public ResponseEntity<List<Certificate>> getCertificateList(@RequestParam(required = false) MultiValueMap<String, Object> params) throws ResourceNotFoundException, ControllerException {
        List<Certificate> certificates = new ArrayList<>();
        if (params.size() == 0) {
            certificates = certificateService.getAll();
        } else {
            validateSortValues(params);
            certificates = certificateService.getFilteredList(params);
        }
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(View.UI.class)
    public ResponseEntity<Certificate> getCertificate(@PathVariable("id") @Min(1L) Long id) throws ResourceNotFoundException, DaoException {
        Certificate certificate = certificateService.getById(id);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }


    @DeleteMapping("/{id}/tags/{tag_id}")
    public ResponseEntity<Certificate> deleteCertificate(@PathVariable("id") @Min(1L) Long id, @PathVariable("tag_id") @Min(1L) Long idTag) throws ResourceNotFoundException, DaoException {
        return certificateService.deleteTag(id, idTag)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable("id") @Min(1L) Long id) throws ResourceNotFoundException {
        ResponseEntity<Void> responseEntity;
        if (certificateService.deleteById(id)) {
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return responseEntity;
    }


    @PostMapping
    public ResponseEntity<Certificate> addCertificate(@Valid @RequestBody Certificate certificate, BindingResult bindingResult) throws DaoException {
        return certificateService.add(certificate)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Certificate> updateCertificate(@PathVariable("id") @Min(1L) Long id, @RequestBody Certificate certificate) throws ControllerException, ResourceNotFoundException, DaoException {
        validateIntToBeUpdated(certificate.getDuration());
        validateIntToBeUpdated(certificate.getPrice());
        return certificateService.update(certificate, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
