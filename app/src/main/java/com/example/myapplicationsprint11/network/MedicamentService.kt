package com.example.myapplicationsprint11.network

import com.example.myapplicationsprint11.model.ResponseMedicament
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MedicamentService {
    @GET("/api/patients/{patientId}/medicaments")
    fun getMedicamentsByPatientId(@Path("patientId") patientId: String): Call<List<ResponseMedicament>>

    @POST("/api/patients/{patientId}/medicaments")
    fun addMedicament(
        @Path("patientId") patientId: String,
        @Body medicament: ResponseMedicament
    ): Call<ResponseMedicament>

    @PUT("/{patientId}/medicaments/{medicamentName}")
    fun updateMedicament(
        @Path("patientId") patientId: String,
        @Path("medicamentName") medicamentName: String,
        @Body updatedMedicament: ResponseMedicament
    ): Call<ResponseMedicament>
    @DELETE("/patients/{patientId}/medicaments/{medicamentName}")
    fun deleteMedicament(
        @Path("patientId") patientId: String,
        @Path("medicamentName") medicamentName: String
    ): Call<Void>
}
