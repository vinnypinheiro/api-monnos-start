package com.api.monnos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@Document(collection = "Planet")
@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
public class Planet {

    @Id
    private long id;

    @Indexed(unique = true)
    @NotNull(message = "O campo nome deve ser informado")
    private String nome;

    private String clima;

    private String terreno;

    private Integer filmesTotal;

}
