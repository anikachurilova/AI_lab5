import java.util.Objects;

public class Room {
    String number;
    int capacity;

    public Room(String number, int capacity) {
        this.number = number;
        this.capacity = capacity;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Room{" +
                "number='" + number + '\'' +
                ", capacity=" + capacity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return capacity == room.capacity &&
                Objects.equals(number, room.number);
    }

}
