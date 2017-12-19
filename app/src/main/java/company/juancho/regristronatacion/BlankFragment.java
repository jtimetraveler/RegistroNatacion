package company.juancho.regristronatacion;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {


    public static final String TAG = "BlankFragment";

    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance(Bundle arguments){
        BlankFragment f = new BlankFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }




    String titulo, dia;
    int cantidad;






    //El fragment se ha adjuntado al Activity
    //Fijar las callback para interactuar con el Activity durante onAttach():


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Nos aseguramos de que la actividad contenedora haya implementado la
        // interfaz de retrollamada. Si no, lanzamos una excepción
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " debe implementar FragmentIterationListener");
        }
    }


    TextView textView;



   //El Fragment ha sido creado
   //Instanciar todos los objetos que no sean vistas(Buttons, ListViews, TextViews) en el método onCreate()
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    //El Fragment va a cargar su layout, el cual debemos especificar
    //Instanciar los objetos que si son vistas, también durante onCreateView():
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FrameLayout frameLayout = new FrameLayout(super.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 35);
        frameLayout.setLayoutParams(layoutParams);
        textView = new TextView(super.getContext());
        frameLayout.addView(textView);
        frameLayout.setBackgroundColor(Color.BLUE);


        return frameLayout;
    }


    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v =  inflater.inflate(R.layout.examplefragment_view, container, false);
        return v;
    }*/


    // Fijar todos los parámetros que queramos de nuestras vistas y restaurar estados en onViewCreated():
    //La vista de layout ha sido creada y ya está disponible
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView.setText("FRAGMENTO VACIO");
    }

    //Fijar los parámetros que tengan que ver con el Activity en onActivityCreated():
    //El Activity que contiene el Fragment ha terminado su creación
    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); //Indicamos que este Fragment tiene su propio menu de opciones
    }*/


    //– Eliminar la referencia al Callback durante onDetach():
    //El Fragment ha sido quitado de su Activity y ya no está disponible
    @Override
    public void onDetach() {

        super.onDetach();
    }


    public void cambiarTexto(String texto) {
        textView.setText(texto);
    }


}
