apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: labate
  spec:
    selector:
      matchLabels:
        app:  labate
    #number of replicas
    replicas: 2
    template:
      metadata:
        labels:
          app: labate
          version: "1.0"
      spec:
       # additional
       # ConfigMaps and Secrets can be mounted as environment variables or as files within a container.
        volumes:
          - name: sql-volume
            secret:
              secretName: example-secret
        containers:
          - name: labate
            image: gcr.io/$PROJECT_ID/labate:latest
            imagePullPolicy: "Always"
            volumeMounts:
              - name: sql-volume
                mountPath: /var/secrets/sql
            readinessProbe:
              tcpSocket:
                port: 8080
              initialDelaySeconds: 5
            livenessProbe:
              initialDelaySeconds: 5
              tcpSocket:
                port: 8080
            ports:
              - containerPort: 8080
            env:
              - name: GOOGLE_APPLICATION_CREDENTIALS
                value: /var/secrets/google/key.json
              - name: DB_USER
                valueFrom:
                  secretKeyRef:
                    name: sql-db-secret
                    key: username
              - name: DB_PASS
                valueFrom:
                  secretKeyRef:
                    name: sql-db-secret
                    key: password
              - name: DB_NAME
                valueFrom:
                  secretKeyRef:
                    name: sql-db-secret
                    key: database
  ---
  kind: Service
  apiVersion: v1
  metadata:
    name: labate-service
    labels:
      name: labate-service
  spec:
    ports:
      - port: 8080
        targetPort: 8080
        protocol: TCP
    type: LoadBalancer
    selector:
      app: labate
      version: "1.0"