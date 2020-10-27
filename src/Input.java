import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Input {
//    public static final int POPULATION_SIZE = 9;
//    public static final double MUTATION_RATE = 0.1;
//    public static final double CROSSOVER_RATE = 0.9;
//    public static final int TOURNAMENT_SELECTION_SIZE = 3;
//    public static final int NUMB_OF_ELITE_SCHEDULES = 1;
//    private int scheduleNumb = 0;
//    private int classNumb = 1;


    public static ArrayList<Room> rooms = new ArrayList<>();
    public static ArrayList<Teacher> teachers = new ArrayList<>();
    public static ArrayList<Subject> subjects =new ArrayList<>();
    public static ArrayList<StudentGroup> groups =new ArrayList<>();
    public static ArrayList<PeriodTime> periodTimes =new ArrayList<>();

//    public static void main(String[] args) {
//        takeinput();
//        System.out.println(rooms);
//        System.out.println(subjects);
//        System.out.println(teachers);
//        System.out.println(groups);
//        System.out.println(periodTimes);
//    }



    public Input(){
        takeinput();
    }
    public static void takeinput()// takes input from file input.txt
    {
        try {
            File file = new File("input.txt");
            Scanner scanner = new Scanner(file);


            String line = scanner.nextLine();

            if(line.equals("rooms")){
                while(true) {
                    line = scanner.nextLine();
                    if (!line.equals("periodTime")) {
                        String[] currentRoom = line.split(";");
                        Room r = new Room(currentRoom[0], Integer.parseInt(currentRoom[1]));
                        rooms.add(r);
                    }else{
                        //якщо лінія - мітінг тайм
                        break;
                    }
                }
            }
            if(line.equals("periodTime")){
                while(true) {
                    line = scanner.nextLine();
                    if (!line.equals("teachers")) {
                        String[] currentMeetingTime = line.split(";");
                        periodTimes.add(new PeriodTime(currentMeetingTime[0],currentMeetingTime[1]));
                    }else{
                        //якщо лінія - instructors
                        break;
                    }
                }
            }
            if(line.equals("teachers")){
                while(true) {
                    line = scanner.nextLine();
                    if (!line.equals("subjects")) {
                        String[] currentInstructor = line.split(";");
                        teachers.add(new Teacher(Integer.parseInt(currentInstructor[0]),currentInstructor[1]));
                    }else{
                        //якщо лінія - courses
                        break;
                    }
                }
            }
            if(line.equals("subjects")){
                while(true) {
                    line = scanner.nextLine();
                    if (!line.equals("groups")) {
                        String[] currentCourse = line.split(";");
                        //додаємо лекцію
                        //находимо лектора за індексом
                        ArrayList<Teacher> t_l = new ArrayList<>();
                        for(int i = 0; i< teachers.size(); i++){
                            if(teachers.get(i).id == Integer.parseInt(currentCourse[2])){
                                t_l.add(teachers.get(i));
                            }
                        }
                        subjects.add(new Subject(Integer.parseInt(currentCourse[0]+"0"),currentCourse[1],"L", t_l));
                        //practice course

                        ArrayList<Teacher> t_p = new ArrayList<>();
                        for(int j=3; j<currentCourse.length;j++){
                            for(int i = 0; i< teachers.size(); i++){
                                if(Integer.parseInt(currentCourse[j]) == teachers.get(i).id){
                                    t_p.add(teachers.get(i));
                                    break;
                                }
                            }
                        }
                        subjects.add(new Subject(Integer.parseInt(currentCourse[0]+"1"),currentCourse[1],"P", t_p));
                    }else{
                        //якщо лінія - depts
                        break;
                    }
                }
            }
            if(line.equals("groups")) {
                while (true) {
                    line = scanner.nextLine();
                    if (!line.equals("end")) {
                        String[] currentDep = line.split(";");
                        ArrayList<Subject> dep_subjects = new ArrayList<>();
                        for (int j = 1; j < currentDep.length; j++) {
                            for (int i = 0; i < subjects.size(); i++) {
                                if (Integer.parseInt(currentDep[j] + "0") == subjects.get(i).id) {
                                    dep_subjects.add(subjects.get(i));

                                }
                                if (Integer.parseInt(currentDep[j] + "1") == subjects.get(i).id) {
                                    dep_subjects.add(subjects.get(i));
                                }
                            }
                        }
                        groups.add(new StudentGroup(currentDep[0], dep_subjects));
                    } else {
                        break;
                    }
                }
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Room> getRooms() {
        return rooms;
    }

    public static ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public static ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public static ArrayList<StudentGroup> getGroups() {
        return groups;
    }

    public static ArrayList<PeriodTime> getPeriodTimes() {
        return periodTimes;
    }


}
