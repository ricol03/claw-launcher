package com.whiskersapps.clawlauncher.icon_packs

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.AdaptiveIconDrawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.whiskersapps.clawlauncher.settings.di.SettingsRepo
import com.whiskersapps.clawlauncher.shared.model.App
import com.whiskersapps.clawlauncher.shared.model.App.Icons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import org.whiskersapps.droid.droid_icons.IconFetcher
import org.whiskersapps.droid.droid_icons.models.Icon
import org.xmlpull.v1.XmlPullParser

class IconPacksRepo(
    private val app: Application,
    private val settingsRepo: SettingsRepo,
    context: Context
) {
    private val packageManager = app.packageManager
    private var iconPacks = emptyList<String>()
    private var currentIconPack = ""

    val iconFetcher = IconFetcher(context)

    init {
        fetchIconPacks()

        CoroutineScope(Dispatchers.Main).launch {
            settingsRepo.settingsFlow
                .distinctUntilChangedBy { it.iconPack }
                .collect { settings ->
                    currentIconPack = settings.iconPack
                }
        }
    }

    fun fetchIconPacks() {
        val iconPacksAux = iconFetcher.getIconPacks()
        val iconPacks = mutableListOf<String>()
        for (icon in iconPacksAux) {
            iconPacks.add(icon.packageName)
        }
    }

    /** Gets the themed icon for the app. */
    private fun getThemedIcon(iconPack: String, appPackageName: String): Icon? {
        try {
            val iconPackContext = app.createPackageContext(
                iconPack, Context.CONTEXT_IGNORE_SECURITY
            )

            Log.i("contexto", iconPackContext.toString())

            val appFilterId = getResourceId(iconPackContext, "xml", "appfilter")
            if (appFilterId == 0) return null

            val parser = iconPackContext.resources.getXml(appFilterId)
            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.name == "item") {
                        val component = parser.getAttributeValue(null, "component")

                        if (component != null && component.contains(appPackageName)) {
                            val componentNames = component
                                .substringAfter("ComponentInfo{")
                                .substringBefore("$")
                                .split("/")

                            for (name in componentNames) {
                                if (name == appPackageName) {
                                    val drawable = parser.getAttributeValue(null, "drawable")

                                    if (drawable != null) {
                                        val drawableId =
                                            getResourceId(iconPackContext, "drawable", drawable)
                                        if (drawableId != 0) {

                                            val iconDrawable = ResourcesCompat.getDrawable(
                                                iconPackContext.resources,
                                                drawableId,
                                                iconPackContext.theme
                                            ) ?: run {
                                                Log.e("icon", "Drawable is null")
                                                return null
                                            }

                                            if (iconDrawable is AdaptiveIconDrawable) {
                                                return Icon(
                                                    drawable = iconDrawable,
                                                    adaptive = Icon.Adaptive(
                                                        iconDrawable.background,
                                                        iconDrawable.foreground
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
            return null
        } catch (e: Exception) {
            return null
        }
    }

    private fun getStockIcon(packageName: String): Icon {
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        val iconDrawable = packageManager.getApplicationIcon(appInfo)

        val default = iconDrawable.toBitmap()
        var adaptive = false
        var foreground: Bitmap? = null
        var background: Bitmap? = null

        if (iconDrawable is AdaptiveIconDrawable) {
            try {
                background = iconDrawable.background.toBitmap()
                foreground = iconDrawable.foreground.toBitmap()
                adaptive = true
            } catch (_: Exception) {
            }
        }

        val iconPackContext = app.createPackageContext(
            packageName, Context.CONTEXT_IGNORE_SECURITY
        )

        var adapatito = foreground?.toDrawable(iconPackContext.resources)?.let {
            Icon.Adaptive(
                background?.toDrawable(iconPackContext.resources),
                it
            )
        }

        return Icon(
            drawable = iconDrawable,
            adaptive = adapatito
        )
    }

    fun getAppIcons(packageName: String): Icons {
        val stockIcon = getStockIcon(packageName)

        val themedIcon = if (settingsRepo.settings.value.iconPack != "") getThemedIcon(
            settingsRepo.settings.value.iconPack,
            packageName
        ) else null

        return Icons(
            stock = stockIcon,
            themed = themedIcon
        )
    }

    fun getIconPacks(): List<App> {
        return packageManager.queryIntentActivities(
            Intent("com.novalauncher.THEME"),
            0
        ).map { intent ->
            App(
                packageName = intent.activityInfo.packageName,
                name = intent.activityInfo.loadLabel(packageManager).toString(),
                icons = getAppIcons(intent.activityInfo.packageName),
                shortcuts = emptyList()
            )
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun getResourceId(
        packageContext: Context,
        resourceType: String,
        resourceName: String
    ): Int {
        return packageContext.resources.getIdentifier(
            resourceName,
            resourceType,
            packageContext.packageName
        )
    }
}