# Urban Automated Underground Waste Pneumatic Transport Hub

A comprehensive, full-stack, enterprise-grade simulation and monitoring system for automated underground pneumatic waste transportation networks in modern smart cities.

---

## 1. Project Introduction

Rapid urban density expansion has overwhelmed traditional surface vehicular garbage collection, causing traffic congestion, greenhouse gas emissions, olfactory pollution, and irregular collection cycles. 

The **Urban Automated Underground Waste Pneumatic Transport Hub** is a smart-city infrastructure management platform designed to replace conventional trash trucks with a subsurface high-velocity pneumatic vacuum conduit network.

Solid waste deposited at surface smart collection points is sucked through underground stainless steel pipelines at speeds between **70 km/h and 100 km/h (20–30 m/s)** into centralized compaction and processing terminals. This system provides complete real-time monitoring of:
- **Surface Smart Collection Points & Inlets**
- **Smart Waste Bins** with ultrasonic fill-level sensors & automatic threshold alerting
- **Underground Pneumatic Pipelines** with differential vacuum pressure & airflow velocity tracking
- **Central Transport Hubs & Compactor Vaults** with capacity utilization monitoring
- **Automated Pneumatic Transport Dispatch & Simulation**
- **Material Recovery & Processing** (Composting, Optical Sorting, Baling, Vitrification)
- **Preventative Equipment Maintenance & Technician Management**
- **Autonomous Rule-Based Safety Alerts**

---

## 2. Key Features

### A. Real-Time Telemetry Dashboard
- Displays live cards for:
  - Total Collection Points
  - Total Waste Bins
  - Full Bins (Threshold &ge; 80%)
  - Active Pipelines
  - Active Transport Hubs
  - Waste Collected Today
  - Waste Transported Today
  - Active Safety Alerts
- Live table telemetry for Bins, Pipelines, Hubs, and Safety Incidents.
- Animated underground pneumatic vacuum capsule flow simulation.

### B. Smart Collection Point Management
- Full CRUD operations: Create, Read, Update, Delete.
- GPS coordinates (latitude, longitude), location status, and linked bins tracking.

### C. Smart Waste Bin Management
- Bin Code, Collection Point, Waste Category (Organic, Recyclable, Plastic, Paper, General, Hazardous), Maximum Capacity, and Current Fill Level.
- Formula calculation:
  $$\text{Fill Percentage} = \left(\frac{\text{Current Waste}}{\text{Maximum Capacity}}\right) \times 100$$
- Automated Classification:
  - **0% – 60%** &rarr; `NORMAL`
  - **61% – 80%** &rarr; `WARNING`
  - **81% – 100%** &rarr; `FULL` (Automatic Alert Generation)

### D. Underground Pneumatic Pipeline Management
- Pipeline Code, Source Location, Destination Hub, Length (m), Diameter (cm), Air Pressure (bar), Air Velocity (m/s), and Status (`ACTIVE`, `INACTIVE`, `BLOCKED`, `MAINTENANCE`).
- Autonomous Rule-Based Pressure Telemetry:
  - Air Pressure $< 1.8\text{ bar}$ &rarr; `LOW_PRESSURE` Warning
  - Air Pressure $> 3.8\text{ bar}$ &rarr; `HIGH_PRESSURE` Critical Alert
  - Status `BLOCKED` &rarr; `PIPELINE_BLOCKED` Critical Alert

### E. Transport Hub Management
- Centralized cyclone separation terminals and compactor silos.
- Formula calculation:
  $$\text{Utilization} = \left(\frac{\text{Current Waste}}{\text{Maximum Capacity}}\right) \times 100$$
- Autonomous Safety Thresholds:
  - Utilization $> 80\%$ &rarr; `HUB_CAPACITY_WARNING`
  - Utilization $> 95\%$ &rarr; `HUB_CAPACITY_CRITICAL`

