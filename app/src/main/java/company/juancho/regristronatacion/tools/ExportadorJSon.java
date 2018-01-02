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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import company.juancho.regristronatacion.MainActivity;

/**
 * Created by juancho on 31/12/17.
 */

public class ExportadorJSon extends AsyncTask<List, Integer, Boolean> {

    private static final String NAME_FILE = "datos_nados.json";
    private static final String TAG = "Ficheros";

    ProgressDialog pDialog;
    private Context context;

    public ExportadorJSon(Context context) {
        this.pDialog = new ProgressDialog(context);
        this.context = context;
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
    protected Boolean doInBackground(List... list) {
        if(isExternalStorageWritable()){
            File ruta_sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);



            try {
                File f = new File(ruta_sd.getAbsolutePath(), NAME_FILE);
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                //context.openFileOutput("prueba_int.json", Context.MODE_WORLD_READABLE));
                Gson gson = new Gson();



                int i=0;
                for (Object ob : list) {
                    fout.write(gson.toJson(ob));
                    publishProgress(i*100/list.length);
                    i++;
                    if(isCancelled())
                        break;
                }

                fout.close();
                Log.i(TAG, "Se guardo el archivo en "+ruta_sd.getAbsolutePath());
            } catch (Exception ex) {
                Log.e(TAG, "Error al escribir fichero a memoria externa");
                return false;
            }

            return true;
        } else {

            return false;
        }


    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();

        pDialog.setProgress(progreso);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setMax(100);

        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ExportadorJSon.this.cancel(true);
            }
        });

        pDialog.setProgress(0);
        pDialog.show();


    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean)
        {
            pDialog.dismiss();
            Toast.makeText(context, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCancelled() {
        Toast.makeText(context, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
    }
}
