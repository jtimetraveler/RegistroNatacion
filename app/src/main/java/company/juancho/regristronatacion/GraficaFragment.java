package company.juancho.regristronatacion;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import company.juancho.regristronatacion.tools.Comunicador;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class GraficaFragment extends Fragment {



    Comunicador comunicacion;

    private Button addButton;
    private TextInputLayout addTIL, commentTIL;

    private NadoFragment mejor,ultimo;


    private Nado mejorNado, ultNado;


    XYPlot grafica;
    LineAndPointFormatter series1Format;
    XYSeries series1;



    public GraficaFragment() {
        // Required empty public constructor
    }


    public static GraficaFragment newInstance(Nado mejorNado, Nado ultNado){
        GraficaFragment f = new GraficaFragment();
        if(mejorNado != null && ultNado != null){
            f.mejorNado =mejorNado;
            f.ultNado = ultNado;
        }
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Gets parámetros
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_main, container, false);
    }





    // Fijar todos los parámetros que queramos de nuestras vistas y restaurar estados en onViewCreated():
    //La vista de layout ha sido creada y ya está disponible
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        comunicacion=(Comunicador) getActivity();//ESTO SOLO ES POSIBLE SI MainActivity es una subclase de Comunicador por lo tanto implementa Comunicator: Polimorfismo


        this.lanzarFragmentos(mejorNado,ultNado);


        addButton = (Button) getActivity().findViewById(R.id.add_button);
        addTIL = (TextInputLayout) getActivity().findViewById(R.id.add_til);
        commentTIL = (TextInputLayout) getActivity().findViewById(R.id.comment_til);
        addButton.setEnabled(false);
        EditText addET = addTIL.getEditText();

        addET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    addNado();
                    return true;
                }
                return false;
            }
        });


        addET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean enable = charSequence.length() != 0;
                addButton.setEnabled(enable);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNado();
            }
        });



        //region GRAFICA

        grafica = (XYPlot) getActivity().findViewById(R.id.mySimpleXYPlot);

        lanzarGrafica();
        final Number[] domainLabels = {1, 2, 3, 6, 7, 8, 9, 10, 13, 14};
        grafica.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object o, @NonNull StringBuffer stringBuffer, @NonNull FieldPosition fieldPosition) {
                int i = Math.round(((Number) o).floatValue());
                return stringBuffer.append(domainLabels[i]);
            }

            @Override
            public Object parseObject(String s, @NonNull ParsePosition parsePosition) {
                return null;
            }

        });
        //endregion
    }


    private void addNado() {
        String noteText = addTIL.getEditText().getText().toString();


        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Fecha: " + df.format(new Date());

        Nado nado = new Nado();
        nado.setCantPiletas(Integer.parseInt(noteText));
        nado.setComentario(commentTIL.getEditText().getText().toString());
        nado.setDate(new Date());
        comunicacion.enviarNado(nado);

        addTIL.getEditText().setText("");
        commentTIL.getEditText().setText("");
        addTIL.requestFocus();

        updateGrafica();


    }


    //region Grafica y eso


    private void lanzarGrafica(){
        // Creamos dos arrays de prueba. En el caso real debemos reemplazar

        // estos datos por los que realmente queremos mostrar

        Number[] series1Numbers  = comunicacion.getSeriesUltimosNados(15);


        // Añadimos Línea Número UNO:
        series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),  // Array de datos
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Sólo valores verticales
                null); // Nombre de la primera serie

        // Repetimos para la segunda serie


        // Modificamos los colores de la primera serie
        series1Format = new LineAndPointFormatter(
                Color.rgb(0, 200, 0),                   // Color de la línea
                Color.rgb(0, 100, 0),                   // Color del punto
                Color.rgb(150, 190, 150), null);              // Relleno

        // Una vez definida la serie (datos y estilo), la añadimos al panel
        grafica.addSeries(series1, series1Format);
        grafica.getLegend().setVisible(false);

    }

    public void updateGrafica(){
        Number[] series1Numbers  = comunicacion.getSeriesUltimosNados(15);


        // Añadimos Línea Número UNO:


        grafica.removeSeries(series1);

        series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),  // Array de datos
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Sólo valores verticales
                ""); // Nombre de la primera serie
        grafica.addSeries(series1,series1Format);

        grafica.redraw();

    }







    //endregion


    //region FragmentosNados Pequeños

    public void lanzarFragmentos(Nado mejorNado, Nado ultimoNado){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        //Paso 2: Crear una nueva transacción
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //Paso 3: Crear un nuevo fragmento y añadirlo
        Bundle argumentos = new Bundle();

        argumentos.putString("nado", String.valueOf(mejorNado.getCantPiletas()));
        argumentos.putString("titulo","Mejor Nado: ");
        argumentos.putString("dia", new SimpleDateFormat("dd-MM-yyyy").format(mejorNado.getDate()));


        mejor = NadoFragment.newInstance(argumentos);
        transaction.add(R.id.lay_general, mejor);

        Bundle argumentosUlt = new Bundle();
        argumentosUlt.putString("titulo", "Último nado: ");
        argumentosUlt.putString("nado", String.valueOf(ultimoNado.getCantPiletas()));
        argumentosUlt.putString("dia", new SimpleDateFormat("dd-MM-yyyy").format(ultimoNado.getDate()));//new SimpleDateFormat("dd-MM-yyyy").format(new Date()));


        ultimo = NadoFragment.newInstance(argumentosUlt);


        transaction.add(R.id.lay_general,ultimo,"ultimo");



        //Paso 4: Confirmar el cambio
        transaction.commit();



    }

    public void updateFragments(Nado mejorNado, Nado ultimoNado){
        Bundle argumentos = new Bundle();

        argumentos.putString("nado", String.valueOf(mejorNado.getCantPiletas()));
        argumentos.putString("titulo","Mejor Nado: ");
        argumentos.putString("dia", new SimpleDateFormat("dd-MM-yyyy").format(mejorNado.getDate()));
        mejor.setArguments(argumentos);
        mejor.onStart();

        Bundle argumentosUlt = new Bundle();
        argumentosUlt.putString("titulo", "Último nado: ");
        argumentosUlt.putString("nado", String.valueOf(ultimoNado.getCantPiletas()));
        argumentosUlt.putString("dia", new SimpleDateFormat("dd-MM-yyyy").format(ultimoNado.getDate()));//new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        ultimo.setArguments(argumentosUlt);
        ultimo.onStart();

    }



   //endregion

}
