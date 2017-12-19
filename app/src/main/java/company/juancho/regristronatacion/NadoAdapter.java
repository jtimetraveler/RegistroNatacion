package company.juancho.regristronatacion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by juancho on 25/11/17.
 */

public class NadoAdapter extends ArrayAdapter<Nado> {


    public NadoAdapter(@NonNull Context context, @NonNull List<Nado> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater;
        inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        // ¿Ya se infló este view?
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            convertView = inflater.inflate(
                    R.layout.fragment_nado_list,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.cantidad = (TextView) convertView.findViewById(R.id.text_dato_list);
            holder.fecha = (TextView) convertView.findViewById(R.id.text_fecha_list);
            holder.comentario = (TextView) convertView.findViewById(R.id.text_comentario_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Nado actual.
        Nado nado = getItem(position);

        // Setup.
        holder.cantidad.setText("Piletas: "+nado.getCantPiletas());
        holder.fecha.setText(new SimpleDateFormat("dd-MM-yyyy").format(nado.getDate()));
        holder.comentario.setText(nado.getComentario());


        return convertView;
    }

    static class ViewHolder {

        TextView cantidad, fecha, comentario;

    }
}
