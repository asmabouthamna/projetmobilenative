package com.example.myapplicationsprint11.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplicationsprint11.model.ResponseMedicament
import com.example.myapplicationsprint11.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicamentViewModel : ViewModel() {
    private val _medicaments = MutableLiveData<List<ResponseMedicament>>()
    val medicaments: LiveData<List<ResponseMedicament>> get() = _medicaments

    // Charger les médicaments par ID patient
    fun getMedicamentsByPatientId(patientId: String) {
        RetrofitClient.apiService.getMedicamentsByPatientId(patientId).enqueue(object : Callback<List<ResponseMedicament>> {
            override fun onResponse(call: Call<List<ResponseMedicament>>, response: Response<List<ResponseMedicament>>) {
                if (response.isSuccessful) {
                    _medicaments.value = response.body() ?: emptyList()
                } else {
                    Log.e("MedicamentViewModel", "Échec de la réponse de l'API : ${response.code()}, ${response.errorBody()?.string()}")
                    _medicaments.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<ResponseMedicament>>, t: Throwable) {
                Log.e("MedicamentViewModel", "Erreur lors de l'appel API : ${t.message}")
                _medicaments.value = emptyList()
            }
        })
    }

    // Ajouter un médicament
    fun addMedicament(patientId: String, medicament: ResponseMedicament) {
        RetrofitClient.apiService.addMedicament(patientId, medicament).enqueue(object : Callback<ResponseMedicament> {
            override fun onResponse(call: Call<ResponseMedicament>, response: Response<ResponseMedicament>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val updatedMedicaments = _medicaments.value?.toMutableList() ?: mutableListOf()
                        updatedMedicaments.add(it)
                        _medicaments.value = updatedMedicaments
                    }
                } else {
                    Log.e("MedicamentViewModel", "Erreur API : Code ${response.code()}, ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseMedicament>, t: Throwable) {
                Log.e("MedicamentViewModel", "Échec de l'appel API : ${t.message}")
            }
        })
    }

    fun updateMedicament(patientId: String, medicamentName: String, updatedMedicament: ResponseMedicament) {
        RetrofitClient.apiService.updateMedicament(patientId, medicamentName, updatedMedicament)
            .enqueue(object : Callback<ResponseMedicament> {
                override fun onResponse(call: Call<ResponseMedicament>, response: Response<ResponseMedicament>) {
                    if (response.isSuccessful) {
                        response.body()?.let { updatedMedicament ->
                            val updatedList: List<ResponseMedicament>? = _medicaments.value?.map {
                                if (it.name == updatedMedicament.name) updatedMedicament else it
                            }
                            _medicaments.value = updatedList ?: emptyList()
                        }
                    } else {
                        Log.e("MedicamentViewModel", "Erreur API : Code ${response.code()}, ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ResponseMedicament>, t: Throwable) {
                    Log.e("MedicamentViewModel", "Échec de l'appel API : ${t.message}")
                }
            })
    }
    fun deleteMedicament(patientId: String, medicamentName: String) {
        RetrofitClient.apiService.deleteMedicament(patientId, medicamentName).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Mettez à jour la liste des médicaments après suppression
                    _medicaments.value = _medicaments.value?.filter { it.name != medicamentName }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Gérez les erreurs (affichez un message ou autre)
            }
        })
    }


}
