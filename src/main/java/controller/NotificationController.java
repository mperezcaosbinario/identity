package controller;

import controller.requests.NotificationRequest;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import service.NotificationService;

@Path("/notification")
public class NotificationController {

    private static final Logger logger = Logger.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    @Inject
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @POST
    @WithSession
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createNotification(@Valid NotificationRequest notificationRequest) {
        logger.info("Creating new notification...");
        return notificationService.createNotification(notificationRequest)
                .map(notification -> Response.ok(notification).build())
                .onFailure().invoke(throwable -> logger.error("Failed creating notification: " + throwable.getMessage()));
    }

    @DELETE
    @WithSession
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteNotification(
            @QueryParam("sender") String sender,
            @QueryParam("receiver") String receiver
            ) {
        logger.info("Deleting notification...");
        return notificationService.deleteNotification(sender, receiver)
                .map(notification -> Response.ok(notification).build())
                .onFailure().invoke(throwable -> logger.error("Failed deleting notification: " + throwable.getMessage()));
    }

    @DELETE
    @Path("/notification-by-id")
    @WithSession
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteNotificationById(
            @QueryParam("id") Long id
    ) {
        logger.info("Deleting notification...");
        return notificationService.deleteNotification(id)
                .map(notification -> {
                    logger.info("valor: " + notification);
                    return Response.ok(notification).build();
                })
                .onFailure().invoke(throwable -> logger.error("Failed deleting notification: " + throwable.getMessage()));
    }

    @GET
    @WithSession
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getNotification(@QueryParam("id") Long idNotification) {
        logger.info("Getting notification...");
        return notificationService.getNotification(idNotification)
                .map(notification -> Response.ok(notification.getId()).build());
                //.onFailure()
                //.invoke(throwable -> logger.error("Failed getting notification: " + throwable.getMessage()));
                //.invoke(x -> {throw new NullPointerException("ESTO ES UN NULL");});
    }

    @PUT
    @WithSession
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateNotification(@QueryParam("sender") String sender,
                                            @QueryParam("new_sender") String newSender){
        logger.info("Updating notification...");
        return notificationService.updateNotification(sender, newSender).map(notification -> Response.ok(notification).build())
                .onFailure().invoke(throwable -> logger.error("Failed getting notification: " + throwable.getMessage()));
    }
}
