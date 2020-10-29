import java.util.ArrayList;

public class Output {

    public static void main(String[] args) {

       Input i = new Input();
       ArrayList<PeriodTime> periodTimeFromInput = i.getPeriodTimes();
       ArrayList<Room> roomsFromInput = i.getRooms();
       ArrayList<Teacher> teachersFromInput = i.getTeachers();
       ArrayList<Subject> subjectsFromInput = i.getSubjects();
       ArrayList<StudentGroup> groupsFromInput = i.getGroups();

        CSP cspAlgorithm = new CSP(periodTimeFromInput,roomsFromInput,teachersFromInput,subjectsFromInput, groupsFromInput);
        cspAlgorithm.running();

    }

}
