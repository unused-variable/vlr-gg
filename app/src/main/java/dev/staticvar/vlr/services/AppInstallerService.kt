package dev.staticvar.vlr.services

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import android.widget.Toast
import dev.staticvar.vlr.MainActivity
import dev.staticvar.vlr.utils.e
import dev.staticvar.vlr.utils.i

class AppInstallerService : Service() {

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
      PackageInstaller.STATUS_PENDING_USER_ACTION -> {
        i { "Requesting user confirmation for installation" }
        val confirmationIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
        confirmationIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
          startActivity(confirmationIntent)
        } catch (e: Exception) {
          e.printStackTrace()
          e { "Unable to start installation" }
        }
      }
      PackageInstaller.STATUS_SUCCESS -> {
        i { "Installation succeed" }
        filesDir
          .listFiles()
          ?.filter { it.name.startsWith("update") && it.name.endsWith(".apk") }
          ?.forEach { it.delete() }
        i { "Delete APK(s)" }
        intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)?.let {
          e { it }
          Toast.makeText(this, "App updated!", Toast.LENGTH_SHORT).show()
          startActivity(
            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          )
        }
      }
      else -> {
        filesDir
          .listFiles()
          ?.filter { it.name.startsWith("update") && it.name.endsWith(".apk") }
          ?.forEach { it.delete() }
        i { "Delete APK(s)" }
        intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)?.let {
          e { it }
          Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
          startActivity(
            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          )
        }
      }
    }
    stopSelf()
    return START_NOT_STICKY
  }

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }
}