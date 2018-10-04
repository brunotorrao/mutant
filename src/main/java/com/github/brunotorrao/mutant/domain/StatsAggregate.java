package com.github.brunotorrao.mutant.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class StatsAggregate {
    @Id
    private Boolean mutant;
    private Integer count;
}
