package com.example.expensetracker

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationService : NotificationListenerService() {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.notification?.extras?.let { extras ->
            val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
            val fullContent = "$title $text"

            val amount = TransactionParser.extractAmount(fullContent)
            if (amount != null && amount > 0.0) {
                scope.launch {
                    val db = AppDatabase.getDatabase(applicationContext)
                    db.expenseDao().insert(
                        Expense(
                            amount = amount,
                            source = sbn.packageName,
                            rawText = fullContent
                        )
                    )
                }
            }
        }
    }
}
