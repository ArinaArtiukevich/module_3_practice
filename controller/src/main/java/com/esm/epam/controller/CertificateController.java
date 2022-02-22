package com.esm.epam.controller;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.View;
import com.esm.epam.exception.ControllerException;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.service.CertificateService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static com.esm.epam.validator.ControllerValidator.validateSortValues;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/certificates")
@Validated
public class CertificateController {

    private final CertificateService certificateService;
    private final HateoasBuilder<Certificate> hateoasBuilder;

    public CertificateController(CertificateService certificateService, HateoasBuilder<Certificate> hateoasBuilder) {
        this.certificateService = certificateService;
        this.hateoasBuilder = hateoasBuilder;
    }

    @GetMapping
    @JsonView(View.UI.class)
    public ResponseEntity<List<Certificate>> getCertificateList(@RequestParam(required = false) MultiValueMap<String, Object> params, @RequestParam("page") @Min(0) int page, @RequestParam("size") @Min(1) int size) throws ResourceNotFoundException, ServiceException, DaoException, ControllerException {
        List<Certificate> certificates;
        if (params.size() == 2) {
            certificates = certificateService.getAll(page, size);
        } else {
            validateSortValues(params);
            certificates = certificateService.getFilteredList(params, page, size);
        }
        for (Certificate certificate : certificates) {
           hateoasBuilder.buildFullHateoas(certificate);
        }
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(View.UI.class)
    public ResponseEntity<Certificate> getCertificate(@PathVariable("id") @Min(1L) long id) throws ResourceNotFoundException, DaoException, ControllerException, ServiceException {
        Certificate certificate = certificateService.getById(id);
        hateoasBuilder.buildFullHateoas(certificate);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }


    @DeleteMapping("/{id}/tags/{tagId}")
    @JsonView(View.UI.class)
    public ResponseEntity<RepresentationModel<Certificate>> deleteTagCertificate(@PathVariable("id") @Min(1L) long id, @PathVariable("tagId") @Min(1L) long tagId) throws DaoException, ControllerException, ServiceException, ResourceNotFoundException {
        ResponseEntity<RepresentationModel<Certificate>> responseEntity;
        Optional<Certificate> addedCertificate = certificateService.deleteTag(id, tagId);
        if (addedCertificate.isPresent()) {
            hateoasBuilder.buildFullHateoas(addedCertificate.get());
            responseEntity = new ResponseEntity<>(addedCertificate.get(), OK);
        } else {
            responseEntity = ResponseEntity.noContent().build();
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @JsonView(View.UI.class)
    public ResponseEntity<RepresentationModel<Certificate>> deleteCertificate(@PathVariable("id") @Min(1L) long id) throws ResourceNotFoundException, ControllerException, ServiceException, DaoException {
        ResponseEntity<RepresentationModel<Certificate>> responseEntity;
        if (certificateService.deleteById(id)) {
            RepresentationModel<Certificate> representationModel = new RepresentationModel<>();
            hateoasBuilder.buildDefaultHateoas(representationModel);
            responseEntity = new ResponseEntity<>(representationModel, OK);
        } else {
            responseEntity = ResponseEntity.noContent().build();
        }
        return responseEntity;
    }

    @PostMapping
    @JsonView(View.UI.class)
    public ResponseEntity<RepresentationModel<Certificate>> addCertificate(@RequestBody Certificate certificate) throws DaoException, ControllerException, ServiceException, ResourceNotFoundException {
        ResponseEntity<RepresentationModel<Certificate>> responseEntity;
        Certificate addedCertificate = certificateService.add(certificate);
        hateoasBuilder.buildFullHateoas(addedCertificate);
        responseEntity = new ResponseEntity<>(addedCertificate, OK);

        return responseEntity;
    }

    @PatchMapping("/{id}")
    @JsonView(View.UI.class)
    public ResponseEntity<RepresentationModel<Certificate>> updateCertificate(@PathVariable("id") @Min(1L) long id, @RequestBody Certificate certificate) throws ControllerException, ResourceNotFoundException, DaoException, ServiceException {
        Certificate updatedCertificate = certificateService.update(certificate, id);
        hateoasBuilder.buildFullHateoas(updatedCertificate);
        return new ResponseEntity<>(updatedCertificate, OK);
    }
}
