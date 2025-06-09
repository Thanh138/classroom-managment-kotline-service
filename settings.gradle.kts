rootProject.name = "classroom-management"

// Core services
include(":api-gateway")
include(":service-discovery")
include(":config-server")

// Business services
include(":student-service")
include(":classroom-service")
include(":assignment-service")
include(":grade-service")

// Shared libraries
include(":shared:common-dto")
include(":shared:common-exceptions")
include("api-gateway")
include("api-gateway:service-discovery")
include("auth-service")
include("student-service")
include("student-service:classroom-service")
include("student-service")
include("classroom-service")
include("assignment-service")
include("grade-service")
include("shared")
include("shared:common-dto")
include("shared:common-exceptions")
include("service-discovery")
include("service-discovery")
include("config-server")