package io.github.yoandreferreira.shopValidator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShopDTO {
    private String identifier;
    private String[] dateShop;
    private String status;
    private List<ShopItemDTO> items;

    public ShopDTO() {
    }
}
