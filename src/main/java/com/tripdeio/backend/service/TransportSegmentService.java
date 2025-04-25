package com.tripdeio.backend.service;

import com.tripdeio.backend.dto.TransportSegmentDTO;
import com.tripdeio.backend.entity.TransportMethod;
import com.tripdeio.backend.entity.TransportSegment;
import com.tripdeio.backend.repository.TransportSegmentRepository;

import org.springframework.stereotype.Service;

@Service
public class TransportSegmentService {
    private final TransportSegmentRepository transportSegmentRepository;

    public TransportSegmentService(TransportSegmentRepository transportSegmentRepository) {
        this.transportSegmentRepository = transportSegmentRepository;
    }

    public TransportSegment createTransportSegment(TransportSegmentDTO segmentDTO) {
        TransportSegment segment = new TransportSegment();
        segment.setEstimatedMinutes(segmentDTO.getEstimatedMinutes());
        segment.setEstimatedPrice(segmentDTO.getEstimatedPrice());
        segment.setFromStopId(segmentDTO.getFromStopId());
        segment.setToStopId(segmentDTO.getToStopId());
        segment.setFromStopType(segmentDTO.getFromStopType());
        segment.setToStopType(segmentDTO.getToStopType());
        if (segmentDTO.getTransportMethodId() != null) {
            TransportMethod method = new TransportMethod();
            method.setId(segmentDTO.getTransportMethodId());
            segment.setTransportMethod(method);
        }
        return transportSegmentRepository.save(segment);
    }
}