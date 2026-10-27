FROM node:22-alpine

WORKDIR /app

# Copy express-server
COPY ./express-server ./express-server

# Copy angular (client)
COPY ./angular/dist/cirras-underwriting ./angular/dist/cirras-underwriting

# Install server deps
WORKDIR /app/express-server
RUN npm clean-install --only=production

EXPOSE 8080

CMD ["node", "server.js"]