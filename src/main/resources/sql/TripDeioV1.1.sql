CREATE TABLE app_user (
  id SERIAL PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  email VARCHAR(150) UNIQUE NOT NULL,
  password_hash TEXT NOT NULL,
  is_admin BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE attraction (
  id SERIAL PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  description TEXT,
  lat DOUBLE PRECISION NOT NULL,
  lng DOUBLE PRECISION NOT NULL,
  submitted_by INTEGER REFERENCES app_user(id),
  approved BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE food_joint (
  id SERIAL PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  description TEXT,
  lat DOUBLE PRECISION NOT NULL,
  lng DOUBLE PRECISION NOT NULL,
  submitted_by INTEGER REFERENCES app_user(id),
  approved BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE food_item (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE food_joint_item (
  id SERIAL PRIMARY KEY,
  food_joint_id INTEGER REFERENCES food_joint(id) ON DELETE CASCADE,
  food_item_id INTEGER REFERENCES food_item(id),
  price DECIMAL(10,2) NOT NULL
);

CREATE TABLE route (
  id SERIAL PRIMARY KEY,
  start_name VARCHAR(255),
  end_name VARCHAR(255),
  start_lat DOUBLE PRECISION NOT NULL,
  start_lng DOUBLE PRECISION NOT NULL,
  end_lat DOUBLE PRECISION NOT NULL,
  end_lng DOUBLE PRECISION NOT NULL,
  distance_km DECIMAL(10,2),
  duration_minutes INTEGER,
  polyline TEXT,
  created_by INTEGER REFERENCES app_user(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE itinerary (
  id SERIAL PRIMARY KEY,
  route_id INTEGER REFERENCES route(id) ON DELETE CASCADE,
  app_user_id INTEGER REFERENCES app_user(id),
  title VARCHAR(255),
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE itinerary_stop (
  id SERIAL PRIMARY KEY,
  itinerary_id INTEGER REFERENCES itinerary(id) ON DELETE CASCADE,
  stop_type VARCHAR(50) CHECK (stop_type IN ('attraction', 'food_joint')),
  stop_id INTEGER NOT NULL,
  visit_order INTEGER,
  estimated_minutes INTEGER
);

CREATE TABLE transport_method (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE transport_segment (
  id SERIAL PRIMARY KEY,
  itinerary_id INTEGER REFERENCES itinerary(id) ON DELETE CASCADE,
  from_stop_id INTEGER,
  to_stop_id INTEGER,
  transport_method_id INTEGER REFERENCES transport_method(id),
  estimated_minutes INTEGER,
  estimated_price DECIMAL(10,2)
);

CREATE TABLE attraction_rating (
  id SERIAL PRIMARY KEY,
  attraction_id INTEGER REFERENCES attraction(id) ON DELETE CASCADE,
  app_user_id INTEGER REFERENCES app_user(id),
  rating INTEGER CHECK (rating BETWEEN 1 AND 5),
  comment TEXT,
  image_url TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT NOW(),
  UNIQUE (attraction_id, app_user_id)
);

CREATE TABLE food_joint_rating (
  id SERIAL PRIMARY KEY,
  food_joint_id INTEGER REFERENCES food_joint(id) ON DELETE CASCADE,
  app_user_id INTEGER REFERENCES app_user(id),
  rating INTEGER CHECK (rating BETWEEN 1 AND 5),
  comment TEXT,
  image_url TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT NOW(),
  UNIQUE (food_joint_id, app_user_id)
);

-- app_user
INSERT INTO app_user (username, email, password_hash, is_admin) VALUES
  ('admin', 'admin@example.com', 'hashed_password1', true),
  ('john_doe', 'john@example.com', 'hashed_password2', false),
  ('jane_doe', 'jane@example.com', 'hashed_password3', false);

-- Attractions
INSERT INTO attraction (name, description, lat, lng, submitted_by, approved) VALUES
  ('Temple of the Tooth', 'A sacred Buddhist site in Kandy', 7.2936, 80.6414, 1, true),
  ('Nine Arches Bridge', 'Famous colonial-era bridge in Ella', 6.8751, 81.0462, 2, false),
  ('Sigiriya Rock', 'Ancient rock fortress and palace ruin', 7.9570, 80.7603, 3, true);

-- Food Joints
INSERT INTO food_joint (name, description, lat, lng, submitted_by, approved) VALUES
  ('Upalis by Nawaloka', 'Popular local Sri Lankan cuisine', 6.9182, 79.8627, 1, true),
  ('Cafe Chill', 'Well-known hangout spot in Ella', 6.8721, 81.0432, 2, true),
  ('Theva Cuisine', 'Fine dining restaurant in Kandy', 7.2906, 80.6337, 3, false);

-- Food Items
INSERT INTO food_item (name) VALUES
  ('Kottu Roti'),
  ('Hoppers'),
  ('Rice and Curry');

-- Food Joint Items
INSERT INTO food_joint_item (food_joint_id, food_item_id, price) VALUES
  (1, 1, 450.00),
  (2, 2, 300.00),
  (3, 3, 600.00);

-- Routes
INSERT INTO route (start_name, end_name, start_lat, start_lng, end_lat, end_lng, distance_km, duration_minutes, polyline, created_by_id) VALUES
  ('Colombo Fort', 'Kandy', 6.9335, 79.8500, 7.2906, 80.6337, 115.00, 180, 'polyline1', 1),
  ('Kandy', 'Ella', 7.2906, 80.6337, 6.8667, 81.0500, 140.00, 240, 'polyline2', 2),
  ('Colombo Fort', 'Sigiriya', 6.9335, 79.8500, 7.9570, 80.7603, 165.00, 210, 'polyline3', 3);

-- Itineraries
INSERT INTO itinerary (route_id, app_user_id, title, description) VALUES
  (1, 2, 'Colombo to Kandy Explorer', 'Cultural and scenic itinerary'),
  (2, 3, 'Tea Country to Ella', 'Mountain journey through Sri Lanka'),
  (3, 1, 'Colombo to Sigiriya Adventure', 'Historical exploration');

-- Itinerary Stops
INSERT INTO itinerary_stop (itinerary_id, stop_type, stop_id, visit_order, estimated_minutes) VALUES
  (1, 'attraction', 1, 1, 60),
  (1, 'food_joint', 1, 2, 45),
  (2, 'attraction', 2, 1, 90);

-- Transport Methods
INSERT INTO transport_method (name) VALUES
  ('Bus'),
  ('Train'),
  ('Uber');

-- Transport Segments
INSERT INTO transport_segment (itinerary_id, from_stop_id, to_stop_id, transport_method_id, estimated_minutes, estimated_price) VALUES
  (1, 1, 2, 1, 30, 200.00),
  (2, 2, 3, 2, 120, 350.00),
  (3, 1, 3, 3, 180, 1500.00);

-- Attraction Ratings
INSERT INTO attraction_rating (attraction_id, app_user_id, rating, comment, image_url) VALUES
  (1, 2, 5, 'Absolutely stunning place!', 'img1.jpg'),
  (2, 3, 4, 'Unique bridge and nice walk.', 'img2.jpg'),
  (3, 1, 5, 'Breathtaking view!', 'img3.jpg');

-- Food Joint Ratings
INSERT INTO food_joint_rating (food_joint_id, app_user_id, rating, comment, image_url) VALUES
  (1, 3, 5, 'Tasty food and great service.', 'foodimg1.jpg'),
  (2, 1, 4, 'Nice place to chill.', 'foodimg2.jpg'),
  (3, 2, 3, 'Too pricey for the portion.', 'foodimg3.jpg');


INSERT INTO attraction (name, description, lat, lng, submitted_by, approved) VALUES
('Galle Dutch Fort', 'A UNESCO World Heritage Site, Galle Fort is a 16th-century fortification built by the Portuguese and expanded by the Dutch, blending colonial history and seaside charm.', 6.0300, 80.2128, 1, true),
('Dambulla Cave Temple', 'A UNESCO World Heritage Site, this cave temple complex features five caves with over 150 Buddha statues and vibrant murals, dating back to the 1st century BC.', 7.8567, 80.6489, 1, true),
('Anuradhapura Ancient City', 'One of Sri Lanka''s ancient capitals, Anuradhapura is a UNESCO site with well-preserved ruins, stupas, and the sacred Sri Maha Bodhi tree.', 8.3114, 80.4037, 1, true),
('Yala National Park', 'Famous for its high leopard density, Yala National Park offers safaris to spot elephants, crocodiles, and diverse bird species along the southeast coast.', 6.3667, 81.4000, 1, true),
('Adam''s Peak', 'A sacred mountain with a footprint-shaped mark revered by Buddhists, Hindus, and Muslims, offering a challenging pilgrimage hike with stunning sunrise views.', 6.8094, 80.4994, 1, true),
('Polonnaruwa Ancient City', 'A UNESCO World Heritage Site, Polonnaruwa features 12th-century ruins, including the Gal Vihara rock temple and the Sacred Quadrangle.', 7.9403, 81.0000, 1, true),
('Udawalawe National Park', 'Known for its large elephant population, Udawalawe offers safaris around its reservoir, attracting diverse wildlife and birds.', 6.4385, 80.8884, 1, true),
('Sinharaja Forest Reserve', 'A UNESCO-listed rainforest, Sinharaja is a biodiversity hotspot with endemic birds, mammals, and lush evergreen flora.', 6.4167, 80.5000, 1, true),
('Horton Plains National Park', 'A highland park with dramatic landscapes, including World''s End cliff and Baker''s Falls, ideal for hiking and nature lovers.', 6.8020, 80.7732, 1, true),
('Wilpattu National Park', 'Sri Lanka''s largest national park, known for its natural lakes and wildlife, including leopards, elephants, and sloth bears.', 8.4667, 80.1333, 1, true),
('Minneriya National Park', 'Famous for ''The Gathering,'' where hundreds of elephants congregate around its tank during the dry season.', 8.0333, 80.8333, 1, true),
('Kumana National Park', 'A quieter park in the east, Kumana is known for its birdlife and occasional leopard and elephant sightings.', 6.5833, 81.6667, 1, true),
('Diyaluma Falls', 'Sri Lanka''s second-highest waterfall, Diyaluma offers scenic beauty along the Koslanda-Wellawaya road.', 6.7333, 81.0167, 1, true),
('Bambarakanda Falls', 'The tallest waterfall in Sri Lanka, Bambarakanda cascades dramatically amidst lush greenery in the central highlands.', 6.7667, 80.8333, 1, true),
('Ravana Falls', 'A picturesque waterfall near Ella, named after the mythical king Ravana, popular for its scenic beauty.', 6.8500, 81.0500, 1, true),
('Dunhinda Falls', 'A stunning waterfall near Badulla, accessible via a scenic hike through lush forests.', 7.0167, 81.0667, 1, true),
('Baker''s Falls', 'Located in Horton Plains, this waterfall is named after explorer Sir Samuel Baker, offering a serene highland setting.', 6.8000, 80.7833, 1, true),
('Devon Falls', 'A tiered waterfall near Talawakele, Devon Falls is a scenic stop in the tea country.', 6.9333, 80.6667, 1, true),
('St. Clair''s Falls', 'Known as ''Little Niagara of Sri Lanka,'' this wide waterfall is a striking feature in the tea estates near Talawakele.', 6.9667, 80.6667, 1, true),
('Laxapana Falls', 'A powerful waterfall in the Maskeliya area, known for its hydroelectric significance and natural beauty.', 6.9000, 80.5000, 1, true),
('Ramboda Falls', 'A multi-tiered waterfall near Nuwara Eliya, popular for its accessibility and stunning views.', 7.0667, 80.6667, 1, true),
('Ella Rock', 'A hiking destination near Ella, offering panoramic views of tea plantations and hills.', 6.8667, 81.0500, 1, true),
('Little Adam''s Peak', 'A shorter hike near Ella, providing breathtaking views of the surrounding valleys and tea estates.', 6.8500, 81.0333, 1, true),
('Lipton''s Seat', 'A scenic viewpoint in Haputale, where Sir Thomas Lipton surveyed his tea estates, offering sweeping views.', 6.7333, 80.9500, 1, true),
('Gregory Lake', 'A serene reservoir in Nuwara Eliya, ideal for boating and enjoying the cool climate.', 6.9578, 80.7761, 1, true),
('Hakgala Botanical Garden', 'A highland garden near Nuwara Eliya, known for its rose gardens and diverse plant species.', 6.9167, 80.7667, 1, true),
('Pedro Tea Estate', 'A working tea plantation near Nuwara Eliya, offering tours of the tea-making process.', 6.9833, 80.7667, 1, true),
('World''s End', 'A dramatic cliff in Horton Plains with a 1,200-meter drop, offering stunning views on clear days.', 6.7833, 80.7833, 1, true),
('Colombo', 'The commercial capital, known for its colonial heritage, Gangaramaya Temple, and vibrant Galle Face Green.', 6.9271, 79.8612, 1, true),
('Kandy', 'A cultural hub in the hills, home to the sacred Kandy Lake and vibrant Esala Perahera festival.', 7.2906, 80.6337, 1, true),
('Galle', 'A coastal city with a historic fort, colonial architecture, and vibrant cultural scene.', 6.0535, 80.2210, 1, true),
('Nuwara Eliya', 'Nicknamed ''Little England,'' this hill town is the heart of Sri Lanka''s tea industry with a cool climate.', 6.9708, 80.7829, 1, true),
('Ella', 'A charming hill town known for its scenic hikes, tea plantations, and relaxed vibe.', 6.8667, 81.0333, 1, true),
('Jaffna', 'The northern capital, rich in Tamil culture, with historic temples and the Jaffna Fort.', 9.6685, 80.0074, 1, true),
('Trincomalee', 'A coastal city with pristine beaches, Koneswaram Temple, and whale-watching opportunities.', 8.5874, 81.2152, 1, true),
('Negombo', 'A coastal town near Colombo, known for its fish market, lagoon, and Dutch-era canal.', 7.2008, 79.8737, 1, true),
('Matara', 'A southern coastal city with historic forts, beaches, and the serene Matara Paravi Duwa temple.', 5.9485, 80.5353, 1, true),
('Batticaloa', 'An eastern coastal city with a historic fort, singing fish lagoon, and Kallady Bridge.', 7.7300, 81.6944, 1, true),
('Kataragama', 'A pilgrimage town sacred to Buddhists, Hindus, and Muslims, known for its vibrant festivals.', 6.4135, 81.3346, 1, true),
('Bentota', 'A coastal resort town famous for its golden beaches, water sports, and Ayurvedic retreats.', 6.4250, 80.0000, 1, true),
('Hikkaduwa', 'A lively beach town known for coral reefs, surfing, and vibrant nightlife.', 6.1407, 80.0998, 1, true),
('Unawatuna', 'A picturesque beach with golden sands, ideal for swimming and snorkeling.', 6.0096, 80.2484, 1, true),
('Mirissa', 'A tropical beach known for whale watching, surfing, and a laid-back vibe.', 5.9447, 80.4522, 1, true),
('Arugam Bay', 'A world-renowned surfing destination on the east coast with a relaxed beach culture.', 6.8500, 81.8333, 1, true),
('Knuckles Mountain Range', 'A UNESCO-listed range with rugged peaks, hiking trails, and diverse flora and fauna.', 7.3833, 80.8333, 1, true),
('Bundala National Park', 'A wetland sanctuary known for its migratory birds, crocodiles, and quiet safaris.', 6.1833, 81.2000, 1, true),
('Gal Oya National Park', 'A remote park where elephants swim across Senanayake Samudra lake, offering unique boat safaris.', 7.2167, 81.3667, 1, true),
('Ritigala Strict Nature Reserve', 'A forested hill with ancient monastic ruins, offering a blend of history and nature.', 8.1167, 80.6667, 1, true),
('Koneswaram Temple', 'A historic Hindu temple in Trincomalee, perched on a cliff with stunning ocean views.', 8.5822, 81.2450, 1, true),
('Nallur Kandaswamy Kovil', 'A vibrant Hindu temple in Jaffna, known for its towering gopuram and cultural festivals.', 9.6735, 80.0295, 1, true),
('Gangaramaya Temple', 'A striking Buddhist temple in Colombo, blending Sri Lankan, Thai, and Chinese architectural styles.', 6.9167, 79.8567, 1, true);

ALTER TABLE public.attraction ALTER COLUMN description TYPE varchar(1000) USING description::varchar(1000);
