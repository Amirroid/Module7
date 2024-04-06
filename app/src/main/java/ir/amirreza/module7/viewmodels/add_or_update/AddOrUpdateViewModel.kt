package ir.amirreza.module7.viewmodels.add_or_update

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.amirreza.module7.data.db.AppDatabase
import ir.amirreza.module7.data.models.RockType
import ir.amirreza.module7.data.models.Well
import ir.amirreza.module7.data.models.WellLayer
import ir.amirreza.module7.data.models.WellLayersWithRockType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddOrUpdateViewModel : ViewModel() {
    var wellName by mutableStateOf("")
    var gasOilDepth by mutableStateOf("")
    var capacity by mutableStateOf("")
    var rockType by mutableStateOf<RockType?>(null)
    var fromDepth by mutableStateOf("")
    var toDepth by mutableStateOf("")

    private val _layers = MutableStateFlow<List<WellLayersWithRockType>>(emptyList())
    val layers = _layers.asStateFlow()

    private val _rockTypes = MutableStateFlow<List<RockType>>(emptyList())
    val rockTypes = _rockTypes.asStateFlow()


    private val deletedLayers = mutableListOf<WellLayer>()

    var message by mutableStateOf("")

    private var currentId = 0

    private val dao = AppDatabase.INSTANT!!.dao

    fun initId(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            val well = dao.getWell(id).first()
            currentId = well.well.wellId
            delay(1000)
            wellName = well.well.wellName
            gasOilDepth = well.well.gasOilDepth.toString()
            capacity = well.well.capacity.toString()
            _layers.update { it + well.layers }
        }
    }

    init {
        getRockTypes()
    }

    private fun getRockTypes() = viewModelScope.launch(Dispatchers.IO) {
        dao.getRockTypes().collectLatest { rockTypesValue ->
            _rockTypes.update { rockTypesValue }
            delay(1000)
            rockTypesValue.firstOrNull()?.let {
                runCatching {
                    rockType = it
                }
            }
        }
    }

    fun addLayer() {
        val fromInt = fromDepth.toIntOrNull()
        val toInt = toDepth.toIntOrNull()
        if (fromInt != null && toInt != null && fromInt >= (_layers.value.lastOrNull()?.layer?.endPoint
                ?: 0) && (fromInt == (_layers.value.lastOrNull()?.layer?.endPoint ?: 0))
        ) {
            _layers.update {
                it + WellLayersWithRockType(
                    layer = WellLayer(
                        wellLayerId = 0,
                        wellId = 0,
                        rockTypeId = rockType!!.rockTypeId,
                        startPoint = fromInt,
                        endPoint = toInt
                    ),
                    rockType = rockType!!
                )
            }
            fromDepth = ""
            toDepth = ""
        } else {
            message = "Fields not valid"
        }
    }

    fun deleteLayer(index: Int) {
        val layer = _layers.value[index]
        if (layer.layer.wellLayerId != 0) {
            deletedLayers.add(layer.layer)
        }
        _layers.update { it - layer }
    }

    fun submit(onEnd: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val gasOilDepthInt = gasOilDepth.toIntOrNull()
        val capacityInt = gasOilDepth.toIntOrNull()
        if (gasOilDepthInt != null && capacityInt != null && _layers.value.isNotEmpty() && capacity.toIntOrNull() != null && wellName.isNotEmpty()) {
            val well = dao.insertWell(
                Well(
                    currentId,
                    wellTypeId = 1,
                    wellName = wellName,
                    gasOilDepthInt,
                    capacityInt
                )
            )
            if (_layers.value.isNotEmpty()) {
                val mappedLayers = _layers.value.map { it.layer.copy(wellId = well.toInt()) }
                dao.insertWellLayers(mappedLayers)
            }
            if (deletedLayers.isNotEmpty()){
                dao.deleteLayer(deletedLayers)
            }
            withContext(Dispatchers.Main) {
                onEnd.invoke()
            }
        } else {
            message = "Data not valid!"
        }
    }
}