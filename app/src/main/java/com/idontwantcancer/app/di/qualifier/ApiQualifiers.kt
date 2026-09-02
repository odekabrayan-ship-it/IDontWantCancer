package com.idontwantcancer.app.di.qualifier

import javax.inject.Qualifier

/**
 * Qualifier for the main intelligence agency backend API.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AgencyApi

/**
 * Qualifier for the openFDA API.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FdaApi
