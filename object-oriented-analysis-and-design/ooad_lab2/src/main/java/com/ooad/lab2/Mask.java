package com.ooad.lab2;


import com.ooad.lab2.domain.*;
import com.ooad.lab2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2022-01-07 21:42
 **/
@Service
public class Mask {

    private CourseRepository courseRepository;
    private InstructingDirectionRepository instructingDirectionRepository;
    private InstructingModuleRepository instructingModuleRepository;
    private InstructingPlanRepository instructingPlanRepository;
    private MajorRepository majorRepository;
    private ProgressRepository progressRepository;
    private StudentRepository studentRepository;
    private StudyRecordRepository studyRecordRepository;

    @Autowired
    public Mask(CourseRepository courseRepository, InstructingDirectionRepository instructingDirectionRepository, InstructingModuleRepository instructingModuleRepository, InstructingPlanRepository instructingPlanRepository, MajorRepository majorRepository, ProgressRepository progressRepository, StudentRepository studentRepository, StudyRecordRepository studyRecordRepository) {
        this.courseRepository = courseRepository;
        this.instructingDirectionRepository = instructingDirectionRepository;
        this.instructingModuleRepository = instructingModuleRepository;
        this.instructingPlanRepository = instructingPlanRepository;
        this.majorRepository = majorRepository;
        this.progressRepository = progressRepository;
        this.studentRepository = studentRepository;
        this.studyRecordRepository = studyRecordRepository;
    }


    public void clearAllRepo() {
        courseRepository.deleteAll();
        instructingDirectionRepository.deleteAll();
        instructingModuleRepository.deleteAll();
        instructingPlanRepository.deleteAll();
        majorRepository.deleteAll();
        progressRepository.deleteAll();
        studentRepository.deleteAll();
        studyRecordRepository.deleteAll();
    }

    public void initialize() {
        Util util = new Util();
        List<String> courseStrings = util.readFile("src\\main\\resources\\initial\\Courses_Info.txt");
        // 初始化课程
        if (null != courseStrings && !courseStrings.isEmpty()) {
            for (String string : courseStrings) {
                String[] attributes = util.splitStringByComma(string);
                for (int i = 0; i < attributes.length; i++) {
                    attributes[i] = attributes[i].trim();
                }
                if (null != courseRepository.findByCourseCode(attributes[0])) continue;
                Course course = new Course();
                course.setCourseCode(attributes[0]);
                course.setCourseName(attributes[1]);
                course.setCredits(Integer.parseInt(attributes[2]));
                if (!"none".equals(attributes[3])) {
                    course.setSubstantial(true);
                    course.setSubCourseCode(attributes[3]);
                } else {
                    course.setSubstantial(false);
                }
                courseRepository.save(course);
            }
        }
        // 初始化学习记录
        List<String> learningStrings = util.readFile("src\\main\\resources\\initial\\Learning.txt");
        if (null != learningStrings && !learningStrings.isEmpty()) {
            for (String learningString : learningStrings) {
                String[] attributes = util.splitStringByComma(learningString);
                for (int i = 0; i < attributes.length; i++) {
                    attributes[i] = attributes[i].trim();
                }
                if (null != studyRecordRepository.findByCourseCodeAndStudentNumber(attributes[1], attributes[0]))
                    continue;
                StudyRecord studyRecord = new StudyRecord();
                studyRecord.setStudentNumber(attributes[0]);
                studyRecord.setCourseCode(attributes[1]);
                studyRecordRepository.save(studyRecord);
            }
        }
        List<String> majorStrings = util.readFile("src\\main\\resources\\initial\\专业名.txt");
        if (null != majorStrings && !majorStrings.isEmpty()) {
            for (String majorString : majorStrings) {
                String[] attributes = util.splitStringByComma(majorString);
                for (int i = 0; i < attributes.length; i++) {
                    attributes[i] = attributes[i].trim();
                }
                if (null != majorRepository.findMajorByMajorName(attributes[0])) continue;
                Major major = new Major();
                major.setMajorName(attributes[0]);
                majorRepository.save(major);
            }
        }
        for (String majorString : majorStrings) {
            if ("Network Engineering".equals(majorString)) continue;
            initializeMajor(majorString);
        }
        // 初始学生信息
        List<String> studentStrings = util.readFile("src\\main\\resources\\initial\\Students_Info.txt");
        if (null != studentStrings && !studentStrings.isEmpty()) {
            for (String studentString : studentStrings) {
                String[] attributes = util.splitStringByComma(studentString);
                for (int i = 0; i < attributes.length; i++) {
                    attributes[i] = attributes[i].trim();
                }
                if (null != studentRepository.findByStudentNumber(attributes[0])) {
                    continue;
                }
                Student student = new Student();
                student.setStudentNumber(attributes[0]);
                student.setName(attributes[1]);
                Major major = majorRepository.findMajorByMajorName(attributes[2]);
                student.setMajor(major);
                studentRepository.save(student);
            }
        }

    }