### F. Pneumatic Waste Transport Simulator
- Implements complete physics and business validation logic:
  1. Validates that the selected bin contains waste.
  2. Ensures the routing pipeline is in `ACTIVE` operational state.
  3. Ensures the destination receiving hub has sufficient available capacity.
  4. Creates transport record with status `IN_TRANSIT`.
  5. Deducts waste from the source bin; updates bin metrics and records `lastEmptied` timestamp if emptied.
  6. Increases waste in the destination hub; updates utilization metrics.
  7. Updates transport record to `COMPLETED`.
  8. Triggers autonomous safety checks on all modified equipment.

### G. Central Hub Waste Processing
- Categorized treatment pipelines:
  - **ORGANIC** &rarr; Anaerobic Bio-Digestion & Methane Capture
  - **RECYCLABLE** &rarr; Automated Optical Sensor Sorting
  - **PLASTIC** &rarr; Shredding & Pelletizing
  - **PAPER** &rarr; Hydraulic Compression & Baling
  - **GENERAL** &rarr; Refuse-Derived Fuel Gasification
  - **HAZARDOUS** &rarr; High-Temperature Plasma Vitrification
- Deducts processed waste from the hub silo upon completion.

### H. Equipment Maintenance Desk
- Scheduled and emergency work orders for Pipelines, Collection Points, Waste Bins, and Transport Hubs.
- Tracks lead technicians, cost, description, and status (`PENDING`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`).

### I. Safety Alert Engine
- Autonomous background rule engine creating categorized alerts (`INFO`, `WARNING`, `CRITICAL`).
- One-click incident resolution endpoint (`PUT /api/alerts/{id}/resolve`).

---

## 3. Technology Stack

Strictly implemented according to project specifications without external frontend frameworks:

| Component | Technology | Version / Specification |
| :--- | :--- | :--- |
| **Frontend Structure** | Semantic HTML5 | Pure HTML5 semantic elements |
| **Frontend Styling** | Vanilla CSS3 | Custom Design System, Cyberpunk/Obsidian Glassmorphism |
| **Frontend Scripting**| Vanilla JavaScript | Fetch API, DOM manipulation (No React/Vue/Angular) |
| **Backend Framework** | Java / Spring Boot | Spring Boot 3.3.4, Java 21 LTS |
| **Data Persistence**  | Spring Data JPA / Hibernate | Object-Relational Mapping (ORM) |
| **Database**          | MySQL Server 8.0 | InnoDB Engine, Foreign Keys, UTF8mb4 |
| **API Testing**       | Postman & MySQL Client | Postman Collection JSON, Native MySQL CLI |

---

## 4. System Architecture

```text
+-------------------------------------------------------------+
|                     CLIENT TIER                             |
|  - HTML5 Pages (index.html, dashboard.html, etc.)           |
|  - Custom CSS3 Design System (css/style.css)                |
|  - Vanilla JavaScript Fetch API (js/app.js)                 |
+------------------------------+------------------------------+
                               |
                               | HTTP / JSON REST APIs
                               v
+-------------------------------------------------------------+
|                   SPRING BOOT BACKEND                       |
|  - Controller Layer: REST Endpoints (@CrossOrigin)          |
|  - Service Layer: Business Logic & Simulation Engine        |
|  - Exception Handling: GlobalExceptionHandler (@Valid)      |
|  - Repository Layer: Spring Data JPA (HikariCP)             |
+------------------------------+------------------------------+
                               |
                               | JDBC Driver (MySQL Connector/J)
                               v
+-------------------------------------------------------------+
|                     DATABASE TIER                           |
|  - MySQL 8.0 Server (urban_waste_hub)                       |
|  - Relational Schema with Foreign Key Constraints           |
|  - Direct Verification via MySQL Client                     |
+-------------------------------------------------------------+
```

---

## 5. Database Schema & ER Architecture

The database `urban_waste_hub` contains 8 primary relational tables:

```text
[collection_point] (1) <------- (N) [waste_bin]
       |                                   |
       |                                   | (1)
       |                                   |
       |                                   v (N)
[pipeline] (1) <------------------- [waste_transport]
       ^                                   |
       |                                   | (N)
       |                                   v
[transport_hub] (1) <----------------------+ (1)
       |
       +------ (1) <---------------- (N) [waste_processing]

[maintenance]   (Equipment-based auditing)
[alert]         (System safety & sensor rule events)
```

### Table Definitions:

1. **`collection_point`**:
   - `id` (BIGINT, PK, AI)
   - `point_code` (VARCHAR, UNIQUE)
   - `location` (VARCHAR)
   - `latitude` (DOUBLE), `longitude` (DOUBLE)
   - `status` (VARCHAR: `ACTIVE`, `INACTIVE`, `MAINTENANCE`)
   - `created_at` (DATETIME)

2. **`waste_bin`**:
   - `id` (BIGINT, PK, AI)
   - `bin_code` (VARCHAR, UNIQUE)
   - `collection_point_id` (BIGINT, FK &rarr; `collection_point.id`)
   - `waste_type` (VARCHAR: `ORGANIC`, `RECYCLABLE`, `PLASTIC`, `PAPER`, `GENERAL`, `HAZARDOUS`)
   - `capacity` (DOUBLE)
   - `current_waste` (DOUBLE)
   - `fill_percentage` (DOUBLE)
   - `status` (VARCHAR: `NORMAL`, `WARNING`, `FULL`)
   - `last_emptied` (DATETIME)

3. **`pipeline`**:
   - `id` (BIGINT, PK, AI)
   - `pipeline_code` (VARCHAR, UNIQUE)
   - `source_point` (VARCHAR), `destination_hub` (VARCHAR)
   - `length` (DOUBLE), `diameter` (DOUBLE)
   - `air_pressure` (DOUBLE), `air_velocity` (DOUBLE)
   - `status` (VARCHAR: `ACTIVE`, `INACTIVE`, `BLOCKED`, `MAINTENANCE`)
   - `last_inspection_date` (DATE)

4. **`transport_hub`**:
   - `id` (BIGINT, PK, AI)
   - `hub_code` (VARCHAR, UNIQUE)
   - `hub_name` (VARCHAR), `location` (VARCHAR)
   - `maximum_capacity` (DOUBLE), `current_waste` (DOUBLE), `utilization` (DOUBLE)
   - `status` (VARCHAR: `ACTIVE`, `STANDBY`, `MAINTENANCE`, `FULL`)

5. **`waste_transport`**:
   - `id` (BIGINT, PK, AI)
   - `bin_id` (BIGINT, FK &rarr; `waste_bin.id`)
   - `pipeline_id` (BIGINT, FK &rarr; `pipeline.id`)
   - `hub_id` (BIGINT, FK &rarr; `transport_hub.id`)
   - `waste_type` (VARCHAR), `quantity` (DOUBLE)
   - `start_time` (DATETIME), `end_time` (DATETIME)
   - `status` (VARCHAR: `SCHEDULED`, `IN_TRANSIT`, `COMPLETED`, `CANCELLED`)

6. **`waste_processing`**:
   - `id` (BIGINT, PK, AI)
   - `hub_id` (BIGINT, FK &rarr; `transport_hub.id`)
   - `waste_type` (VARCHAR), `quantity` (DOUBLE)
   - `processing_method` (VARCHAR), `processing_date` (DATETIME), `status` (VARCHAR)

7. **`maintenance`**:
   - `id` (BIGINT, PK, AI)
   - `equipment_type` (VARCHAR), `equipment_id` (VARCHAR)
   - `description` (TEXT), `maintenance_date` (DATE), `technician` (VARCHAR), `cost` (DOUBLE), `status` (VARCHAR)

8. **`alert`**:
   - `id` (BIGINT, PK, AI)
   - `alert_type` (VARCHAR), `equipment_type` (VARCHAR), `equipment_id` (VARCHAR)
   - `message` (TEXT), `severity` (VARCHAR: `INFO`, `WARNING`, `CRITICAL`)
   - `created_at` (DATETIME), `resolved` (BOOLEAN), `resolved_at` (DATETIME)

---

## 6. Project Structure

```text
urban-waste-hub/
│
├── backend/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── urbanwastehub/
│           │           ├── controller/
│           │           │   ├── AlertController.java
│           │           │   ├── CollectionPointController.java
│           │           │   ├── DashboardController.java
│           │           │   ├── MaintenanceController.java
│           │           │   ├── PipelineController.java
│           │           │   ├── TransportHubController.java
│           │           │   ├── WasteBinController.java
│           │           │   ├── WasteProcessingController.java
│           │           │   └── WasteTransportController.java
│           │           ├── service/
│           │           │   ├── AlertService.java
│           │           │   ├── CollectionPointService.java
│           │           │   ├── DashboardService.java
│           │           │   ├── DataInitializationService.java
│           │           │   ├── MaintenanceService.java
│           │           │   ├── PipelineService.java
│           │           │   ├── TransportHubService.java
│           │           │   ├── WasteBinService.java
│           │           │   ├── WasteProcessingService.java
│           │           │   └── WasteTransportService.java
│           │           ├── repository/
│           │           │   ├── AlertRepository.java
│           │           │   ├── CollectionPointRepository.java
│           │           │   ├── MaintenanceRepository.java
│           │           │   ├── PipelineRepository.java
│           │           │   ├── TransportHubRepository.java
│           │           │   ├── WasteBinRepository.java
│           │           │   ├── WasteProcessingRepository.java
│           │           │   └── WasteTransportRepository.java
│           │           ├── entity/
│           │           │   ├── Alert.java
│           │           │   ├── CollectionPoint.java
│           │           │   ├── Maintenance.java
│           │           │   ├── Pipeline.java
│           │           │   ├── TransportHub.java
│           │           │   ├── WasteBin.java
│           │           │   ├── WasteProcessing.java
│           │           │   └── WasteTransport.java
│           │           ├── dto/
│           │           │   ├── CollectionPointDTO.java
│           │           │   ├── DashboardStatsDTO.java
│           │           │   ├── MaintenanceDTO.java
│           │           │   ├── PipelineDTO.java
│           │           │   ├── TransportHubDTO.java
│           │           │   ├── WasteBinDTO.java
│           │           │   ├── WasteProcessingRequest.java
│           │           │   └── WasteTransportRequest.java
│           │           ├── exception/
│           │           │   ├── BadRequestException.java
│           │           │   ├── GlobalExceptionHandler.java
│           │           │   └── ResourceNotFoundException.java
│           │           └── UrbanWasteHubApplication.java
│           │
│           └── resources/
│               ├── application.properties
│               └── static/ (All compiled frontend assets)
│
├── frontend/
│   ├── index.html
│   ├── dashboard.html
│   ├── collection-points.html
│   ├── bins.html
│   ├── pipelines.html
│   ├── hubs.html
│   ├── transports.html
│   ├── processing.html
│   ├── maintenance.html
│   ├── alerts.html
│   ├── css/
│   │   └── style.css
│   └── js/
│       └── app.js
│
├── database/
│   └── database.sql
│
├── postman/
│   └── urban-waste-hub.postman_collection.json
│
└── README.md
```

---

## 7. MySQL Setup & Configuration

### A. Entering Your MySQL Password
Open:
`urban-waste-hub/backend/src/main/resources/application.properties`

Configure your MySQL connection details:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/urban_waste_hub?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
# ENTER YOUR MYSQL PASSWORD HERE:
spring.datasource.password=mysql1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### B. Initializing Database Schema & Seed Data
You can run the provided `database.sql` script directly in your MySQL Client or command prompt:
```bash
mysql -u root -p urban_waste_hub < database/database.sql
```
*(Or the Spring Boot application will automatically initialize tables and seed data upon startup!)*

---

## 8. How to Run the System

### Step 1: Start the Spring Boot Backend
Open a terminal in `urban-waste-hub/backend`:
```bash
# Compile and build the executable JAR:
mvn clean package -DskipTests

# Run the Spring Boot application:
java -jar target/urban-waste-hub-1.0.0.jar
```
Tomcat will launch on **`http://localhost:8080`**.

### Step 2: Open the Frontend
You can access the frontend in either of two ways:
1. **Via Spring Boot Server (Recommended)**:
   Navigate to:
   - **`http://localhost:8080/`** (Landing Portal)
   - **`http://localhost:8080/dashboard.html`** (Live Telemetry Dashboard)
   - **`http://localhost:8080/transports.html`** (Pneumatic Transport Simulation)
2. **Direct Browser File Access**:
   Double click `frontend/index.html` or `frontend/dashboard.html` in your file explorer. All JavaScript fetch calls will connect seamlessly to `http://localhost:8080/api` through global CORS support!

---

## 9. Postman REST API Testing Guide

An importable Postman collection file is provided at:
`urban-waste-hub/postman/urban-waste-hub.postman_collection.json`

### Import into Postman:
1. Open Postman &rarr; Click **Import** &rarr; Select `urban-waste-hub.postman_collection.json`.
2. All 8 module folders with pre-configured requests will load into your workspace.

### Key API Endpoint Summary:

| Module | Method | URL | Description | Expected Status |
| :--- | :--- | :--- | :--- | :--- |
| **Dashboard** | `GET` | `/api/dashboard/stats` | System KPI telemetry cards | `200 OK` |
| **Collection Points** | `GET` | `/api/collection-points` | List all intake points | `200 OK` |
| | `POST` | `/api/collection-points` | Register new collection point | `201 CREATED` |
| | `PUT` | `/api/collection-points/{id}` | Update point details | `200 OK` |
| | `DELETE` | `/api/collection-points/{id}` | Remove collection point | `200 OK` |
| **Smart Bins** | `GET` | `/api/bins` | List all smart bins & fill levels | `200 OK` |
| | `POST` | `/api/bins` | Register bin (Auto alerts if $\ge 80\%$) | `201 CREATED` |
| | `PUT` | `/api/bins/{id}` | Update bin current waste | `200 OK` |
| | `DELETE` | `/api/bins/{id}` | Delete smart bin | `200 OK` |
| **Pipelines** | `GET` | `/api/pipelines` | List vacuum conduits & pressure | `200 OK` |
| | `POST` | `/api/pipelines` | Add pipeline (Pressure rule check) | `201 CREATED` |
| | `PUT` | `/api/pipelines/{id}` | Update pipeline telemetry / status | `200 OK` |
| **Transport Hubs** | `GET` | `/api/hubs` | List terminals & capacity | `200 OK` |
| | `POST` | `/api/hubs` | Register new transport hub | `201 CREATED` |
| | `PUT` | `/api/hubs/{id}` | Update capacity metrics | `200 OK` |
| **Transports** | `POST` | `/api/transports` | Execute pneumatic evacuation | `201 CREATED` |
| | `GET` | `/api/transports` | Audit trail of all transits | `200 OK` |
| **Processing** | `POST` | `/api/processing` | Log waste treatment batch | `201 CREATED` |
| | `GET` | `/api/processing` | View treatment history | `200 OK` |
| **Maintenance** | `POST` | `/api/maintenance` | Log maintenance ticket | `201 CREATED` |
| | `GET` | `/api/maintenance` | View equipment service tickets | `200 OK` |
| **Alerts** | `GET` | `/api/alerts/active` | Retrieve active safety alerts | `200 OK` |
| | `PUT` | `/api/alerts/{id}/resolve`| Resolve safety alert | `200 OK` |

### Sample JSON Requests:

#### 1. Create Smart Waste Bin (`POST /api/bins`)
```json
{
  "binCode": "BIN011",
  "collectionPointId": 1,
  "wasteType": "ORGANIC",
  "capacity": 500.0,
  "currentWaste": 425.0
}
```
*Note: Because 425 / 500 = 85.0%, Spring Boot will automatically classify this bin as `FULL` and autonomously generate a `BIN_WARNING` alert in the database!*

#### 2. Execute Pneumatic Transport Simulation (`POST /api/transports`)
```json
{
  "binId": 11,
  "pipelineId": 1,
  "hubId": 1,
  "quantity": 300.0
}
```
*Response (`201 CREATED`):*
```json
{
  "id": 12,
  "bin": { "binCode": "BIN011", "currentWaste": 125.0, "fillPercentage": 25.0, "status": "NORMAL" },
  "pipeline": { "pipelineCode": "PL001", "status": "ACTIVE" },
  "hub": { "hubCode": "HUB001", "currentWaste": 7150.0, "utilization": 71.5 },
  "wasteType": "ORGANIC",
  "quantity": 300.0,
  "status": "COMPLETED"
}
```

---

## 10. MySQL Client Verification Commands

To demonstrate the real-time database connection in your project viva or presentation, open the MySQL Client command line:

```sql
-- 1. Connect to the database
USE urban_waste_hub;

-- 2. Verify all 8 tables
SHOW TABLES;

-- 3. Check Collection Points
SELECT id, point_code, location, status FROM collection_point;

-- 4. Check Smart Waste Bins and Fill Levels
SELECT id, bin_code, waste_type, capacity, current_waste, fill_percentage, status FROM waste_bin;

-- 5. Check Pneumatic Pipelines and Vacuum Pressure
SELECT id, pipeline_code, source_point, destination_hub, air_pressure, air_velocity, status FROM pipeline;

-- 6. Check Transport Hubs & Silo Utilization
SELECT id, hub_code, hub_name, maximum_capacity, current_waste, utilization, status FROM transport_hub;

-- 7. Verify Completed Pneumatic Transports
SELECT id, bin_id, pipeline_id, hub_id, waste_type, quantity, status FROM waste_transport ORDER BY id DESC;

-- 8. Verify Autonomous System Safety Alerts
SELECT id, alert_type, equipment_type, equipment_id, severity, message, resolved FROM alert WHERE resolved = FALSE;
```

---

## 11. Presentation Explanation (For College Viva & Demonstration)

### Elevator Pitch:
> "Our project implements an **Urban Automated Underground Waste Pneumatic Transport Hub** designed to modernize smart-city solid waste management. Rather than relying on diesel garbage trucks that cause traffic congestion, carbon emissions, and odor pollution, our system uses underground high-velocity vacuum pipelines to transport solid refuse directly from street inlets to centralized compaction hubs at speeds over 70 km/h.
>
> Our architecture consists of:
> 1. A pure **HTML5/CSS3/Vanilla JavaScript** frontend featuring real-time telemetry dashboards, interactive pipeline simulation, and responsive equipment management.
> 2. A **Java Spring Boot REST API** backend enforcing layered architecture (Controller &rarr; Service &rarr; Repository), global exception handling, and autonomous rule-based alert engines.
> 3. A **MySQL Database** maintaining transactional integrity with foreign keys, auditing, and real-time synchronization.
>
> Whenever a transport is dispatched, the system dynamically verifies bin waste levels, validates active pipeline vacuum pressure, confirms receiving hub capacity, atomically transfers the waste, updates sensor percentages, and generates automated safety alerts."

### Key Viva Questions & Answers:
1. **Q: How does the simulation work without physical IoT hardware?**
   - *A: We model sensor parameters—such as ultrasonic fill level, barometric differential air pressure, and airflow velocity—as real-time variables in MySQL. When transactions occur via REST API or the UI simulator, the Service layer applies mathematical models to update fill percentages, pipeline statuses, and hub utilization, while autonomous listeners check for threshold violations.*
2. **Q: How are alerts autonomously generated?**
   - *A: In `AlertService`, whenever a bin exceeds 80% or 100% capacity, a pipeline's pressure deviates outside the safe 1.8–3.8 bar range, a pipeline becomes BLOCKED, or a hub exceeds 80% or 95% utilization, the system immediately inserts a deduplicated alert record into MySQL, which is reflected in real time on the dashboard.*
3. **Q: How is data consistency maintained during pneumatic transport?**
   - *A: Spring's `@Transactional` annotation guarantees atomic execution. Deducting waste from the source bin, adding waste to the receiving hub, updating metrics, and recording the transport log all succeed or roll back together.*

---

## 12. Troubleshooting

1. **MySQL Access Denied (Error 1045)**:
   - Ensure the password entered in `application.properties` matches your local MySQL `root` user password.
2. **Port 8080 already in use**:
   - Change `server.port=8081` in `application.properties` and update `const API_BASE = 'http://localhost:8081/api'` in `frontend/js/app.js`.
3. **Data not showing on frontend**:
   - Ensure the Spring Boot backend is running (`java -jar target/urban-waste-hub-1.0.0.jar`).
   - Check the browser developer tools console (F12) to confirm successful HTTP calls to `http://localhost:8080/api/...`.
