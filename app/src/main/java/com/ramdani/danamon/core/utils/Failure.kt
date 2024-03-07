package com.ramdani.danamon.core.utils

sealed class Failure {
    object NetworkConnection : Failure()
    data class ServerError(val message: String) : Failure()
    object ExpiredSession: Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()

    data class DataNotExist(val message: String) : Failure()
}
