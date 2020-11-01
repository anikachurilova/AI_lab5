import java.util.Objects;

public class Slot {
    private int id;
    private StudentGroup group;
    private Subject subject;
    private Teacher teacher;
    private PeriodTime periodTime;
    private Room room;
    private String typeLesson;

    public Slot(PeriodTime periodTime, Room room) {
        this.periodTime = periodTime;
        this.room = room;
    }

    public Slot(StudentGroup group, Subject subject, Teacher teacher, String typeLesson){
        this.subject = subject;
        this.group = group;
        this.teacher = teacher;
        this.typeLesson = typeLesson;
    }

    public Slot(StudentGroup group, Subject subject, Teacher teacher, String typeLesson, PeriodTime periodTime, Room room) {
        this.group = group;
        this.subject = subject;
        this.teacher = teacher;
        this.typeLesson = typeLesson;
        this.periodTime = periodTime;
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return id == slot.id &&
                Objects.equals(group, slot.group) &&
                Objects.equals(subject, slot.subject) &&
                Objects.equals(teacher, slot.teacher) &&
                Objects.equals(periodTime, slot.periodTime) &&
                Objects.equals(room, slot.room) &&
                Objects.equals(typeLesson, slot.typeLesson);
    }


    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
    public void setPeriodTime(PeriodTime periodTime) { this.periodTime = periodTime; }
    public void setRoom(Room room){ this.room = room; }
    public int getId() { return id; }
    public String getTypeLesson(){ return typeLesson;}
    public StudentGroup getGroup() { return group; }
    public Subject getSubject() { return subject; }
    public Teacher getTeacher() { return teacher; }
    public PeriodTime getPeriodTime() { return periodTime; }
    public Room getRoom() { return room; }
    public String toString() {
        return "["+group.getName()+","+subject.getName()+","+room.getNumber()+","+teacher.getId()+","+periodTime.getId() +"]";
    }
}
