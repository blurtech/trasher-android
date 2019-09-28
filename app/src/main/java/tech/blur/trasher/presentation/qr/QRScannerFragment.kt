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

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//
//        val menuItem: MenuItem
//
//        if (mFlash) {
//            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on)
//        } else {
//            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off)
//        }
//        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle presses on the action bar items
//        when (item.itemId) {
//            R.id.menu_flash -> {
//                mFlash = !mFlash
//                if (mFlash) {
//                    item.setTitle(R.string.flash_on)
//                } else {
//                    item.setTitle(R.string.flash_off)
//                }
//                mScannerView!!.flash = mFlash
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

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
            rawResult.text.contains("bagType") -> {
                val result = Gson().fromJson(rawResult.text, TrashcanInfo::class.java)
                userSession.qrCodeCanData(result)

            }
            else -> {
                val dialog = AlertDialog.Builder(context!!)
                dialog.setMessage("Это не похоже на QR код мусорки или пакета")
                    .setPositiveButton("Ok") { _, _ ->
                        findNavController().popBackStack()
                    }
                dialog.show()

            }
        }
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
