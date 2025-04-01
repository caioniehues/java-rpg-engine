# Using Docker with the ChatDM

_Contributed by [Brandon Whichard](https://github.com/bwhichard)._

# Docker Support

You can run ChatDM in a Docker container, which provides an isolated environment with all necessary dependencies. The included Dockerfile uses Eclipse Temurin Java 21 as the base image and sets up the application with its web interface enabled.

## Building and Running with Docker

1. Build the Docker image:
   ```bash
   docker build -t chatdm .
   ```

2. Run the container:
   ```bash
   docker run -d -p 8080:8080 chatdm
   ```

This will start the application and expose it on port 8080.

## Testing the Oracle Endpoints

Once the container is running, you can test the Oracle endpoints using curl or your web browser:

1. List all available oracles:
   ```bash
   curl http://localhost:8080/chatdm/api/oracle
   ```

2. Get information about all oracles:
   ```bash
   curl http://localhost:8080/chatdm/api/oracle/info
   ```

3. Get a random entry from a specific oracle (e.g., NPC_Motivations):
   ```bash
   curl http://localhost:8080/chatdm/api/oracle/NPC_Motivations/random
   ```

The responses will be in JSON format, making it easy to integrate with other tools or scripts.