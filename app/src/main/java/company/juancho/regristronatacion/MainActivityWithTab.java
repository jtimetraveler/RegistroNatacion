package company.juancho.regristronatacion;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import company.juancho.regristronatacion.tools.Comunicador;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

public class MainActivityWithTab extends AppCompatActivity implements Comunicador{


    private int MAX_ITEMS = 15;

    private Box<Nado> nadoBox;
    private Query<Nado> nadoQuery;


    private GraficaFragment fragmentGrafica;
    private ListaFragment fragmentLista;

    private BoxStore boxStore;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private InkPageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region Inicios por defecto
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_with_tab);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
        //endregion

        //region ObjectBox y demas

        // do this once, for example in your Application class
        boxStore = MyObjectBox.builder().androidContext(this).build();
        // do this in your activities/fragments to get hold of a Box
        nadoBox = boxStore.boxFor(Nado.class);


        //endregion


        //region Fragmentos
        fragmentGrafica = GraficaFragment.newInstance(getMejorNado(),getUltimoNado());
        fragmentLista = ListaFragment.newInstance(getUltimosNados(MAX_ITEMS));
        //endregion




    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //boxStore.close();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boxStore.close();
    }

    //region   MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            nadoBox.removeAll();
            fragmentGrafica.updateFragments(getMejorNado(),getUltimoNado());
            fragmentGrafica.updateGrafica();
            fragmentLista.limpiarLista();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //endregion


    //region VIEWPAGER
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity_with_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            String[] arg = {" ",""};
            switch (position){
                case 0:
                    return fragmentGrafica;
                case 1:
                    return fragmentLista;


                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

    //endregion

    //region Comunicador

    @Override
    public void enviarNado(Nado nado) {
        nadoBox.put(nado);
        fragmentGrafica.updateFragments(getMejorNado(),getUltimoNado());
        fragmentLista.actualizarLista(getUltimosNados(MAX_ITEMS));

    }

    @Override
    public Number[] getSeriesUltimosNados(int cantidadDeNados){
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




    //endregion



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

}
