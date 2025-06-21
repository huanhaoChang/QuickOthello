package jp.co.nissan.ae.quickothello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import dagger.hilt.android.AndroidEntryPoint
import jp.co.nissan.ae.quickothello.ui.screens.OthelloGameScreen
import jp.co.nissan.ae.quickothello.ui.theme.QuickOthelloTheme
import jp.co.nissan.ae.quickothello.viewmodel.OthelloViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickOthelloTheme {
                val viewModel: OthelloViewModel = hiltViewModel()
                val lifecycleOwner = LocalLifecycleOwner.current

                // Observe lifecycle events
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        when (event) {
                            Lifecycle.Event.ON_RESUME -> {
                                viewModel.onAppResumed()
                            }
                            Lifecycle.Event.ON_PAUSE -> {
                                viewModel.onAppPaused()
                            }
                            else -> {}
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OthelloGameScreen(
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QuickOthelloTheme {
        OthelloGameScreen()
    }
}