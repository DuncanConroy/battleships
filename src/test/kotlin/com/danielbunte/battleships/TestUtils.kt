package com.danielbunte.battleships

import io.mockk.MockK
import io.mockk.MockKDsl
import kotlin.reflect.KClass

inline fun <reified T : Any> mockkRelaxed(
    name: String? = null,
    vararg moreInterfaces: KClass<*>,
    relaxUnitFun: Boolean = false,
    block: T.() -> Unit = {}
): T = MockK.useImpl {
    MockKDsl.internalMockk(
        name,
        relaxed = true,
        *moreInterfaces,
        relaxUnitFun = relaxUnitFun,
        block = block
    )
}
