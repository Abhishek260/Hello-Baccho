package com.example.hellobaccho.dataModels

data class PeriodSelection (
    var viewFromDate: String? = null,
    var viewToDate: String? = null,
    var sqlFromDate: String? = null,
    var sqlToDate: String? = null,
    var sqlsingleDate: String? = null,
    var viewsingleDate: String? = null,
)