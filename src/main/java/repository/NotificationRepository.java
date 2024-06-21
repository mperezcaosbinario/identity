package repository;

import commons.repository.SoftDeleteRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import repository.model.NotificationEntity;

import java.util.List;

@ApplicationScoped
public class NotificationRepository implements SoftDeleteRepository<NotificationEntity> {
    @Override
    public Uni<Void> delete(NotificationEntity entity) {
        return SoftDeleteRepository.super.delete(entity);
    }

    @Override
    public Uni<Long> deleteByQuery(String query, Object... params){ return SoftDeleteRepository.super.deleteByQuery(query, params);}

    @Override
    public Uni<Boolean> deleteById(Long id) {
        return SoftDeleteRepository.super.deleteById(id);
    }

    @Override
    public Uni<List<NotificationEntity>> findAllNotDeleted() {
        return SoftDeleteRepository.super.findAllNotDeleted();
    }

    @Override
    public Uni<NotificationEntity> findById(Long id){ return SoftDeleteRepository.super.findById(id); }

}
