@file:OptIn(DelicateCoroutinesApi::class)
package com.example.fotobudka

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
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
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import java.util.*


class CaptureFragment : Fragment() {

    private lateinit var capReq: CaptureRequest.Builder
    private lateinit var handler: Handler
    private lateinit var handlerThread: HandlerThread
    private lateinit var cameraManager: CameraManager
    private lateinit var textureView: TextureView
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var cameraDevice: CameraDevice
    private lateinit var imageReader: ImageReader

    private lateinit var settingsFab: FloatingActionButton
    private lateinit var captureFab: FloatingActionButton

    private var job: Job? = null

    private var pictures = ArrayList<String>()


    override fun onDestroy() {
        super.onDestroy()
        cameraDevice.close()
        handler.removeCallbacksAndMessages(null)
        handlerThread.quitSafely()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_capture, container, false)

        textureView = view.findViewById(R.id.cameraPreview)
        cameraManager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler((handlerThread).looper)

        settingsFab = view.findViewById(R.id.settingsFAB)
        captureFab = view.findViewById(R.id.captureFAB)


        settingsFab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_captureFragment_to_settingsFragment)
        }

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

        captureFab.setOnClickListener {
            val currentJob = job
            if (currentJob == null || currentJob.isCompleted) {
                job = GlobalScope.launch(Dispatchers.Main) {
                    captureFab.isEnabled = false
                    settingsFab.isEnabled = false
                    val sound = MediaActionSound()
                    sound.play(MediaActionSound.START_VIDEO_RECORDING)
                    pictures = ArrayList()
                    for (i in 0 until Keeper.count) {
                        delay(Keeper.delay.toLong())
                        capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                        capReq.addTarget(imageReader.surface)
                        cameraCaptureSession.capture(capReq.build(),null,null)
                    }
                    @Suppress("ControlFlowWithEmptyBody")
                    while (pictures.size<Keeper.count) {/*Waits till all photos are recorder - a safety measure*/}
                    sound.play(MediaActionSound.STOP_VIDEO_RECORDING)
                    captureFab.isEnabled = true
                    settingsFab.isEnabled = true

//                    send data via http
                    val pdfId = sendHttp(pictures,Keeper.name,Keeper.getBackHex(),Keeper.getFontHex())
                    Toast.makeText(context, pdfId.await(), Toast.LENGTH_SHORT).show()

                }
            }
        }

        return view
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

}