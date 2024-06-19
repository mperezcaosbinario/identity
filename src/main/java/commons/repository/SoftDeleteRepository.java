package commons.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import java.util.List;

public interface SoftDeleteRepository<T extends SoftDeleteEntity> extends PanacheRepositoryBase<T, Long> {

    @Override
    default Uni<Void> delete(T entity) {
        Logger logger = Logger.getLogger(SoftDeleteRepository.class);
        if (entity != null && !entity.isDeleted()) {
            logger.info("Deleting record...");
            entity.softDelete();  // Marca la entidad como eliminada
            return persistAndFlush(entity).replaceWithVoid();  // Persiste la entidad y devuelve Uni<Void>
        }
        logger.info("Record doesnt exist...");
        return Uni.createFrom().voidItem();
    }

    @Override
    default Uni<Boolean> deleteById(Long id) {
        Logger logger = Logger.getLogger(SoftDeleteRepository.class);
        return findById(id)
                .flatMap(entity -> {
                    if(entity != null){
                        return delete(entity);
                    }else{
                        return Uni.createFrom().voidItem();
                    }
                }).map(x -> true);
    }

    default Uni<List<T>> findAllNotDeleted() {
        return list("deletedAt is null");
    }
}
