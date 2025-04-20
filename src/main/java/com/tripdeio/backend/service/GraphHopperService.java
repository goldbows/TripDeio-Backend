package com.tripdeio.backend.service;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.PointList;
import com.tripdeio.backend.utils.GraphHopperUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class GraphHopperService {

    private final GraphHopper graphHopper;

    public GraphHopperService() {
        graphHopper = new GraphHopperOSM();

        String osmFilePath = "src/main/resources/map/sri-lanka-latest.osm.pbf";
        String cacheLocation = "src/main/resources/graph-cache";

        try {
            // Check if the files are present
            if (!new File(osmFilePath).exists()) {
                throw new RuntimeException("OSM file not found: " + osmFilePath);
            }

            if (!new File(cacheLocation).exists()) {
                throw new RuntimeException("Graph cache directory not found: " + cacheLocation);
            }

            graphHopper.setOSMFile(osmFilePath);
            graphHopper.setGraphHopperLocation(cacheLocation);

            // Define profile and CH profile
            graphHopper.setProfiles(
                    new Profile("car").setVehicle("car").setWeighting("fastest")
            );
            graphHopper.getCHPreparationHandler().setCHProfiles(
                    new CHProfile("car")
            );

            // Set encoding manager
            graphHopper.setEncodingManager(EncodingManager.create("car"));

            // Load graph from cache or import if not available
            graphHopper.importOrLoad();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize GraphHopperService", e);
        }
    }

    public ResponsePath getRoute(double startLat, double startLng, double endLat, double endLng) {
        GHRequest request = new GHRequest(startLat, startLng, endLat, endLng)
                .setProfile("car"); // must match profile defined earlier
        GHResponse response = graphHopper.route(request);
        return response.getBest();
    }

    public double getRouteDistance(double fromLat, double fromLng, double toLat, double toLng) {
        ResponsePath path = getRoute(fromLat, fromLng, toLat, toLng);
        return path.getDistance(); // in meters
    }

    public List<double[]> sampleRoutePoints(ResponsePath path, double intervalMeters) {
        PointList points = path.getPoints();
        List<double[]> sampled = new ArrayList<>();
        double accumulated = 0.0;

        for (int i = 1; i < points.size(); i++) {
            double lat1 = points.getLat(i - 1);
            double lon1 = points.getLon(i - 1);
            double lat2 = points.getLat(i);
            double lon2 = points.getLon(i);

            double dist = GraphHopperUtils.haversine(lat1, lon1, lat2, lon2); // in meters
            accumulated += dist;

            if (accumulated >= intervalMeters) {
                sampled.add(new double[]{lat2, lon2});
                accumulated = 0;
            }
        }

        return sampled;
    }

    public GraphHopper getGraphHopper() {
        return this.graphHopper;
    }

}


