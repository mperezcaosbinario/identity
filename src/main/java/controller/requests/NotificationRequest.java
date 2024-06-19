package controller.requests;

import jakarta.validation.constraints.NotBlank;

public record NotificationRequest(
        @NotBlank(message="sender may not be blank") String sender,
        @NotBlank(message="receiver may not be blank") String receiver,
        @NotBlank(message="message may not be blank") String message
){}

