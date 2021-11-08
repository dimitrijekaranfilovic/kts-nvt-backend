package com.ktsnvt.ktsnvt.dto.readFoodAndDrinkRequests;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ReadFoodAndDrinkRequestResponse {

    private Integer id;

    private Integer amount;

    private LocalDateTime sentAt;

    private String item;

}
