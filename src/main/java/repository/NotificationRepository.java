package repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import repository.model.NotificationEntity;


@ApplicationScoped
public class NotificationRepository implements PanacheRepository<NotificationEntity> {
    @Override
    public Uni<Void> delete(NotificationEntity notificationEntity) {
        //Implementar con el update en la bdd
        return PanacheRepository.super.delete(notificationEntity);
    }
}
