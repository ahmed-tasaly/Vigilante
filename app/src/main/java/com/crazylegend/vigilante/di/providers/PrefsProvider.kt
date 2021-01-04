package com.crazylegend.vigilante.di.providers

import android.content.SharedPreferences
import android.view.Gravity
import androidx.core.content.edit
import com.crazylegend.kotlinextensions.misc.disableNightMode
import com.crazylegend.kotlinextensions.misc.enableNightMode
import com.crazylegend.kotlinextensions.sharedprefs.putBoolean
import com.crazylegend.kotlinextensions.sharedprefs.putFloat
import com.crazylegend.kotlinextensions.sharedprefs.putInt
import com.crazylegend.kotlinextensions.sharedprefs.putString
import com.crazylegend.vigilante.customization.CustomizationFragment.Companion.COLOR_DOT_PREF_ADDITION
import com.crazylegend.vigilante.customization.CustomizationFragment.Companion.COLOR_NOTIFICATION_PREF_ADDITION
import com.crazylegend.vigilante.customization.CustomizationFragment.Companion.POSITION_PREF_ADDITION
import com.crazylegend.vigilante.customization.CustomizationFragment.Companion.SIZE_PREF_ADDITION
import com.crazylegend.vigilante.customization.CustomizationFragment.Companion.VIBRATION_PREF_ADDITION
import com.crazylegend.vigilante.di.qualifiers.EncryptedPrefs
import com.crazylegend.vigilante.settings.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@Singleton
class PrefsProvider @Inject constructor(@EncryptedPrefs
                                        private val defaultPrefs: SharedPreferences) {

    companion object {
        const val DEFAULT_LAYOUT_POSITION = 0
        const val DEFAULT_VIBRATION_POSITION = 0
        const val DEFAULT_DOT_COLOR = 31727
        const val DEFAULT_NOTIFICATION_COLOR = 31727
        const val DEFAULT_DOT_SIZE = 20f
    }

    //region date format
    fun updateDateFormat(value: String) = defaultPrefs.putString(DATE_PREF_KEY, value)
    val getDateFormat
        get() = defaultPrefs.getString(DATE_PREF_KEY, DEFAULT_DATE_FORMAT) ?: DEFAULT_DATE_FORMAT
    //endregion

    //region notifications status
    val areNotificationsEnabled get() = defaultPrefs.getBoolean(NOTIFICATIONS_PREF_KEY, true)
    fun updateNotificationsValue(value: Boolean) = defaultPrefs.putBoolean(NOTIFICATIONS_PREF_KEY, value)
    //endregion

    //region theme
    val isDarkThemeEnabled get() = defaultPrefs.getBoolean(THEME_PREF_KEY, false)
    fun changeTheme() {
        defaultPrefs.edit(true) { putBoolean(THEME_PREF_KEY, !isDarkThemeEnabled) }
        applyThemeLogic()
    }

    fun applyThemeLogic() {
        if (isDarkThemeEnabled) enableNightMode() else disableNightMode()
    }
    //endregion

    //region dot status
    val isDotEnabled get() = defaultPrefs.getBoolean(DOT_PREF_KEY, true)
    fun setDotStatus(status: Boolean) = defaultPrefs.putBoolean(DOT_PREF_KEY, status)
    //endregion

    //region notification exclusion
    val isVigilanteExcludedFromNotifications get() = defaultPrefs.getBoolean(EXCLUDE_VIGILANTE_FROM_NOTIFICATIONS_PREF_KEY, false)
    fun setExcludeVigilanteFromNotificationsStatus(newValue: Boolean) = defaultPrefs.putBoolean(EXCLUDE_VIGILANTE_FROM_NOTIFICATIONS_PREF_KEY, newValue)
    //endregion

    //region camera prefs
    val getCameraColorPref get() = getColorPref(CAMERA_CUSTOMIZATION_BASE_PREF)
    val getCameraSizePref get() = getSizePref(CAMERA_CUSTOMIZATION_BASE_PREF)
    val getCameraPositionPref get() = getPositionPref(CAMERA_CUSTOMIZATION_BASE_PREF)
    val getLayoutCameraPositionPref get() = getLayoutPosition(getCameraPositionPref)
    val getCameraNotificationLEDColorPref get() = getNotificationColorPref(CAMERA_CUSTOMIZATION_BASE_PREF)

    val getCameraVibrationPositionPref get() = getVibrationPref(CAMERA_CUSTOMIZATION_BASE_PREF)
    val getCameraVibrationEffectPref get() = getVibrationEffect(getCameraVibrationPositionPref)
    //endregion

    //region mic prefs
    val getMicColorPref get() = getColorPref(MIC_CUSTOMIZATION_BASE_PREF)
    val getMicSizePref get() = getSizePref(MIC_CUSTOMIZATION_BASE_PREF)
    val getMicPositionPref get() = getPositionPref(MIC_CUSTOMIZATION_BASE_PREF)
    val getLayoutMicPositionPref get() = getLayoutPosition(getMicPositionPref)
    val getMicNotificationLEDColorPref get() = getNotificationColorPref(MIC_CUSTOMIZATION_BASE_PREF)

    val getMicVibrationPositionPref get() = getVibrationPref(MIC_CUSTOMIZATION_BASE_PREF)
    val getMicVibrationEffectPref get() = getVibrationEffect(getMicVibrationPositionPref)
    //endregion

    fun saveColorPref(prefBaseName: String, pickedColor: Int) = defaultPrefs.putInt(prefBaseName, pickedColor)
    fun saveSizePref(prefBaseName: String, sizeSlider: Float) = defaultPrefs.putFloat(prefBaseName, sizeSlider)
    fun savePositionPref(prefBaseName: String, position: Int) = defaultPrefs.putInt(prefBaseName, position)
    fun saveNotificationColorPref(prefBaseName: String, pickedColor: Int) = defaultPrefs.putInt(prefBaseName, pickedColor)

    private fun getColorPref(basePref: String) = defaultPrefs.getInt(basePref + COLOR_DOT_PREF_ADDITION, DEFAULT_DOT_COLOR)
    private fun getNotificationColorPref(basePref: String) = defaultPrefs.getInt(basePref + COLOR_NOTIFICATION_PREF_ADDITION, DEFAULT_NOTIFICATION_COLOR)
    private fun getSizePref(basePref: String) = defaultPrefs.getFloat(basePref + SIZE_PREF_ADDITION, DEFAULT_DOT_SIZE)

    /**
    0 is top right corner
    1 is top left corner

    2 is center right side
    3 is center left side

    4 is bottom right corner
    5 is bottom left corner
     * @return Int
     */
    private fun getPositionPref(basePref: String) = defaultPrefs.getInt(basePref + POSITION_PREF_ADDITION, DEFAULT_LAYOUT_POSITION)

    private fun getLayoutPosition(pref: Int) = when (pref) {
        0 -> Gravity.TOP or Gravity.END
        1 -> Gravity.TOP or Gravity.START
        2 -> Gravity.CENTER_VERTICAL or Gravity.END
        3 -> Gravity.CENTER_VERTICAL or Gravity.START
        4 -> Gravity.BOTTOM or Gravity.END
        5 -> Gravity.BOTTOM or Gravity.START
        else -> Gravity.TOP or Gravity.END
    }

    //region intro
    val isIntroShown get() = defaultPrefs.getBoolean(INTRO_PREF, false)
    fun setIntroShown() {
        defaultPrefs.putBoolean(INTRO_PREF, true)
    }
    //endregion

    //region biometric auth
    val isBiometricAuthEnabled get() = defaultPrefs.getBoolean(BIOMETRIC_AUTH_PREF_KEY, false)
    fun updateBiometricStatus(status: Boolean) = defaultPrefs.putBoolean(BIOMETRIC_AUTH_PREF_KEY, status)
    //endregion

    //region bypass dnd
    val isBypassDNDEnabled get() = defaultPrefs.getBoolean(BYPASS_DND_PREF_KEY, false)
    fun updateDNDValue(value: Boolean) = defaultPrefs.putBoolean(BYPASS_DND_PREF_KEY, value)
    //endregion


    //region vibration
    private fun getVibrationPref(basePref: String) = defaultPrefs.getInt(basePref + VIBRATION_PREF_ADDITION, DEFAULT_VIBRATION_POSITION)

    fun getVibrationEffect(pref: Int) = when (pref) {
        0 -> null
        1 -> longArrayOf(60, 80, 100)
        2 -> longArrayOf(150, 180, 200)
        3 -> longArrayOf(250, 300, 350)
        else -> null
    }
    //endregion
}