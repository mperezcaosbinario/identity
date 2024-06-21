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

    default Uni<Long> deleteByQuery(String query, Object... params) {
        return list(query + " and deletedAt is null", params)
                .flatMap(entities -> {
                    long countDeleted = 0;
                    Uni<Void> result = Uni.createFrom().voidItem();
                    for (T entity : entities) {
                        if (!entity.isDeleted()) {
                            entity.softDelete();
                            result = result.chain(ignore -> persistAndFlush(entity).replaceWithVoid());
                            countDeleted++;
                        }
                    }
                    return result.replaceWith(countDeleted);
                });
    }

    default Uni<List<T>> findAllNotDeleted() {
        return list("deletedAt is null");
    }

    default Uni<T> findById(Long id) {
        return find("id = ?1 and deletedAt is null", id).firstResult();
    }

    default Uni<List<T>> findNotDeleted(String query, Object... params) {
        String newQuery = query + " and deletedAt is null";
        return list(newQuery, params);
    }

    default Uni<T> findOneNotDeleted(String query, Object... params) {
        String newQuery = query + " and deletedAt is null";
        return find(newQuery, params).firstResult();
    }

    default Uni<List<T>> findByFieldNotDeleted(String fieldName, Object value) {
        return list(fieldName + " = ?1 and deletedAt is null", value);
    }

    default Uni<T> findByFieldSingleResultNotDeleted(String fieldName, Object value) {
        return find(fieldName + " = ?1 and deletedAt is null", value).firstResult();
    }

}
