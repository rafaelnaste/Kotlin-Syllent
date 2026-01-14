package com.syllent.connectdev.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.syllent.connectdev.R
import com.syllent.connectdev.databinding.ActivityMainBinding
import com.syllent.connectdev.service.BizBundleFamilyServiceImpl
import com.thingclips.smart.android.common.utils.L
import com.thingclips.smart.bizbundle.core.api.BizBundleInitializer
import com.thingclips.smart.bizbundle.core.api.RouteEventListener
import com.thingclips.smart.bizbundle.core.api.ServiceEventListener
import com.thingclips.smart.bizbundle.family.api.AbsBizBundleFamilyService
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingGetHomeListCallback
import com.thingclips.smart.interior.api.IThingDeviceActiveListener
import com.thingclips.smart.interior.device.ThingDeviceActivatorManager
import com.thingclips.smart.router.api.UrlBuilder
import com.thingclips.smart.sdk.api.micro.MicroServiceManager
import com.thingclips.smart.uispecs.scan.api.ScanManager

/**
 * Main Activity that displays after successful login.
 * Handles BizBundle initialization and device pairing functionality.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentHomeId: Long = 0L
    private var currentHomeName: String = ""
    private var isBizBundleInitialized = false

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupClickListeners()
        loadHomeList()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupClickListeners() {
        // Add Device Button - Opens pairing UI
        binding.btnAddDevice.setOnClickListener {
            if (currentHomeId > 0 && isBizBundleInitialized) {
                openDevicePairing()
            } else if (currentHomeId == 0L) {
                Toast.makeText(this, R.string.no_home_found, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Aguarde a inicializacao...", Toast.LENGTH_SHORT).show()
            }
        }

        // Scan QR Code Button
        binding.btnScanQR.setOnClickListener {
            if (isBizBundleInitialized) {
                openQRCodeScanner()
            } else {
                Toast.makeText(this, "Aguarde a inicializacao...", Toast.LENGTH_SHORT).show()
            }
        }

        // Logout Button
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun loadHomeList() {
        showLoading(true)
        binding.tvHomeStatus.text = getString(R.string.loading_homes)

        ThingHomeSdk.getHomeManagerInstance().queryHomeList(object : IThingGetHomeListCallback {
            override fun onSuccess(homeBeans: List<HomeBean>?) {
                runOnUiThread {
                    showLoading(false)
                    if (!homeBeans.isNullOrEmpty()) {
                        // Use the first home found
                        val firstHome = homeBeans[0]
                        currentHomeId = firstHome.homeId
                        currentHomeName = firstHome.name ?: "Casa"

                        binding.tvHomeName.text = getString(R.string.home_name, currentHomeName)
                        binding.tvHomeStatus.text = "ID: $currentHomeId"

                        // Initialize BizBundle with the home data
                        initializeBizBundle()
                    } else {
                        binding.tvHomeName.text = getString(R.string.no_home_found)
                        binding.tvHomeStatus.text = ""
                        Log.w(TAG, "No homes found for this user")
                    }
                }
            }

            override fun onError(errorCode: String?, error: String?) {
                runOnUiThread {
                    showLoading(false)
                    binding.tvHomeName.text = getString(R.string.error_network)
                    binding.tvHomeStatus.text = "Erro: $error"
                    Log.e(TAG, "Error loading homes: $error (code: $errorCode)")
                }
            }
        })
    }

    private fun initializeBizBundle() {
        Log.d(TAG, "Initializing BizBundle...")

        try {
            // Initialize BizBundle
            BizBundleInitializer.init(
                this.application,
                object : RouteEventListener {
                    override fun onFaild(errorCode: Int, urlBuilder: UrlBuilder?) {
                        Log.e(TAG, "Route not implemented: ${urlBuilder?.target} - ${urlBuilder?.params}")
                    }
                },
                object : ServiceEventListener {
                    override fun onFaild(serviceName: String?) {
                        Log.e(TAG, "Service not implemented: $serviceName")
                    }
                }
            )

            // Register the home service
            val familyService = BizBundleFamilyServiceImpl()
            BizBundleInitializer.registerService(
                AbsBizBundleFamilyService::class.java,
                familyService
            )

            // Set current home data
            val service = MicroServiceManager.getInstance()
                .findServiceByInterface(AbsBizBundleFamilyService::class.java.name) as? AbsBizBundleFamilyService
            service?.shiftCurrentFamily(currentHomeId, currentHomeName)

            // Notify BizBundle about login
            BizBundleInitializer.onLogin()

            isBizBundleInitialized = true
            Log.d(TAG, "BizBundle initialized successfully for home: $currentHomeName ($currentHomeId)")

        } catch (e: Exception) {
            Log.e(TAG, "Error initializing BizBundle", e)
            Toast.makeText(this, "Erro ao inicializar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openDevicePairing() {
        Log.d(TAG, "Opening device pairing UI...")

        try {
            // Start device activator with listener
            ThingDeviceActivatorManager.startDeviceActiveAction(this)

            ThingDeviceActivatorManager.addListener(object : IThingDeviceActiveListener {
                override fun onDevicesAdd(devIds: MutableList<String>?) {
                    runOnUiThread {
                        if (!devIds.isNullOrEmpty()) {
                            Toast.makeText(
                                this@MainActivity,
                                "Dispositivos adicionados: ${devIds.size}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, "Devices added: $devIds")
                        }
                    }
                }

                override fun onRoomDataUpdate() {
                    Log.d(TAG, "Room data updated")
                }

                override fun onOpenDevicePanel(devId: String?) {
                    Log.d(TAG, "Open device panel: $devId")
                }
            })

        } catch (e: Exception) {
            Log.e(TAG, "Error opening device pairing", e)
            Toast.makeText(this, "Erro ao abrir pareamento: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openQRCodeScanner() {
        Log.d(TAG, "Opening QR code scanner...")

        try {
            ScanManager.openScan(this)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening QR scanner", e)
            Toast.makeText(this, "Erro ao abrir scanner: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle(R.string.logout)
            .setMessage(R.string.logout_confirm)
            .setPositiveButton(R.string.yes) { _, _ ->
                performLogout()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun performLogout() {
        showLoading(true)

        // Notify BizBundle about logout
        if (isBizBundleInitialized) {
            BizBundleInitializer.onLogout(this)
        }

        // Logout from Tuya SDK
        ThingHomeSdk.getUserInstance().logout(object : com.thingclips.smart.android.user.api.ILogoutCallback {
            override fun onSuccess() {
                runOnUiThread {
                    showLoading(false)
                    navigateToLogin()
                }
            }

            override fun onError(code: String?, error: String?) {
                runOnUiThread {
                    showLoading(false)
                    // Navigate to login anyway
                    navigateToLogin()
                }
            }
        })
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnAddDevice.isEnabled = !show
        binding.btnScanQR.isEnabled = !show
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove device activator listener if needed
        try {
            ThingDeviceActivatorManager.removeListener()
        } catch (e: Exception) {
            Log.e(TAG, "Error removing listener", e)
        }
    }
}
