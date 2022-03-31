package com.springboot.rest.api.server.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

/**
 * Common properties that all primary entities should have.
 * Primarily for auditing purposes.
 */

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditableEntity {

    /**
     * Timestamp of when this entity was created
     */
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * Timestamp of when this entity was last updated
     */
    @UpdateTimestamp
    private Timestamp lastModifiedAt;

    /**
     * Name of the user that created this entity
     */
    @CreatedBy
    private Integer createdByUserId;

    /**
     * Name of the user that last updated this entity
     */
    @LastModifiedBy
    private Integer lastModifiedByUserId;
}
