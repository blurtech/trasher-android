package tech.blur.trasher.presentation.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.koin.android.ext.android.inject
import tech.blur.trasher.R
import tech.blur.trasher.UserSession
import tech.blur.trasher.common.ext.observeNonNull
import tech.blur.trasher.domain.Parcel
import tech.blur.trasher.domain.TrashcanInfo
import tech.blur.trasher.presentation.BaseFragment
import tech.blur.trasher.presentation.view.SupportNavigationHide
import java.util.*

class QRScannerFragment : BaseFragment(), ZXingScannerView.ResultHandler, SupportNavigationHide {
    override var hideNavigation: ((Boolean) -> Unit)? = null

    private var mScannerView: ZXingScannerView? = null
    private var mFlash: Boolean = false
    private var mAutoFocus: Boolean = false
    private var mSelectedIndices: ArrayList<Int>? = null
    private var mCameraId = -1

    private val userSession: UserSession by inject()

    private val qrScanerViewModel: QRScanerViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        state: Bundle?
    ): View? {
        hideNavigation?.invoke(true)
        mScannerView = ZXingScannerView(activity)
        if (state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false)
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true)
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS)
            mCameraId = state.getInt(CAMERA_ID, -1)
        } else {
            mFlash = false
            mAutoFocus = true
            mSelectedIndices = null
            mCameraId = -1
        }
        setupFormats()
        return mScannerView
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera(mCameraId)
        mScannerView!!.flash = mFlash
        mScannerView!!.setAutoFocus(mAutoFocus)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(FLASH_STATE, mFlash)
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus)
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices)
        outState.putInt(CAMERA_ID, mCameraId)
    }

    override fun handleResult(rawResult: Result) {
        when {
            rawResult.text.contains("canType") -> {
                val result = Gson().fromJson(rawResult.text, TrashcanInfo::class.java)
                userSession.qrCodeCanData(result)
                findNavController().navigate(R.id.action_qrScannerFragment_to_trashEjectionFragment)
            }
            rawResult.text.contains("parcelId") -> {
                val result = Gson().fromJson(rawResult.text, Parcel::class.java)
//                userSession.qrCodeCanData(result)
                qrScanerViewModel.registerParcelsSubject.onNext(result)

                qrScanerViewModel.registrationResult.observeNonNull(this){
                    if (it != -1) {
                        showDialog("$it пакетов добавлено на ваш аккаунт", true)
                    } else {
                        showDialog("Этот QR код уже отсканировали", true)
                    }
                }

            }
            else -> {
                showDialog("Это не похоже на QR код мусорки или пакета", true)
            }
        }
    }

    private fun showDialog(message:String, popBack: Boolean = false){
        val dialog = AlertDialog.Builder(context!!)
        dialog.setMessage(message)
            .setPositiveButton("Ok") { _, _ ->
                if (popBack) findNavController().popBackStack()
            }
        dialog.show()
    }

    private fun setupFormats() {
        val formats = ArrayList<BarcodeFormat>()
        formats.add(BarcodeFormat.QR_CODE)

        if (mScannerView != null) {
            mScannerView!!.setFormats(formats)
        }
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    companion object {
        private val FLASH_STATE = "FLASH_STATE"
        private val AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE"
        private val SELECTED_FORMATS = "SELECTED_FORMATS"
        private val CAMERA_ID = "CAMERA_ID"
    }
}
