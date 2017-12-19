package company.juancho.regristronatacion;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaFragment extends Fragment {

    private ListView myNadoList;
    private NadoAdapter myNadoAdapter;
    private List<Nado> myNados;



    public ListaFragment() {
        // Required empty public constructor
    }


    public static ListaFragment newInstance(List<Nado> mNados) {
        ListaFragment fragment = new ListaFragment();

        if(mNados != null){
            fragment.myNados = mNados;

        }
        return fragment;
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
        View root = inflater.inflate(R.layout.fragment_lista, container, false);


        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Instancia del ListView.
        myNadoList = (ListView) getActivity().findViewById(R.id.list_view_item);

        // Inicializar el adaptador con la fuente de datos.
        myNadoAdapter = new NadoAdapter(getActivity(), myNados);

        //Relacionando la lista con el adaptador
        myNadoList.setAdapter(myNadoAdapter);

        if(myNados==null){
            myNadoAdapter.clear();
        }
        // Eventos
        myNadoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Nado currentLead = myNadoAdapter.getItem(position);
                Toast.makeText(getActivity(),
                        "Comentario: " + currentLead.getComentario()+"\n"+currentLead.getDate().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        setHasOptionsMenu(true);
    }



    public void limpiarLista(){
        myNadoAdapter.clear();
    }

    public void actualizarLista(List<Nado> nados){
        myNadoAdapter.clear();
        myNadoAdapter.addAll(nados);

    }


}
