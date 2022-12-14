package ru.practicum.explore.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participant_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_owner", referencedColumnName = "id", nullable = false)
    private User eventOwner;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event", referencedColumnName = "id", nullable = false)
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requester", referencedColumnName = "id", nullable = false)
    private User requester;

    @Column(name = "created_on")
    private LocalDateTime createOn;

    @Column(name = "status")
    private String status;
}
