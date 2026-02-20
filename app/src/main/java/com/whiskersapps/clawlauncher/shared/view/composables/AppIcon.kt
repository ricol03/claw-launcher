package com.whiskersapps.clawlauncher.shared.view.composables

import android.R
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.graphics.drawable.shapes.Shape
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.core.graphics.drawable.toBitmap
import com.whiskersapps.clawlauncher.launcher.search.composables.getBitmapFromDrawable
import com.whiskersapps.clawlauncher.shared.model.App
import com.whiskersapps.clawlauncher.shared.utils.modifyWhen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.whiskersapps.droid.droid_icons.IconFetcher
import org.whiskersapps.droid.droid_icons.models.IconPack

@Composable
fun AppIcon(
    app: App,
    size: Dp? = null,
    isThemed: Boolean
) {
    var icon: Drawable? = null

    icon = try {
        if (isThemed) app.icons.themed!!.drawable else app.icons.stock.drawable
    } catch (e: NullPointerException) {
        app.icons.stock.drawable
    }

    Box(
        modifier = Modifier
            .modifyWhen(size != null) {
                this.size(size!!)
            }
            .fillMaxHeight()
    ) {
        val bitmap = getBitmapFromDrawable(icon)

        Image(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = "${app.name} icon",
            contentScale = ContentScale.FillBounds
        )
    }
}