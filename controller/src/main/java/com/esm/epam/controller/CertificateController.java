package com.esm.epam.controller;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.View;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.service.CertificateService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static com.esm.epam.validator.ControllerValidator.validateSortValues;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/certificates")
@Validated
@AllArgsConstructor
public class CertificateController {
    private final CertificateService certificateService;
    private final HateoasBuilder<Certificate> hateoasBuilder;

    @GetMapping
    @ResponseStatus(OK)
    @JsonView(View.UI.class)
    public List<Certificate> getCertificateList(@RequestParam(required = false) MultiValueMap<String, Object> params, @RequestParam("page") @Min(1) int page, @RequestParam("size") @Min(1) int size) {
        validateSortValues(params);
        List<Certificate> certificates = certificateService.getCertificates(params, page, size);
        certificates.forEach(hateoasBuilder::buildFullHateoas);
        return certificates;
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @JsonView(View.UI.class)
    public Certificate getCertificate(@PathVariable("id") @Min(1L) long id) {
        Certificate certificate = certificateService.getById(id);
        hateoasBuilder.buildFullHateoas(certificate);
        return certificate;
    }


    @DeleteMapping("/{id}/tags/{tagId}")
    @JsonView(View.UI.class)
    public ResponseEntity<RepresentationModel<Certificate>> deleteTagCertificate(@PathVariable("id") @Min(1L) long id, @PathVariable("tagId") @Min(1L) long tagId) {
        ResponseEntity<RepresentationModel<Certificate>> responseEntity;
        Optional<Certificate> updatedCertificate = certificateService.deleteTag(id, tagId);
        if (updatedCertificate.isPresent()) {
            hateoasBuilder.buildFullHateoas(updatedCertificate.get());
            responseEntity = new ResponseEntity<>(updatedCertificate.get(), NO_CONTENT);
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @JsonView(View.UI.class)
    public ResponseEntity<RepresentationModel<Certificate>> deleteCertificate(@PathVariable("id") @Min(1L) long id) {
        ResponseEntity<RepresentationModel<Certificate>> responseEntity;
        if (certificateService.deleteById(id)) {
            RepresentationModel<Certificate> representationModel = new RepresentationModel<>();
            hateoasBuilder.buildDefaultHateoas(representationModel);
            responseEntity = new ResponseEntity<>(representationModel, NO_CONTENT);
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }
        return responseEntity;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @JsonView(View.UI.class)
    public RepresentationModel<Certificate> addCertificate(@RequestBody Certificate certificate) {
        Certificate addedCertificate = certificateService.add(certificate);
        hateoasBuilder.buildFullHateoas(addedCertificate);
        return addedCertificate;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    @JsonView(View.UI.class)
    public RepresentationModel<Certificate> updateCertificate(@PathVariable("id") @Min(1L) long id, @RequestBody Certificate certificate) {
        Certificate updatedCertificate = certificateService.update(certificate, id);
        hateoasBuilder.buildFullHateoas(updatedCertificate);
        return updatedCertificate;
    }
}
