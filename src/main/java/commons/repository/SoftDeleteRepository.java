package commons.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface SoftDeleteRepository<T extends SoftDeleteEntity> extends PanacheRepositoryBase<T, Long> {

    @Override
    default Uni<Void> delete(T entity) {
        if (entity != null && !entity.isDeleted()) {
            entity.softDelete();  // Marca la entidad como eliminada
            return persist(entity).replaceWithVoid();  // Persiste la entidad y devuelve Uni<Void>
        }
        return Uni.createFrom().voidItem();
    }

    default Uni<Boolean> deleteById(Long id) {
        return findById(id)
                .flatMap(entity -> entity != null ? delete(entity).map(x -> true) : Uni.createFrom().item(true));
    }

    default Uni<List<T>> findAllNotDeleted() {
        return list("deletedAt is null");
    }
}
