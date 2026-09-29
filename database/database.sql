-- =====================================================================
-- Urban Automated Underground Waste Pneumatic Transport Hub
-- Database Schema & Sample Seed Data
-- =====================================================================

CREATE DATABASE IF NOT EXISTS urban_waste_hub;
USE urban_waste_hub;

-- 1. Collection Point Table
DROP TABLE IF EXISTS alert;
DROP TABLE IF EXISTS maintenance;
DROP TABLE IF EXISTS waste_processing;
DROP TABLE IF EXISTS waste_transport;
DROP TABLE IF EXISTS waste_bin;
DROP TABLE IF EXISTS pipeline;
DROP TABLE IF EXISTS transport_hub;
DROP TABLE IF EXISTS collection_point;

CREATE TABLE collection_point (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    point_code VARCHAR(50) NOT NULL UNIQUE,
    location VARCHAR(255) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Smart Waste Bin Table
CREATE TABLE waste_bin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bin_code VARCHAR(50) NOT NULL UNIQUE,
    collection_point_id BIGINT NOT NULL,
    waste_type VARCHAR(50) NOT NULL,
    capacity DOUBLE NOT NULL,
    current_waste DOUBLE NOT NULL DEFAULT 0.0,
    fill_percentage DOUBLE NOT NULL DEFAULT 0.0,
    status VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
    last_emptied DATETIME,
    CONSTRAINT fk_bin_collection_point FOREIGN KEY (collection_point_id) REFERENCES collection_point(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Underground Pipeline Table
CREATE TABLE pipeline (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pipeline_code VARCHAR(50) NOT NULL UNIQUE,
    source_point VARCHAR(255) NOT NULL,
    destination_hub VARCHAR(255) NOT NULL,
    length DOUBLE NOT NULL,
    diameter DOUBLE NOT NULL,
    air_pressure DOUBLE NOT NULL DEFAULT 2.5,
    air_velocity DOUBLE NOT NULL DEFAULT 25.0,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    last_inspection_date DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Transport Hub Table
CREATE TABLE transport_hub (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hub_code VARCHAR(50) NOT NULL UNIQUE,
    hub_name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    maximum_capacity DOUBLE NOT NULL,
    current_waste DOUBLE NOT NULL DEFAULT 0.0,
    utilization DOUBLE NOT NULL DEFAULT 0.0,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Waste Transport Table
CREATE TABLE waste_transport (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bin_id BIGINT NOT NULL,
    pipeline_id BIGINT NOT NULL,
    hub_id BIGINT NOT NULL,
    waste_type VARCHAR(50) NOT NULL,
    
    quantity DOUBLE NOT NULL,
    start_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_time DATETIME,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    CONSTRAINT fk_transport_bin FOREIGN KEY (bin_id) REFERENCES waste_bin(id),
    CONSTRAINT fk_transport_pipeline FOREIGN KEY (pipeline_id) REFERENCES pipeline(id),
    CONSTRAINT fk_transport_hub FOREIGN KEY (hub_id) REFERENCES transport_hub(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Waste Processing Table
CREATE TABLE waste_processing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hub_id BIGINT NOT NULL,
    waste_type VARCHAR(50) NOT NULL,
    quantity DOUBLE NOT NULL,
    processing_method VARCHAR(255) NOT NULL,
    processing_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'COMPLETED',
    CONSTRAINT fk_processing_hub FOREIGN KEY (hub_id) REFERENCES transport_hub(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Maintenance Table
CREATE TABLE maintenance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    equipment_type VARCHAR(50) NOT NULL,
    equipment_id VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    maintenance_date DATE NOT NULL,
    technician VARCHAR(100) NOT NULL,
    cost DOUBLE NOT NULL DEFAULT 0.0,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Alerts Table
CREATE TABLE alert (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    alert_type VARCHAR(50) NOT NULL,
    equipment_type VARCHAR(50) NOT NULL,
    equipment_id VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    severity VARCHAR(50) NOT NULL DEFAULT 'INFO',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved BOOLEAN NOT NULL DEFAULT FALSE,
    resolved_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================================
-- SAMPLE SEED DATA
-- =====================================================================

-- Insert 5 Collection Points
INSERT INTO collection_point (id, point_code, location, latitude, longitude, status, created_at) VALUES
(1, 'CP001', 'Central Plaza - North Gate', 11.0168, 76.9558, 'ACTIVE', NOW() - INTERVAL 10 DAY),
(2, 'CP002', 'Tech Park Boulevard', 11.0285, 76.9632, 'ACTIVE', NOW() - INTERVAL 8 DAY),
(3, 'CP003', 'Green Valley Residential District', 11.0094, 76.9421, 'ACTIVE', NOW() - INTERVAL 7 DAY),
(4, 'CP004', 'Metro Commercial Complex', 11.0341, 76.9710, 'ACTIVE', NOW() - INTERVAL 5 DAY),
(5, 'CP005', 'Riverside Medical & Research Quarter', 11.0412, 76.9805, 'ACTIVE', NOW() - INTERVAL 3 DAY);

-- Insert 10 Smart Waste Bins
INSERT INTO waste_bin (id, bin_code, collection_point_id, waste_type, capacity, current_waste, fill_percentage, status, last_emptied) VALUES
(1, 'BIN001', 1, 'ORGANIC', 500.0, 420.0, 84.0, 'FULL', NOW() - INTERVAL 14 HOUR),
(2, 'BIN002', 1, 'RECYCLABLE', 400.0, 210.0, 52.5, 'NORMAL', NOW() - INTERVAL 13 HOUR),
(3, 'BIN003', 2, 'PLASTIC', 350.0, 250.0, 71.4, 'WARNING', NOW() - INTERVAL 11 HOUR),
(4, 'BIN004', 2, 'PAPER', 300.0, 110.0, 36.7, 'NORMAL', NOW() - INTERVAL 10 HOUR),
(5, 'BIN005', 3, 'ORGANIC', 600.0, 520.0, 86.7, 'FULL', NOW() - INTERVAL 9 HOUR),
(6, 'BIN006', 3, 'GENERAL', 450.0, 190.0, 42.2, 'NORMAL', NOW() - INTERVAL 8 HOUR),
(7, 'BIN007', 4, 'PLASTIC', 400.0, 385.0, 96.3, 'FULL', NOW() - INTERVAL 6 HOUR),
(8, 'BIN008', 4, 'RECYCLABLE', 350.0, 240.0, 68.6, 'WARNING', NOW() - INTERVAL 5 HOUR),
(9, 'BIN009', 5, 'HAZARDOUS', 250.0, 140.0, 56.0, 'NORMAL', NOW() - INTERVAL 3 HOUR),
(10, 'BIN010', 5, 'GENERAL', 500.0, 150.0, 30.0, 'NORMAL', NOW() - INTERVAL 2 HOUR);

-- Insert 5 Underground Pipelines
INSERT INTO pipeline (id, pipeline_code, source_point, destination_hub, length, diameter, air_pressure, air_velocity, status, last_inspection_date) VALUES
(1, 'PL001', 'CP001 - Central Plaza', 'HUB001 - Central Alpha', 1200.0, 50.0, 2.6, 24.0, 'ACTIVE', CURDATE() - INTERVAL 12 DAY),
(2, 'PL002', 'CP002 - Tech Park', 'HUB001 - Central Alpha', 1850.0, 50.0, 2.8, 26.5, 'ACTIVE', CURDATE() - INTERVAL 25 DAY),
(3, 'PL003', 'CP003 - Green Valley', 'HUB001 - Central Alpha', 2400.0, 60.0, 1.5, 15.0, 'ACTIVE', CURDATE() - INTERVAL 45 DAY),
(4, 'PL004', 'CP004 - Metro Complex', 'HUB002 - Southside Beta', 980.0, 50.0, 2.5, 23.0, 'ACTIVE', CURDATE() - INTERVAL 8 DAY),
(5, 'PL005', 'CP005 - Medical Quarter', 'HUB002 - Southside Beta', 1650.0, 45.0, 4.2, 31.0, 'BLOCKED', CURDATE() - INTERVAL 3 DAY);

-- Insert 2 Transport Hubs
INSERT INTO transport_hub (id, hub_code, hub_name, location, maximum_capacity, current_waste, utilization, status) VALUES
(1, 'HUB001', 'Central Pneumatic Terminal Alpha', 'Sector 4 Underground Deep Vault', 10000.0, 6850.0, 68.5, 'ACTIVE'),
(2, 'HUB002', 'Southside Automated Sorting Hub Beta', 'Sub-level 2 Industrial Logistics Zone', 8000.0, 6720.0, 84.0, 'ACTIVE');

-- Insert 11 Waste Transport Records
INSERT INTO waste_transport (id, bin_id, pipeline_id, hub_id, waste_type, quantity, start_time, end_time, status) VALUES
(1, 1, 1, 1, 'ORGANIC', 380.0, NOW() - INTERVAL 14 HOUR, NOW() - INTERVAL 14 HOUR + INTERVAL 6 MINUTE, 'COMPLETED'),
(2, 2, 1, 1, 'RECYCLABLE', 290.0, NOW() - INTERVAL 13 HOUR, NOW() - INTERVAL 13 HOUR + INTERVAL 4 MINUTE, 'COMPLETED'),
(3, 3, 2, 1, 'PLASTIC', 280.0, NOW() - INTERVAL 11 HOUR, NOW() - INTERVAL 11 HOUR + INTERVAL 5 MINUTE, 'COMPLETED'),
(4, 4, 2, 1, 'PAPER', 220.0, NOW() - INTERVAL 10 HOUR, NOW() - INTERVAL 10 HOUR + INTERVAL 3 MINUTE, 'COMPLETED'),
(5, 5, 3, 1, 'ORGANIC', 450.0, NOW() - INTERVAL 9 HOUR, NOW() - INTERVAL 9 HOUR + INTERVAL 8 MINUTE, 'COMPLETED'),
(6, 6, 3, 1, 'GENERAL', 310.0, NOW() - INTERVAL 8 HOUR, NOW() - INTERVAL 8 HOUR + INTERVAL 7 MINUTE, 'COMPLETED'),
(7, 7, 4, 2, 'PLASTIC', 340.0, NOW() - INTERVAL 6 HOUR, NOW() - INTERVAL 6 HOUR + INTERVAL 4 MINUTE, 'COMPLETED'),
(8, 8, 4, 2, 'RECYCLABLE', 270.0, NOW() - INTERVAL 5 HOUR, NOW() - INTERVAL 5 HOUR + INTERVAL 4 MINUTE, 'COMPLETED'),
(9, 9, 4, 2, 'HAZARDOUS', 180.0, NOW() - INTERVAL 3 HOUR, NOW() - INTERVAL 3 HOUR + INTERVAL 3 MINUTE, 'COMPLETED'),
(10, 10, 4, 2, 'GENERAL', 390.0, NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 2 HOUR + INTERVAL 5 MINUTE, 'COMPLETED'),
(11, 1, 1, 1, 'ORGANIC', 220.0, NOW() - INTERVAL 45 MINUTE, NOW() - INTERVAL 40 MINUTE, 'COMPLETED');

-- Insert Waste Processing Records
INSERT INTO waste_processing (id, hub_id, waste_type, quantity, processing_method, processing_date, status) VALUES
(1, 1, 'ORGANIC', 1200.0, 'ANAEROBIC_BIO_DIGESTION', NOW() - INTERVAL 12 HOUR, 'COMPLETED'),
(2, 1, 'RECYCLABLE', 850.0, 'AUTOMATED_OPTICAL_SORTING', NOW() - INTERVAL 10 HOUR, 'COMPLETED'),
(3, 1, 'PAPER', 600.0, 'HYDRAULIC_BALING', NOW() - INTERVAL 8 HOUR, 'COMPLETED'),
(4, 2, 'PLASTIC', 950.0, 'PELLETIZING_RECOVERY', NOW() - INTERVAL 5 HOUR, 'COMPLETED'),
(5, 2, 'HAZARDOUS', 300.0, 'HIGH_TEMP_PLASMA_GASIFICATION', NOW() - INTERVAL 3 HOUR, 'COMPLETED'),
(6, 2, 'GENERAL', 1100.0, 'REFUSE_DERIVED_FUEL_CONVERSION', NOW() - INTERVAL 1 HOUR, 'IN_PROGRESS');

-- Insert Maintenance Records
INSERT INTO maintenance (id, equipment_type, equipment_id, description, maintenance_date, technician, cost, status) VALUES
(1, 'PIPELINE', 'PL005', 'Emergency pneumatic clearing due to solid blockage at junction 4B', CURDATE(), 'Marcus Vance', 450.0, 'IN_PROGRESS'),
(2, 'PIPELINE', 'PL003', 'Exhauster turbine vacuum seal calibration and sensor check', CURDATE() - INTERVAL 1 DAY, 'David Chen', 180.0, 'PENDING'),
(3, 'TRANSPORT_HUB', 'HUB002', 'Automated compactor ram lubrication and load cell inspection', CURDATE() - INTERVAL 4 DAY, 'Elena Rostova', 320.0, 'COMPLETED'),
(4, 'COLLECTION_POINT', 'CP004', 'Inlet chute airlock valve replacement and proximity sensor test', CURDATE() - INTERVAL 6 DAY, 'Marcus Vance', 140.0, 'COMPLETED'),
(5, 'WASTE_BIN', 'BIN007', 'Ultrasonic fill level sensor cleaning and recalibration', CURDATE() - INTERVAL 9 DAY, 'Sarah Jenkins', 75.0, 'COMPLETED');

-- Insert Alerts
INSERT INTO alert (id, alert_type, equipment_type, equipment_id, message, severity, created_at, resolved, resolved_at) VALUES
(1, 'BIN_WARNING', 'WASTE_BIN', 'BIN001', 'Bin BIN001 fill level is at 84.0% (Threshold >= 80%). Dispatch transport soon.', 'WARNING', NOW() - INTERVAL 4 HOUR, FALSE, NULL),
(2, 'BIN_WARNING', 'WASTE_BIN', 'BIN005', 'Bin BIN005 fill level is at 86.7% (Threshold >= 80%). Dispatch transport soon.', 'WARNING', NOW() - INTERVAL 3 HOUR, FALSE, NULL),
(3, 'BIN_WARNING', 'WASTE_BIN', 'BIN007', 'Bin BIN007 fill level is at 96.3% (Threshold >= 80%). Dispatch transport soon.', 'WARNING', NOW() - INTERVAL 2 HOUR, FALSE, NULL),
(4, 'LOW_PRESSURE', 'PIPELINE', 'PL003', 'Underground pipeline PL003 air pressure is abnormally LOW (1.5 bar < 1.8 bar min threshold).', 'WARNING', NOW() - INTERVAL 5 HOUR, FALSE, NULL),
(5, 'PIPELINE_BLOCKED', 'PIPELINE', 'PL005', 'Underground pipeline PL005 (CP005 - Medical Quarter -> HUB002 - Southside Beta) is BLOCKED! Air flow stopped.', 'CRITICAL', NOW() - INTERVAL 1 HOUR, FALSE, NULL),
(6, 'HIGH_PRESSURE', 'PIPELINE', 'PL005', 'Underground pipeline PL005 air pressure is abnormally HIGH (4.2 bar > 3.8 bar max threshold). Risk of pneumatic seal rupture!', 'CRITICAL', NOW() - INTERVAL 1 HOUR, FALSE, NULL),
(7, 'HUB_CAPACITY_WARNING', 'TRANSPORT_HUB', 'HUB002', 'Transport Hub HUB002 (Southside Automated Sorting Hub Beta) utilization reached 84.0% (Threshold >= 80%). Initiate waste processing.', 'WARNING', NOW() - INTERVAL 30 MINUTE, FALSE, NULL);

-- =====================================================================
-- VERIFICATION QUERIES FOR MYSQL CLIENT
-- =====================================================================
-- SELECT * FROM collection_point;
-- SELECT * FROM waste_bin;
-- SELECT * FROM pipeline;
-- SELECT * FROM transport_hub;
-- SELECT * FROM waste_transport;
-- SELECT * FROM waste_processing;
-- SELECT * FROM maintenance;
-- SELECT * FROM alert;
