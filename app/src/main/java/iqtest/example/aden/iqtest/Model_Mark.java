package iqtest.example.aden.iqtest;


public class Model_Mark {
    private String Date;
    private int Marks;

    public Model_Mark(String date, int marks) {
        Date = date;
        Marks = marks;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getMarks() {
        return Marks;
    }

    public void setMarks(int marks) {
        Marks = marks;
    }

    public String getMarkString(){
        return "Tested Date : " + this.Date + "\nMarks : " + this.Marks + "/10";
    }
}
