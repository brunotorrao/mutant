package com.github.brunotorrao.mutant.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mutant {
    
    @Id
    private String id;
    private List<String> dna;
    private Integer calls;
    private boolean isMutant;
}
