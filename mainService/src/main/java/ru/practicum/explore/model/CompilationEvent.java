package ru.practicum.explore.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "compilation_event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompilationEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "compilation_id")
    Long compilationId;

    @Column(name = "event_id")
    Long eventId;
}
