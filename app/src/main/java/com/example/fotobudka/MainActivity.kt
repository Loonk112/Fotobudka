package com.example.fotobudka

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.media.MediaActionSound
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import java.util.Base64


class MainActivity : AppCompatActivity() {

    private lateinit var capReq: CaptureRequest.Builder
    private lateinit var handler: Handler
    private lateinit var handlerThread: HandlerThread
    private lateinit var cameraManager: CameraManager
    private lateinit var textureView: TextureView
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var cameraDevice: CameraDevice
    private lateinit var imageReader: ImageReader

    private lateinit var timeIn: EditText
    private lateinit var countIn: EditText
    private lateinit var nameIn: EditText

    private var seriesName = ""
    private var targetCount: Int = 0
    private var delay = 0
    private var count: Int = 0
    private var job: Job? = null

    private var pictures = ArrayList<String>()

    override fun onDestroy() {
        super.onDestroy()
        cameraDevice.close()
        handler.removeCallbacksAndMessages(null)
        handlerThread.quitSafely()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPermissions()

        timeIn = findViewById(R.id.timeIn)
        countIn = findViewById(R.id.countIn)
        nameIn = findViewById(R.id.nameIn)

        // Default values in! //TODO: Set the correct values here!
        timeIn.setText("0.8")
        countIn.setText("3")
        nameIn.setText("Banner")

        textureView = findViewById(R.id.cameraPreview)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler((handlerThread).looper)

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }

        }


        imageReader = ImageReader.newInstance(1080,1920, ImageFormat.JPEG, 1)
        //Was switched to lambda
        //imageReader.setOnImageAvailableListener(object: ImageReader.OnImageAvailableListener{
        //            override fun onImageAvailable(reader: ImageReader?) {
        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader?.acquireLatestImage()

            val buffer = image!!.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())

            image.close()

            val encodedString: String = Base64.getEncoder().encodeToString(bytes)

            pictures.add(encodedString)

            val sound = MediaActionSound()
            sound.play(MediaActionSound.SHUTTER_CLICK)
        }, handler)

        findViewById<FloatingActionButton>(R.id.floatingActionButton).apply {
            setOnClickListener {
                targetCount = countIn.text.toString().toInt()
                delay = (timeIn.text.toString().toDouble() * 1000).toInt()
                seriesName = nameIn.text.toString()

                if (targetCount > 0 && delay >= 100 && seriesName != "") {

                    val currentJob = job
                    if (currentJob == null || currentJob.isCompleted) {
                        job = GlobalScope.launch(Dispatchers.IO) {
                            pictures = ArrayList()
                            for (i in 0..targetCount) {
                                count = i
                                delay(delay.toLong())
                                capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                                capReq.addTarget(imageReader.surface)
                                cameraCaptureSession.capture(capReq.build(),null,null)
                            }
                            sender()
                        }
                    } else {
                        Toast.makeText(context, "Photo taking in progress...", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Wrong inputs", Toast.LENGTH_SHORT).show()
                }


            }
        }

    }

    fun sender() {
        //TODO: Send dat from here
    }

    @SuppressLint("MissingPermission")
    fun openCamera() {
        cameraManager.openCamera(cameraManager.cameraIdList[0], object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera

                capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                val surface = Surface(textureView.surfaceTexture)
                capReq.addTarget(surface)

                //TODO: Search for alternative
                @Suppress("DEPRECATION")
                cameraDevice.createCaptureSession(listOf(surface, imageReader.surface), object :
                    CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        cameraCaptureSession = session
                        cameraCaptureSession.setRepeatingRequest(capReq.build(), null, null)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {

                    }
                }, handler)
            }


            override fun onDisconnected(camera: CameraDevice) {

            }

            override fun onError(camera: CameraDevice, error: Int) {

            }
        }, handler)
    }

    private fun getPermissions() {
        val permissionsList = mutableListOf<String>()

        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.CAMERA)
        }
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionsList.size > 0) {
            requestPermissions(permissionsList.toTypedArray(), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                getPermissions()
            }
        }
    }
}