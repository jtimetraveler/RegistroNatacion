package company.juancho.regristronatacion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class NadoListFragment extends Fragment {


    public NadoListFragment() {
        // Required empty public constructor
    }


    public static NadoFragment newInstance(String[] arguments){
        NadoFragment f = new NadoFragment();
        if(arguments != null){
            // f.setArguments(arguments);
        }
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nado_list, container, false);
    }

}
