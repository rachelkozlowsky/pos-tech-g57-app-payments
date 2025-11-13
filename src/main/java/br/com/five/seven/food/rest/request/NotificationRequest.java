package br.com.five.seven.food.rest.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
        private String id;

        @JsonAlias("live_mode")
        private boolean liveMode;

        @NotNull(message = "Type é obrigatório; value: payment")
        private String type;

        @JsonAlias("date_created")
        private String dateCreated;

        @JsonAlias("user_id")
        private String userId;

        @JsonAlias("api_version")
        private String apiVersion;
        
        @NotNull(message = "Action é obrigatório; value: payment.updated")
        private String action;

        @Valid
        private NotificationDataRequest data;

}
