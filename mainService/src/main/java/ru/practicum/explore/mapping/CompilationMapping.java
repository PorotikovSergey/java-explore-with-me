package ru.practicum.explore.mapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.CompilationDto;
import ru.practicum.explore.dto.NewCompilationDto;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.model.Compilation;
import ru.practicum.explore.service.CompilationService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationMapping {
    private final CompilationService compilationService;
    private final Mapper mapper;

    public CompilationDto postCompilationAdmin(NewCompilationDto newCompilationDto) {
        Compilation compilation = mapper.fromNewDtoToCompilation(newCompilationDto);

        Compilation backCompilation = compilationService.postCompilationAdmin(compilation);

        return mapper.fromCompilationToDto(backCompilation);
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

    public void unpinCompilationAdmin(long compId) {
        compilationService.unpinCompilationAdmin(compId);
    }

    public void pinCompilationAdmin(long compId) {
        compilationService.pinCompilationAdmin(compId);
    }

    public List<CompilationDto> getCompilationsPublic(Boolean pinned, Integer from, Integer size) {

        List<Compilation> list = compilationService.getCompilationsPublic(pinned, from, size);
        if (list.isEmpty()) {
            throw new NotFoundException("Список подборок пуст");
        }

        return list.stream().map(mapper::fromCompilationToDto).collect(Collectors.toList());
    }

    public CompilationDto getCompilationByIdPublic(long compId) {

        Compilation compilation = compilationService.getCompilationByIdPublic(compId);

        return mapper.fromCompilationToDto(compilation);
    }
}
