package com.example.springboot3server.demo.observed;

import com.example.commoninterface.file.FileCreationRequest;
import com.example.commoninterface.file.FileCreationResponse;
import com.example.springboot3server.util.FileCreationResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("observed/files")
@RequiredArgsConstructor
public class ObservedFileController {

    private final ObservedFileCreationService observedFileCreationService;

    @PostMapping
    public ResponseEntity<FileCreationResponse> postFile(@RequestBody final FileCreationRequest creationRequest) {
        final var file = observedFileCreationService.create(creationRequest);
        return ResponseEntity.ok(FileCreationResponseMapper.map(file));
    }

}
