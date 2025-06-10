package com.example.gateway.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.cloud.gateway.route.RouteDefinitionLocator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class OpenApiConfig(
    @Lazy private val routeDefinitionLocator: RouteDefinitionLocator
) {
    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("Classroom Management API")
                .description("Documentation for all services")
                .version("1.0.0")
        )

    @Bean
    fun serviceApis(): List<GroupedOpenApi> {
        val groups = mutableListOf<GroupedOpenApi>()
        val definitions = routeDefinitionLocator.routeDefinitions.collectList().block()

        // Add API Gateway's own API
        groups.add(
            GroupedOpenApi.builder()
                .group("api-gateway")
                .pathsToMatch("/**")
                .build()
        )

        // Add APIs for each service
        definitions?.forEach { routeDefinition ->
            routeDefinition.id?.let { serviceName ->
                groups.add(
                    GroupedOpenApi.builder()
                        .group(serviceName)
                        .pathsToMatch("/$serviceName/**")
                        .build()
                )
            }
        }

        return groups
    }
}