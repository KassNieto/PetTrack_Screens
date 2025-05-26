package com.pettrack.pettrack.models;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static File from(Context context, Uri uri) throws IOException {
        String path = getPath(context, uri);
        return new File(path);
    }

    private static String getPath(Context context, Uri uri) {
        // Implementación para obtener la ruta real del archivo desde la URI
        // Esta es una implementación básica - puede necesitar ajustes
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // Implementación para documentos
            return DocumentsContract.getDocumentId(uri);
        } else {
            // Implementación para archivos normales
            return uri.getPath();
        }
    }
}