    public Major initializeMajor(String majorName) {
        Util util = new Util();
        Major major = majorRepository.findMajorByMajorName(majorName);
        String fileName = "src\\main\\resources\\initial\\" + majorName + ".txt";
        List<String> majorDetails = util.readFile(fileName);
        int[] marks = new int[8];
        int start = 0;
        for (int i = 0; i < majorDetails.size(); i++) {
            if (majorDetails.get(i).startsWith("[")) {
                marks[start++] = i;
            }
        }

        String[] info = majorDetails.get(0).split(" ");
        major.setBasicCompulsoryCredit(Integer.parseInt(info[info.length - 1]));
        Set<Course> basicCompulsoryCourseSet = new HashSet<>();
        for (int i = 1; i < marks[1]; i++) {
            basicCompulsoryCourseSet.add(courseRepository.findByCourseCode(majorDetails.get(i)));
        }
        major.setBasicCompulsoryCourses(basicCompulsoryCourseSet);


        info = majorDetails.get(marks[1]).split(" ");
        major.setMajorCompulsoryCredit(Integer.parseInt(info[info.length - 1]));
        Set<Course> majorCompulsoryCourseSet = new HashSet<>();
        for (int i = marks[1] + 1; i < marks[2]; i++) {
            majorCompulsoryCourseSet.add(courseRepository.findByCourseCode(majorDetails.get(i)));
        }
        major.setMajorCompulsoryCourses(majorCompulsoryCourseSet);

        info = majorDetails.get(marks[2]).split(" ");
        major.setMajorElectiveCredit(Integer.parseInt(info[info.length - 1]));
        Set<Course> majorElectiveCourseSet = new HashSet<>();
        for (int i = marks[2] + 1; i < marks[3]; i++) {
            majorElectiveCourseSet.add(courseRepository.findByCourseCode(majorDetails.get(i)));
        }
        major.setMajorElectiveCourses(majorElectiveCourseSet);
        majorRepository.save(major);

        info = majorDetails.get(marks[3]).split(" ");
        InstructingModule instructingModule1 = new InstructingModule();
        instructingModule1.setName("1");
        instructingModule1.setCredits(Integer.parseInt(info[info.length - 1]));
        Set<Course> courses = new HashSet<>();
        for (int i = marks[3] + 1; i < marks[4]; i++) {
            courses.add(courseRepository.findByCourseCode(majorDetails.get(i)));
        }
        instructingModule1.setCourses(courses);
        instructingModuleRepository.save(instructingModule1);

        info = majorDetails.get(marks[4]).split(" ");
        InstructingModule instructingModule2 = new InstructingModule();
        instructingModule2.setName("2");
        instructingModule2.setCredits(Integer.parseInt(info[info.length - 1]));
        courses = new HashSet<>();
        for (int i = marks[4] + 1; i < marks[5]; i++) {
            courses.add(courseRepository.findByCourseCode(majorDetails.get(i)));
        }
        instructingModule2.setCourses(courses);
        instructingModuleRepository.save(instructingModule2);
        Set<InstructingModule> modules = new HashSet<>();
        modules.add(instructingModule1);
        modules.add(instructingModule2);
        major.setModules(modules);

        info = majorDetails.get(marks[5]).split(" ");
        InstructingDirection instructingDirection1 = new InstructingDirection();
        instructingDirection1.setName("A");
        instructingDirection1.setCredits(Integer.parseInt(info[info.length - 1]));
        courses = new HashSet<>();
        for (int i = marks[5] + 1; i < marks[6]; i++) {
            courses.add(courseRepository.findByCourseCode(majorDetails.get(i)));
        }
        instructingDirection1.setCourses(courses);
        instructingDirectionRepository.save(instructingDirection1);

        info = majorDetails.get(marks[6]).split(" ");
        InstructingDirection instructingDirection2 = new InstructingDirection();
        instructingDirection2.setName("B");
        instructingDirection2.setCredits(Integer.parseInt(info[info.length - 1]));
        courses = new HashSet<>();
        for (int i = marks[6] + 1; i < marks[7]; i++) {
            courses.add(courseRepository.findByCourseCode(majorDetails.get(i)));
        }
        instructingDirection2.setCourses(courses);
        instructingDirectionRepository.save(instructingDirection2);

        Set<InstructingDirection> directions = new HashSet<>();
        directions.add(instructingDirection1);
        directions.add(instructingDirection2);
        major.setDirections(directions);
        info = majorDetails.get(marks[7]).split(" ");
        major.setOtherElectiveCredits(Integer.parseInt(info[info.length - 1]));
        majorRepository.save(major);
        return major;
    }

