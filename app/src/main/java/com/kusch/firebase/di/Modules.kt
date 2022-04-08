package com.kusch.exam.di

import com.kusch.firebase.model.FirebaseUtil
import com.kusch.firebase.ui.PressureViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val applicationModule = module {
    single { FirebaseUtil() }
    viewModel { PressureViewModel(get()) }
}
