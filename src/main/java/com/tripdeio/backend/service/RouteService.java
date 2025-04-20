package com.tripdeio.backend.service;

import com.graphhopper.util.PointList;
import com.tripdeio.backend.dto.AttractionImageDTO;
import com.tripdeio.backend.dto.RouteResponseDTO;
import com.tripdeio.backend.dto.AttractionResponseDTO;
import com.tripdeio.backend.entity.Attraction;
import com.tripdeio.backend.repository.AttractionRepository;
import com.tripdeio.backend.utils.GraphHopperUtils;
import com.tripdeio.backend.utils.PolylineUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {
    private final GraphHopperService graphHopperService;
    private final AttractionRepository attractionRepository;

    public RouteService(GraphHopperService graphHopperService, AttractionRepository attractionRepository) {
        this.graphHopperService = graphHopperService;
        this.attractionRepository = attractionRepository;
    }

    /**
     * This method find the attractions along with the route within 20km distance
     *
     * @param startLat start latitude
     * @param startLng start longitude
     * @param endLat end latitude
     * @param endLng end longitude
     * @return RouteResponseDTO
     */
    public RouteResponseDTO findAttractionsNearRoute(double startLat, double startLng, double endLat, double endLng) {
        var route = graphHopperService.getRoute(startLat, startLng, endLat, endLng);
        var sampledPoints = graphHopperService.sampleRoutePoints(route, 2000); // every 2km
        var allAttractions = attractionRepository.findByApprovedTrue();

        List<AttractionResponseDTO> nearbyAttractionResponseDTOS = new ArrayList<>();

        for (Attraction attraction : allAttractions) {
            double attractionLat = attraction.getLat();
            double attractionLng = attraction.getLng();

            for (double[] point : sampledPoints) {
                double haversineDistance = GraphHopperUtils.haversine(
                        point[0], point[1], attractionLat, attractionLng);

                if (haversineDistance <= 25_000) { // Only check route if roughly near
                    double routeDistance = graphHopperService.getRouteDistance(
                            point[0], point[1], attractionLat, attractionLng);

                    if (routeDistance <= 20_000) {
                        AttractionResponseDTO attractionResponseDTO = this.getAttractionResponseDTO(attraction, attractionLat, attractionLng);
                        nearbyAttractionResponseDTOS.add(attractionResponseDTO);
                        break; // Don't check other points for this attraction
                    }
                }
            }
        }

        nearbyAttractionResponseDTOS.sort((a1, a2) -> {
            double distanceA1 = getDistanceFromRoute(startLat, startLng, a1.getLat(), a1.getLng());
            double distanceA2 = getDistanceFromRoute(startLat, startLng, a2.getLat(), a2.getLng());
            return Double.compare(distanceA1, distanceA2);
        });

        PointList points = route.getPoints();
        List<double[]> latLngList = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            latLngList.add(new double[]{points.getLat(i), points.getLon(i)});
        }

        String polyline = PolylineUtil.encodePolyline(latLngList);

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

