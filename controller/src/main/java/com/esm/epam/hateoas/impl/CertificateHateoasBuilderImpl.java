package com.esm.epam.hateoas.impl;

import com.esm.epam.controller.CertificateController;
import com.esm.epam.entity.Certificate;
import com.esm.epam.exception.ControllerException;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.hateoas.HateoasBuilder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateHateoasBuilderImpl implements HateoasBuilder<Certificate> {
    @Override
    public void buildDefaultHateoas(RepresentationModel model) throws ControllerException, ServiceException, ResourceNotFoundException, DaoException {
        MultiValueMap<String, Object> localParams = new LinkedMultiValueMap<>();
        localParams.add("sort", "name");
        model.add(linkTo(methodOn(CertificateController.class).getCertificateList(localParams, 0, 5)).withSelfRel().withType("GET"));
        model.add(linkTo(methodOn(CertificateController.class).addCertificate(new Certificate())).withSelfRel().withType("POST"));
    }

    @Override
    public void buildFullHateoas(Certificate certificate) throws ResourceNotFoundException, DaoException, ControllerException, ServiceException {
        buildDefaultHateoas(certificate);
        certificate.add(linkTo(methodOn(CertificateController.class).getCertificate(certificate.getId())).withSelfRel().withType("GET"));
        certificate.add(linkTo(methodOn(CertificateController.class).deleteCertificate(certificate.getId())).withSelfRel().withType("DELETE"));
        certificate.add(linkTo(methodOn(CertificateController.class).deleteTagCertificate(certificate.getId(), 1L)).withRel("Tag").withType("DELETE"));
        certificate.add(linkTo(methodOn(CertificateController.class).updateCertificate(certificate.getId(), new Certificate())).withSelfRel().withType("PATCH"));
    }
}
