# Use the Node.js v20.11.1 image
FROM node:20.11.1-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the package.json and package-lock.json files
COPY package*.json ./

# Install the dependencies
RUN npm install

# Copy the rest of the application files
COPY . .

# Build the React application
RUN npm run build

# Install the 'serve' package globally to serve the built app
RUN npm install -g serve

# Expose port 3000
EXPOSE 3000

# Serve the application on port 3000
CMD ["serve", "-s", "build", "-l", "3000"]
