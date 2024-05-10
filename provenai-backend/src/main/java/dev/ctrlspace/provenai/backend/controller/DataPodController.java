package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.controller.specs.DataPodControllerSpec;
import dev.ctrlspace.provenai.backend.converters.DataPodConverter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.dtos.DataPodDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.DataPodCriteria;
import dev.ctrlspace.provenai.backend.services.DataPodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class DataPodController implements DataPodControllerSpec {


    private DataPodService dataPodService;

    private DataPodConverter dataPodConverter;

    @Autowired
    public DataPodController(DataPodService dataPodService,
                             DataPodConverter dataPodConverter) {
        this.dataPodService = dataPodService;
        this.dataPodConverter = dataPodConverter;
    }

    @GetMapping("/data-pods")
    public Page<DataPod> getAllDataPods(DataPodCriteria criteria, Pageable pageable) throws ProvenAiException {
        return dataPodService.getAllDataPods(criteria, pageable);
    }

    @GetMapping("/data-pods/{id}")
    public DataPod getById(@PathVariable UUID id) throws ProvenAiException {
        return dataPodService.getDataPodById(id);
    }



    @PostMapping("/data-pods")
    public DataPod create(@RequestBody DataPodDTO dataPodDto) throws ProvenAiException {

        DataPod dataPod = dataPodConverter.toEntity(dataPodDto);

        return dataPodService.createDataPod(dataPod, dataPodDto.getUsagePolicies());
    }

//    @PostMapping("/data-pods/{id}/credential-offer")
//    public DataPod createDataPodVerifiableId(UUID id) {
//        return DataPod
//                .builder()
//                .id(id)
//                .credentialOfferUrl("openid-credential-offer://issuer.portal.walt.id/?credential_offer_uri=https%3A%2F%2Fissuer.portal.walt.id%2Fopenid4vc%2FcredentialOffer%3Fid%3D99d79908-32e6-4bd7-9873-562d2d262e12")
//                .credentialJwt("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsIm5hbWUiOiJKb2huIERvZSIsImVtYWlsIjoiam9obkBkb2UuY29tIn0.7J1Gzv")
//                .build();

//delete dataPod

    @DeleteMapping("/data-pods/{id}")
    public void delete(@PathVariable UUID id) throws ProvenAiException {
        dataPodService.deleteDataPodById(id);
    }

//    update data pod

    @PutMapping("/data-pods/{id}")
    public DataPod update(@PathVariable UUID id, DataPod dataPod) throws ProvenAiException {

        return dataPodService.updateDataPod( dataPod);
    }




}
