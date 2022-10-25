package ru.practicum.explore.model;

import lombok.*;
import ru.practicum.explore.dto.UserShortDto;

import javax.persistence.*;

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

    @Column(name = "owner_id")
    private long ownerId;

    @Column(name = "category_id")
    private long categoryId;

    @Column(name = "confirmed_requests")
    private long confirmedRequests;

    @Column(name = "created_on")
    private String createdOn;

    @Column(name = "event_date")
    private String eventDate;

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
    private String publishedOn;

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
