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

    @Column(name = "event_owner_id")
    private long ownerId;

    @Column(name = "event")
    private long event;

    @Column(name = "requester")
    private long requester;

    @Column(name = "create_on")
    private LocalDateTime createOn;

    @Column(name = "status")
    private String status;
}
