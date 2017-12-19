package company.juancho.regristronatacion;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;


public class MainActivity extends AppCompatActivity {

    NadoFragment mejor,ultimo;
    LinearLayout layout;

    String cantMejor, fechaMejor;
    String cantUlt, fechaUlt;

    Button addButton;
    TextInputLayout addTIL, commentTIL;


    private Box<Nado> nadoBox;
    private Query<Nado> nadoQuery;

    Nado ultimoNado;
    Nado mejorNado;

    XYPlot grafica;


    LineAndPointFormatter series1Format;
    XYSeries series1;



    public void lanzarFragmentos(Nado mejorNado, Nado ultimoNado){

        FragmentManager fragmentManager = getSupportFragmentManager();

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


    private void updateF(Nado mejorNado, Nado ultimoNado){
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

    private void updateFragment(){
        this.updateF(getMejorNado(),getUltimoNado());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);






        // Inicializamos el objeto XYPlot búscandolo desde el layout:

            //check
            //region parte grafica


        addButton = (Button) findViewById(R.id.add_button);
        addTIL = (TextInputLayout) findViewById(R.id.add_til);
        commentTIL = (TextInputLayout) findViewById(R.id.comment_til);


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



            //endregion

            //region ObjectBox y demas

        // do this once, for example in your Application class
        BoxStore boxStore = MyObjectBox.builder().androidContext(this).build();
        // do this in your activities/fragments to get hold of a Box
        nadoBox = boxStore.boxFor(Nado.class);
            //endregion


        //check
        lanzarFragmentos(getMejorNado(),getUltimoNado());

        //region GRAFICA

        grafica = (XYPlot) findViewById(R.id.mySimpleXYPlot);

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
        addTIL.getEditText().setText("");

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Fecha: " + df.format(new Date());

        Nado nado = new Nado();
        nado.setCantPiletas(Integer.parseInt(noteText));
        nado.setComentario(commentTIL.getEditText().getText().toString());
        nado.setDate(new Date());
        nadoBox.put(nado);
        updateFragment();

        updateGrafica();


    }


    //region BOX

    private void updateNados() {
        List<Nado> notes = nadoQuery.find();
        //notesAdapter.setNotes(notes);
    }

    private Nado getUltimoNado(){
        if(nadoBox.count()==0.0){
            return new Nado(0,0,new Date());
        } else {
            nadoQuery = nadoBox.query().orderDesc(Nado_.date).build();
            return nadoQuery.findFirst();

        }


    }

    private Nado getMejorNado(){
        if(nadoBox.count()==0.0){
            return new Nado(0,0,new Date());
        } else {
            nadoQuery = nadoBox.query().orderDesc(Nado_.cantPiletas).build();
            return nadoQuery.findFirst();

        }
    }

    private List<Nado> getUltimosNados(int cantidadNados){
        List<Nado> aux = new ArrayList<>();
        if(nadoBox.count()==0.0){
            aux.add(new Nado(0,0,new Date()));
        } else {
            nadoQuery = nadoBox.query().orderDesc(Nado_.date).build();
            aux = nadoQuery.find(0,cantidadNados);

        }
        return aux;
    }


    //endregion



    //region MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            nadoBox.removeAll();
            updateFragment();
            updateGrafica();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //endregion





    //region Grafica y eso


    private void lanzarGrafica(){
        // Creamos dos arrays de prueba. En el caso real debemos reemplazar

        // estos datos por los que realmente queremos mostrar

        Number[] series1Numbers  = getCantUltimosNados(15);


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

    private void updateGrafica(){
        Number[] series1Numbers  = getCantUltimosNados(15);


        // Añadimos Línea Número UNO:


        grafica.removeSeries(series1);

        series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),  // Array de datos
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Sólo valores verticales
                ""); // Nombre de la primera serie
        grafica.addSeries(series1,series1Format);

        grafica.redraw();

    }


    private Number[] getCantUltimosNados(int cantidad){

        List<Nado> aux = getUltimosNados(15);
        if(aux.size()==0){
            Number[] numAux = {0,0,0};
            return numAux;
        }else {
            int n = aux.size();
            Number[] num = new Number[n];
            for (int i = 0; i < aux.size(); i++) {
                num[i] = aux.get(n-i-1).getCantPiletas();
            }
            return num;
        }
    }

    private void lanzarGrafica2(){
        // create a couple arrays of y-values to plot:

        Number[] series1Numbers = getCantUltimosNados(15);


        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "");


        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);



        // add an "dash" effect to the series2 line:


        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));



        // add a new series' to the 23xyplot:
        grafica.addSeries(series1, series1Format);





    }


    //endregion




    //region ejemplo
    /*


    private EditText editText;
    private View addNoteButton;

    private Box<Note> notesBox;
    private Query<Note> notesQuery;
    private NotesAdapter notesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setUpViews();

        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        notesBox = boxStore.boxFor(Note.class);

        // query all notes, sorted a-z by their text (http://greenrobot.org/objectbox/documentation/queries/)
        notesQuery = notesBox.query().order(Note_.text).build();
        updateNotes();
    }


    private void updateNotes() {
        List<Note> notes = notesQuery.find();
        notesAdapter.setNotes(notes);
    }

    protected void setUpViews() {
        ListView listView = findViewById(R.id.listViewNotes);
        listView.setOnItemClickListener(noteClickListener);

        notesAdapter = new NotesAdapter();
        listView.setAdapter(notesAdapter);

        addNoteButton = findViewById(R.id.buttonAdd);
        addNoteButton.setEnabled(false);

        editText = findViewById(R.id.editTextNote);
        editText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addNote();
                    return true;
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                addNoteButton.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void onAddButtonClick(View view) {
        addNote();
    }

    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        Note note = new Note();
        note.setText(noteText);
        note.setComment(comment);
        note.setDate(new Date());
        notesBox.put(note);
        Log.d(App.TAG, "Inserted new note, ID: " + note.getId());

        updateNotes();
    }

    */

    //endregion
}
