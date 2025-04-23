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

  -- Add geom column
  ALTER TABLE attraction ADD COLUMN geom geometry(Point, 4326);

  ALTER TABLE public.attraction ALTER COLUMN description TYPE varchar(1000) USING description::varchar(1000);

INSERT INTO attraction (name, description, lat, lng, submitted_by, approved, geom) VALUES
('Pigeon Island National Park', 'A marine park near Trincomalee, known for snorkeling and diving with vibrant coral reefs and marine life.', 8.7220, 81.2040, 1, true, NULL),
('Tangalle Beach', 'A serene beach in the south with golden sands, ideal for swimming and witnessing turtle nesting.', 6.0240, 80.7940, 1, true, NULL),
('Rekawa Beach', 'A quiet beach near Tangalle, famous for its turtle conservation projects and night turtle-watching tours.', 6.0450, 80.8500, 1, true, NULL),
('Kalkudah Beach', 'A pristine beach on the east coast near Batticaloa, known for its calm waters and coral reefs.', 7.9167, 81.5667, 1, true, NULL),
('Pasikudah Beach', 'A popular east coast beach with shallow, clear waters, perfect for families and snorkeling.', 7.9300, 81.5600, 1, true, NULL),
('Nilaveli Beach', 'A tranquil beach near Trincomalee, offering white sands, clear waters, and access to Pigeon Island.', 8.6833, 81.1833, 1, true, NULL),
('Marble Beach', 'A secluded beach near Trincomalee, managed by the Sri Lankan Air Force, with calm waters and stunning views.', 8.6333, 81.2167, 1, true, NULL),
('Jungle Beach', 'A hidden beach near Unawatuna, surrounded by lush greenery, offering a peaceful escape.', 6.0030, 80.2600, 1, true, NULL),
('Wijaya Beach', 'A small beach in Dalawella near Unawatuna, famous for its rope swing and relaxed vibe.', 6.0100, 80.2450, 1, true, NULL),
('Dickwella Beach', 'A southern beach with golden sands and good surfing conditions, near the Hummanaya Blowhole.', 5.9617, 80.6950, 1, true, NULL),
('Talalla Beach', 'A crescent-shaped beach in the south, ideal for swimming and yoga retreats, with a laid-back atmosphere.', 5.9833, 80.7167, 1, true, NULL),
('Hiriketiya Beach', 'A small, horseshoe-shaped beach near Dickwella, popular for surfing and its hip, bohemian vibe.', 5.9667, 80.7000, 1, true, NULL),
('Goyambokka Beach', 'A secluded beach near Tangalle, known for its calm waters and natural rock pools.', 6.0300, 80.8000, 1, true, NULL),
('Polhena Beach', 'A reef-protected beach near Matara, ideal for snorkeling and swimming with sea turtles.', 5.9400, 80.5500, 1, true, NULL),
('Medaketiya Beach', 'A quieter section of Tangalle Beach, offering long stretches of sand and a relaxed atmosphere.', 6.0200, 80.7900, 1, true, NULL),
('Mawella Beach', 'A peaceful beach near Tangalle, with soft sands and shallow waters, perfect for a serene getaway.', 6.0000, 80.7333, 1, true, NULL),
('Koggala Beach', 'A long beach near Galle, known for its turtle hatchery and proximity to the Koggala Lake.', 5.9860, 80.3280, 1, true, NULL),
('Thalpe Beach', 'A quiet beach near Galle, offering privacy and clear waters, popular among luxury travelers.', 6.0000, 80.2833, 1, true, NULL),
('Mullikulam Beach', 'A remote beach in the northwest near Wilpattu, known for its untouched beauty and calm waters.', 8.4000, 79.9667, 1, true, NULL),
('Casuarina Beach', 'A stunning beach in Jaffna’s Karainagar area, named for its casuarina trees, with soft sands and shallow waters.', 9.7333, 79.8833, 1, true, NULL),
('Keerimalai Springs', 'A sacred hot spring in Jaffna, believed to have healing properties, located near the ocean.', 9.8167, 80.3167, 1, true, NULL),
('Nagadeepa Purana Viharaya', 'An ancient Buddhist temple on Nainativu Island near Jaffna, significant for its historical and religious importance.', 9.6167, 79.7667, 1, true, NULL),
('Delft Island', 'A remote island off Jaffna, known for its wild horses, Baobab trees, and colonial ruins.', 9.5167, 79.6833, 1, true, NULL),
('Point Pedro', 'The northernmost point of Sri Lanka, offering scenic ocean views and a historic lighthouse.', 9.8333, 80.2333, 1, true, NULL),
('Jaffna Public Library', 'An iconic library in Jaffna, a symbol of Tamil culture, rebuilt after being destroyed during the civil war.', 9.6615, 80.0093, 1, true, NULL),
('Velgam Vehera', 'An ancient Buddhist monastery near Trincomalee, significant for its Sinhala-Tamil historical coexistence.', 8.6667, 81.2000, 1, true, NULL),
('Fort Fredrick', 'A colonial fort in Trincomalee, housing the Koneswaram Temple and offering panoramic views of the harbor.', 8.5833, 81.2333, 1, true, NULL),
('Lover’s Leap', 'A cliff in Trincomalee with a romantic legend, offering views of the ocean and Koneswaram Temple.', 8.5800, 81.2400, 1, true, NULL),
('Batticaloa Lighthouse', 'A historic lighthouse on the east coast, offering views of the lagoon and surrounding beaches.', 7.7167, 81.7000, 1, true, NULL),
('Kokkilai Lagoon', 'A bird-watching paradise between Trincomalee and Mullaitivu, home to migratory birds and mangroves.', 9.0000, 80.9667, 1, true, NULL),
('Muhudu Maha Viharaya', 'An ancient Buddhist temple near Pottuvil, believed to date back over 2,000 years, located by the beach.', 6.8667, 81.8333, 1, true, NULL),
('Kudumbigala Monastery', 'A remote forest monastery in the east near Kumana, known for its ancient cave temples and panoramic views.', 6.6333, 81.6667, 1, true, NULL),
('Okanda Devalaya', 'A Hindu shrine near Kumana National Park, a starting point for pilgrims heading to Kataragama.', 6.6667, 81.7667, 1, true, NULL),
('Lahugala National Park', 'A small park near Pottuvil, known for its elephant herds and ancient Lahugala Kitulana temple.', 6.8833, 81.7167, 1, true, NULL),
('Hummanaya Blowhole', 'The second-largest blowhole in the world, near Dickwella, where seawater shoots up to 30 meters.', 5.9667, 80.6667, 1, true, NULL),
('Paravi Duwa Temple', 'A serene Buddhist temple on an islet in Matara, connected by a small bridge, offering peaceful views.', 5.9485, 80.5353, 1, true, NULL),
('Weherahena Temple', 'A unique underground temple near Matara, featuring a large Buddha statue and vibrant murals.', 5.9500, 80.5667, 1, true, NULL),
('Dondra Head Lighthouse', 'The southernmost point of Sri Lanka in Dondra, with the tallest lighthouse in the country.', 5.9217, 80.5917, 1, true, NULL),
('Kirinda Temple', 'A historic temple near Tissamaharama, associated with Queen Viharamahadevi, offering ocean views.', 6.2167, 81.3167, 1, true, NULL),
('Tissamaharama Raja Maha Vihara', 'An ancient Buddhist temple in Tissamaharama, believed to date back to the 2nd century BC.', 6.2833, 81.2833, 1, true, NULL),
('Mulkirigala Raja Maha Vihara', 'A rock temple near Tangalle, known as ‘Little Sigiriya,’ with ancient caves and frescoes.', 6.1333, 80.7333, 1, true, NULL),
('Maduwanwela Walawwa', 'A historic mansion in the south near Ratnapura, showcasing colonial-era architecture and artifacts.', 6.4167, 80.6667, 1, true, NULL),
('Belihuloya', 'A picturesque village in the hills, known for its waterfalls, hiking trails, and cool climate.', 6.7167, 80.7833, 1, true, NULL),
('Surathali Falls', 'A lesser-known waterfall near Belihuloya, offering a refreshing stop for hikers.', 6.7000, 80.7667, 1, true, NULL),
('Galaboda Falls', 'A scenic waterfall near Nawalapitiya, surrounded by tea estates and lush greenery.', 7.2167, 80.5167, 1, true, NULL),
('Huluganga Falls', 'A hidden waterfall near Kandy, accessible via a hike through tea plantations.', 7.3333, 80.6667, 1, true, NULL),
('Hanthana Mountain Range', 'A hiking area near Kandy, offering trails with panoramic views of the city and surrounding hills.', 7.2667, 80.6167, 1, true, NULL),
('Aluvihare Rock Temple', 'A historic temple near Matale, where the Buddhist Pali Canon was first written down.', 7.5000, 80.6167, 1, true, NULL),
('Nalanda Gedige', 'An ancient stone temple near Matale, blending Buddhist and Hindu architectural styles.', 7.6667, 80.6333, 1, true, NULL),
('Wasgamuwa National Park', 'A park in the Central Province, known for its elephant population and ancient irrigation tanks.', 7.7167, 80.9333, 1, true, NULL),
('Kandalama Lake', 'A serene reservoir near Dambulla, offering scenic views and a peaceful setting.', 7.8667, 80.6833, 1, true, NULL),
('Ibbankatuwa Megalithic Tombs', 'An ancient burial site near Dambulla, dating back to the pre-Buddhist era, with stone cists.', 7.8500, 80.6333, 1, true, NULL),
('Popham’s Arboretum', 'A unique forest reserve near Dambulla, ideal for birdwatching and nature walks.', 7.8667, 80.6500, 1, true, NULL),
('Namal Uyana', 'A rose quartz mountain range and forest reserve near Anuradhapura, with a Buddhist monastery.', 8.1667, 80.5000, 1, true, NULL),
('Mihintale', 'A sacred hill near Anuradhapura, considered the birthplace of Buddhism in Sri Lanka.', 8.3500, 80.5167, 1, true, NULL),
('Ruwanwelisaya', 'A massive stupa in Anuradhapura, one of the tallest ancient monuments in the world, built in the 2nd century BC.', 8.3500, 80.3967, 1, true, NULL),
('Jaya Sri Maha Bodhi', 'A sacred fig tree in Anuradhapura, believed to be the oldest living tree planted by humans, from a cutting of the Bodhi tree.', 8.3450, 80.3950, 1, true, NULL),
('Thuparamaya', 'The first Buddhist stupa in Sri Lanka, located in Anuradhapura, built in the 3rd century BC.', 8.3550, 80.4000, 1, true, NULL),
('Abhayagiri Vihara', 'A major monastery in Anuradhapura, known for its large stupa and ancient monastic ruins.', 8.3700, 80.3950, 1, true, NULL),
('Jetavanaramaya', 'A colossal stupa in Anuradhapura, once the third tallest structure in the ancient world.', 8.3517, 80.4033, 1, true, NULL),
('Samadhi Buddha Statue', 'A serene Buddha statue in Anuradhapura, depicting deep meditation, carved in the 4th century.', 8.3600, 80.3950, 1, true, NULL),
('Kuttam Pokuna', 'Ancient twin ponds in Anuradhapura, showcasing advanced hydraulic engineering of the era.', 8.3667, 80.4000, 1, true, NULL),
('Lankarama Stupa', 'A smaller stupa in Anuradhapura, surrounded by stone pillars, built in the 1st century.', 8.3400, 80.3900, 1, true, NULL),
('Gal Vihara', 'A rock temple in Polonnaruwa with four colossal Buddha statues carved into a single granite rock.', 7.9667, 81.0000, 1, true, NULL),
('Parakrama Samudra', 'A massive man-made lake in Polonnaruwa, built by King Parakramabahu I for irrigation.', 7.9167, 80.9667, 1, true, NULL),
('Rankoth Vehera', 'A large stupa in Polonnaruwa, the fourth largest in Sri Lanka, built in the 12th century.', 7.9500, 81.0000, 1, true, NULL),
('Lankatilaka Viharaya', 'A striking image house in Polonnaruwa with a massive Buddha statue and intricate brickwork.', 7.9400, 81.0000, 1, true, NULL),
('Tivanka Image House', 'A Polonnaruwa temple known for its murals and a Buddha statue in the rare ‘thrice-bent’ posture.', 7.9667, 81.0167, 1, true, NULL),
('Polonnaruwa Vatadage', 'A circular relic house in Polonnaruwa, showcasing intricate stone carvings and ancient architecture.', 7.9467, 81.0000, 1, true, NULL),
('Madunagala Hot Springs', 'A set of hot springs near Hambantota, known for their therapeutic qualities.', 6.3167, 81.0500, 1, true, NULL),
('Ridiyagama Safari Park', 'A drive-through safari park near Hambantota, home to lions, zebras, and other exotic animals.', 6.1667, 81.1167, 1, true, NULL),
('Dry Zone Botanic Gardens', 'The first botanical garden in Sri Lanka’s dry zone, located near Hambantota, showcasing native flora.', 6.1500, 81.1000, 1, true, NULL),
('Kalametiya Bird Sanctuary', 'A coastal wetland near Tangalle, a haven for migratory birds and lagoon biodiversity.', 6.0833, 80.9333, 1, true, NULL),
('Ussangoda National Park', 'A unique coastal park near Ambalantota with red soil cliffs, believed to be a landing site in the Ramayana epic.', 6.1000, 81.0167, 1, true, NULL),
('Madu River', 'A wetland near Balapitiya, offering boat safaris through mangroves, islands, and cinnamon plantations.', 6.3000, 80.0500, 1, true, NULL),
('Kosgoda Turtle Hatchery', 'A conservation project near Bentota, where visitors can see baby turtles and learn about turtle protection.', 6.3333, 80.0333, 1, true, NULL),
('Lunuganga Estate', 'The former home of architect Geoffrey Bawa in Bentota, a beautifully landscaped garden estate.', 6.4000, 80.0167, 1, true, NULL),
('Brief Garden', 'A tropical garden estate near Beruwala, created by Bevis Bawa, with sculptures and lush greenery.', 6.4167, 80.0000, 1, true, NULL),
('Kalutara Bodhiya', 'A sacred Buddhist site in Kalutara with a large stupa, one of the few hollow stupas in the world.', 6.5833, 79.9667, 1, true, NULL),
('Richmond Castle', 'A colonial mansion in Kalutara, blending European and Indian architectural styles, with lush gardens.', 6.6000, 80.0000, 1, true, NULL),
('Beruwala Lighthouse', 'A historic lighthouse on Barberyn Island near Beruwala, offering ocean views.', 6.4667, 79.9833, 1, true, NULL),
('Kande Viharaya', 'A temple in Beruwala with a 48-meter tall Buddha statue, one of the tallest seated Buddha statues in Sri Lanka.', 6.4333, 80.0000, 1, true, NULL),
('Moragalla Beach', 'A quiet beach near Beruwala, ideal for swimming and relaxing with fewer crowds.', 6.4500, 79.9833, 1, true, NULL),
('Seenigama Muhudu Viharaya', 'A small temple on an islet near Hikkaduwa, accessible by boat, dedicated to the deity Devol Deviyo.', 6.1333, 80.1167, 1, true, NULL),
('Hikkaduwa Coral Sanctuary', 'A marine park in Hikkaduwa, where visitors can see coral reefs and tropical fish via glass-bottom boats.', 6.1333, 80.1000, 1, true, NULL),
('Tsunami Honganji Viharaya', 'A temple near Hikkaduwa with a large Buddha statue, built to commemorate victims of the 2004 tsunami.', 6.1167, 80.1167, 1, true, NULL),
('Puswelulla Spice Garden', 'A spice garden near Hikkaduwa, offering tours to learn about Sri Lankan spices and Ayurvedic remedies.', 6.1500, 80.1167, 1, true, NULL),
('Galle Lighthouse', 'A historic lighthouse within Galle Fort, offering views of the Indian Ocean and the fort’s ramparts.', 6.0250, 80.2167, 1, true, NULL),
('Galle National Museum', 'A museum within Galle Fort, showcasing the region’s colonial history and cultural artifacts.', 6.0300, 80.2167, 1, true, NULL),
('Jungle Beach Rumassala', 'A secluded beach near Galle, surrounded by the Rumassala hill, believed to be part of the Ramayana epic.', 6.0167, 80.2333, 1, true, NULL),
('Unawatuna Lagoon', 'A serene lagoon near Unawatuna Beach, ideal for kayaking and birdwatching.', 6.0167, 80.2500, 1, true, NULL),
('Mihiripenna Beach', 'A quiet beach near Unawatuna, known for its clear waters and relaxed atmosphere.', 6.0000, 80.2667, 1, true, NULL),
('Kanneliya Forest Reserve', 'A lowland rainforest near Galle, known for its biodiversity, waterfalls, and hiking trails.', 6.2500, 80.3333, 1, true, NULL),
('Hiyare Rainforest', 'A small rainforest reserve near Galle, offering nature trails and a reservoir with diverse wildlife.', 6.0667, 80.3167, 1, true, NULL),
('Martin Wickramasinghe Museum', 'A museum in Koggala near Galle, dedicated to the famous Sri Lankan author, showcasing folk culture.', 5.9833, 80.3167, 1, true, NULL),
('Handunugoda Tea Estate', 'A tea plantation near Ahangama, famous for producing virgin white tea.', 5.9667, 80.3667, 1, true, NULL),
('Dooli Ella Falls', 'A lesser-known waterfall near Kosgoda, surrounded by lush greenery.', 6.3167, 80.0333, 1, true, NULL),
('Induruwa Beach', 'A tranquil beach near Bentota, known for its turtle hatcheries and calm waters.', 6.3667, 80.0167, 1, true, NULL),
('Aluthgama Fish Market', 'A bustling market near Bentota, offering a glimpse into local fishing culture.', 6.4333, 80.0000, 1, true, NULL),
('Victoria Park', 'A beautifully landscaped park in Nuwara Eliya, ideal for a leisurely stroll amidst flowers and trees.', 6.9667, 80.7667, 1, true, NULL),
('Moon Plains', 'A highland area near Nuwara Eliya, offering panoramic views of surrounding mountains and tea estates.', 6.9333, 80.7833, 1, true, NULL),
('Lover’s Leap Waterfall', 'A small waterfall near Nuwara Eliya, named after a romantic legend, accessible via a short hike.', 6.9667, 80.7833, 1, true, NULL),
('Shanthipura Viewpoint', 'The highest village in Sri Lanka near Nuwara Eliya, offering stunning views of the hill country.', 6.9167, 80.8167, 1, true, NULL),
('Seetha Amman Temple', 'A Hindu temple near Nuwara Eliya, believed to be the site where Sita was held captive in the Ramayana epic.', 6.9333, 80.8000, 1, true, NULL),
('Bomburu Ella Falls', 'A multi-tiered waterfall near Nuwara Eliya, accessible via a hike through tea plantations.', 7.0000, 80.8333, 1, true, NULL),
('Hakgala Rock', 'A hiking spot near Nuwara Eliya, offering views of the Hakgala Botanical Garden and surrounding hills.', 6.9167, 80.7667, 1, true, NULL),
('Single Tree Hill', 'A viewpoint in Nuwara Eliya, accessible via a short hike, offering panoramic views of the town.', 6.9667, 80.7833, 1, true, NULL),
('Ella Gap', 'A scenic viewpoint in Ella, offering views of the valley and distant southern plains.', 6.8667, 81.0500, 1, true, NULL),
('Rawana Ella Cave', 'A cave near Ella, believed to be linked to the Ramayana epic, accessible via a hike.', 6.8500, 81.0500, 1, true, NULL),
('Dowa Rock Temple', 'A small cave temple near Ella, with a partially carved Buddha statue and ancient murals.', 6.8667, 81.0333, 1, true, NULL),
('Nine Arch Bridge', 'An iconic colonial-era bridge near Ella, surrounded by tea plantations, popular for photography.', 6.8667, 81.0667, 1, true, NULL),
('Demodara Loop', 'A unique railway loop near Ella, where the train passes under itself, an engineering marvel.', 6.9000, 81.0667, 1, true, NULL),
('Adisham Bungalow', 'A colonial monastery-turned-bungalow near Haputale, surrounded by tea estates and gardens.', 6.8333, 80.9667, 1, true, NULL),
('Thangamale Sanctuary', 'A forested area near Haputale, ideal for birdwatching and nature walks with misty views.', 6.7667, 80.9667, 1, true, NULL),
('Dambatenne Tea Factory', 'A historic tea factory near Haputale, offering tours of the tea production process.', 6.7833, 80.9667, 1, true, NULL),
('Bambarakanda Ella Falls', 'A lesser-known waterfall near Haputale, offering a quieter alternative to the famous Bambarakanda Falls.', 6.7667, 80.8333, 1, true, NULL),
('Haputale Forest Reserve', 'A misty forest near Haputale, ideal for hiking and spotting endemic birds.', 6.7667, 80.9667, 1, true, NULL),
('Ohiya', 'A remote village near Horton Plains, offering hiking trails and views of the hill country.', 6.8167, 80.8333, 1, true, NULL),
('Devil’s Staircase', 'A challenging hiking trail near Ohiya, known for its steep descent and scenic views.', 6.8000, 80.8500, 1, true, NULL),
('Bopath Ella Falls', 'A waterfall near Ratnapura, shaped like a bo leaf, surrounded by lush greenery.', 6.8000, 80.3667, 1, true, NULL),
('Sinharaja Peak', 'A hiking trail within Sinharaja Forest Reserve, leading to a viewpoint with panoramic forest views.', 6.4167, 80.5000, 1, true, NULL),
('Pitawala Pathana', 'A mini World’s End in the Knuckles Range, with a steep drop and grasslands.', 7.5167, 80.7667, 1, true, NULL),
('Riverston', 'A viewpoint in the Knuckles Range, offering misty views and hiking trails.', 7.5333, 80.7333, 1, true, NULL),
('Manigala', 'A peak in the Knuckles Range, popular for hiking with views of the surrounding valleys.', 7.4667, 80.7667, 1, true, NULL),
('Corbett’s Gap', 'A scenic viewpoint in the Knuckles Range, known for its cool climate and panoramic vistas.', 7.5000, 80.7167, 1, true, NULL),
('Deanston Mini World’s End', 'A cliff in the Knuckles Range, offering dramatic views of the lowlands.', 7.5000, 80.7667, 1, true, NULL),
('Sari Ella Falls', 'A waterfall in the Knuckles Range, accessible via a hike through dense forest.', 7.4667, 80.7333, 1, true, NULL),
('Rathna Ella Falls', 'A tall waterfall in the Ratnapura district, surrounded by tea estates and greenery.', 6.7167, 80.4000, 1, true, NULL),
('Katugas Ella Falls', 'A lesser-known waterfall near Ratnapura, ideal for a quiet visit amidst nature.', 6.6833, 80.4167, 1, true, NULL),
('Samanalawewa Reservoir', 'A scenic reservoir near Ratnapura, offering boating and views of the surrounding hills.', 6.6833, 80.4667, 1, true, NULL),
('Udawatta Kele Sanctuary', 'A forest reserve in Kandy, offering walking trails, birdwatching, and a peaceful escape.', 7.3000, 80.6417, 1, true, NULL),
('Royal Botanical Gardens', 'A sprawling garden in Peradeniya near Kandy, known for its orchid collection and giant trees.', 7.2717, 80.5950, 1, true, NULL),
('Embekka Devalaya', 'A 14th-century temple near Kandy, famous for its intricately carved wooden pillars.', 7.2167, 80.5667, 1, true, NULL),
('Gadaladeniya Temple', 'An ancient temple near Kandy, known for its South Indian architectural style and stone carvings.', 7.2500, 80.5667, 1, true, NULL),
('Lankatilaka Temple', 'A 14th-century Buddhist temple near Kandy, with a mix of Sinhala and Dravidian architecture.', 7.2500, 80.5833, 1, true, NULL),
('Degaldoruwa Raja Maha Vihara', 'A cave temple near Kandy, known for its well-preserved frescoes and Buddha statues.', 7.3000, 80.6667, 1, true, NULL),
('Kandy Lake', 'A man-made lake in the heart of Kandy, offering scenic views and a peaceful walk.', 7.2917, 80.6417, 1, true, NULL),
('Bahirawakanda Viharaya', 'A temple in Kandy with a large Buddha statue overlooking the city, offering panoramic views.', 7.2833, 80.6333, 1, true, NULL),
('Knuckles Bamboo Bridge', 'A scenic bamboo bridge in the Knuckles Range, crossing a stream amidst lush forest.', 7.5000, 80.7667, 1, true, NULL),
('Sigiriya Village', 'A traditional village near Sigiriya, offering cultural experiences like bullock cart rides, local meals, and insights into rural life.', 7.9475, 80.7589, 1, true, NULL),
('Pinnawala Elephant Orphanage', 'A sanctuary near Kegalle that cares for orphaned and injured elephants, famous for its daily river bathing sessions.', 7.3010, 80.3870, 1, true, NULL),
('Galle Face Green', 'A popular oceanfront promenade in Colombo, perfect for evening strolls, street food, and kite flying with views of the Indian Ocean.', 6.9248, 79.8450, 1, true, NULL),
('Viharamahadevi Park', 'Colombo’s largest park, featuring a giant Buddha statue, walking paths, and a lake, ideal for relaxation.', 6.9128, 79.8636, 1, true, NULL),
('Pettah Floating Market', 'A vibrant market in Colombo on Beira Lake, offering local crafts, food stalls, and a unique shopping experience.', 6.9385, 79.8542, 1, true, NULL),
('Independence Square', 'A historic landmark in Colombo, surrounded by gardens and a memorial hall, commemorating Sri Lanka’s independence.', 6.9040, 79.8678, 1, true, NULL),
('Jami Ul-Alfar Mosque', 'A striking red-and-white mosque in Colombo’s Pettah area, known for its unique architecture and cultural significance.', 6.9382, 79.8515, 1, true, NULL),
('Uppuveli Beach', 'A relaxed beach near Trincomalee, offering golden sands, calm waters, and a laid-back atmosphere.', 8.6060, 81.2190, 1, true, NULL),
('Kallady Bridge', 'A scenic bridge in Batticaloa, famous for the legend of the singing fish that can be heard at night.', 7.7100, 81.6950, 1, true, NULL),
('Nainativu Nagapooshani Amman Temple', 'A significant Hindu temple on Nainativu Island, dedicated to Goddess Amman, attracting thousands of pilgrims.', 9.6105, 79.7750, 1, true, NULL),
('Jaffna Fort', 'A 17th-century fort in Jaffna, built by the Portuguese and later expanded by the Dutch, overlooking the lagoon.', 9.6620, 80.0080, 1, true, NULL),
('Mannar Fort', 'A colonial fort on Mannar Island, built by the Portuguese and later captured by the Dutch, with ocean views.', 8.9810, 79.9140, 1, true, NULL),
('Thalaimannar Pier', 'The westernmost point of Mannar, historically part of Adam’s Bridge, offering views of the sea and migratory birds.', 9.1010, 79.7280, 1, true, NULL),
('Madhu Church', 'A sacred Catholic shrine in Mannar, known for its annual festival and serene forest setting.', 8.8500, 80.2000, 1, true, NULL),
('Adams Bridge', 'A chain of limestone shoals between Mannar and India, also known as Rama’s Bridge, steeped in mythology.', 9.1200, 79.5200, 1, true, NULL),
('Kalpitiya Dutch Fort', 'A 17th-century fort in Kalpitiya, built by the Dutch, offering historical insights and ocean views.', 8.2360, 79.7660, 1, true, NULL),
('Kalpitiya Lagoon', 'A scenic lagoon in the northwest, ideal for kitesurfing, dolphin watching, and mangrove exploration.', 8.2400, 79.7500, 1, true, NULL),
('Wilpattu Leopard Trail', 'A lesser-known trail in Wilpattu National Park, offering a higher chance of spotting leopards in their natural habitat.', 8.4660, 80.1300, 1, true, NULL),
('Kumana Bird Sanctuary', 'A section of Kumana National Park, renowned for its diverse bird species, including migratory waders.', 6.5800, 81.6700, 1, true, NULL),
('Star Fort', 'A unique star-shaped fort in Matara, built by the Dutch in the 18th century, offering historical insights.', 5.9470, 80.5480, 1, true, NULL),
('Silent Beach', 'A secluded beach near Tangalle, known for its untouched beauty and serene environment.', 6.0550, 80.8400, 1, true, NULL),
('Peacock Hill', 'A viewpoint in Pussellawa near Kandy, offering panoramic views of the surrounding tea estates and hills.', 7.1500, 80.6333, 1, true, NULL),
('Ambuluwawa Tower', 'A unique biodiversity complex near Gampola, featuring a spiral tower with 360-degree views of the Knuckles Range.', 7.1667, 80.5333, 1, true, NULL),
('Hanuman Temple', 'A Hindu temple in Ramboda, dedicated to Lord Hanuman, offering scenic views of the tea country.', 7.0500, 80.6667, 1, true, NULL),
('Haggala Strict Nature Reserve', 'A protected area near Nuwara Eliya, known for its montane forest, wildlife, and hiking trails.', 6.9200, 80.7700, 1, true, NULL),
('Haputale', 'A hill town known for its cool climate, tea estates, and stunning views of the southern plains.', 6.7680, 80.9560, 1, true, NULL),
('Sinharaja Bird Watching Trail', 'A trail within Sinharaja Forest Reserve, ideal for spotting endemic birds like the Sri Lanka blue magpie.', 6.4100, 80.5000, 1, true, NULL),
('Kitulgala White Water Rafting', 'A popular spot on the Kelani River near Kitulgala, offering thrilling rafting experiences amidst rainforests.', 6.9960, 80.4120, 1, true, NULL),
('Makulella Bird Sanctuary', 'A lesser-known sanctuary near Kitulgala, home to diverse bird species and a peaceful forest setting.', 7.0000, 80.4200, 1, true, NULL),
('Belilena Cave', 'A prehistoric cave near Kitulgala, where evidence of human habitation dating back 32,000 years was found.', 6.9700, 80.4300, 1, true, NULL),
('Samanalakanda', 'A butterfly mountain near Ratnapura, part of the Adam’s Peak range, known for its biodiversity.', 6.8167, 80.5000, 1, true, NULL),
('Gemmological Museum', 'A museum in Ratnapura, showcasing Sri Lanka’s gem industry with displays of precious stones and mining tools.', 6.6800, 80.4000, 1, true, NULL),
('Pompekelle Fort', 'A small Dutch fort in Ratnapura, offering a glimpse into colonial history amidst the gem city.', 6.6830, 80.4050, 1, true, NULL),
('Udawalawa Elephant Transit Home', 'A rehabilitation center within Udawalawa National Park, caring for orphaned elephant calves before releasing them to the wild.', 6.4350, 80.8850, 1, true, NULL),
('Dry Zone Botanic Garden', 'A botanical garden near Hambantota, showcasing native plants of Sri Lanka’s dry zone, with walking trails.', 6.1500, 81.1333, 1, true, NULL),
('Tissamaharama Stupa', 'An ancient stupa in Tissamaharama, believed to date back to the 2nd century BC, a sacred Buddhist site.', 6.2800, 81.2833, 1, true, NULL),
('Debarawewa Lake', 'A serene lake near Tissamaharama, attracting birdlife and offering a peaceful setting for relaxation.', 6.2830, 81.2900, 1, true, NULL),
('Lunugamvehera National Park', 'A park near Tissamaharama, serving as a corridor for elephants migrating between Yala and Udawalawa.', 6.3667, 81.1667, 1, true, NULL),
('Kataragama Devalaya', 'A Hindu shrine in Kataragama, dedicated to Lord Murugan, attracting devotees from multiple faiths.', 6.4160, 81.3330, 1, true, NULL),
('Sella Kataragama', 'A sacred site near Kataragama, featuring a small temple and a river, believed to be a place of spiritual cleansing.', 6.4100, 81.3400, 1, true, NULL),
('Ruhunu Maha Kataragama Devalaya', 'A Buddhist temple in Kataragama, part of the town’s sacred complex, known for its annual festival.', 6.4130, 81.3300, 1, true, NULL),
('Vedihitikanda', 'A hill near Kataragama, considered a sacred site, offering hiking and views of the surrounding area.', 6.4000, 81.3500, 1, true, NULL),
('Okanda Beach', 'A remote beach near Kumana National Park, considered the starting point for the Kataragama pilgrimage.', 6.6667, 81.8000, 1, true, NULL),
('Magul Maha Viharaya', 'A historic temple near Lahugala, believed to be the site of King Kavantissa’s wedding, with ancient ruins.', 6.8667, 81.7500, 1, true, NULL),
('Whiskey Point', 'A popular surfing spot in Arugam Bay, known for its consistent waves and relaxed beach vibe.', 6.8400, 81.8330, 1, true, NULL),
('Elephant Rock', 'A surfing and viewpoints near Arugam Bay, offering panoramic views of the coastline and wildlife.', 6.8200, 81.8400, 1, true, NULL),
('Pottuvil Lagoon', 'A scenic lagoon near Arugam Bay, ideal for boat tours to spot wildlife and mangroves.', 6.8830, 81.8330, 1, true, NULL),
('Panama Tank', 'A historic reservoir near Arugam Bay, surrounded by paddy fields and a peaceful rural setting.', 6.7667, 81.8167, 1, true, NULL),
('Kottukal Beach', 'A secluded beach near Pottuvil, known for its rock formations and serene environment.', 6.9167, 81.8330, 1, true, NULL),
('Sangamankandy Pillaiyar Kovil', 'A Hindu temple near Batticaloa, popular among locals, set in a peaceful rural area.', 7.7500, 81.6830, 1, true, NULL),
('Batticaloa Fort', 'A 17th-century Dutch fort by the lagoon in Batticaloa, offering historical insights and scenic views.', 7.7160, 81.7000, 1, true, NULL),
('Kokkadicholai Lagoon', 'A lagoon near Batticaloa, known for its biodiversity, fishing communities, and boat tours.', 7.6167, 81.7333, 1, true, NULL),
('Kodiyar Bay', 'A scenic bay near Trincomalee, offering calm waters, fishing villages, and a peaceful atmosphere.', 8.5500, 81.2330, 1, true, NULL),
('Seruwawila Mangala Raja Maha Vihara', 'A historic Buddhist temple near Trincomalee, believed to house a relic of the Buddha.', 8.3667, 81.3167, 1, true, NULL),
('Thiriyaya Girihandu Seya', 'One of the oldest stupas in Sri Lanka, located near Trincomalee, built in the 2nd century BC.', 8.8667, 81.0167, 1, true, NULL),
('Charty Beach', 'A serene beach near Jaffna, ideal for relaxation and enjoying the northern coastline.', 9.6830, 80.0167, 1, true, NULL),
('Kayts Island', 'An island near Jaffna, offering historical sites, fishing villages, and a peaceful escape.', 9.7000, 79.8667, 1, true, NULL),
('Hammenhiel Fort', 'A small Dutch fort on an island near Kayts, built in the 17th century, surrounded by the sea.', 9.7167, 79.8667, 1, true, NULL),
('Kankesanthurai Beach', 'A beach near Jaffna, known for its lighthouse and historical significance as a former port.', 9.8167, 80.0500, 1, true, NULL),
('Dambakola Patuna', 'A historic port near Jaffna, where the sacred Bo tree sapling arrived in Sri Lanka in the 3rd century BC.', 9.8167, 80.1667, 1, true, NULL),
('Kudiramalai Point', 'A historic site on the northwest coast near Wilpattu, associated with ancient Tamil rulers and trade.', 8.5333, 79.8667, 1, true, NULL),
('Dorawaka Rock', 'A rock formation near Kalpitiya, offering panoramic views of the ocean and surrounding lagoons.', 8.2500, 79.7667, 1, true, NULL),
('Dutch Bay', 'A scenic bay in Kalpitiya, popular for dolphin watching, kitesurfing, and sunset views.', 8.2330, 79.7500, 1, true, NULL),
('Alankuda Beach', 'A pristine beach in Kalpitiya, known for its kitesurfing schools and quiet, sandy shoreline.', 8.2000, 79.7167, 1, true, NULL),
('Thalawila Church', 'A historic Catholic church near Kalpitiya, known for its annual feast and coastal location.', 8.1167, 79.7500, 1, true, NULL),
('Anawilundawa Wetland Sanctuary', 'A Ramsar wetland site near Chilaw, known for its birdlife, mangroves, and biodiversity.', 7.7000, 79.8167, 1, true, NULL),
('Munneswaram Temple', 'A significant Hindu temple near Chilaw, dedicated to Lord Shiva, known for its ancient history and festivals.', 7.5833, 79.8833, 1, true, NULL),
('Negombo Dutch Fort', 'A 17th-century fort in Negombo, built by the Portuguese and later expanded by the Dutch, near the lagoon.', 7.2070, 79.8350, 1, true, NULL),
('Negombo Lagoon', 'A scenic lagoon in Negombo, ideal for boat tours, fishing, and spotting water birds.', 7.2000, 79.8500, 1, true, NULL),
('Angurukaramulla Temple', 'A Buddhist temple in Negombo, known for its 6-meter reclining Buddha statue and vibrant murals.', 7.2130, 79.8460, 1, true, NULL),
('St. Mary’s Church', 'A historic Catholic church in Negombo, known for its colonial architecture and vibrant frescoes.', 7.2100, 79.8380, 1, true, NULL),
('Muthurajawela Marsh', 'A wetland near Negombo, offering boat tours to explore its mangroves, birds, and crocodiles.', 7.0500, 79.8667, 1, true, NULL),
('Kelaniya Raja Maha Vihara', 'A sacred Buddhist temple near Colombo, known for its ancient stupa and frescoes, visited by the Buddha.', 6.9550, 79.9200, 1, true, NULL),
('Kalutara Chaitya', 'A large hollow stupa in Kalutara, one of the few in the world, offering panoramic views from inside.', 6.5860, 79.9620, 1, true, NULL),
('Bentota River', 'A scenic river near Bentota, ideal for boat safaris to explore mangroves, wildlife, and local life.', 6.4200, 80.0000, 1, true, NULL),
('Galle Clock Tower', 'A historic clock tower within Galle Fort, built in 1883, offering views of the fort and ocean.', 6.0305, 80.2160, 1, true, NULL),
('Japanese Peace Pagoda', 'A Buddhist stupa near Unawatuna, built by Japanese monks, offering panoramic views and a serene atmosphere.', 6.0100, 80.2600, 1, true, NULL),
('Koggala Lake', 'A scenic lake near Galle, ideal for boat tours to explore islands, mangroves, and cinnamon cultivation.', 5.9900, 80.3300, 1, true, NULL),
('Ahangama Surfing Beach', 'A popular surfing spot near Galle, known for its consistent waves and laid-back beach culture.', 5.9667, 80.4000, 1, true, NULL),
('Midigama Beach', 'A surfing beach near Weligama, offering right-hand waves and a relaxed atmosphere for surfers.', 5.9667, 80.4167, 1, true, NULL),
('Weligama Bay', 'A wide bay near Weligama, famous for beginner-friendly surf waves and stilt fishermen.', 5.9667, 80.4333, 1, true, NULL),
('Mirissa Parrot Rock', 'A small rock island off Mirissa Beach, offering views of the coastline and a short climb.', 5.9440, 80.4520, 1, true, NULL),
('Coconut Tree Hill', 'A scenic viewpoint in Mirissa, featuring a hill lined with coconut trees overlooking the ocean.', 5.9400, 80.4600, 1, true, NULL),
('Secret Beach Mirissa', 'A hidden beach near Mirissa, accessible via a short jungle path, offering solitude and clear waters.', 5.9380, 80.4650, 1, true, NULL),
('Rathgama Lake', 'A serene lake near Galle, ideal for kayaking and birdwatching, surrounded by mangroves.', 6.0667, 80.1667, 1, true, NULL),
('Dedduwa Lake', 'A peaceful lake near Bentota, offering boat rides and a glimpse into rural life.', 6.3833, 80.0167, 1, true, NULL),
('Horton Plains Mini World’s End', 'A smaller viewpoint in Horton Plains, offering a less crowded alternative to the main World’s End.', 6.7900, 80.7800, 1, true, NULL),
('Thotupola Kanda', 'The third highest peak in Sri Lanka, located in Horton Plains, offering hiking and misty views.', 6.8100, 80.7667, 1, true, NULL),
('Kirigalpoththa', 'The second highest peak in Sri Lanka, accessible via Horton Plains, known for its challenging hike.', 6.8000, 80.7500, 1, true, NULL),
('Galway’s Land National Park', 'A small park in Nuwara Eliya, known for its birdlife, including endemic species, and peaceful trails.', 6.9667, 80.7667, 1, true, NULL),
('Lake Gregory Viewpoint', 'A scenic spot overlooking Gregory Lake in Nuwara Eliya, ideal for photography and relaxation.', 6.9600, 80.7750, 1, true, NULL),
('Pidurutalagala', 'The highest mountain in Sri Lanka near Nuwara Eliya, offering restricted access but stunning views for those permitted.', 7.0000, 80.7667, 1, true, NULL),
('Kothmale Hanging Bridge', 'A suspension bridge near Kandy, offering scenic views of the Mahaweli River and surrounding hills.', 7.0667, 80.7000, 1, true, NULL),
('Hunnasgiriya', 'A hill near Kandy, known for its tea estates, waterfalls, and panoramic views of the Knuckles Range.', 7.3000, 80.6833, 1, true, NULL),
('Sembuwatta Lake', 'A man-made lake near Elkaduwa, surrounded by pine forests and tea estates, offering a serene escape.', 7.4000, 80.6833, 1, true, NULL),
('Wasgamuwa Elephant Corridor', 'A migratory path in Wasgamuwa National Park, where elephants travel between forests, visible during safaris.', 7.7000, 80.9167, 1, true, NULL),
('Minneriya Tank', 'A historic reservoir in Minneriya National Park, attracting elephants and birds during the dry season.', 8.0330, 80.8330, 1, true, NULL),
('Kaudulla Tank', 'An ancient tank in Kaudulla National Park, a gathering spot for elephants and a scenic backdrop for safaris.', 8.1500, 80.9160, 1, true, NULL),
('Hurulu Eco Park', 'A forest reserve near Habarana, offering safaris to spot elephants and other wildlife.', 8.2000, 80.8667, 1, true, NULL),
('Ritigala Forest Monastery', 'A section of Ritigala Strict Nature Reserve, featuring ancient ruins and meditation caves.', 8.1160, 80.6660, 1, true, NULL),
('Avukana Buddha Statue', 'A 5th-century standing Buddha statue near Kekirawa, carved from a single rock, showcasing ancient artistry.', 8.0167, 80.5167, 1, true, NULL),
('Kantale Tank', 'A historic reservoir near Trincomalee, built in ancient times, surrounded by lush greenery and birdlife.', 8.3667, 81.0167, 1, true, NULL),
('Sampur Beach', 'A quiet beach near Trincomalee, offering views of the harbor and a peaceful escape.', 8.5000, 81.2833, 1, true, NULL),
('Arisimalai Beach', 'A remote beach near Trincomalee, known for its rocky landscape and serene environment.', 8.6667, 81.1333, 1, true, NULL),
('Foul Point', 'A coastal area near Trincomalee, known for its lighthouse and whale-watching opportunities.', 8.5500, 81.2500, 1, true, NULL),
('Illukakanda Beach', 'A secluded beach near Batticaloa, offering a peaceful retreat with golden sands.', 7.8000, 81.6667, 1, true, NULL),
('Navalady Beach', 'A beach near Batticaloa, known for its long sandy stretch and views of the lagoon.', 7.7330, 81.6830, 1, true, NULL),
('Kayankerni Beach', 'A coral reef beach near Batticaloa, ideal for snorkeling and exploring marine life.', 7.9333, 81.5500, 1, true, NULL),
('Thiruchendur Beach', 'A beach near Jaffna, offering a quiet escape with views of the northern coast.', 9.6667, 80.0167, 1, true, NULL),
('Manalkadu Beach', 'A remote beach near Jaffna, known for its sand dunes and St. Anthony’s Church.', 9.8000, 80.1667, 1, true, NULL),
('Kokkavil Tower', 'A historic communication tower near Kilinochchi, a symbol of the region’s post-war recovery.', 9.4000, 80.4000, 1, true, NULL),
('Iranamadu Tank', 'A large reservoir near Kilinochchi, built in ancient times, offering scenic views and birdwatching.', 9.3333, 80.4167, 1, true, NULL),
('Pooneryn Fort', 'A small fort near Pooneryn, built by the Portuguese, offering views of the Jaffna Lagoon.', 9.5000, 80.2000, 1, true, NULL),
('Elephant Pass', 'A strategic causeway near Jaffna, historically significant, with a war memorial and lagoon views.', 9.5167, 80.4167, 1, true, NULL),
('Mannar Baobab Tree', 'A massive Baobab tree in Mannar, believed to be over 700 years old, planted by Arab traders.', 8.9667, 79.9167, 1, true, NULL),
('Giant’s Tank', 'A historic reservoir in Mannar, built in ancient times, supporting agriculture and attracting birds.', 8.8667, 80.0333, 1, true, NULL),
('Aruvi Aru', 'A river in Mannar, also known as Malwathu Oya, significant for its role in ancient irrigation systems.', 8.9000, 80.1000, 1, true, NULL),
('Vankalai Bird Sanctuary', 'A wetland near Mannar, a key stopover for migratory birds, including flamingos.', 9.0000, 79.9333, 1, true, NULL),
('Kudawilsandu Beach', 'A remote beach near Kalpitiya, known for its untouched beauty and coral reefs.', 8.1667, 79.7167, 1, true, NULL),
('Kandakuliya Beach', 'A beach in Kalpitiya, popular for dolphin watching and its long, sandy shoreline.', 8.1833, 79.7000, 1, true, NULL),
('Puttalam Lagoon', 'A lagoon near Puttalam, offering boat tours, birdwatching, and views of salt pans.', 8.0333, 79.8333, 1, true, NULL),
('Kala Oya Estuary', 'A scenic estuary near Wilpattu, where the Kala Oya River meets the sea, attracting birds and wildlife.', 8.3333, 79.8667, 1, true, NULL),
('Eluwankulama Tank', 'A historic reservoir near Wilpattu, offering a peaceful setting and birdwatching opportunities.', 8.4000, 80.0167, 1, true, NULL),
('Kumuduwewa Lake', 'A lake near Hambantota, attracting migratory birds and offering a serene environment.', 6.1333, 81.1333, 1, true, NULL),
('Yoda Wewa', 'A large ancient tank in Tissamaharama, built by King Mahanaga, supporting agriculture and wildlife.', 6.2667, 81.2833, 1, true, NULL),
('Weerawila Lake', 'A lake near Tissamaharama, known for its birdlife, including flamingos, and scenic surroundings.', 6.2333, 81.2333, 1, true, NULL),
('Bundala Bird Lagoon', 'A lagoon within Bundala National Park, a Ramsar site, famous for its migratory birds.', 6.1830, 81.2000, 1, true, NULL),
('Gal Oya Valley', 'A scenic valley within Gal Oya National Park, offering hiking and views of the Senanayake Samudra.', 7.2000, 81.3667, 1, true, NULL),
('Danigala Circular Rock', 'A unique rock formation in the Polonnaruwa district, offering panoramic views and a UFO-like shape.', 7.7500, 81.2500, 1, true, NULL),
('Flood Plains National Park', 'A park near Polonnaruwa, known for its grasslands, floodplains, and elephant sightings.', 7.9667, 81.0333, 1, true, NULL),
('Somawathiya National Park', 'A park near Polonnaruwa, home to the Somawathiya Chaitya, with elephants and diverse wildlife.', 8.1167, 81.1667, 1, true, NULL),
('Maduru Oya National Park', 'A park near Batticaloa, known for its ancient ruins, elephants, and the Maduru Oya reservoir.', 7.6167, 81.2167, 1, true, NULL),
('Gal Oya Reservoir', 'A large reservoir in Gal Oya National Park, offering boat safaris to spot swimming elephants.', 7.2160, 81.3660, 1, true, NULL),
('Panduwasnuwara', 'An ancient kingdom near Kurunegala, with ruins of a 12th-century palace and moat.', 7.6167, 80.1167, 1, true, NULL),
('Ridi Viharaya', 'A cave temple near Kurunegala, known for its silver Buddha statue and ancient inscriptions.', 7.5667, 80.4000, 1, true, NULL),
('Athugala', 'A rock in Kurunegala, topped with a large Buddha statue, offering panoramic views of the city.', 7.4833, 80.3667, 1, true, NULL),
('Yapahuwa Rock Fortress', 'A 13th-century fortress near Kurunegala, known for its ornate stone staircase and ancient ruins.', 7.8000, 80.3000, 1, true, NULL),
('Inamaluwa', 'A village near Sigiriya, known for its rural charm, traditional crafts, and proximity to Pidurangala.', 7.9167, 80.7500, 1, true, NULL),
('Pidurangala Rock', 'A rock near Sigiriya, offering a challenging hike and panoramic views of Sigiriya Rock.', 7.9667, 80.7667, 1, true, NULL),
('Dambulla Wholesale Market', 'A bustling market near Dambulla, known for its fresh produce and vibrant local atmosphere.', 7.8660, 80.6500, 1, true, NULL),
('Kandalama Elephant Corridor', 'A migratory path near Dambulla, where elephants travel between forests, visible during certain seasons.', 7.8833, 80.7000, 1, true, NULL),
('Bowatenna Reservoir', 'A reservoir near Matale, supporting irrigation and offering a peaceful setting for visitors.', 7.6667, 80.6667, 1, true, NULL),
('Wariyapola', 'A village near Matale, known for its traditional pottery and rural lifestyle.', 7.6167, 80.5667, 1, true, NULL),
('Knuckles Forest Reserve', 'A biodiversity hotspot near Kandy, offering hiking trails, waterfalls, and endemic species.', 7.4167, 80.8333, 1, true, NULL),
('Rangala', 'A village in the Knuckles Range, known for its tea estates, hiking trails, and cool climate.', 7.3333, 80.8167, 1, true, NULL),
('Deanston', 'A tea estate in the Knuckles Range, offering tours and scenic views of the surrounding hills.', 7.3667, 80.8000, 1, true, NULL),
('Meemure', 'A remote village in the Knuckles Range, offering an authentic rural experience, hiking, and waterfalls.', 7.4333, 80.8500, 1, true, NULL),
('Lakegala', 'A rock peak in the Knuckles Range, associated with the Ramayana, offering a challenging hike.', 7.4500, 80.8667, 1, true, NULL),
('Sera Ella', 'A waterfall near Matale, flowing through a cave-like rock formation, ideal for nature lovers.', 7.5667, 80.6333, 1, true, NULL),
('Upper Hanthana', 'A hiking trail near Kandy, offering views of the city and the Knuckles Range.', 7.2667, 80.6333, 1, true, NULL),
('Suriyagoda Viharaya', 'A temple near Kandy, offering a peaceful setting and views of the Mahaweli River.', 7.2667, 80.6000, 1, true, NULL),
('Hindagala Viharaya', 'A cave temple near Kandy, known for its ancient rock paintings and serene environment.', 7.2500, 80.6167, 1, true, NULL),
('Nelligala International Buddhist Centre', 'A modern Buddhist temple near Kandy, featuring a golden stupa and panoramic views.', 7.1833, 80.5667, 1, true, NULL),
('Kandy View Point', 'A popular viewpoint in Kandy, offering sweeping views of the city, lake, and surrounding hills.', 7.2833, 80.6333, 1, true, NULL),
('Pallekele International Cricket Stadium', 'A scenic cricket stadium near Kandy, surrounded by hills, hosting international matches.', 7.2833, 80.7333, 1, true, NULL),
('Mahaweli River', 'The longest river in Sri Lanka, flowing through Kandy, offering scenic views and boat rides.', 7.3000, 80.6500, 1, true, NULL),
('Dunhinda Aranya Senasanaya', 'A forest monastery near Badulla, offering a serene retreat with caves and meditation spaces.', 7.0167, 81.0660, 1, true, NULL),
('Bogoda Wooden Bridge', 'A 16th-century wooden bridge near Badulla, one of the oldest in Sri Lanka, showcasing ancient architecture.', 7.0000, 81.0167, 1, true, NULL),
('Muthiyangana Raja Maha Vihara', 'A sacred Buddhist temple in Badulla, believed to have been visited by the Buddha.', 6.9833, 81.0500, 1, true, NULL),
('Buduruwagala', 'An ancient Buddhist site near Wellawaya, featuring seven colossal rock-cut statues, including a 15-meter Buddha.', 6.6833, 81.1167, 1, true, NULL),
('Maligawila Buddha Statue', 'A 7th-century standing Buddha statue near Monaragala, carved from a single limestone rock.', 6.4167, 81.3167, 1, true, NULL),
('Yudaganawa Stupa', 'An ancient stupa near Buttala, one of the largest in Sri Lanka, with historical significance.', 6.6000, 81.2500, 1, true, NULL),
('Dematamal Viharaya', 'A temple near Okkampitiya, famous for sheltering a monk during a 3rd-century conflict.', 6.6667, 81.2000, 1, true, NULL),
('Uva Halpewatte Tea Factory', 'A tea factory near Ella, offering tours of the tea-making process and tastings.', 6.8833, 81.0500, 1, true, NULL),
('Idalgashinna', 'A remote hill station near Haputale, known for its railway station and misty landscapes.', 6.7833, 80.9000, 1, true, NULL),
('Diyatalawa', 'A highland town near Haputale, known for its military base, cool climate, and Fox Hill.', 6.8167, 80.9667, 1, true, NULL);


