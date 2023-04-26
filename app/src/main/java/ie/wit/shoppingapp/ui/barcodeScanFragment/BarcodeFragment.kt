//https://www.youtube.com/watch?v=YNDSnR0Xhps&t=1544s&ab_channel=DecconTech
package ie.wit.shoppingapp.ui.barcodeScanFragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Matrix
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector

import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import ie.wit.shoppingapp.R
import ie.wit.shoppingapp.databinding.FragmentBarcodeBinding
import ie.wit.shoppingapp.models.StoreJSONStore
import ie.wit.shoppingapp.models.StoreModel
import java.io.IOException

class BarcodeFragment : Fragment() {

    private val requestCodeCameraPermission = 1001
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    private lateinit var binding: FragmentBarcodeBinding
    private var productList = listOf<StoreModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBarcodeBinding.inflate(inflater, container, false)
        productList = StoreJSONStore.findAll()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cameraButton.setOnClickListener {
            if (checkSelfPermission(
                    requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            ) {
                takePicture()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), requestCodeCameraPermission)
            }
        }
        setupControls()
    }


    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(requireContext(), "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue
                    Toast.makeText(requireContext(), "value- $scannedValue", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "No barcode found", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1002)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002 && resultCode == Activity.RESULT_OK) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap

            // Rotate bitmap to the correct orientation
            val matrix = Matrix()
            matrix.postRotate(90f)
            val rotatedBitmap =
                Bitmap.createBitmap(photo, 0, 0, photo.width, photo.height, matrix, true)

            // Convert the rotated bitmap to a Frame
            val frame = Frame.Builder().setBitmap(rotatedBitmap).build()

            // Perform barcode detection on the Frame
            val barcodes = barcodeDetector.detect(frame)

            if (barcodes.size() == 1) {
                scannedValue = barcodes.valueAt(0).rawValue

                // Check if the scanned barcode matches with any product's barcode in the productList
                for (prod in productList) {
                    if (prod.barcode == scannedValue) {
                        // Navigate to the details page for the recognized product
                        val action =
                            BarcodeFragmentDirections.actionBarcodeFragmentToDetailsFragment(prod.id)
                        findNavController().navigate(action)
                        return
                    }
                }

                // No product found with this barcode
                Toast.makeText(
                    requireContext(),
                    "No product found with this barcode",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(), "No barcode found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
