package company.juancho.regristronatacion;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NadoFragment extends Fragment {

    private TextView titulo, dato, dia;


    public NadoFragment() {
        // Required empty public constructor
    }


    public static NadoFragment newInstance(Bundle arguments){
        NadoFragment f = new NadoFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View itemView = inflater.inflate(R.layout.fragment_nado, container, false);

        //Nuevos parametros para el view del fragmento
        /*LinearLayout.LayoutParams params =    new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                40);
        //Nueva Regla: EL fragmento estara debajo del boton add_fragment

        //Margenes: top:41dp
        params.setMargins(30,10,30,0);
        //Setear los parametros al view
        itemView.setLayoutParams(params);*/
        itemView.setBackgroundColor(Color.LTGRAY);



        titulo = (TextView) itemView.findViewById(R.id.text_title);
        dato = (TextView) itemView.findViewById(R.id.text_dato);
        dia = (TextView) itemView.findViewById(R.id.text_date);

        return itemView;
    }


    public void setTitulo(){
        Bundle arg = this.getArguments();
        this.titulo.setText(arg.getString("titulo"));
        dato.setText(getArguments().getString("nado")+" piletas");
        dia.setText("Fecha: "+arg.getString("dia"));
    }



    // Fijar todos los parámetros que queramos de nuestras vistas y restaurar estados en onViewCreated():
    //La vista de layout ha sido creada y ya está disponible
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setTitulo();

    }

    @Override
    public void onStart() {
        super.onStart();
        setTitulo();

    }
}