UPDATE attraction SET geom = ST_SetSRID(ST_MakePoint(lng, lat), 4326) WHERE geom IS NULL;

DROP INDEX IF EXISTS idx_attraction_geom;
DROP INDEX IF EXISTS idx_attraction_geom_32644;

CREATE INDEX idx_attraction_geom ON attraction USING GIST (geom);


CREATE TABLE waypoint (
  id SERIAL PRIMARY KEY,
  itinerary_id INTEGER REFERENCES itinerary(id) ON DELETE CASCADE,
  name VARCHAR(255),
  lat DOUBLE PRECISION NOT NULL,
  lng DOUBLE PRECISION NOT NULL,
  type VARCHAR(50) CHECK (type IN ('city', 'attraction', 'detour', 'final')),
  visit_order INTEGER NOT NULL
);


CREATE TABLE segment_instruction (
  id SERIAL PRIMARY KEY,
  from_waypoint_id INTEGER REFERENCES waypoint(id) ON DELETE CASCADE,
  to_waypoint_id INTEGER REFERENCES waypoint(id) ON DELETE CASCADE,
  transport_method_id INTEGER REFERENCES transport_method(id),
  instructions TEXT,
  turn_by_turn_json JSONB, -- could store turn-by-turn navigation steps from GraphHopper
  duration_minutes INTEGER,
  distance_km DECIMAL(10,2),
  estimated_price DECIMAL(10,2)
);


CREATE TABLE attraction_ticket (
  id SERIAL PRIMARY KEY,
  attraction_id INTEGER REFERENCES attraction(id),
  ticket_type VARCHAR(100),
  price DECIMAL(10,2)
);

CREATE TABLE city (
  id SERIAL PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  lat DOUBLE PRECISION NOT NULL,
  lng DOUBLE PRECISION NOT NULL,
  country VARCHAR(100),
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
