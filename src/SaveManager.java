import java.io.*;

public class SaveManager {
    private static final String FILE_PATH = "student.dat";
    private static final String GUILT_PATH = "guilt.dat";
    private static final String SEMESTER_PATH = "semester.dat";
    private static final String PROFESSOR_PATH = "professor.dat";
    private static final String DATE_PATH = "date.dat";

    // 학생 저장 및 불러오기
    public static void saveStudent(Student student) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(student);
            System.out.println("캐릭터 저장 완료");
        } catch (IOException e) {
            System.out.println("캐릭터 저장 실패: " + e.getMessage());
        }
    }

    public static Student loadStudent() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Student student = (Student) in.readObject();
            System.out.println("캐릭터 불러오기 성공");
            return student;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("캐릭터 불러오기 실패: " + e.getMessage());
            return null;
        }
    }

    // 자괴감 저장 및 불러오기
    public static void saveMentalManager(MentalManager mm) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(GUILT_PATH))) {
            out.writeObject(mm);
            System.out.println("자괴감 저장 완료");
        } catch (IOException e) {
            System.out.println("자괴감 저장 실패: " + e.getMessage());
        }
    }

    public static MentalManager loadMentalManager() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(GUILT_PATH))) {
            MentalManager mm = (MentalManager) in.readObject();
            MentalManager.setInstance(mm);
            System.out.println("자괴감 불러오기 성공");
            return mm;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("자괴감 불러오기 실패: " + e.getMessage());
            return MentalManager.getInstance();
        }
    }

    // 학기 저장 및 불러오기
    public static void saveSemester(SemesterManager sm) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SEMESTER_PATH))) {
            out.writeObject(sm);
            System.out.println("학기 저장 완료");
        } catch (IOException e) {
            System.out.println("학기 저장 실패: " + e.getMessage());
        }
    }

    public static SemesterManager loadSemester() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SEMESTER_PATH))) {
            SemesterManager sm = (SemesterManager) in.readObject();
            SemesterManager.setInstance(sm);
            System.out.println("학기 불러오기 성공");
            return sm;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("학기 불러오기 실패: " + e.getMessage());
            return SemesterManager.getInstance();
        }
    }

    // 교수 저장 및 불러오기
    public static void saveProfessor(ProfessorManager pm) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PROFESSOR_PATH))) {
            out.writeObject(pm);
            System.out.println("교수 저장 완료");
        } catch (IOException e) {
            System.out.println("교수 저장 실패: " + e.getMessage());
        }
    }

    public static ProfessorManager loadProfessor() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PROFESSOR_PATH))) {
            ProfessorManager pm = (ProfessorManager) in.readObject();
            ProfessorManager.setInstance(pm);
            System.out.println("교수 불러오기 성공");
            return pm;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("교수 불러오기 실패: " + e.getMessage());
            return ProfessorManager.getInstance();
        }
    }

    // 날짜 저장 및 불러오기
    public static void saveDate(VirtualDateManager dm) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATE_PATH))) {
            out.writeObject(dm);
            System.out.println("날짜 저장 완료");
        } catch (IOException e) {
            System.out.println("날짜 저장 실패: " + e.getMessage());
        }
    }

    public static VirtualDateManager loadDate() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATE_PATH))) {
            VirtualDateManager dm = (VirtualDateManager) in.readObject();
            VirtualDateManager.setInstance(dm);
            System.out.println("날짜 불러오기 성공");
            return dm;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("날짜 불러오기 실패: " + e.getMessage());
            return VirtualDateManager.getInstance();
        }
    }

    public static boolean hasSavedStudent() {
        File file = new File("student.dat");
        return file.exists();
    }


    // 전체 삭제
    public static void deleteAll() {
        new File(FILE_PATH).delete();
        new File(GUILT_PATH).delete();
        new File(SEMESTER_PATH).delete();
        new File(PROFESSOR_PATH).delete();
        new File(DATE_PATH).delete();
        System.out.println("모든 저장 파일 삭제 완료");
    }

    public static void resetAllData() {
        deleteAll();
        MentalManager.reset();
        SemesterManager.reset();
        VirtualDateManager.reset();
        ProfessorManager.reset();
    }

}
