package company.juancho.regristronatacion.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import company.juancho.regristronatacion.MainActivityWithTab;
import company.juancho.regristronatacion.Nado;

/**
 * Created by juancho on 31/12/17.
 */

public class ImportadorJSon extends AsyncTask<Void, Integer, Boolean> {

    private static final String TAG = "Ficheros";

    private static final String NAME_FILE = "datos_nados.json";

    private Context context;

    Comunicador comunicador;
    private List<Nado> nados;

    public ImportadorJSon(MainActivityWithTab activityWithTab) {
        this.context = activityWithTab;
        comunicador = (Comunicador) activityWithTab;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        Log.w("Ficheros", "Memoria extarna no montada");
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;
    }






    @Override
    protected Boolean doInBackground(Void... voids) {
        if(isExternalStorageReadable()){
            File ruta_sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            try {

                File f = new File(ruta_sd.getAbsolutePath(),NAME_FILE );

                nados = GSonNadoParser.getNadosToJson(new FileInputStream(f));

                Log.i(TAG,"Exito al leer fichero "+ruta_sd.getAbsolutePath()+"/"+NAME_FILE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG,"Error al leer fichero "+ruta_sd.getAbsolutePath()+"/"+NAME_FILE);
            }


            return true;
        } else {

            return false;
        }


    }






    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean)
        {
            comunicador.setNados(nados);
            Toast.makeText(context, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCancelled() {
        Toast.makeText(context, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
    }
}
