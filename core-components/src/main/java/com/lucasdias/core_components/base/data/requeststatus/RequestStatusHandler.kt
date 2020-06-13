package com.lucasdias.core_components.base.data.requeststatus

import com.lucasdias.core_components.base.data.requeststatus.RequestStatus.ClientError
import com.lucasdias.core_components.base.data.requeststatus.RequestStatus.GenericError
import com.lucasdias.core_components.base.data.requeststatus.RequestStatus.Informational
import com.lucasdias.core_components.base.data.requeststatus.RequestStatus.Redirection
import com.lucasdias.core_components.base.data.requeststatus.RequestStatus.ServerError
import com.lucasdias.core_components.base.data.requeststatus.RequestStatus.Success
import com.lucasdias.core_components.base.data.requeststatus.RequestStatus.SuccessWithoutData

class RequestStatusHandler {

    companion object {

        fun execute(code: Int?, data: Any?): RequestStatus {

            return code?.let { nonNullCode ->
                when (nonNullCode) {

                    in MIN_INFORMATIONAL_CODE..MAX_INFORMATIONAL_CODE ->
                        Informational(nonNullCode)

                    in MIN_SUCCESS_CODE..MAX_SUCCESS_CODE -> {
                        val successType = successHandler(code, data)
                        return successType
                    }

                    in MIN_REDIRECTION_CODE..MAX_REDIRECTION_CODE ->
                        Redirection(nonNullCode)

                    in MIN_CLIENT_ERROR_CODE..MAX_CLIENT_ERROR_CODE ->
                        ClientError(nonNullCode)

                    in MIN_SERVER_ERROR_CODE..MAX_SERVER_ERROR_CODE ->
                        ServerError(nonNullCode)

                    else -> GenericError(nonNullCode)
                }
            } ?: run {
                GenericError()
            }
        }

        private fun successHandler(nonNullCode: Int, body: Any?): RequestStatus {
            return if ((body is ArrayList<*> && body.isEmpty()) || body == null) SuccessWithoutData(nonNullCode)
            else Success(nonNullCode)
        }

        private const val MIN_INFORMATIONAL_CODE = 100
        private const val MIN_SUCCESS_CODE = 200
        private const val MIN_REDIRECTION_CODE = 300
        private const val MIN_CLIENT_ERROR_CODE = 400
        private const val MIN_SERVER_ERROR_CODE = 500
        private const val MAX_INFORMATIONAL_CODE = 199
        private const val MAX_SUCCESS_CODE = 299
        private const val MAX_REDIRECTION_CODE = 399
        private const val MAX_CLIENT_ERROR_CODE = 499
        private const val MAX_SERVER_ERROR_CODE = 599
    }
}
