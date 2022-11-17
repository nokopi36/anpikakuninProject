package com.hiyama.anpikakuninproject.data

object SafetyCheckInfo {
    var check = ""

    fun initialize(safetyCheck: SafetyCheck) {
        check = safetyCheck.check
    }
}