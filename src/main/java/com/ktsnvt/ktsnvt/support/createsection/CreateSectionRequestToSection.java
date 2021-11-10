package com.ktsnvt.ktsnvt.support.createsection;

import com.ktsnvt.ktsnvt.dto.createsection.CreateSectionRequest;
import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CreateSectionRequestToSection extends AbstractConverter<CreateSectionRequest, Section> {
    @Override
    public Section convert(@NonNull CreateSectionRequest source) {
        return getModelMapper().map(source, Section.class);
    }
}
