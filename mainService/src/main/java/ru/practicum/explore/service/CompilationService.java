package ru.practicum.explore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.Compilation;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.storage.CompilationRepository;
import ru.practicum.explore.storage.EventRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationService(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    public List<Compilation> getCompilationsPublic(Boolean pinned, Integer from, Integer size) {
        List<Compilation> list = compilationRepository.findAllByPinned(pinned);
        List<Compilation> afterPageableList = getPageableList(list, from, size);
        return afterPageableList;
    }

    public Compilation getCompilationByIdPublic(long compId) {
        Optional<Compilation> optional = compilationRepository.findById(compId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public Compilation postCompilationAdmin(Compilation compilation) {
        compilationRepository.save(compilation);
        return compilation;
    }

    public Compilation deleteCompilationAdmin(long compId) {
        Optional<Compilation> optional = compilationRepository.findById(compId);
        if (optional.isPresent()) {
            Compilation compilation = optional.get();
            compilationRepository.deleteById(compId);
            return compilation;
        } else {
            return null;
        }
    }

    public Compilation deleteEventFromCompilationAdmin(long compId, long eventId) {
        Optional<Compilation> optional = compilationRepository.findById(compId);
        Optional<Event> optional2 = eventRepository.findById(eventId);
        if (optional.isPresent() && optional2.isPresent()) {
            Compilation compilation = optional.get();
            compilation.getEventList().remove(optional2.get());
            compilation.getEventList().remove(optional2.get());
            compilationRepository.save(compilation);
            return compilation;
        } else {
            return null;
        }
    }

    public Compilation addEventToCompilationAdmin(long compId, long eventId) {
        Optional<Compilation> optional = compilationRepository.findById(compId);
        Optional<Event> optional2 = eventRepository.findById(eventId);
        if (optional.isPresent() && optional2.isPresent()) {
            Compilation compilation = optional.get();
            compilation.getEventList().add(optional2.get());
            compilation.getEventList().add(optional2.get());
            compilationRepository.save(compilation);
            return compilation;
        } else {
            return null;
        }
    }

    public Compilation unpinCompilationAdmin(long compId) {
        Optional<Compilation> optional = compilationRepository.findById(compId);
        if (optional.isPresent()) {
            Compilation compilation = optional.get();
            compilation.setPinned(false);
            compilationRepository.save(compilation);
            return compilation;
        } else {
            return null;
        }
    }

    public Compilation pinCompilationAdmin(long compId) {
        Optional<Compilation> optional = compilationRepository.findById(compId);
        if (optional.isPresent()) {
            Compilation compilation = optional.get();
            compilation.setPinned(true);
            compilationRepository.save(compilation);
            return compilation;
        } else {
            return null;
        }
    }

    private List<Compilation> getPageableList(List<Compilation> list, int from, int size) {
        PagedListHolder<Compilation> page = new PagedListHolder<>(list.subList(from, list.size()));
        page.setPageSize(size);
        page.setPage(0);
        return page.getPageList();
    }
}
