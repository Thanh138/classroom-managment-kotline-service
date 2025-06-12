rootProject.name = "classroom-management"

// Core services
include(":api-gateway")
include(":service-discovery")
include(":config-server")

// Business services
include(":auth-service")
include(":student-service")
include(":classroom-service")
include(":assignment-service")
include(":grade-service")

// Shared libraries
include(":shared:common-dto")
include(":shared:common-exceptions")

// Create the shared directory structure
file("shared/common-dto").mkdirs()

// Additional includes
include("api-gateway")
include("api-gateway:service-discovery")
include("student-service")
include("student-service:classroom-service")
include("classroom-service")
include("assignment-service")
include("grade-service")
include("shared")
include("service-discovery")

include(
    "api-gateway",
    "auth-service",
    "student-service",
    "config-server",
    "shared:common-dto"
)