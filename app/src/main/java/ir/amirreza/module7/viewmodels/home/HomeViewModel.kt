package ir.amirreza.module7.viewmodels.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.amirreza.module7.data.db.AppDatabase
import ir.amirreza.module7.data.models.RockType
import ir.amirreza.module7.data.models.Well
import ir.amirreza.module7.data.models.WellData
import ir.amirreza.module7.data.models.WellLayer
import ir.amirreza.module7.data.models.WellLayersWithRockType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _data = MutableStateFlow<WellData?>(null)
    val data = _data.asStateFlow()

    private val dao = AppDatabase.INSTANT!!.dao

    private val _wells = MutableStateFlow<List<Well>>(emptyList())
    val wells = _wells.asStateFlow()

    private var _well = MutableStateFlow<Well?>(null)
    var well = _well.asStateFlow()

    init {
        getWellData()
    }

    fun getData(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        dao.getWell(id).collectLatest { value ->
            val newData = if (value.layers.isNotEmpty() && value.layers.last().layer.endPoint <= value.well.gasOilDepth) {
                value.copy(
                    layers = value.layers + WellLayersWithRockType(
                        layer = WellLayer(
                            value.layers.last().layer.wellLayerId + 1,
                            wellId = value.well.wellId,
                            rockTypeId = 0,
                            startPoint = value.layers.last().layer.endPoint,
                            endPoint = value.well.gasOilDepth
                        ),
                        rockType = RockType(
                            0,
                            "Oil / Gas",
                            "#5e5e5e"
                        )
                    )
                )
            } else value
            _data.update { newData }
        }
    }

    private fun getWellData() = viewModelScope.launch(Dispatchers.IO) {
        dao.getWells().collectLatest { wellsValue ->
            _wells.update {
                wellsValue
            }
            wellsValue.firstOrNull()?.let { wellValue ->
                if (_data.value == null) {
                    getData(wellValue.wellId)
                }
                _well.update { wellValue }
            }
        }
    }
}