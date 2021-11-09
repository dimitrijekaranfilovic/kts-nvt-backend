package com.ktsnvt.ktsnvt.support.createsection;

import com.ktsnvt.ktsnvt.dto.createsection.CreateSectionResponse;
import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SectionToCreateSectionResponse extends AbstractConverter<Section, CreateSectionResponse> {
    @Override
    public CreateSectionResponse convert(@NonNull Section source) {
        return getModelMapper().map(source, CreateSectionResponse.class);
    }
}
