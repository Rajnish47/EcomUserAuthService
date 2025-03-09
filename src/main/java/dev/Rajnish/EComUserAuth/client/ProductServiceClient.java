package dev.Rajnish.EComUserAuth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import dev.Rajnish.EComUserAuth.dto.CreateCartRequestDTO;


@Component
public class ProductServiceClient {

    @Value("${product.api.url}")
    private String productServiceBaseURL;
    @Value("${product.api.cart.add}")
    private String productServiceNewCartPath;

    public void createNewCart(CreateCartRequestDTO createCartRequestDTO)
    {
        WebClient webClient = WebClient.builder().build();
        String createCartUrl = productServiceBaseURL.concat(productServiceNewCartPath);
        System.out.println(createCartUrl);
        webClient.post().uri(createCartUrl).bodyValue(createCartRequestDTO).retrieve().bodyToMono(Void.class).subscribe();
    }    
}
