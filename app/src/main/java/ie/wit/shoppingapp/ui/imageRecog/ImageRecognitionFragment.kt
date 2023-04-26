//https://www.youtube.com/watch?v=jhGm4KDafKU&t=8s&ab_channel=IJApps

package ie.wit.shoppingapp.ui.imageRecog

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.renderscript.Element
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.shoppingapp.R
import ie.wit.shoppingapp.ml.Model
import ie.wit.shoppingapp.models.StoreJSONStore
//import ie.wit.shoppingapp.models.StoreManager
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer



import ie.wit.shoppingapp.models.StoreModel
import ie.wit.shoppingapp.ui.list.ListViewModel
import org.tensorflow.lite.DataType
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.min


// image recognition
class ImageRecognitionFragment : Fragment() {
    private lateinit var confidence: TextView
    private lateinit var camera: Button
    private lateinit var gallery: Button
    private lateinit var imageView: ImageView
    private lateinit var result: TextView
    private val imageSize = 224
    private var productList = listOf<StoreModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recognition, container, false)
        productList = StoreJSONStore.findAll()
        camera = view.findViewById(R.id.button)
        gallery = view.findViewById(R.id.button2)
        result = view.findViewById(R.id.result)
        imageView = view.findViewById(R.id.imageView)
        confidence = view.findViewById(R.id.confidencesText)

        camera.setOnClickListener {
            if (checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 3)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }


        gallery.setOnClickListener {
            val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(cameraIntent, 1)
        }
//        var isGallery = args.openGallery
//        if(isGallery)
//        {
//            val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(cameraIntent, 1)
//        }

        return view
    }

    fun classifyImage(image: Bitmap) {
        try {
            val model = Model.newInstance(requireContext())

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)

            // Create a flattened array of RGB values from the bitmap image
            val pixels = IntArray(imageSize * imageSize)
            image.getPixels(pixels, 0, imageSize, 0, 0, imageSize, imageSize)

            // Convert each pixel value to a normalized float value and add it to the input tensor buffer
            // avoids iterating over each pixel and calling the ByteBuffer.putFloat() method for each RGB value.

            for (i in pixels.indices) {
                val pixel = pixels[i]
                val r = ((pixel shr 16) and 0xFF).toFloat() / 255.0f
                val g = ((pixel shr 8) and 0xFF).toFloat() / 255.0f
                val b = (pixel and 0xFF).toFloat() / 255.0f

                inputFeature0.buffer.putFloat(r)
                inputFeature0.buffer.putFloat(g)
                inputFeature0.buffer.putFloat(b)
            }

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val confidences = outputFeature0.floatArray
            // find the index of the class with the biggest confidence.
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf("Banana", "Pringles", "Quell Water", "7up", "RedBull","Milk","Fanta")
            result.text = classes[maxPos]
            // use the index to look up the corresponding product from your product list
            val recognizedProduct = classes[maxPos]
            for (prod in productList) {
                if (prod.productName == (recognizedProduct)) {
                    // navigate to the details page for the recognized product
                    val action = ImageRecognitionFragmentDirections.actionImageRecognitionFragmentToDetailsFragment(prod.id)

                    findNavController().navigate(action)
                }
            }

            // Releases model resources if no longer used.
            model.close()

            // Display the confidence scores for each class
            var s = ""
            for (i in classes.indices) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100)
            }
            confidence.text = s

        } catch (e: IOException) {
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 3) {
                val image = data?.extras?.get("data") as Bitmap
                val dimension = min(image.width, image.height)
                var scaledImage = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
                imageView.setImageBitmap(scaledImage)
                scaledImage = Bitmap.createScaledBitmap(scaledImage, imageSize, imageSize, false)
                classifyImage(scaledImage)
            } else if (requestCode == 1 && data != null) {
                val dat = data.data
                var image: Bitmap? = null
                try {
                    image = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, dat)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                imageView.setImageBitmap(image)
                image = Bitmap.createScaledBitmap(image!!, imageSize, imageSize, false)
                classifyImage(image)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}


