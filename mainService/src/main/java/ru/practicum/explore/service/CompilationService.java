package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> result = compilationRepository.findAllByPinned(pinned, pageable).getContent();
        log.info("Из бд получен следующий лист подборок: {}", result);
        return result;
    }

    public Compilation getCompilationByIdPublic(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND));
        log.info("Из бд получена следующая подборка: {}", compilation);
        return compilation;
    }

    public Compilation postCompilationAdmin(Compilation compilation) {
        compilationRepository.save(compilation);
        log.info("В бд сохранена следующая подборка: {}", compilation);
        return compilation;
    }

    public void deleteCompilationAdmin(long compId) {
        compilationRepository.deleteById(compId);
        log.info("Из бд удалена подборка с id: {}", compId);
    }

    public void deleteEventFromCompilationAdmin(long compId, long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

        compilation.getEventList().remove(event);
        compilationRepository.save(compilation);
        log.info("После удаления события из подборки, она выглядит так: {}", compilation);
    }

    public void addEventToCompilationAdmin(long compId, long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

        compilation.getEventList().add(event);
        compilationRepository.save(compilation);
        log.info("После добавления события в подборку, она выглядит так: {}", compilation);
    }

    public void pinCompilationAdmin(long compId, boolean isPinned) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND));

        compilation.setPinned(isPinned);
        compilationRepository.save(compilation);
        log.info("После прикрепления/открепления подборки, она выглядит так: {}", compilation);
    }
}
