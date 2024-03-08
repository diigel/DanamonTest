package com.ramdani.danamon.core.extenstions

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

fun Context.showDeleteConfirmationDialog(
    title: String = "Konfirmasi Penghapusan",
    message: String = "Apakah Anda yakin ingin menghapus data ini?",
    positiveButtonLabel: String = "Ya",
    negativeButtonLabel: String = "Tidak",
    onPositiveClick: (DialogInterface) -> Unit
) {
    val alertDialogBuilder = AlertDialog.Builder(this)

    alertDialogBuilder.setTitle(title)
    alertDialogBuilder.setMessage(message)

    alertDialogBuilder.setPositiveButton(positiveButtonLabel) { dialog, _ ->
        onPositiveClick.invoke(dialog)
    }

    alertDialogBuilder.setNegativeButton(negativeButtonLabel) { dialog, _ ->
        dialog.dismiss()
    }

    val alertDialog: AlertDialog = alertDialogBuilder.create()
    alertDialog.show()
}