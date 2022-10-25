package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventAndOwnerId(long eventId, long ownerId);
    Request findRequestByEventAndOwnerIdAndId(long eventId, long ownerId, long id);
    List<Request> findAllByRequester(long requesterId);
    Request findRequestByEventAndRequester(long eventId, long requesterId);
    List<Request> findAllByEvent(long eventId);
    Request findRequestByIdAndRequester(long id, long requesterId);
}
