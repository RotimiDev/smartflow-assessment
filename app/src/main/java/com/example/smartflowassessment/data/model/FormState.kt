package com.example.smartflowassessment.data.model

sealed class FormState {
    data object Idle : FormState()

    data object Success : FormState()

    data class Error(
        val message: String?,
    ) : FormState()
}
