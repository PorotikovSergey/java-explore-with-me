package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.model.Compilation;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.storage.CompilationRepository;
import ru.practicum.explore.storage.EventRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationService {
    private static final String COMPILATION_NOT_FOUND = "Подборки по данному id нет в базе";
    private static final String EVENT_NOT_FOUND = "События по данному id нет в базе";

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public List<Compilation> getCompilationsPublic(Boolean pinned, Integer from, Integer size) {
        List<Compilation> list = compilationRepository.findAllByPinned(pinned);
        return getPageableList(list, from, size);
    }

    public Compilation getCompilationByIdPublic(long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND));
    }

    public Compilation postCompilationAdmin(Compilation compilation) {
        compilationRepository.save(compilation);
        return compilation;
    }

    public void deleteCompilationAdmin(long compId) {
        try {
            compilationRepository.deleteById(compId);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(COMPILATION_NOT_FOUND);
        }
    }

    public void deleteEventFromCompilationAdmin(long compId, long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

        compilation.getEventList().remove(event);
        compilationRepository.save(compilation);
    }

    public void addEventToCompilationAdmin(long compId, long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

        compilation.getEventList().add(event);
        compilationRepository.save(compilation);
    }

    public void unpinCompilationAdmin(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND));

        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    public void pinCompilationAdmin(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND));

        compilation.setPinned(true);
        compilationRepository.save(compilation);
}

    private List<Compilation> getPageableList(List<Compilation> list, int from, int size) {
        PagedListHolder<Compilation> page = new PagedListHolder<>(list.subList(from, list.size()));
        page.setPageSize(size);
        page.setPage(0);
        return page.getPageList();
    }
}
