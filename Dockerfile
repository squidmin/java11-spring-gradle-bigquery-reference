FROM eclipse-temurin:11-jdk-alpine

### CLI arguments ###
ARG GCP_PROJECT_ID
ARG GOOGLE_APPLICATION_CREDENTIALS
###

### Environment variables ###
# OS
ENV APP_DIR=/usr/local/app
# JVM arguments.
ENV GCP_PROJECT_ID=$GCP_PROJECT_ID
ENV GOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
###

# Copy the project into the container.
ADD . ${APP_DIR}

ENTRYPOINT ["sh", "-c", "cd /usr/local/app && sh"]
