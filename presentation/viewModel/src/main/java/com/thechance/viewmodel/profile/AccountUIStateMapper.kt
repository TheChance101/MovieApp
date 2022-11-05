package com.thechance.viewmodel.profile

import com.thechance.viewmodel.BuildConfig
import javax.inject.Inject

class AccountUIStateMapper @Inject constructor() :
    com.devfalah.usecases.mappers.Mapper<com.devfalah.models.Account, ProfileUIState> {
    override fun map(input: com.devfalah.models.Account): ProfileUIState {
        return ProfileUIState(
            avatarPath = BuildConfig.IMAGE_BASE_PATH + input.avatarPath,
            name = input.name,
            username = input.username
        )
    }
}