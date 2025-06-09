package com.example.medipet; // Reemplaza con tu nombre de paquete

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.medipet.R;

import java.util.Calendar;

public class Menu extends AppCompatActivity {

    // Define los package names para las apps
    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    private static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
    private static final String INSTAGRAM_PACKAGE_NAME = "com.instagram.android";

    private TextView copyrightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        // No es necesario findViewById para los botones si usas android:onClick en el XML
        // y los métodos correspondientes están en la Activity.
    }



    /**
     * Método llamado por el atributo android:onClick en los botones del XML.
     * Identifica qué botón fue presionado y llama a openSocialMediaApp.
     */
    public void openSocialMedia(View view) {
        int id = view.getId();
        if (id == R.id.whatsappButton) {
            openSocialMediaApp(this, WHATSAPP_PACKAGE_NAME, "https://wa.me/YOUR_PHONE_NUMBER_WITH_COUNTRY_CODE?text=Hola!", "https://www.whatsapp.com/");
            // Reemplaza YOUR_PHONE_NUMBER_WITH_COUNTRY_CODE
        } else if (id == R.id.facebookButton) {
            // Para Facebook, es mejor intentar abrir con el ID de la página o perfil
            // Ejemplo: "fb://page/YOUR_PAGE_ID" o "fb://profile/YOUR_PROFILE_ID"
            // Si no, la URL web
            openSocialMediaApp(this, FACEBOOK_PACKAGE_NAME, "fb://page/YOUR_PAGE_ID", "https://www.facebook.com/YOUR_PAGE_USERNAME");
            // Reemplaza YOUR_PAGE_ID y YOUR_PAGE_USERNAME
        } else if (id == R.id.instagramButton) {
            // Para Instagram: "instagram://user?username=YOUR_USERNAME"
            openSocialMediaApp(this, INSTAGRAM_PACKAGE_NAME, "instagram://user?username=YOUR_USERNAME", "https://www.instagram.com/YOUR_USERNAME/");
            // Reemplaza YOUR_USERNAME

        }
    }

    /**
     * Intenta abrir la aplicación social media especificada.
     * Si la app no está instalada, intenta abrir la URL web.
     *
     * @param context Contexto de la aplicación.
     * @param packageName El nombre del paquete de la aplicación social.
     * @param appUri La URI para abrir directamente la app (puede ser específica del perfil/página).
     * @param webUrl La URL del sitio web como fallback.
     */
    public static void openSocialMediaApp(Context context, String packageName, String appUri, String webUrl) {
        Intent intent;
        PackageManager pm = context.getPackageManager();

        try {
            // Intenta crear un intent para la URI específica de la app
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUri));
            // Verifica si la app específica puede manejar esta URI (puede no ser necesario si ya comprobamos el paquete)
            // if (intent.resolveActivity(pm) != null) {
            //    context.startActivity(intent);
            //    return;
            // }

            // Si la URI específica no funciona o queremos ser más directos, intentamos lanzar por paquete
            // (Esto puede no llevar a un perfil específico directamente con algunas apps,
            // pero la URI sí debería)
            if (isAppInstalled(context, packageName)) {
                if (!appUri.isEmpty() && appUri.startsWith(packageName.split("\\.")[1]+"://")) { // Heurística simple para scheme de app
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUri));
                } else {
                    intent = pm.getLaunchIntentForPackage(packageName);
                }

                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    return;
                }
            }
        } catch (Exception e) {
            // La app no está instalada o la URI no es válida, no hacer nada aquí, pasará al fallback.
            // Log.e("SocialMedia", "Error al intentar abrir la app " + packageName + " con URI: " + appUri, e);
        }

        // Fallback: Si la app no está instalada o la URI falló, abrir la URL web
        try {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
            context.startActivity(webIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No se pudo abrir el enlace. App no instalada y no hay navegador.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Verifica si una aplicación está instalada.
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
