package com.api.monnos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@Document(collection = "Counter")
@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
public class Counter {

    @Id
    private String id;
    private long seq;
}
