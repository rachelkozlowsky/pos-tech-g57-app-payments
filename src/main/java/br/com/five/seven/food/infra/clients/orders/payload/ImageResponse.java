package br.com.five.seven.food.infra.clients.orders.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {
    private String url;

    public static List<ImageResponse> fromDomainList(List<Image> images) {
        return images.stream()
                .map(ImageResponse::fromDomain)
                .toList();
    }

    private static ImageResponse fromDomain(Image image) {
        ImageResponse response = new ImageResponse();
        response.setUrl(image.getUrl());
        return response;
    }
}
