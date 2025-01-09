package com.example.danjamserver.util.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public abstract class MongoBaseEntity implements Serializable {

    @CreatedDate
    @Field("createdDateTime")
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Field("modifiedDateTime")
    private LocalDateTime modifiedDateTime;
}
