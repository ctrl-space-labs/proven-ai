package dev.ctrlspace.provenai.backend.converters;

import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.dtos.DataPodDTO;
import dev.ctrlspace.provenai.backend.model.dtos.DataPodPublicDTO;
import org.springframework.stereotype.Component;

@Component
public class DataPodConverter implements ProvenAIConverter<DataPod, DataPodDTO>{

    @Override
    public DataPodDTO toDTO(DataPod dataPod) {
        DataPodDTO dataPodDTO = new DataPodDTO();

        dataPodDTO.setId(dataPod.getId());
        dataPodDTO.setOrganizationId(dataPod.getOrganizationId());
        dataPodDTO.setPodUniqueName(dataPod.getPodUniqueName());
        dataPodDTO.setCreatedAt(dataPod.getCreatedAt());
        dataPodDTO.setUpdatedAt(dataPod.getUpdatedAt());
        dataPodDTO.setHostUrl(dataPod.getHostUrl());
        dataPodDTO.setCreatedBy(dataPod.getCreatedBy());
        dataPodDTO.setUpdatedBy(dataPod.getUpdatedBy());

        return dataPodDTO;

    }

    @Override
    public DataPod toEntity(DataPodDTO dataPodDTO) {
        DataPod dataPod = new DataPod();

        dataPod.setId(dataPodDTO.getId());
        dataPod.setOrganizationId(dataPodDTO.getOrganizationId());
        dataPod.setPodUniqueName(dataPodDTO.getPodUniqueName());
        dataPod.setHostUrl(dataPodDTO.getHostUrl());
        dataPod.setCreatedAt(dataPodDTO.getCreatedAt());
        dataPod.setUpdatedAt(dataPodDTO.getUpdatedAt());
        dataPod.setCreatedBy(dataPodDTO.getCreatedBy());
        dataPod.setUpdatedBy(dataPodDTO.getUpdatedBy());

        return dataPod;
    }


    public DataPodPublicDTO toPublicDTO(DataPod dataPod) {
        return DataPodPublicDTO.builder()
                .id(dataPod.getId())
                .podUniqueName(dataPod.getPodUniqueName())
                .organizationId(dataPod.getOrganizationId())
                .build();
    }


}
