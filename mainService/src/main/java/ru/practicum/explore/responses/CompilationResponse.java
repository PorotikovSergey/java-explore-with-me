package ru.practicum.explore.responses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.dto.CompilationDto;
import ru.practicum.explore.dto.NewCompilationDto;
import ru.practicum.explore.model.ApiError;
import ru.practicum.explore.model.Compilation;
import ru.practicum.explore.service.CompilationService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CompilationResponse {

    private final CompilationService compilationService;
    private final Mapper mapper;

    @Autowired
    public CompilationResponse(CompilationService compilationService, Mapper mapper) {
        this.compilationService = compilationService;
        this.mapper = mapper;
    }

    public ResponseEntity<Object> postCompilationAdmin(NewCompilationDto newCompilationDto) {
        Compilation compilation = mapper.fromNewDtoToCompilation(newCompilationDto);
        Compilation backCompilation = compilationService.postCompilationAdmin(compilation);
        if (backCompilation == null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        CompilationDto resultCompilationDto = mapper.fromCompilationToDto(backCompilation);
        return new ResponseEntity<>(resultCompilationDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteCompilationAdmin(long compId) {
        Compilation backCompilation = compilationService.deleteCompilationAdmin(compId);
        if(backCompilation==null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        CompilationDto compilationDto = mapper.fromCompilationToDto(backCompilation);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteEventFromCompilationAdmin(long compId, long eventId) {
        Compilation compilation = compilationService.deleteEventFromCompilationAdmin(compId, eventId);
        if (compilation != null) {
            CompilationDto compilationDto = mapper.fromCompilationToDto(compilation);
            return new ResponseEntity<>(compilationDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> addEventToCompilationAdmin(long compId, long eventId) {
        Compilation compilation = compilationService.addEventToCompilationAdmin(compId, eventId);
        if (compilation != null) {
            CompilationDto compilationDto = mapper.fromCompilationToDto(compilation);
            return new ResponseEntity<>(compilationDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> unpinCompilationAdmin(long compId) {
        Compilation compilation = compilationService.unpinCompilationAdmin(compId);
        if (compilation != null) {
            CompilationDto compilationDto = mapper.fromCompilationToDto(compilation);
            return new ResponseEntity<>(compilationDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> pinCompilationAdmin(long compId) {
        Compilation compilation = compilationService.pinCompilationAdmin(compId);
        if (compilation != null) {
            CompilationDto compilationDto = mapper.fromCompilationToDto(compilation);
            return new ResponseEntity<>(compilationDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getCompilationsPublic(Boolean pinned, Integer from, Integer size) {
        List<Compilation> list = compilationService.getCompilationsPublic(pinned, from, size);
        if(list.isEmpty()) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        List<CompilationDto> resultList = list.stream().map(mapper::fromCompilationToDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCompilationByIdPublic(long compId) {
        Compilation compilation = compilationService.getCompilationByIdPublic(compId);
        if(compilation==null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        CompilationDto compilationDto = mapper.fromCompilationToDto(compilation);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }
}
