package com.tripdeio.backend.controller;

import com.tripdeio.backend.dto.TransportSegmentDTO;
import com.tripdeio.backend.entity.TransportSegment;
import com.tripdeio.backend.service.TransportSegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transport-segments")
public class TransportSegmentController {
    @Autowired
    private TransportSegmentService transportSegmentService;

    @PostMapping
    public ResponseEntity<TransportSegment> createTransportSegment(@RequestBody TransportSegmentDTO segmentDTO) {
        TransportSegment segment = transportSegmentService.createTransportSegment(segmentDTO);
        return ResponseEntity.ok(segment);
    }
}