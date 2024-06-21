package service;

import controller.requests.NotificationRequest;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.SqlResult;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import repository.NotificationRepository;
import repository.model.NotificationEntity;

@ApplicationScoped
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MySQLPool client;

    @Inject
    public NotificationService(NotificationRepository notificationRepository, MySQLPool client) {
        this.notificationRepository = notificationRepository;
        this.client = client;
    }

    private static final Logger logger = Logger.getLogger(NotificationService.class);

    @Transactional
    public Uni<NotificationEntity> insert(NotificationEntity newNotification) {
        return notificationRepository.persistAndFlush(newNotification)
                .invoke(token -> logger.info("Saving new notification record..."));
    }

    public Uni<Long> createNotification(NotificationRequest notificationRequest) {
        NotificationEntity notificationEntity = new NotificationEntity(
                notificationRequest.sender(),
                notificationRequest.receiver(),
                notificationRequest.message()
        );
        return insert(notificationEntity)
                .map(NotificationEntity::getId);
    }

    @Transactional
    public Uni<Long> deleteNotification(String sender, String receiver){
        return notificationRepository.deleteByQuery("sender = ?1 and receiver = ?2", sender, receiver);
    }

    @Transactional
    public Uni<Boolean> deleteNotification(Long id){
        return notificationRepository.deleteById(id);
    }

    /*
    @Transactional
    public Uni<Integer> deleteNotifications(Long id){
        return client.preparedQuery("DELETE FROM notification WHERE id = ?")
                .execute(Tuple.of(id))
                .onItem().transform(SqlResult::rowCount);
    }

     */

    public Uni<NotificationEntity> getNotification(Long id){
        return notificationRepository.findById(id);
    }

    public Uni<Integer> updateNotification(String sender, String newSender){
        return notificationRepository.update("sender = ?1 where sender = ?2", newSender, sender);
    }

}
