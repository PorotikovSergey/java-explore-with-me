package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventIdAndEventOwnerId(long eventId, long ownerId);

    Request findRequestByEventIdAndEventOwnerIdAndId(long eventId, long ownerId, long id);

    List<Request> findAllByRequesterId(long requesterId);

    Request findRequestByEventIdAndRequesterId(long eventId, long requesterId);

    List<Request> findAllByEventId(long eventId);

    Request findRequestByIdAndRequesterId(long id, long requesterId);
}
