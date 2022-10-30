package ru.practicum.explore.responses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.apierrors.ForbiddenError;
import ru.practicum.explore.apierrors.NotFoundApiError;
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
public class CompilationResponse {

    private final CompilationService compilationService;
    private final Mapper mapper;

    public ResponseEntity<Object> postCompilationAdmin(NewCompilationDto newCompilationDto) {
        Compilation compilation = mapper.fromNewDtoToCompilation(newCompilationDto);
        Compilation backCompilation;
        try {
            backCompilation = compilationService.postCompilationAdmin(compilation);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("compilation"), HttpStatus.FORBIDDEN);
        }
//
//        if (backCompilation == null) {
//            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilation"), HttpStatus.NOT_FOUND);
//        }

        CompilationDto resultCompilationDto = mapper.fromCompilationToDto(backCompilation);
        return new ResponseEntity<>(resultCompilationDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteCompilationAdmin(long compId) {
        Compilation backCompilation;
        try {
            backCompilation = compilationService.deleteCompilationAdmin(compId);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilation"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("compilation"), HttpStatus.FORBIDDEN);
        }
//
//        if (backCompilation == null) {
//            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilation"), HttpStatus.NOT_FOUND);
//        }

        CompilationDto compilationDto = mapper.fromCompilationToDto(backCompilation);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteEventFromCompilationAdmin(long compId, long eventId) {
        Compilation compilation;
        try {
            compilation = compilationService.deleteEventFromCompilationAdmin(compId, eventId);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilation"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("compilation"), HttpStatus.FORBIDDEN);
        }
//
//        if (compilation == null) {
//            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilation"), HttpStatus.NOT_FOUND);
//        }

        CompilationDto compilationDto = mapper.fromCompilationToDto(compilation);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> addEventToCompilationAdmin(long compId, long eventId) {
        Compilation compilation;
        try {
            compilation = compilationService.addEventToCompilationAdmin(compId, eventId);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilation"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("compilation"), HttpStatus.FORBIDDEN);
        }
//
//        if (compilation == null) {
//            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilation"), HttpStatus.NOT_FOUND);
//        }

        CompilationDto compilationDto = mapper.fromCompilationToDto(compilation);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> unpinCompilationAdmin(long compId) {
        Compilation compilation;
        try {
            compilation = compilationService.unpinCompilationAdmin(compId);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilation"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("compilation"), HttpStatus.FORBIDDEN);
        }
//
//        if (compilation == null) {
//            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilation"), HttpStatus.NOT_FOUND);
//        }

        CompilationDto compilationDto = mapper.fromCompilationToDto(compilation);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> pinCompilationAdmin(long compId) {
        Compilation compilation;
        try {
            compilation = compilationService.pinCompilationAdmin(compId);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilation"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("compilation"), HttpStatus.FORBIDDEN);
        }

        CompilationDto compilationDto = mapper.fromCompilationToDto(compilation);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCompilationsPublic(Boolean pinned, Integer from, Integer size) {
        List<Compilation> list;
        try {
            list = compilationService.getCompilationsPublic(pinned, from, size);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("compilations"), HttpStatus.FORBIDDEN);
        }

        if (list.isEmpty()) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilations"), HttpStatus.NOT_FOUND);
        }

        List<CompilationDto> resultList = list.stream().map(mapper::fromCompilationToDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCompilationByIdPublic(long compId) {
        Compilation compilation;
        try {
            compilation = compilationService.getCompilationByIdPublic(compId);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("compilation"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("compilation"), HttpStatus.FORBIDDEN);
        }

        CompilationDto compilationDto = mapper.fromCompilationToDto(compilation);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }
}
