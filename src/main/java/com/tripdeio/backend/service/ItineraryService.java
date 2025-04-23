package com.tripdeio.backend.service;

import com.graphhopper.ResponsePath;
import com.graphhopper.util.PointList;
import com.tripdeio.backend.dto.*;
import com.tripdeio.backend.entity.Attraction;
import com.tripdeio.backend.entity.City;
import com.tripdeio.backend.entity.FoodJoint;
import com.tripdeio.backend.entity.TransportSegment;
import com.tripdeio.backend.repository.AttractionRepository;
import com.tripdeio.backend.repository.CityRepository;
import com.tripdeio.backend.repository.FoodJointRepository;
import com.tripdeio.backend.repository.TransportSegmentRepository;
import com.tripdeio.backend.utils.GraphHopperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItineraryService {

    @Autowired
    private GraphHopperService graphHopperService;

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TransportSegmentRepository transportSegmentRepository;

    @Autowired
    private FoodJointRepository foodJointRepository;
    private static final double CITY_RADIUS_KM = 20.0; // 20 km radius for cities
    private static final double FOOD_JOINT_RADIUS_KM = 5.0; // 5 km radius for food joints
    private static final double CAR_PRICE_PER_KM = 50.0; // Example: 50 LKR per km for car

    public ItineraryDTO generateItinerary(Long startAttractionId, Long endAttractionId, List<Long> selectedAttractionIds) {
        // Validate inputs
        if (startAttractionId == null || endAttractionId == null || selectedAttractionIds == null) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        // Fetch start and end attractions
        Attraction startAttraction = attractionRepository.findById(startAttractionId)
                .orElseThrow(() -> new IllegalArgumentException("Start attraction not found"));
        Attraction endAttraction = attractionRepository.findById(endAttractionId)
                .orElseThrow(() -> new IllegalArgumentException("End attraction not found"));

        // Fetch selected attractions
        List<Attraction> selectedAttractions = attractionRepository.findAllById(selectedAttractionIds);
        if (selectedAttractions.size() != selectedAttractionIds.size()) {
            throw new IllegalArgumentException("Some selected attractions not found");
        }

        // Sort attractions by distance from start point
        List<Attraction> orderedAttractions = sortAttractionsByDistance(startAttraction, selectedAttractions);
        orderedAttractions.add(endAttraction);

        // Create itinerary segments
        List<SegmentDTO> segments = new ArrayList<>();
        Attraction previousAttraction = startAttraction;

        for (Attraction currentAttraction : orderedAttractions) {
            if (currentAttraction.equals(startAttraction)) {
                continue; // Skip the start point as a destination
            }

            SegmentDTO segment = createSegment(previousAttraction, currentAttraction);
            segments.add(segment);
            previousAttraction = currentAttraction;
        }

        // Create ItineraryDTO
        return new ItineraryDTO(
                null, // Itinerary ID can be set if stored in DB
                createAttractionResponseDTO(startAttraction),
                createAttractionResponseDTO(endAttraction),
                segments
        );
    }

    private List<Attraction> sortAttractionsByDistance(Attraction startAttraction, List<Attraction> attractions) {
        return attractions.stream()
                .sorted(Comparator.comparingDouble(attraction ->
                        GraphHopperUtils.haversine(
                                startAttraction.getLat(), startAttraction.getLng(),
                                attraction.getLat(), attraction.getLng())))
                .collect(Collectors.toList());
    }

    private SegmentDTO createSegment(Attraction fromAttraction, Attraction toAttraction) {
        // Fetch transport options
        List<TransportOptionDTO> transportOptions = getTransportOptions(fromAttraction, toAttraction);

        // Fetch attraction details
        AttractionDetailsDTO attractionDetails = new AttractionDetailsDTO(
                toAttraction.getVisitDurationMinutes(),
                toAttraction.getTicketPrice()
        );

        // Fetch nearby food joints
        List<FoodJointDTO> nearbyFoodJoints = getNearbyFoodJoints(toAttraction.getLat(), toAttraction.getLng());

        return new SegmentDTO(
                createAttractionResponseDTO(fromAttraction),
                createAttractionResponseDTO(toAttraction),
                transportOptions,
                attractionDetails,
                nearbyFoodJoints
        );
    }

    private List<TransportOptionDTO> getTransportOptions(Attraction fromAttraction, Attraction toAttraction) {
        List<TransportOptionDTO> options = new ArrayList<>();

        // Get route between attractions
        ResponsePath route = graphHopperService.getRoute(
                fromAttraction.getLat(), fromAttraction.getLng(),
                toAttraction.getLat(), toAttraction.getLng());

        // Find cities within 20 km of the route
        List<City> nearbyCities = findNearbyCities(route);

        // Assume fromAttraction and toAttraction are also cities (e.g., A and Z)
        City fromCity = cityRepository.findById(fromAttraction.getId())
                .orElse(null); // Check if fromAttraction is a city
        City toCity = cityRepository.findById(toAttraction.getId())
                .orElse(null); // Check if toAttraction is a city

        boolean isFromStopAttraction = true;
        boolean isToStopAttraction = true;
        if (fromCity != null) isFromStopAttraction = false;
        if (toCity != null) isToStopAttraction = false;

        // Build transport segments
        List<TransportSegment> segments = new ArrayList<>();
        if (fromCity != null && toCity != null) {
            // Direct city-to-city transport
            segments.addAll(transportSegmentRepository.findByFromStopIdAndToStopId(
                    fromCity.getId(), toCity.getId()));
        }

        // Check intermediate cities
        for (City city : nearbyCities) {
            if (fromCity != null) {
                segments.addAll(transportSegmentRepository.findByFromStopIdAndToStopId(
                        fromCity.getId(), city.getId()));
            }
            if (toCity != null) {
                segments.addAll(transportSegmentRepository.findByFromStopIdAndToStopId(
                        city.getId(), toCity.getId()));
            }
        }

        if (!segments.isEmpty()) {
            // Group segments by transport method
            Map<String, List<TransportSegment>> segmentsByMethod = segments.stream()
                    .collect(Collectors.groupingBy(segment -> segment.getTransportMethod().getName()));

            for (Map.Entry<String, List<TransportSegment>> entry : segmentsByMethod.entrySet()) {
                String method = entry.getKey();
                if (method.equalsIgnoreCase("Car")) {
                    continue; // Skip car if transport segments are available
                }

                List<TransportStepDTO> steps = entry.getValue().stream()
                        .map(segment -> {
                            String fromName = segment.getFromStopType().equals("city")
                                    ? cityRepository.findById(segment.getFromStopId()).map(City::getName).orElse("Unknown")
                                    : attractionRepository.findById(segment.getFromStopId()).map(Attraction::getName).orElse("Unknown");
                            String toName = segment.getToStopType().equals("city")
                                    ? cityRepository.findById(segment.getToStopId()).map(City::getName).orElse("Unknown")
                                    : attractionRepository.findById(segment.getToStopId()).map(Attraction::getName).orElse("Unknown");
                            return new TransportStepDTO(
                                    String.format("Take %s from %s to %s", method.toLowerCase(), fromName, toName),
                                    segment.getEstimatedMinutes(),
                                    segment.getEstimatedPrice()
                            );
                        })
                        .collect(Collectors.toList());

                // Add steps to reach fromAttraction to nearest city/attraction and from last city/attraction to toAttraction
                if (isFromStopAttraction && !nearbyCities.isEmpty()) {
                    steps.addFirst(createCarStep(fromAttraction, nearbyCities.getFirst()));
                }
                if (isToStopAttraction && !nearbyCities.isEmpty()) {
                    steps.add(createCarStep(nearbyCities.getLast(), toAttraction));
                }

                options.add(new TransportOptionDTO(method, steps));
            }
        } else {
            // No transport segments; provide car option
            double distance = route.getDistance() / 1000.0; // Convert to km
            int durationMinutes = (int) Math.round(route.getTime() / 60000.0); // Convert ms to minutes
            double price = distance * CAR_PRICE_PER_KM;

            List<TransportStepDTO> steps = Collections.singletonList(
                    new TransportStepDTO(
                            String.format("Drive from %s to %s", fromAttraction.getName(), toAttraction.getName()),
                            durationMinutes,
                            price
                    )
            );

            options.add(new TransportOptionDTO("Car", steps));
        }

        return options;
    }

    private List<City> findNearbyCities(ResponsePath route) {
        PointList points = route.getPoints();
        List<City> nearbyCities = new ArrayList<>();
        List<City> allCities = cityRepository.findAll();

        for (City city : allCities) {
            for (int i = 0; i < points.size(); i++) {
                double distance = GraphHopperUtils.haversine(
                        points.getLat(i), points.getLon(i),
                        city.getLat(), city.getLng()) / 1000.0; // Convert to km
                if (distance <= CITY_RADIUS_KM) {
                    nearbyCities.add(city);
                    break;
                }
            }
        }

        // Sort cities by distance from the start of the route
        return nearbyCities.stream()
                .sorted(Comparator.comparingDouble(city ->
                        GraphHopperUtils.haversine(
                                points.getLat(0), points.getLon(0),
                                city.getLat(), city.getLng())))
                .collect(Collectors.toList());
    }

    private TransportStepDTO createCarStep(Attraction from, City to) {
        ResponsePath route = graphHopperService.getRoute(
                from.getLat(), from.getLng(),
                to.getLat(), to.getLng());
        double distance = route.getDistance() / 1000.0; // Convert to km
        int durationMinutes = (int) Math.round(route.getTime() / 60000.0); // Convert ms to minutes
        double price = distance * CAR_PRICE_PER_KM;

        return new TransportStepDTO(
                String.format("Drive from %s to %s", from.getName(), to.getName()),
                durationMinutes,
                price
        );
    }

    private TransportStepDTO createCarStep(City from, Attraction to) {
        ResponsePath route = graphHopperService.getRoute(
                from.getLat(), from.getLng(),
                to.getLat(), to.getLng());
        double distance = route.getDistance() / 1000.0; // Convert to km
        int durationMinutes = (int) Math.round(route.getTime() / 60000.0); // Convert ms to minutes
        double price = distance * CAR_PRICE_PER_KM;

        return new TransportStepDTO(
                String.format("Drive from %s to %s", from.getName(), to.getName()),
                durationMinutes,
                price
        );
    }

    private List<FoodJointDTO> getNearbyFoodJoints(double lat, double lng) {
        List<FoodJoint> foodJoints = foodJointRepository.findByApprovedTrue();
        List<FoodJointDTO> nearbyFoodJoints = new ArrayList<>();

        for (FoodJoint foodJoint : foodJoints) {
            double distance = GraphHopperUtils.haversine(lat, lng, foodJoint.getLat(), foodJoint.getLng()) / 1000.0; // Convert to km
            if (distance <= FOOD_JOINT_RADIUS_KM) {
                List<FoodItemDTO> foodItems = null;

                nearbyFoodJoints.add(new FoodJointDTO(
                        foodJoint.getId(),
                        foodJoint.getName(),
                        foodJoint.getDescription(),
                        foodJoint.getLat(),
                        foodJoint.getLng(),
                        foodItems
                ));
            }
        }

        return nearbyFoodJoints;
    }

    private AttractionResponseDTO createAttractionResponseDTO(Attraction attraction) {
        List<AttractionImageDTO> imageDTOs = attraction.getImages().stream()
                .map(img -> new AttractionImageDTO(img.getId(), img.getImageUrl()))
                .collect(Collectors.toList());

        return new AttractionResponseDTO(
                attraction.getId(),
                attraction.getName(),
                attraction.getDescription(),
                attraction.getLat(),
                attraction.getLng(),
                imageDTOs
        );
    }
}