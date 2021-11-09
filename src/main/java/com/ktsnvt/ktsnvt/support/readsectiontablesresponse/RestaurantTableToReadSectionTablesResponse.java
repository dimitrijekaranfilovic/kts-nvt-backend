package com.ktsnvt.ktsnvt.support.readsectiontablesresponse;

import com.ktsnvt.ktsnvt.dto.readsectiontablesresponse.ReadSectionTablesResponse;
import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class RestaurantTableToReadSectionTablesResponse extends AbstractConverter<RestaurantTable, ReadSectionTablesResponse> {
    @Override
    public ReadSectionTablesResponse convert(RestaurantTable response) {
        return getModelMapper().map(response, ReadSectionTablesResponse.class);
    }
}
