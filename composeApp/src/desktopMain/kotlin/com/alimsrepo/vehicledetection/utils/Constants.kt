/**
 * Application constants
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.utils

object Constants {
    // Network configuration
    const val DEFAULT_SERVER_HOST = "localhost"
    const val DEFAULT_SERVER_PORT = 1234
    const val DEFAULT_SERVER_URL = "http://$DEFAULT_SERVER_HOST:$DEFAULT_SERVER_PORT"
    const val DEFAULT_WEBSOCKET_URL = "ws://$DEFAULT_SERVER_HOST:$DEFAULT_SERVER_PORT/ws"
    
    // API endpoints
    const val ENDPOINT_HEALTH = "/health"
    const val ENDPOINT_STATS = "/stats"
    const val ENDPOINT_CONFIG = "/config"
    const val ENDPOINT_PERFORMANCE = "/performance"
    const val ENDPOINT_CONTROL_PLAYBACK = "/control/playback"
    const val ENDPOINT_CONTROL_DETECTION = "/control/detection"
    const val ENDPOINT_CONTROL_BROADCAST = "/control/broadcast"
    const val ENDPOINT_CONTROL_DEVICE = "/control/device"
    
    // WebSocket commands
    const val WS_COMMAND_GET_STATS = "get_stats"
    const val WS_COMMAND_GET_SYSTEM_INFO = "get_system_info"
    const val WS_COMMAND_RESET_STATS = "reset_stats"
    const val WS_COMMAND_GET_CLIENTS = "get_clients"
    
    // UI configuration
    const val APP_TITLE = "M1 Vehicle Detection Desktop Client"
    const val MIN_WINDOW_WIDTH = 1200
    const val MIN_WINDOW_HEIGHT = 800
    const val DEFAULT_WINDOW_WIDTH = 1600
    const val DEFAULT_WINDOW_HEIGHT = 1000
    
    // Performance
    const val STATS_UPDATE_INTERVAL_MS = 2000L
    const val RECONNECT_INTERVAL_MS = 5000L
    const val CONNECTION_TIMEOUT_MS = 10000L
    
    // Vehicle classes
    val VEHICLE_CLASSES = listOf("car", "truck", "bus", "motorcycle", "bicycle")
    
    // Colors for vehicle detection
    val VEHICLE_COLORS = mapOf(
        "car" to 0xFF00FF00,        // Green
        "truck" to 0xFF0000FF,      // Blue
        "bus" to 0xFFFF0000,        // Red
        "motorcycle" to 0xFFFFFF00, // Yellow
        "bicycle" to 0xFFFF00FF     // Magenta
    )
}