package com.alim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import android.util.Log
import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.alim.StaticValues.host
import com.alim.StaticValues.ip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors

object StaticValues {
    var ip = "34.87.172.238" //"9.9.9.9"
    var host = "8000"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)


        setContent {
            var url by remember { mutableStateOf(ip) }
            var port by remember { mutableStateOf(host) }
            var finalUrl by remember { mutableStateOf("ws://$ip:$host/ws") }
//            App()
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .weight(0.6f)
                ) {
                    CameraStreamingScreen(finalUrl)
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .weight(0.4f)
                        .padding(32.dp)
                ) {
                    TextField(
                        value = url,
                        onValueChange = { newUrl ->
                            url = newUrl
                        },
                        label = { androidx.compose.material3.Text("Enter Server URL") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    TextField(
                        value = port,
                        onValueChange = { newUrl ->
                            port = newUrl
                        },
                        label = { androidx.compose.material3.Text("Enter Server Port") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            ip = url
                            host = port
                            recreate()
                        }
                    ) {
                        Text("Done")
                    }

                    Text("Connected To: $finalUrl", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun CameraStreamingScreen(finalUrl: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageCapture = ImageCapture.Builder()
                .setTargetRotation(Surface.ROTATION_0)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)

                // Start sending frames (periodic captures)
                startFrameStreaming(imageCapture, finalUrl)
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

fun startFrameStreaming(imageCapture: ImageCapture, finalUrl: String) {
    val socketClient = SocketClient(finalUrl)
    socketClient.connect()

    val executor = Executors.newSingleThreadExecutor()
    CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            val file = File.createTempFile("frame", ".jpg")
            val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val bitmap = android.graphics.BitmapFactory.decodeFile(file.absolutePath)
                    val scaled = android.graphics.Bitmap.createScaledBitmap(bitmap, 1080, 1080, true)
                    val stream = java.io.ByteArrayOutputStream()
                    scaled.compress(android.graphics.Bitmap.CompressFormat.JPEG, 70, stream)
                    socketClient.sendImageBytes(stream.toByteArray())

                    stream.close()
                    file.delete()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("Stream", "Capture error: ${exception.message}")
                }
            })

            delay(30L)
        }
    }
}