package com.bilgeadam.basurveyapp.repositories.base;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Eralp Nitelik
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, Oid> extends JpaRepository<T, Oid> {

    /*
        Find Methods For Active Entities
     */


    @Query("SELECT t FROM #{#entityName} t WHERE t.state = 'ACTIVE' AND t.oid = ?1")
    Optional<T> findActiveById(@NonNull Oid oid);




    @Query("SELECT t FROM #{#entityName} t WHERE t.state = 'ACTIVE' ORDER BY t.updatedAt DESC")
    List<T> findAllActive();

    @Query("SELECT t FROM #{#entityName} t WHERE t.state = 'ACTIVE' ORDER BY t.updatedAt DESC")
    Page<T> findAllActive(@NonNull Pageable pageable);

    /*
        Find Methods For Deleted Entities
     */

    @Query("SELECT t FROM #{#entityName} t WHERE t.state = 'DELETED' AND t.oid = ?1")
    Optional<T> findDeletedById(Oid oid);

    @Query("SELECT t FROM #{#entityName} t WHERE t.state = 'DELETED' ORDER BY t.updatedAt DESC")
    List<T> findAllDeleted();

    @Query("SELECT t FROM #{#entityName} t WHERE t.state = 'DELETED' ORDER BY t.updatedAt DESC")
    Page<T> findAllDeleted(Pageable pageable);

    /*
        Modified Delete Methods
     */


    default boolean softDeleteById(Oid oid){
        T entity = findActiveById(oid).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        entity.setState(State.DELETED);
        save(entity);
        return true;
    };

    default boolean activeById(Oid oid){
        T entity = findDeletedById(oid).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        entity.setState(State.ACTIVE);
        save(entity);
        return true;
    };

}
