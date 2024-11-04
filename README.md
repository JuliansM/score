# Microservicio de Score

Microservicio que obtiene el score total de los vehículos en circulación por año.

Comando Docker para construir la imagen de docker en Azure ACR
------------------------------------------------------------------
```az acr login --name scoremsjava```

```docker buildx build --platform linux/amd64,linux/arm64 -t scoremsjava.azurecr.io/score-ms:latest --push .```

Comandos Kubernetes para levantar contenedor desde CLI de Azure
------------------------------------------------------------------
### 1. Obtener credenciales de conexión al AKS aks-vehicle-platform
```az aks get-credentials --resource-group rg-vehicle-platform --name vehicle-score-platform```

### 2. Aplicar ConfigMap
```kubectl apply -f configmap.yaml```

### 3. Desplegar el servicio
```kubectl apply -f deployment.yaml```

### 4. Crear el servicio
```kubectl apply -f service.yaml```

### 5. Aplicar Horizontal Pods Auto-Scaler
```kubectl apply -f hpa.yaml```

### 6. Obtener el listado de Pods creados
```kubectl get pods -n vehicle-platform```