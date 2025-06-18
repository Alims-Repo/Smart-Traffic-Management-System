package com.gub

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import oshi.SystemInfo
import oshi.hardware.CentralProcessor.TickType
import kotlin.concurrent.thread

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        get("/") {
//            call.respondText("Ktor: ${Greeting().greet()}")
            call.respondText(getSystemStatus())
        }
    }
}


fun getSystemStatus(): String {
    val si = SystemInfo()
    val hardware = si.hardware

    // CPU usage
    val processor = hardware.processor
    val prevTicks = processor.systemCpuLoadTicks
    Thread.sleep(1000) // Wait a bit to get difference
    val currTicks = processor.systemCpuLoadTicks

    val tickDiff = LongArray(prevTicks.size) { i -> currTicks[i] - prevTicks[i] }
    val totalCpu = tickDiff.sum().toDouble()
    val idleCpu = tickDiff[TickType.IDLE.ordinal].toDouble()
    val cpuUsage = 100.0 * (1.0 - idleCpu / totalCpu)

    // Memory usage
    val memory = hardware.memory
    val totalMem = memory.total / (1024 * 1024)
    val availableMem = memory.available / (1024 * 1024)
    val usedMem = totalMem - availableMem

    // Network usage (all interfaces)
    val networkStats = hardware.networkIFs.joinToString("\n") { netIF ->
        netIF.updateAttributes()
        "${netIF.name}: Sent=${netIF.bytesSent} B, Received=${netIF.bytesRecv} B"
    }

    return """
        🧠 Memory Usage: $usedMem / $totalMem MB
        🖥️ CPU Usage: ${"%.2f".format(cpuUsage)}%
        
        📡 Network Interfaces:
        $networkStats
    """.trimIndent()
}