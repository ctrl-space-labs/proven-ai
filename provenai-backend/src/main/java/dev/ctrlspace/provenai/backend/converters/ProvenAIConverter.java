package dev.ctrlspace.provenai.backend.converters;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProvenAIConverter<Entity, DTO>  {

        DTO toDTO(Entity entity) throws Exception, JsonProcessingException;

        Entity toEntity(DTO dto) throws Exception;
    }


