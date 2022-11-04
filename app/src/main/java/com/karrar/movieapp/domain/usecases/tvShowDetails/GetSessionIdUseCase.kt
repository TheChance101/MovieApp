package com.karrar.movieapp.domain.usecases.tvShowDetails

import com.thechance.repository.AccountRepository
import javax.inject.Inject

class GetSessionIdUseCase @Inject constructor(
    private val accountRepository: com.thechance.repository.AccountRepository
) {
    operator fun invoke(): String? {
        return accountRepository.getSessionId()
    }
}
