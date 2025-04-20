package com.tripdeio.backend.service;

import com.graphhopper.util.PointList;
import com.tripdeio.backend.dto.RouteResponseDTO;
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

    public RouteResponseDTO findAttractionsNearRoute(double startLat, double startLng, double endLat, double endLng) {
        var route = graphHopperService.getRoute(startLat, startLng, endLat, endLng);
        var sampledPoints = graphHopperService.sampleRoutePoints(route, 2000); // every 2km
        var allAttractions = attractionRepository.findByApprovedTrue();

        List<Attraction> nearbyAttractions = new ArrayList<>();

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
                        nearbyAttractions.add(attraction);
                        break; // Don't check other points for this attraction
                    }
                }
            }
        }

        PointList points = route.getPoints();
        List<double[]> latLngList = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            latLngList.add(new double[]{points.getLat(i), points.getLon(i)});
        }

        String polyline = PolylineUtil.encodePolyline(latLngList);

        double totalDistance = Math.round(route.getDistance() / 10.0) / 100.0; // in km
        long totalTime = route.getTime() / 1000; // milliseconds to seconds

        return new RouteResponseDTO(totalDistance, totalTime, nearbyAttractions, polyline);
    }
}

