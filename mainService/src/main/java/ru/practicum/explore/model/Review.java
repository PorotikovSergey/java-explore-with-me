package ru.practicum.explore.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author", referencedColumnName = "id", nullable = false)
    private User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event", referencedColumnName = "id", nullable = false)
    private Event event;

    @Column(name = "text")
    private String text;

    @Column(name = "event_rating")
    private long eventRating;
    
    @Column(name = "review_rating")
    private float commentRating;
    
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "counter")
    private long counter;
}
