package ir.amirreza.module7.data.states

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

data class AppState(
    val navigation: NavHostController
)

@SuppressLint("ComposableNaming")
@Composable
fun rememberAppState(): AppState {
    val navigation = rememberNavController()
    return remember {
        AppState(navigation)
    }
}

val LocalAppState = staticCompositionLocalOf<AppState> { error("") }
