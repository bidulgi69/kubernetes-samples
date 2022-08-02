rootProject.name = "microservice-deploy-kubernetes"

include(
    ":api",
    ":microservices:product-service",
    ":microservices:review-service",
    ":microservices:product-composite-service"
)
