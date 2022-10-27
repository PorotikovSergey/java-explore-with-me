package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventIdAndEventOwnerId(long eventId, long ownerId);

    List<Request> findAllByEventIdAndAndEventOwnerId(long eventId, long ownerId);

//    Request findRequestByEventAndOwnerIdAndId(long eventId, long ownerId, long id);

    Request findRequestByEventIdAndEventOwnerIdAndId(long eventId, long ownerId, long id);

    List<Request> findAllByRequester(long requesterId);

    Request findRequestByEventAndRequester(long eventId, long requesterId);

    Request findRequestByEventIdAndRequester(long eventId, long requesterId);

    List<Request> findAllByEvent(long eventId);

    List<Request> findAllByEventId(long eventId);

    Request findRequestByIdAndRequester(long id, long requesterId);
}
