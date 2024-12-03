package com.example.myapplicationsprint11.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplicationsprint11.R
import com.example.myapplicationsprint11.model.ResponseMedicament
import com.example.myapplicationsprint11.viewmodel.MedicamentViewModel

@Composable
fun MedicamentScreen(viewModel: MedicamentViewModel = viewModel()) {
    var patientId by remember { mutableStateOf("") }
    val medicaments by viewModel.medicaments.observeAsState(emptyList())
    var showAddMedicamentDialog by remember { mutableStateOf(false) }
    var showUpdateMedicamentDialog by remember { mutableStateOf(false) }
    var selectedMedicament by remember { mutableStateOf<ResponseMedicament?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.health),
            contentDescription = "Health Background",
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.Transparent)
        ) {
            // Title
            Text(
                text = "Patient Medicament Details",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Patient ID Input
            OutlinedTextField(
                value = patientId,
                onValueChange = { patientId = it },
                label = { Text("Enter Patient ID") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Get Medicaments Button
            Button(
                onClick = { viewModel.getMedicamentsByPatientId(patientId) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Text("Get Medicaments")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Medicament List
            if (medicaments.isNotEmpty()) {
                Text(
                    text = "Medications for Patient ID $patientId:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(medicaments) { medicament ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Name: ${medicament.name ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                                    Text("Dosage: ${medicament.dosage ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                                    Text("Frequency: ${medicament.frequency ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                                    Text("Start Date: ${medicament.startDate ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                                    Text("End Date: ${medicament.endDate ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                                }
                                IconButton(onClick = {
                                    selectedMedicament = medicament
                                    showUpdateMedicamentDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Update Medicament",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(onClick = {
                                    viewModel.deleteMedicament(patientId, medicament.name ?: "")
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Medicament",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Text(
                    "No medicaments found. Please enter a valid Patient ID.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }

        // Floating Action Button for Adding Medicament
        FloatingActionButton(
            onClick = { showAddMedicamentDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Medicament",
                tint = Color.White
            )
        }
    }

    // Add Medicament Dialog
    if (showAddMedicamentDialog) {
        AddMedicamentDialog(
            patientId = patientId,
            onAddMedicament = { medicament ->
                viewModel.addMedicament(patientId, medicament)
                showAddMedicamentDialog = false
            },
            onDismiss = { showAddMedicamentDialog = false }
        )
    }

    // Update Medicament Dialog
    if (showUpdateMedicamentDialog) {
        selectedMedicament?.let { medicament ->
            UpdateMedicamentDialog(
                patientId = patientId,
                medicament = medicament,
                onUpdateMedicament = { updatedMedicament ->
                    viewModel.updateMedicament(patientId, medicament.name ?: "", updatedMedicament)
                    showUpdateMedicamentDialog = false
                },
                onDismiss = { showUpdateMedicamentDialog = false }
            )
        }
    }
}

@Composable
fun UpdateMedicamentDialog(
    patientId: String,
    medicament: ResponseMedicament,
    onUpdateMedicament: (ResponseMedicament) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(medicament.name ?: "") }
    var dosage by remember { mutableStateOf(medicament.dosage ?: "") }
    var frequency by remember { mutableStateOf(medicament.frequency ?: "") }
    var startDate by remember { mutableStateOf(medicament.startDate ?: "") }
    var endDate by remember { mutableStateOf(medicament.endDate ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Medicament for Patient ID: $patientId") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = dosage, onValueChange = { dosage = it }, label = { Text("Dosage") })
                OutlinedTextField(value = frequency, onValueChange = { frequency = it }, label = { Text("Frequency") })
                OutlinedTextField(value = startDate, onValueChange = { startDate = it }, label = { Text("Start Date") })
                OutlinedTextField(value = endDate, onValueChange = { endDate = it }, label = { Text("End Date") })
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotEmpty() && dosage.isNotEmpty() && frequency.isNotEmpty()) {
                    val updatedMedicament = ResponseMedicament(name, dosage, frequency, startDate, endDate)
                    onUpdateMedicament(updatedMedicament)
                    onDismiss()
                }
            }) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddMedicamentDialog(
    patientId: String,
    onAddMedicament: (ResponseMedicament) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Medicament for Patient ID: $patientId") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = dosage, onValueChange = { dosage = it }, label = { Text("Dosage") })
                OutlinedTextField(value = frequency, onValueChange = { frequency = it }, label = { Text("Frequency") })
                OutlinedTextField(value = startDate, onValueChange = { startDate = it }, label = { Text("Start Date") })
                OutlinedTextField(value = endDate, onValueChange = { endDate = it }, label = { Text("End Date") })
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotEmpty() && dosage.isNotEmpty() && frequency.isNotEmpty()) {
                    val medicament = ResponseMedicament(name, dosage, frequency, startDate, endDate)
                    onAddMedicament(medicament)
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
