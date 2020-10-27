import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Subject {
    int id;
    String name;
    String type;
    ArrayList<Teacher> teacher;

    public Subject(int id, String name, String type, ArrayList<Teacher> teacher) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.teacher = teacher;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Teacher> getTeacher() {
        return teacher;
    }

    public void setTeacher(ArrayList<Teacher> teacher) {
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id == subject.id &&
                Objects.equals(name, subject.name) &&
                Objects.equals(type, subject.type) &&
                Objects.equals(teacher, subject.teacher);
    }


    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", teacher=" + teacher +
                '}';
    }
}
