package ru.practicum.explore.model;

import lombok.*;
import ru.practicum.explore.dto.UserShortDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "state")
    private String state = EventState.PENDING.toString();

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private User owner;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @Column(name = "confirmed_requests")
    private long confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @OneToMany(mappedBy = "event")
    private List<Request> requests;

    @Transient
    private UserShortDto initiator;

    @Transient
    private Location location;

    @Column(name = "location_id")
    private long locationId;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private long participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(name = "views")
    private long views;

    @Column(name = "title")
    private String title;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "description")
    private String description;
}