    public ProgressOutput outputStudent(String studentNumber, String directionStr) {
        Student student = studentRepository.findByStudentNumber(studentNumber);
        if (null == student) return null;
        ProgressOutput output = new ProgressOutput();
        Major major = student.getMajor();
        String name = student.getName();
        String stuNumber = student.getStudentNumber();
        String majorName = major.getMajorName();
        output.setName(student.getName());
        output.setStudentNumber(stuNumber);
        output.setMajor(majorName);
        output.setDirection(directionStr);
        Set<StudyRecord> studyRecords = studyRecordRepository.findAllByStudentNumber(stuNumber);
        HashSet<Course> studiedCourses = new HashSet<>();
        for (StudyRecord studyRecord : studyRecords) {
            studiedCourses.add(courseRepository.findByCourseCode(studyRecord.getCourseCode()));
        }

        Set<Course> basicCompulsoryCourses = major.getBasicCompulsoryCourses();
        Set<Course> majorCompulsoryCourses = major.getMajorCompulsoryCourses();
        Set<Course> majorElectiveCourses = major.getMajorElectiveCourses();
        Set<InstructingDirection> instructingDirections = major.getDirections();
        InstructingDirection myInstructingDirection = new InstructingDirection();
        Set<Course> directionCourses = new HashSet<>();
        Set<Course> anotherDirectionCourses = new HashSet<>();
        InstructingDirection[] directions = new InstructingDirection[2];
        int cnt = 0;
        for (InstructingDirection instructingDirection : instructingDirections) {
            directions[cnt++] = instructingDirection;
        }
        if (directions[0].getName().equals(directionStr)) {
            myInstructingDirection = directions[0];
            directionCourses = directions[0].getCourses();
            anotherDirectionCourses = directions[1].getCourses();
        } else {
            myInstructingDirection = directions[1];
            directionCourses = directions[1].getCourses();
            anotherDirectionCourses = directions[0].getCourses();
        }
        Set<InstructingModule> instructingModules = major.getModules();
        Set<Course> moduleOneCourses = new HashSet<>();
        Set<Course> moduleTwoCourses = new HashSet<>();


        SingleProgressOutput basicOutput = new SingleProgressOutput();
        List<Object> basicList = new ArrayList<>();
        basicOutput.setType("必修的基础课与专业基础课");
        basicOutput.setRequired(major.getBasicCompulsoryCredit() + major.getMajorCompulsoryCredit());
        SingleProgressOutput majorElectiveOutput = new SingleProgressOutput();
        List<Object> majorElectiveList = new ArrayList<>();
        majorElectiveOutput.setType("专业选修课");
        majorElectiveOutput.setRequired(major.getMajorElectiveCredit());
        SingleProgressOutput moduleOneOutput = new SingleProgressOutput();
        moduleOneOutput.setType("模块课 1");
        List<Object> moduleOneList = new ArrayList<>();
        SingleProgressOutput moduleTwoOutput = new SingleProgressOutput();

        moduleTwoOutput.setType("模块课 2");
        List<Object> moduleTwoList = new ArrayList<>();
        SingleProgressOutput electiveOutput = new SingleProgressOutput();
        electiveOutput.setType("任意选修课");
        List<Object> electiveList = new ArrayList<>();
        electiveOutput.setRequired(major.getOtherElectiveCredits());
        SingleProgressOutput directionOutput = new SingleProgressOutput();
        directionOutput.setType("方向课 " + directionStr);
        directionOutput.setRequired(myInstructingDirection.getCredits());
        List<Object> directionList = new ArrayList<>();

        for (InstructingModule instructingModule : instructingModules) {
            if ("1".equals(instructingModule.getName())) {
                moduleOneCourses = instructingModule.getCourses();
                moduleOneOutput.setRequired(instructingModule.getCredits());
            } else {
                moduleTwoCourses = instructingModule.getCourses();
                moduleTwoOutput.setRequired(instructingModule.getCredits());
            }
        }


        SingleProgressOutput[] singleProgressOutputs = {basicOutput, majorElectiveOutput, moduleOneOutput, moduleTwoOutput, electiveOutput, directionOutput};
        List<Object>[] resultLists = new List[]{basicList, majorElectiveList, moduleOneList, moduleTwoList, electiveList, directionList};

        for (Course studiedCours : studiedCourses) {
            boolean calculated = false;
            if (isCourseCodeInSet(studiedCours, basicCompulsoryCourses) || isCourseCodeInSet(studiedCours, majorCompulsoryCourses)) {
                addToStatics(studiedCours, basicList, basicOutput);
                calculated = true;
            }
            if (isCourseCodeInSet(studiedCours, majorElectiveCourses)) {
                addToStatics(studiedCours, majorElectiveList, majorElectiveOutput);
                calculated = true;
            }
            if (isCourseCodeInSet(studiedCours, directionCourses)) {
                addToStatics(studiedCours, directionList, directionOutput);
                calculated = true;
            }
            if (isCourseCodeInSet(studiedCours, moduleOneCourses)) {
                addToStatics(studiedCours, moduleOneList, moduleOneOutput);
                calculated = true;
            }
            if (isCourseCodeInSet(studiedCours, moduleTwoCourses)) {
                addToStatics(studiedCours, moduleTwoList, moduleTwoOutput);
                calculated = true;
            }
            if (!calculated && !isCourseCodeInSet(studiedCours, anotherDirectionCourses)) {
                addToStatics(studiedCours, electiveList, electiveOutput);
            }
        }

        ArrayList<SingleProgressOutput> results = new ArrayList<>();
        HashMap<String, List<Object>> resultMap = new HashMap<>();
        //统计各部分
        for (int i = 0; i < singleProgressOutputs.length; i++) {
            SingleProgressOutput singleProgressOutput = singleProgressOutputs[i];
            double perc = (double) singleProgressOutput.getCredits() / (double) singleProgressOutput.getRequired();
            int percInt = (int) (perc * 100);
            singleProgressOutput.setPercentage((percInt >= 100 ? 100 : percInt) + "%");
            if (singleProgressOutput != electiveOutput) {
                CourseOutput tmp = new CourseOutput();
                tmp.setCourseName(singleProgressOutput.getType() + "超出学分");

                if ("模块课 1".equals(singleProgressOutput.getType()) || "模块课 2".equals(singleProgressOutput.getType())) {
                    if (singleProgressOutput.getCourses() > singleProgressOutput.getRequired()) {
                        int overCourse = singleProgressOutput.getCourses() - singleProgressOutput.getRequired();
                        int overCredits = 2 * overCourse;
                        tmp.setCredits(overCredits);
                    } else {
                        tmp.setCredits(0);
                    }
                } else {
                    if (singleProgressOutput.getCredits() > singleProgressOutput.getRequired()) {
                        tmp.setCredits(singleProgressOutput.getCredits() - singleProgressOutput.getRequired());
                    } else {
                        tmp.setCredits(0);
                    }
                }
                tmp.setRemarks("null");
                electiveList.add(tmp);
                electiveOutput.setCredits(electiveOutput.getCredits());
            }


            ConclusionOutput conclusion = new ConclusionOutput();
            StringBuilder sb = new StringBuilder();
            if ("模块课 1".equals(singleProgressOutput.getType()) || "模块课 2".equals(singleProgressOutput.getType())) {
                int left = singleProgressOutput.getRequired() - singleProgressOutput.getCourses();
                if (left < 0) left = 0;
                int over = singleProgressOutput.getCourses() - singleProgressOutput.getRequired();
                if (over < 0) over = 0;
                sb.append("要求").append(singleProgressOutput.getRequired()).append("门课，缺少").append(left).append("门课").append("。");
            } else {
                int alreadyCredits = singleProgressOutput.getCredits();
                if (singleProgressOutput.getType().equals("任意选修课")) {
                    alreadyCredits = 0;
                    for (Object o : electiveList) {
                        if (!(((CourseOutput) o).getCourseName().charAt(0) <= 'Z' && ((CourseOutput) o).getCourseName().charAt(0) >= 'A')) {
                            alreadyCredits += ((CourseOutput) o).getCredits();
                        }
                    }
                }

                int left = singleProgressOutput.getRequired() - alreadyCredits;
                if (left < 0) left = 0;
                int over = alreadyCredits - singleProgressOutput.getRequired();
                if (over < 0) over = 0;
                sb.append("要求").append(singleProgressOutput.getRequired()).append("学分，缺少").append(left).append("学分");
                if (over > 0) {
                    sb.append("，超出").append(over).append("学分。");
                } else {
                    sb.append("。");
                }
            }
            conclusion.setConclusion(sb.toString());
            conclusion.setRemarks(singleProgressOutput.getPercentage());

            results.add(singleProgressOutput);
            List<Object> tmpList = resultLists[i];
            tmpList.add(conclusion);
            resultMap.put(singleProgressOutput.getType(), tmpList);
        }


        output.setProgressOutputs(results);
        output.setProgressConclusion(resultMap);
        return output;
    }

    private boolean isCourseCodeInSet(Course course, Set<Course> courses) {
        if (null == courses) return false;
        for (Course cours : courses) {
            if (course.getCourseCode().equals(cours.getCourseCode()) || (cours.getSubstantial() && cours.getSubCourseCode().equals(course.getCourseCode()))) {
                return true;
            }
        }
        return false;
    }



    private void addToStatics(Course course, List<Object> courseList, SingleProgressOutput singleProgressOutput) {
        if (null != course.getSubCourseCode() && "exchange".equals(course.getSubCourseCode())){
            course = courseRepository.findCourseBySubCourseCode(course.getCourseCode());
        }
        singleProgressOutput.setCourses(singleProgressOutput.getCourses() + 1);
        singleProgressOutput.setCredits(singleProgressOutput.getCredits() + course.getCredits());
        CourseOutput tmp = new CourseOutput();
        tmp.setCourseName(course.getCourseCode() + "-" + course.getCourseName());
        tmp.setCredits(course.getCredits());
        tmp.setRemarks(course.getSubCourseCode());
        if (null == course.getSubCourseCode()) {
            tmp.setRemarks("null");
        }
        courseList.add(tmp);

    }
}
