package com.gub.services

import com.gub.models.core.NetworkInfo
import com.gub.models.core.ModelSystemStatus
import oshi.SystemInfo
import oshi.hardware.CentralProcessor.TickType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StatusService {

    private val si = SystemInfo()
    private val hardware = si.hardware
    private val processor = hardware.processor

    private var prevTicks: LongArray = processor.systemCpuLoadTicks
    var latency: Long = -1L

    fun getStatus(): ModelSystemStatus {
        // CPU load without blocking
        val currTicks = processor.systemCpuLoadTicks
        val tickDiff = LongArray(prevTicks.size) { i -> currTicks[i] - prevTicks[i] }
        prevTicks = currTicks // update for next time

        val totalCpu = tickDiff.sum().toDouble()
        val idleCpu = tickDiff[TickType.IDLE.ordinal].toDouble()
        val cpuUsage = if (totalCpu > 0) 100.0 * (1.0 - idleCpu / totalCpu) else 0.0

        val memory = hardware.memory
        val totalMem = memory.total / (1024 * 1024)
        val availableMem = memory.available / (1024 * 1024)
        val usedMem = totalMem - availableMem

        val netStats = hardware.networkIFs.map {
            it.updateAttributes()
            NetworkInfo(it.name, it.bytesSent, it.bytesRecv)
        }

        val timestamp = System.currentTimeMillis()

        return ModelSystemStatus(
            backendStatus = "Running",
            aiServiceStatus = "Running",
            trafficSignalStatus = "Auto",
            timestamp = timestamp,
            cpuUsage = cpuUsage.toInt(),
            usedMemoryMb = usedMem,
            totalMemoryMb = totalMem,
            networkStats = netStats,
            latencyMs = latency
        )
    }
}