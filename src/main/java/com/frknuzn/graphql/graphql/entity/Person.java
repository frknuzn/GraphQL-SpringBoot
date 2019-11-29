package com.frknuzn.graphql.graphql.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {

    @Id
    private Long id;
    private String name;
    private String mobile;
    private String email;
    private String[] address;
}
