package com.example.expensetracker

object TransactionParser {
    private val regex = Regex("""(?i)(?:rs\.?|inr|spent|debited|paid|charged|txn of|usd|\$)\s*([\d,]+\.?\d{0,2})""")

    fun extractAmount(text: String): Double? {
        val match = regex.find(text) ?: return null
        val rawNum = match.groupValues[1].replace(",", "")
        return rawNum.toDoubleOrNull()
    }
}
