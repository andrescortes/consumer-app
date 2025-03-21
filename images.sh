#!/bin/bash

# Variables
DOCKER_USER="devcoderjava"
REPO_NAME="consumer-app"
TAG="latest"  # Change this if needed
IMAGE_NAME="$DOCKER_USER/$REPO_NAME:$TAG"

# Build the Docker image
echo "🔨 Building Docker image: $IMAGE_NAME..."
if docker build -t $IMAGE_NAME .; then
    echo "✅ Build successful!"
else
    echo "❌ Build failed!" >&2
    exit 1
fi

# Push the image to Docker Hub
echo "🚀 Pushing image to Docker Hub..."
if docker push $IMAGE_NAME; then
    echo "✅ Image pushed successfully: $IMAGE_NAME"
else
    echo "❌ Image push failed!" >&2
    exit 1
fi

echo "🎉 Done!"
