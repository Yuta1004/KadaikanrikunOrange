package work.nityc_nyuta.kadaikanrikun;


import java.util.concurrent.TimeoutException;

public class KadaiShowList {
    long id;
    String ID_Name;
    String Date;
    int color_r;
    int color_g;
    int color_b;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getID_Name() {
        return ID_Name;
    }
    public void setID_Name(String ID_Name) {
        this.ID_Name = ID_Name;
    }

    public String getDate() {
        return Date;
    }
    public void setDate(String Date) {
        this.Date = Date;
    }

    public int getColor_r() {return color_r;}
    public void setColor_r(int color_r) {this.color_r = color_r;}

    public int getColor_g() {return color_g;}
    public void setColor_g(int color_g) {this.color_g = color_g;}

    public int getColor_b() {return color_b;}
    public void setColor_b(int color_b) {this.color_b = color_b;}


}