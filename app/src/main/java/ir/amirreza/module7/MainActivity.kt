package ir.amirreza.module7

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.amirreza.module7.data.states.LocalAppState
import ir.amirreza.module7.features.add_or_update.AddOrUpdateScreen
import ir.amirreza.module7.features.home.HomeScreen
import ir.amirreza.module7.ui.theme.Module7Theme
import ir.amirreza.module7.utils.checkInternet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val snackBarState = SnackbarHostState()

    private var initialConnectiveMessage = true

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initNetworkCheck()
        setContent {
            Module7Theme {
                Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = {
                    SnackbarHost(hostState = snackBarState)
                }) { innerPadding ->
                    SetUpNavigation(Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun initNetworkCheck() {
        lifecycleScope.launch {
            checkInternet(this@MainActivity, lifecycleScope).collectLatest {
                if (initialConnectiveMessage.not()) {
                    lifecycleScope.launch {
                        snackBarState.showSnackbar(
                            if (it) "Connect" else "Disconnect",
                            withDismissAction = true
                        )
                    }
                }
                initialConnectiveMessage = false
            }
        }
    }


    @Composable
    fun SetUpNavigation(modifier: Modifier) {
        val appState = LocalAppState.current
        val navigation = appState.navigation
        NavHost(navController = navigation, startDestination = "home", modifier = modifier) {
            composable("home") {
                HomeScreen()
            }
            composable("add_or_update?id={id}", arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )) {
                val id = it.arguments?.getInt("id") ?: 0
                AddOrUpdateScreen(id)
            }
        }
    }
}