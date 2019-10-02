package pl.poul12.matchzone.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Rating {
    private Long id;
    private Long countedVotes;
    private Double sumOfVotes;
}
