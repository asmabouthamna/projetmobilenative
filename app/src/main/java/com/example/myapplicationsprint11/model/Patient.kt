package com.example.myapplicationsprint11.model

data class Patient(
	val address: String? = null,
	val name: String? = null,
	val age: Int? = null,
	val medicaments: List<MedicamentsItem?>? = null

	)

data class MedicamentsItem(

	val name: String? = null,
	val dosage: String? = null,
	val frequency: String? = null,
	val startDate: String? = null,
	val endDate: String? = null




	)

