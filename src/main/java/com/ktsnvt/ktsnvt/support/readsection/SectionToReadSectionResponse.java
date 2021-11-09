package com.ktsnvt.ktsnvt.support.readsection;

import com.ktsnvt.ktsnvt.dto.readsection.ReadSectionResponse;
import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SectionToReadSectionResponse extends AbstractConverter<Section, ReadSectionResponse> {

    @Override
    public ReadSectionResponse convert(@NonNull Section response) {
        return getModelMapper().map(response, ReadSectionResponse.class);
    }
}
