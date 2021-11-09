package com.ktsnvt.ktsnvt.dto.readfoodanddrinkrequests;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ReadFoodAndDrinkRequestResponse {

    private Integer id;

    private Integer amount;

    private LocalDateTime sentAt;

    private LocalDateTime takenAt;

    private String item;

    private String preparedBy;

}
