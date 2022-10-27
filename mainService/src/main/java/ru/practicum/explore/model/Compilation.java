package ru.practicum.explore.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Compilation {

    @Column(name = "events")
    private String events = "";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "pinned")
    private boolean pinned;
}
