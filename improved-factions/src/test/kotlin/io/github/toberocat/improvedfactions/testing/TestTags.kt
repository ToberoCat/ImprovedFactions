package io.github.toberocat.improvedfactions.testing

import org.junit.jupiter.api.Tag
import java.lang.annotation.Inherited

object TestTags {
    const val UNIT = "unit"
    const val INTEGRATION = "integration"
    const val DATABASE = "database"
}

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Tag(TestTags.UNIT)
annotation class UnitTest

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Tag(TestTags.INTEGRATION)
@Inherited
annotation class IntegrationTest

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Tag(TestTags.INTEGRATION)
@Tag(TestTags.DATABASE)
@Inherited
annotation class DatabaseTest
