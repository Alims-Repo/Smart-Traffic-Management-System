# Smart Traffic Management System - Detection Resources

This directory contains video files and other resources for the vehicle detection service.

## Video Files

For development and testing, place your traffic video files here:
- `1.mp4` - Primary test video file
- Add additional video files as needed

## Video Requirements

- Format: MP4, AVI, MOV
- Resolution: 720p or higher recommended
- Frame rate: 15-30 FPS
- Content: Traffic scenes with vehicles

## Sample Data

If no video file is available, the system will:
1. Generate synthetic traffic data for testing
2. Use a webcam if available
3. Provide mock detection results

## Usage

The detection service automatically detects video files in this directory and uses them for vehicle detection and traffic analysis.