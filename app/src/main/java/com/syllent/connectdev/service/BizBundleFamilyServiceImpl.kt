package com.syllent.connectdev.service

import com.thingclips.smart.bizbundle.family.api.AbsBizBundleFamilyService

/**
 * Implementation of the BizBundle Family Service.
 * This service provides the current home ID and name to the BizBundle components.
 */
class BizBundleFamilyServiceImpl : AbsBizBundleFamilyService() {

    private var homeId: Long = 0L
    private var homeName: String = ""

    /**
     * Returns the current home ID.
     */
    override fun getCurrentHomeId(): Long {
        return homeId
    }

    /**
     * Called when the current family/home is changed.
     * Updates the stored home ID and name.
     */
    override fun shiftCurrentFamily(familyId: Long, curName: String?) {
        super.shiftCurrentFamily(familyId, curName)
        homeId = familyId
        homeName = curName ?: ""
    }

    /**
     * Returns the current home name.
     */
    fun getCurrentHomeName(): String {
        return homeName
    }
}
