package ru.practicum.explore.mapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.CompilationDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.dto.NewCompilationDto;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.model.Compilation;
import ru.practicum.explore.service.CompilationService;
import ru.practicum.explore.service.EventService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationMapping {
    private final CompilationService compilationService;
    private final EventService eventService;
    private final EventMapping eventMapping;

    //-----------------------------мапперы--------------------------------------------

    public CompilationDto fromCompilationToDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.isPinned());
        if (!compilation.getEventList().isEmpty()) {
            List<EventShortDto> list = compilation.getEventList().stream()
                    .map(eventMapping::fromEventToShortDto)
                    .collect(Collectors.toList());
            compilationDto.setEvents(list);
        } else {
            compilationDto.setEvents(Collections.emptyList());
        }
        return compilationDto;
    }

    public Compilation fromNewDtoToCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.isPinned());
        if (!newCompilationDto.getEvents().isEmpty()) {
            compilation.setEventList(eventService.getAllByIds(newCompilationDto.getEvents()));
            compilation.setEventList(eventService.getAllByIds(newCompilationDto.getEvents()));
        } else {
            compilation.setEventList(new ArrayList<>());
        }
        return compilation;
    }

    //------------------------------------------------------------------------------

    public CompilationDto postCompilationAdmin(NewCompilationDto newCompilationDto) {
        Compilation compilation = fromNewDtoToCompilation(newCompilationDto);

        Compilation backCompilation = compilationService.postCompilationAdmin(compilation);

        return fromCompilationToDto(backCompilation);
    }

    public void deleteCompilationAdmin(long compId) {
        compilationService.deleteCompilationAdmin(compId);
    }

    public void deleteEventFromCompilationAdmin(long compId, long eventId) {
        compilationService.deleteEventFromCompilationAdmin(compId, eventId);
    }

    public void addEventToCompilationAdmin(long compId, long eventId) {
        compilationService.addEventToCompilationAdmin(compId, eventId);
    }

    public void pinCompilationAdmin(long compId, boolean isPinned) {
        compilationService.pinCompilationAdmin(compId, isPinned);
    }

    public List<CompilationDto> getCompilationsPublic(Boolean pinned, Integer from, Integer size) {

        List<Compilation> list = compilationService.getCompilationsPublic(pinned, from, size);
        if (list.isEmpty()) {
            throw new NotFoundException("Список подборок пуст");
        }

        return list.stream().map(this::fromCompilationToDto).collect(Collectors.toList());
    }

    public CompilationDto getCompilationByIdPublic(long compId) {

        Compilation compilation = compilationService.getCompilationByIdPublic(compId);

        return fromCompilationToDto(compilation);
    }
}
