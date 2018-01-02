package company.juancho.regristronatacion;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by juancho on 24/10/17.
 */


@Entity
public class Nado {

    @Id
    long id;

    private int cantPiletas;
    private Date date;
    private String comentario;

    public Nado(){

    }

    public Nado(int cantPiletas, Date date, String comentario){
        this.cantPiletas = cantPiletas;
        this.date = date;
        this.comentario = comentario;
    }

    public Nado(long id, int cantPiletas, Date date) {
        this.id = id;
        this.cantPiletas = cantPiletas;
        this.date = date;
    }

    public Nado(long id, int cantPiletas, Date date, String comentario) {
        this.id = id;
        this.cantPiletas = cantPiletas;
        this.date = date;
        this.comentario = comentario;
    }

    //region Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCantPiletas() {
        return cantPiletas;
    }

    public void setCantPiletas(int cantPiletas) {
        this.cantPiletas = cantPiletas;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    //endregion
}
