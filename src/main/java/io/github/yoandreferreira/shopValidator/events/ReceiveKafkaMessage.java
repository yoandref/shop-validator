package io.github.yoandreferreira.shopValidator.events;

import com.google.gson.Gson;
import io.github.yoandreferreira.shopValidator.dto.ShopDTO;
import io.github.yoandreferreira.shopValidator.dto.ShopItemDTO;
import io.github.yoandreferreira.shopValidator.model.Product;
import io.github.yoandreferreira.shopValidator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiveKafkaMessage {
    private static final String SHOP_TOPIC_NAME = "SHOP_TOPIC";
    private static final String SHOP_TOPIC_EVENT_NAME = "SHOP_TOPIC_EVENT";
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ShopDTO> kafkaTemplate;

    @KafkaListener(topics = SHOP_TOPIC_NAME, groupId = "group")
    public void listenShopTopic(String msg) {
        ShopDTO shopDTO = new Gson().fromJson(msg, ShopDTO.class);
        try {
            log.info("Compra recebida no tópico: {}.", shopDTO.getIdentifier());
            boolean sucess = Boolean.TRUE;
            for(ShopItemDTO item : shopDTO.getItems()) {
                productRepository.findByIdentifier(item.getProductIdentifier());
            }
        } catch (Exception ex) {
             log.error("Erro no processamento da compra {}", shopDTO.getIdentifier());
        }
    }

    private boolean isValidShop(ShopItemDTO item, Product product) {
        return product != null && product.getAmount() >= item.getAmount();
    }
    //Envia uma mensagem p/ kafka indicando erro na compra
    private void shopError(ShopDTO shopDTO) {
        log.info("Erro no processamento da compra {}.", shopDTO.getIdentifier());
        shopDTO.setStatus("ERROR");
        kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDTO);
    }
    //Envia uma mensagem p/ kafka indicando sucesso na compra
    private void shopSucesso(ShopDTO shopDTO) {
        log.info("Erro no processamento da compra {}.", shopDTO.getIdentifier());
        shopDTO.setStatus("SUCESSO");
        kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDTO);
    }
}
