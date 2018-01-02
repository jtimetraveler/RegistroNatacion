package company.juancho.regristronatacion.tools;

import java.util.List;

import company.juancho.regristronatacion.Nado;

/**
 * Created by juancho on 28/11/17.
 */

public interface Comunicador {
    public  void enviarNado(Nado nado);

    public Number[] getSeriesUltimosNados(int cantidadDeNados);

    public void setNados(List<Nado> nados);
 }
