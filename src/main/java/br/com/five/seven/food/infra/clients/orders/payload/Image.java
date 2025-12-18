package br.com.five.seven.food.infra.clients.orders.payload;


public class Image {

    private String url;

    public Image(String url) {
        this.url = url;
    }

    public Image() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageResponse toResponse() {
        ImageResponse response = new ImageResponse();
        response.setUrl(this.url);
        return response;
    }
}
