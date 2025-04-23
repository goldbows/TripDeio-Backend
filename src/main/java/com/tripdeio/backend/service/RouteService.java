package com.tripdeio.backend.service;

import com.graphhopper.ResponsePath;
import com.graphhopper.util.PointList;
import com.tripdeio.backend.dto.AttractionImageDTO;
import com.tripdeio.backend.dto.RouteResponseDTO;
import com.tripdeio.backend.dto.AttractionResponseDTO;
import com.tripdeio.backend.entity.Attraction;
import com.tripdeio.backend.repository.AttractionRepository;
import com.tripdeio.backend.utils.GraphHopperUtils;
import com.tripdeio.backend.utils.PolylineUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {
    @Autowired
    private GraphHopperService graphHopperService;

    @Autowired
    private AttractionRepository attractionRepository;

    public RouteResponseDTO findAttractionsNearRoute(double startLat, double startLng, double endLat, double endLng) {
        // Get route from GraphHopper
        ResponsePath route = graphHopperService.getRoute(startLat, startLng, endLat, endLng);

        // Convert route to WKT linestring
        PointList points = route.getPoints();
        StringBuilder wktBuilder = new StringBuilder("LINESTRING(");
        for (int i = 0; i < points.size(); i++) {
            if (i > 0) wktBuilder.append(",");
            wktBuilder.append(points.getLon(i)).append(" ").append(points.getLat(i));
        }
        wktBuilder.append(")");
        String routeWkt = wktBuilder.toString();

        // Query attractions near route using PostGIS
        List<Attraction> nearbyAttractions = attractionRepository.findNearRoute(routeWkt);

        // Convert to DTOs
        List<AttractionResponseDTO> nearbyAttractionResponseDTOS = new ArrayList<>();
        for (Attraction attraction : nearbyAttractions) {
            double attractionLat = attraction.getLat();
            double attractionLng = attraction.getLng();
            AttractionResponseDTO attractionResponseDTO = this.getAttractionResponseDTO(attraction, attractionLat, attractionLng);
            nearbyAttractionResponseDTOS.add(attractionResponseDTO);
        }

        // Sort by distance from start
        nearbyAttractionResponseDTOS.sort((a1, a2) -> {
            double distanceA1 = getDistanceFromRoute(startLat, startLng, a1.getLat(), a1.getLng());
            double distanceA2 = getDistanceFromRoute(startLat, startLng, a2.getLat(), a2.getLng());
            return Double.compare(distanceA1, distanceA2);
        });

        // Prepare polyline
        List<double[]> latLngList = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            latLngList.add(new double[]{points.getLat(i), points.getLon(i)});
        }
        String polyline = PolylineUtil.encodePolyline(latLngList);

        // Calculate total distance and time
        double totalDistance = Math.round(route.getDistance() / 10.0) / 100.0; // in km
        long totalTime = route.getTime() / 1000; // milliseconds to seconds

        return new RouteResponseDTO(totalDistance, totalTime, nearbyAttractionResponseDTOS, polyline);
    }

    /**
     * This method creates attraction response DTO along with image URLs
     *
     * @param attraction attraction entity
     * @param attractionLat latitude
     * @param attractionLng longitude
     * @return AttractionResponseDTO
     */
    private AttractionResponseDTO getAttractionResponseDTO(Attraction attraction, double attractionLat, double attractionLng) {
        List<AttractionImageDTO> imageDTOs = attraction.getImages().stream()
                .map(img -> new AttractionImageDTO(img.getId(), img.getImageUrl()))
                .toList();

        AttractionResponseDTO attractionResponseDTO = new AttractionResponseDTO(
                attraction.getId(),
                attraction.getName(),
                attraction.getDescription(),
                attractionLat,
                attractionLng,
                imageDTOs
        );
        return attractionResponseDTO;
    }

    /**
     * This method returns the distance from
     *
     * @param startLat start latitude
     * @param startLng start longitude
     * @param attractionLat  attraction latitude
     * @param attractionLng attraction longitude
     * @return distance
     */
    private double getDistanceFromRoute(double startLat, double startLng, double attractionLat, double attractionLng) {
        // Use Haversine or a more accurate route distance calculation
        return GraphHopperUtils.haversine(startLat, startLng, attractionLat, attractionLng);
    }
}

