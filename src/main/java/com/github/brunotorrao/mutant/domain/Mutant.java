package com.github.brunotorrao.mutant.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import static java.lang.String.join;

@Data
@Document(collection = "mutants")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mutant {
    
    @Id
    private Integer id;
    private List<String> dna;
    private Boolean mutant;
    
    public Boolean isMutant() {
        return mutant;
    }
    
    public void fillId() {
        this.id = join("", this.dna).hashCode();
    }
}
