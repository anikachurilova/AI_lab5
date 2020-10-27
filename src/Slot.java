public class Slot {
    private int id;
    private StudentGroup group;
    private Subject subject;
    private Teacher teacher;
    private PeriodTime periodTime;
    private Room room;
    public Slot(int id, StudentGroup group, Subject subject, Teacher teacher){
        this.id = id;
        this.subject = subject;
        this.group = group;
        this.teacher = teacher;
    }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
    public void setPeriodTime(PeriodTime periodTime) { this.periodTime = periodTime; }
    public void setRoom(Room room){ this.room = room; }
    public int getId() { return id; }
    public StudentGroup getGroup() { return group; }
    public Subject getSubject() { return subject; }
    public Teacher getTeacher() { return teacher; }
    public PeriodTime getPeriodTime() { return periodTime; }
    public Room getRoom() { return room; }
    public String toString() {
        return "["+group.getName()+","+subject.getName()+","+room.getNumber()+","+teacher.getId()+","+periodTime.getId() +"]";
    }
}
