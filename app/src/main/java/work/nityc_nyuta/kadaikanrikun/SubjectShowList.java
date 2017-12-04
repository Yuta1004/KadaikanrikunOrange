package work.nityc_nyuta.kadaikanrikun;


import java.util.concurrent.TimeoutException;

public class SubjectShowList {
    long id;
    String Name;
    String Teacher;
    int color_r;
    int color_g;
    int color_b;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }

    public String getTeacher() {
        return Teacher;
    }
    public void setTeacher(String Teacher) {
        this.Teacher = Teacher;
    }

    public int getColor_r() {return color_r;}
    public void setColor_r(int color_r) {this.color_r = color_r;}

    public int getColor_g() {return color_g;}
    public void setColor_g(int color_g) {this.color_g = color_g;}

    public int getColor_b() {return color_b;}
    public void setColor_b(int color_b) {this.color_b = color_b;}


}