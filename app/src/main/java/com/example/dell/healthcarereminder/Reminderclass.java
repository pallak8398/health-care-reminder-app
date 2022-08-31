package com.example.dell.healthcarereminder;

public class Reminderclass {
    String name;
    String date;
    String time;
    String table_name;
    int id;
    int main_id;

    public void setName(String name)
    {
        this.name = name;
    }
    public void setTableName(String table_name)
    {
        this.table_name = table_name;
    }
    public void setMainid(int main_id)
    {
        this.main_id=main_id;
    }
    public void setid(int id)
    {
        this.id = id;
    }
    public void setDate(String date)
    {
        this.date = date;
    }
    public void setTime(String time)
    {
        this.time = time;
    }
    public String getTime()
    {
        return time;
    }
    public String getName()
    {
        return name;
    }
    public String getDate()
    {
        return date;
    }
    public String getTableName()
    {
        return table_name;
    }
    public int getid()
    {
        return id;
    }
    public int getMain_id()
    {
        return main_id;
    }
}
