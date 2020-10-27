import java.util.Objects;

public class PeriodTime {
    String id;
    String time;

    public PeriodTime(String id, String time) {
        this.id = id;
        this.time = time;
    }

    @Override
    public String toString() {
        return "PeriodTime{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodTime that = (PeriodTime) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(time, that.time);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
