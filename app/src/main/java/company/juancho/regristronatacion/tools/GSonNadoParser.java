package company.juancho.regristronatacion.tools;

import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import company.juancho.regristronatacion.Nado;

/**
 * Created by juancho on 30/12/17.
 */

public class GSonNadoParser {


    private static final String TAG = "Ficheros";


    public void escribirTXT(String nombre, String[] cuerpo) {
        try {

            //Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
            File archivo = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), nombre+ ".txt");

            if (archivo.exists()) {
                //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
                FileWriter writer = new FileWriter(archivo, true);

                for(int i =0;i<cuerpo.length;i++)
                    writer.write(cuerpo[i]);

                writer.close();
            } else {
                Log.e(TAG, "Error al crear archivo");
            }

        } catch (IOException e1) {
            e1.printStackTrace();
            Log.e(TAG, "Error al escribir archivo" + e1.getMessage());
        }
    }

    OutputStreamWriter outputStreamWriter;
    BufferedReader bufferedReader;



    static public void exportObjectToJSon(OutputStream outputStream, Object ob) {
        try {
            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            outputStream);

            //context.openFileOutput("prueba_int.json", Context.MODE_WORLD_READABLE));
            Gson gson = new Gson();

            fout.write(gson.toJson(ob));
            fout.close();
            Log.i(TAG, "Se guardo el archivo con "+gson.toJson(ob));
        } catch (Exception ex) {
            Log.e(TAG, "Error al escribir fichero a memoria interna");
        }
    }

    static public void exportListToJSon(OutputStream outputStream, List list) {
        try {
            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            outputStream);

            //context.openFileOutput("prueba_int.json", Context.MODE_WORLD_READABLE));
            Gson gson = new Gson();


            // for loop advance

            for (Object ob : list) {
                fout.write(gson.toJson(ob));

            }

            fout.close();
            //Log.i(TAG, "Se guardo el archivo con "+gson.toJson(ob));
        } catch (Exception ex) {
            Log.e(TAG, "Error al escribir fichero a memoria interna");
        }
    }







    //InputStream = context.openFileInput("prueba_int.json")



    public static  String leerFichero(InputStream inputStream) {
        try {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    inputStream));

            String texto = fin.readLine();
            fin.close();
            Log.i(TAG, "Se leyó el archivo con "+texto);
            return texto;
        } catch (Exception ex) {
            Log.e(TAG, "Error al leer fichero desde memoria interna");
            return "";
        }

    }




    private final static String DATE_PATTERN = "MMM dd, yyyy hh:mm:ss a";



    public static Nado getNadoToJson(InputStream inputStream){
        String jsonOutput = leerFichero(inputStream);
        if(jsonOutput!=""){
            Gson gson = new GsonBuilder().setDateFormat(DATE_PATTERN).create();
            //Type listType = new TypeToken<List<Nado>>(){}.getType();
            Nado nados =   gson.fromJson(jsonOutput, Nado.class);
            return nados;
        }else{
            return null;
        }

    }

    public static List<Nado> getNadosToJson(InputStream inputStream){
        String jsonOutput = leerFichero(inputStream);
        if(jsonOutput!=""){
            Gson gson = new GsonBuilder().setDateFormat(DATE_PATTERN).create();
            Type listType = new TypeToken<List<Nado>>(){}.getType();
            return   gson.fromJson(jsonOutput, listType);
        }else{
            return null;
        }

    }


}

