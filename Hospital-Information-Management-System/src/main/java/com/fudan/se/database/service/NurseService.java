package com.fudan.se.database.service;

import com.fudan.se.database.domain.*;
import com.fudan.se.database.repository.*;
import com.fudan.se.database.request.*;
import com.fudan.se.database.response.SickBedResponse;
import org.aspectj.weaver.ast.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-24 21:13
 **/
@Service
public class NurseService {

    Logger logger = LoggerFactory.getLogger(NurseService.class);
    private StateRepository stateRepository;
    private PatientRepository patientRepository;
    private NucleicAcidTestRepository nucleicAcidTestRepository;
    private SickBedRepository sickBedRepository;
    private SickRoomRepository sickRoomRepository;
    private RoomNurseRepository roomNurseRepository;
    private CareRepository careRepository;
    private DoctorRepository doctorRepository;
    private NurseLeaderRepository nurseLeaderRepository;
    private StaffRepository staffRepository;
    @Autowired
    @Lazy(true)
    private StaffService staffService;

    @Autowired
    public NurseService(StateRepository stateRepository, PatientRepository patientRepository, NucleicAcidTestRepository nucleicAcidTestRepository, SickBedRepository sickBedRepository, SickRoomRepository sickRoomRepository, RoomNurseRepository roomNurseRepository, CareRepository careRepository, DoctorRepository doctorRepository, NurseLeaderRepository nurseLeaderRepository, StaffRepository staffRepository) {
        this.stateRepository = stateRepository;
        this.patientRepository = patientRepository;
        this.nucleicAcidTestRepository = nucleicAcidTestRepository;
        this.sickBedRepository = sickBedRepository;
        this.sickRoomRepository = sickRoomRepository;
        this.roomNurseRepository = roomNurseRepository;
        this.careRepository = careRepository;
        this.doctorRepository = doctorRepository;
        this.nurseLeaderRepository = nurseLeaderRepository;
        this.staffRepository = staffRepository;
    }

    public Patient findPatient(Integer patientID) {
        return patientRepository.findPatientByPatientID(patientID);
    }

    @Transactional
    public State submitDailyInformation(@RequestBody SubmitDailyInformationRequest request) {
        Date date = new Date();
        date.setTime(date.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 设置日期格式
        String dateStr = simpleDateFormat.format(date);  // 格式转换
        Date dateTo = date;
        try {
             dateTo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        }catch (Exception e){
            logger.warn(e.toString());
        }
        logger.warn(dateTo.toString());
        if(findLatestState(request.getPatientId()).getStateDate().toString().split(" ")[0].equals(dateTo.toString().split(" ")[0])){
            return null;
        }
        State state = new State(dateTo, request.getTemperature(), 0, request.getLiveState(), request.getSickLevel(), request.getSymptom(), request.getPatientId());
        stateRepository.save(state);
        logger.info("登记今日状态" + state.toString());
        Patient patient = patientRepository.findPatientByPatientID(request.getPatientId());
        patient.setLiveState(request.getLiveState());
        patient.setSickLevel(request.getSickLevel());
        patient.setTemperature(request.getTemperature());
        modifyPatient(patient);
        logger.info("登记后的患者" + patientRepository.findPatientByPatientID(patient.getPatientID()).toString());
        return state;
    }

    @Transactional
    public NucleicAcidTest submitNewTest(@RequestBody SubmitNewTest request) {
        NucleicAcidTest test = new NucleicAcidTest(request.getTestDate(), request.getTestResult(), 1, request.getSickLevel(), request.getPatientId());
        if(findLatestTest(request.getPatientId()).getTestDate().toString().split(" ")[0].equals(test.getTestDate().toString().split(" ")[0])){
            return null;
        }
        nucleicAcidTestRepository.save(test);
        logger.info("提交测试" + test.toString());
        return test;
    }

    @Transactional
    public Patient importPatient(ImportPatientRequest importPatientRequest) {
        Patient newPatient = new Patient(0, importPatientRequest.getName(), importPatientRequest.getGender(), importPatientRequest.getAge(), importPatientRequest.getTemperature(), 1, importPatientRequest.getSickLevel());
        patientRepository.save(newPatient);
        Integer patientID = newPatient.getPatientID();

        NucleicAcidTest nucleicAcidTest = new NucleicAcidTest();
        nucleicAcidTest.setPatientID(patientID);
        nucleicAcidTest.setTestDate(importPatientRequest.getTestDate());
        nucleicAcidTest.setTestLiveState(1);
        nucleicAcidTest.setTestResult(importPatientRequest.getTestResult());
        nucleicAcidTest.setTestSickLevel(importPatientRequest.getSickLevel());
        nucleicAcidTestRepository.save(nucleicAcidTest);
        State newState = new State(importPatientRequest.getTestDate(), importPatientRequest.getTemperature(), nucleicAcidTest.getTestID(), 1, importPatientRequest.getSickLevel(), "", patientID);
        stateRepository.save(newState);
        allocateSickBed(newPatient);
        return newPatient;
    }

    @Transactional
    public void allocateSickBedForSomeBody(Integer treatArea){
        for (Patient patient : patientRepository.findAll()) {
            if(patient.getSickBedID() == 0 && patient.getSickLevel().equals(treatArea)){
                allocateSickBed(patient);
                break;
            }
        }
    }
    @Transactional
    public boolean allocateSickBed(Patient patient) {
        Integer sickBedID = 0;
        Integer roomNurseID = 0;
        Integer treatArea = patient.getSickLevel();
        if (null == doctorRepository.findDoctorByTreatAreaID(treatArea) || null == nurseLeaderRepository.findNurseLeaderByTreatAreaID(treatArea)) {
            logger.info("该区域没有医生或护士长");
            return false;
        }
        HashSet<SickRoom> sickRooms = sickRoomRepository.findAllByTreatAreaID(patient.getSickLevel());
        a:
        for (SickRoom sickRoom : sickRooms) {
            HashSet<SickBed> sickBeds = sickBedRepository.findAllBySickRoomID(sickRoom.getSickRoomID());
            for (SickBed sickBed : sickBeds) {
                if (null == patientRepository.findPatientBySickBedID(sickBed.getSickBedID())) {
                    sickBedID = sickBed.getSickBedID();
                    logger.info("成功分配床位" + sickBedID);
                    break a;
                }
            }
        }

        if (sickBedID == 0) {
            logger.info("sickbed == 0");
        }
        int[] dy = {3, 2, 1};
        HashSet<RoomNurse> roomNurses = roomNurseRepository.findAllByTreatAreaID(patient.getSickLevel());
        for (RoomNurse roomNurs : roomNurses) {
            if (careRepository.findAllByRoomNurseID(roomNurs.getStaffID()).size() < dy[patient.getSickLevel() - 1]) {
                roomNurseID = roomNurs.getStaffID();
                logger.info("成功分配护士" + roomNurseID);
                break;
            }
        }

        if (sickBedID == 0 || roomNurseID == 0) {
            return false;
        } else {
            RoomNurse roomNurse = roomNurseRepository.findByStaffID(roomNurseID);
            if (null != roomNurse) {
                Care care = new Care(patient.getPatientID(), roomNurseID);
                careRepository.save(care);
                patient.setSickBedID(sickBedID);
                patientRepository.save(patient);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean reach(Integer roomNurseId){
        Integer treatArea = staffService.findTreatAreaByStaffId(roomNurseId);
        int[] sx = {3,2,1};
        HashSet<Care> cares = careRepository.findAllByRoomNurseID(roomNurseId);
        if(cares.size() >= sx[treatArea-1]){
            logger.warn(roomNurseId + "照顾的病人已达上限");
            return true;
        }else{
            return false;
        }
    }
    @Transactional
    public Staff insertNurse(@RequestBody AddNewRoomNurseRequest request) {
        Integer treateArea = nurseLeaderRepository.findByStaffID(request.getNurseLeaderId()).getTreatAreaID();
        logger.info(treateArea + "区域的护士长添加护士");
        Staff staff = staffService.register(request.getName(), "123", request.getAge(), request.getGender(), 3, treateArea);
//        HashSet<Patient> patients = patientRepository.findAllBySickBedIDAndSickLevel(0,treateArea);
        Iterable<Patient> p = patientRepository.findAll();
        HashSet<Patient> patients = new HashSet<>();
        for (Patient patient : p) {
            patients.add(patient);
        }
        if(null != patients && 0 != patients.size() && null != staff && !reach(staff.getId())){
                for (Patient patient : patients) {
                    if(reach(staff.getId())){
                        break;
                    }
                    if (patient.getSickBedID() == 0 && patient.getSickLevel().equals(treateArea)) {
                        allocateSickBed(patient);
                    }
                }
        }


        return staff;
    }

    public Staff findNurseLeader(Integer treat) {
        NurseLeader nurseLeader;
        nurseLeader = nurseLeaderRepository.findNurseLeaderByTreatAreaID(treat);

        return staffRepository.findStaffById(nurseLeader.getStaffID());
    }

    @Transactional
    public boolean deleteRoomNurse(Integer roomNurseId) {
        HashSet<Care> cares = careRepository.findAllByRoomNurseID(roomNurseId);
        if (null == cares || 0 == cares.size()) {
            staffRepository.deleteById(roomNurseId);
            roomNurseRepository.deleteByStaffID(roomNurseId);
            logger.warn("删除护士" + roomNurseId);
            return true;
        } else {
            return false;
        }

    }

    public HashSet<SickBedResponse> findSickBedsInArea(Integer area) {
        HashSet<SickRoom> sickRooms = sickRoomRepository.findAllByTreatAreaID(area);
        HashSet<SickBedResponse> sickBedResponses = new HashSet<>();
        for (SickRoom sickRoom : sickRooms) {
            HashSet<SickBed> sickBeds = sickBedRepository.findAllBySickRoomID(sickRoom.getSickRoomID());
            for (SickBed sickBed : sickBeds) {
                Patient patient = patientRepository.findPatientBySickBedID(sickBed.getSickBedID());
                int patientId = null == patient ? -1 : patient.getPatientID();
                sickBedResponses.add(new SickBedResponse(sickBed.getSickBedID(), patientId, sickBed.getSickBedCount(), sickBed.getSickRoomID()));
            }
        }
        return sickBedResponses;
    }


    public HashSet<Staff> findRoomNurseInTreatArea(Integer TreatAreaId) {
        HashSet<RoomNurse> roomNurses = roomNurseRepository.findAllByTreatAreaID(TreatAreaId);
        HashSet<Staff> staffs = new HashSet<>();
        for (RoomNurse roomNurs : roomNurses) {
            staffs.add(staffRepository.findStaffById(roomNurs.getStaffID()));
        }
        return staffs;
    }

    public HashSet<Patient> findPatientsInTreatArea(Integer TreatAreaId) {
        Iterable<Patient> p = patientRepository.findAll();
        HashSet<Patient> patients = new HashSet<>();
        HashSet<Patient> result = new HashSet<>();
        for (Patient patient : p) {
            patients.add(patient);
        }
        if(null == patients || 0 == patients.size()){
            return new HashSet<>();
        }else{
            for (Patient patient : patients) {
                if (findWhereSickBedLocated(patient.getSickBedID()).equals(TreatAreaId)){
                    result.add(patient);
                }
            }
        }


        return result;
    }

    public HashSet<Patient> findPatientsCanLeaveHospital() {
        HashSet<Patient> result = new HashSet<>();
        HashSet<Patient> patients = new HashSet<>();
        for (Patient patient : patientRepository.findAll()) {
            patients.add(patient);
        }
        for (Patient patient : patients) {
            if (null == findLatestCorrectThreeState(patient.getPatientID()) || 0 == findLatestCorrectThreeState(patient.getPatientID()).size()) {
                logger.warn(patient.getPatientID() +"号病人想出院，但健康状态数不够");
                continue;
            }
            if (null == findLatestCorrectTwoTest(patient.getPatientID()) || 0 == findLatestCorrectTwoTest(patient.getPatientID()).size()) {
                logger.warn(patient.getPatientID() +"号病人想出院，但健康检测数不够");
                continue;
            }
            logger.info(patient.getPatientID() + "病人可以出院了！");
            result.add(patient);
        }
        return result;
    }

    private NucleicAcidTest findLatestTest(int patientId){
        ArrayList<NucleicAcidTest> list = nucleicAcidTestRepository.findAllByPatientID(patientId);
        if(null == list){
            return null;
        }
        list.sort(new Comparator<NucleicAcidTest>() {
            @Override
            public int compare(NucleicAcidTest o1, NucleicAcidTest o2) {
                return (int) (o2.getTestDate().getTime() - o1.getTestDate().getTime());
            }
        });
        return list.get(0);
    }
    public State findLatestState(int patientId) {
        ArrayList<State> list = stateRepository.findAllByPatientID(patientId);
        if(null == list){
            return null;
        }
        list.sort(new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                return (int) (o2.getStateDate().getTime() - o1.getStateDate().getTime());
            }
        });
        return list.get(0);
    }
    //有两条包括今天的就返回，没有返回null
    public ArrayList<NucleicAcidTest> findLatestCorrectTwoTest(int patientId) {
        ArrayList<NucleicAcidTest> list = nucleicAcidTestRepository.findAllByPatientID(patientId);
        list.sort(new Comparator<NucleicAcidTest>() {
            @Override
            public int compare(NucleicAcidTest o1, NucleicAcidTest o2) {
                return (int) (o2.getTestDate().getTime() - o1.getTestDate().getTime());
            }
        });
        ArrayList<NucleicAcidTest> result = new ArrayList<>();
        if (list.size() >= 2) {
            Date date = new Date();
            if (ifPreviousDay(date, list.get(0).getTestDate(), 0)) {
                if (list.get(0).getTestResult() == 1) {
                    result.add(list.get(0));
                    logger.info("存在" + list.get(0).getTestDate().toString() + "的阴性检测");
                } else {
                    logger.info("存在" + list.get(0).getTestDate().toString() + "的阳性检测");
                }
            } else {
                logger.warn(list.get(0).getTestDate().toString() + "的检测不是今天的检测");
                return null;
            }
            if (ifPreviousDay(date, list.get(1).getTestDate(), 1)) {
                if (list.get(1).getTestResult() == 1) {
                    result.add(list.get(1));
                    logger.info("存在" + list.get(1).getTestDate().toString() + "的阴性检测");
                } else {
                    logger.info("存在" + list.get(1).getTestDate().toString() + "的阳性检测");
                }
            } else {
                logger.warn(list.get(1).getTestDate().toString() + "的检测不是昨天的检测");
                return null;
            }
        }
        return result;
    }

    //有三条就返回，没有返回null
    public ArrayList<State> findLatestCorrectThreeState(int patientId) {
        ArrayList<State> list = stateRepository.findAllByPatientID(patientId);
        list.sort(new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                return (int) (o2.getStateDate().getTime() - o1.getStateDate().getTime());
            }
        });
        ArrayList<State> result = new ArrayList<>();
        if (list.size() >= 3) {
            Date date = new Date();
            if (ifPreviousDay(date, list.get(0).getStateDate(), 0)) {
                if (list.get(0).getTemperature() < 37.3) {
                    result.add(list.get(0));
                    logger.info("存在" + list.get(0).getStateDate().toString() + "的体温正常病人状态");
                } else {
                    logger.info("存在" + list.get(0).getStateDate().toString() + "的体温不正常病人状态");
                }
            } else {
                logger.warn(list.get(0).getStateDate().toString() + "的检测不是今天的状态");
                return null;
            }
            if (ifPreviousDay(date, list.get(1).getStateDate(), 1)) {
                if (list.get(1).getTemperature() < 37.3) {
                    result.add(list.get(1));
                    logger.info("存在" + list.get(1).getStateDate().toString() + "的体温正常病人状态");
                } else {
                    logger.info("存在" + list.get(1).getStateDate().toString() + "的体温不正常病人状态");
                }
            } else {
                logger.warn(list.get(1).getStateDate().toString() + "的检测不是昨天的状态");
                return null;
            }
            if (ifPreviousDay(date, list.get(2).getStateDate(), 2)) {
                if (list.get(2).getTemperature() < 37.3) {
                    result.add(list.get(2));
                    logger.info("存在" + list.get(2).getStateDate().toString() + "的体温正常病人状态");
                } else {
                    logger.info("存在" + list.get(2).getStateDate().toString() + "的体温不正常病人状态");
                }
            } else {
                logger.warn(list.get(2).getStateDate().toString() + "的检测不是前天的状态");
                return null;
            }
        }
        return result;
    }

    public HashSet<Patient> searchSpecialPatient(Integer treatArea) {
        HashSet<Patient> patients = findPatientsInTreatArea(treatArea);
        HashSet<Patient> result = new HashSet<>();
        for (Patient patient : patients) {
            Integer nowLocation = findWhereSickBedLocated(patient.getSickBedID());
            if (!nowLocation.equals(patient.getSickLevel())) {
                logger.info("病人" + patient.getPatientID() + "的等级是" + patient.getSickLevel() + "，但他却在区域" + nowLocation);
                result.add(patient);
            }
        }
        return result;
    }

    public HashSet<Patient> searchPatientByState(@RequestBody searchPatientRequest request) {
        //权限范围
        Integer area = staffService.findTreatAreaByStaffId(request.getStaffId());
        if(area == -1){
            HashSet<Patient> result = new HashSet<>();
            if(null != request.getPatientId()) {
                Patient patient = patientRepository.findPatientByPatientID(request.getPatientId());
                if (null != patient) {
                    result.add(patient);
                    return result;
                }
            }
            if (null == request.getLiveState()){//不限制livestate
                Integer sickLevel = request.getSickLevel();
                HashSet<Patient> patients1 = patientRepository.findAllBySickLevelAndLiveState(sickLevel,0);
                HashSet<Patient> patients2 = patientRepository.findAllBySickLevelAndLiveState(sickLevel,1);
                HashSet<Patient> patients3 = patientRepository.findAllBySickLevelAndLiveState(sickLevel,2);
                result.addAll(patients1);
                result.addAll(patients2);
                result.addAll(patients3);
                return result;
            }else if(null == request.getSickLevel()){
                Integer livestate = request.getLiveState();
                HashSet<Patient> patients1 = patientRepository.findAllBySickLevelAndLiveState(1,livestate);
                HashSet<Patient> patients2 = patientRepository.findAllBySickLevelAndLiveState(2,livestate);
                HashSet<Patient> patients3 = patientRepository.findAllBySickLevelAndLiveState(3,livestate);
                result.addAll(patients1);
                result.addAll(patients2);
                result.addAll(patients3);
                return result;
            }else{
                Integer livestate = request.getLiveState();
                Integer sickLevel = request.getSickLevel();
                result = patientRepository.findAllBySickLevelAndLiveState(sickLevel,livestate);
                return result;
            }
        }
        if(area > 4 || area < 0){
            return new HashSet<>();
        }
        HashSet<Patient> result = new HashSet<>();
        HashSet<Patient> resultReturn = new HashSet<>();

        if(null != request.getPatientId()) {
            Patient patient = patientRepository.findPatientByPatientID(request.getPatientId());
            if (null != patient) {
                if (area.equals(findWhereSickBedLocated(patient.getSickBedID()))) {
                    result.add(patient);
                }
                return result;
            }
        }
            if (null == request.getLiveState()){//不限制livestate
                Integer sickLevel = request.getSickLevel();
                HashSet<Patient> patients1 = patientRepository.findAllBySickLevelAndLiveState(sickLevel,0);
                HashSet<Patient> patients2 = patientRepository.findAllBySickLevelAndLiveState(sickLevel,1);
                HashSet<Patient> patients3 = patientRepository.findAllBySickLevelAndLiveState(sickLevel,2);
                result.addAll(patients1);
                result.addAll(patients2);
                result.addAll(patients3);
                for (Patient patient : result) {
                    if(findWhereSickBedLocated(patient.getSickBedID()).equals(area)){
                        resultReturn.add(patient);
                    }
                }
                return resultReturn;
            }else if(null == request.getSickLevel()){
                Integer livestate = request.getLiveState();
                HashSet<Patient> patients1 = patientRepository.findAllBySickLevelAndLiveState(1,livestate);
                HashSet<Patient> patients2 = patientRepository.findAllBySickLevelAndLiveState(2,livestate);
                HashSet<Patient> patients3 = patientRepository.findAllBySickLevelAndLiveState(3,livestate);
                result.addAll(patients1);
                result.addAll(patients2);
                result.addAll(patients3);
                for (Patient patient : result) {
                    if(findWhereSickBedLocated(patient.getSickBedID()).equals(area)){
                        resultReturn.add(patient);
                    }
                }
                return resultReturn;
            }else{
                Integer livestate = request.getLiveState();
                Integer sickLevel = request.getSickLevel();
                result = patientRepository.findAllBySickLevelAndLiveState(sickLevel,livestate);
                for (Patient patient : result) {
                    if(findWhereSickBedLocated(patient.getSickBedID()).equals(area)){
                        resultReturn.add(patient);
                    }
                }
                return resultReturn;
            }
        }


    public Patient modifyPatient(Patient patient) {
        Integer patientId = patient.getPatientID();
        logger.info(patient.getPatientID() + "号病人信息更改");
        if(patient.getLiveState() == 2){
            logger.info("有病人死亡，将其床位给需要的人");
            Integer area = findWhereSickBedLocated(patient.getSickBedID());
            careRepository.deleteAllByPatientID(patientId);
            stateRepository.deleteAllByPatientID(patientId);
            nucleicAcidTestRepository.deleteAllByPatientID(patientId);
            patient.setSickLevel(-1);
            patient.setSickBedID(-1);
            allocateSickBedForSomeBody(area);
        }
        patientRepository.save(patient);
        return patientRepository.findPatientByPatientID(patientId);
    }

    @Transactional
    public Patient transformPatient(Integer patientId) {
        Patient patient = patientRepository.findPatientByPatientID(patientId);
        Integer roomNurseId = careRepository.findByPatientID(patientId).getRoomNurseID();
        if (allocateSickBed(patient)) {
            careRepository.deleteByRoomNurseIDAndPatientID(roomNurseId, patientId);
            Patient newPatient = patientRepository.findPatientByPatientID(patientId);
            logger.info("病人成功转移到床位" + newPatient.getSickBedID() + ",受护士" + careRepository.findByPatientID(patientId).getRoomNurseID() + "照顾");
            return newPatient;
        } else {
            return null;
        }
    }

    private Integer findWhereSickBedLocated(Integer sickBedID) {
        if (sickBedID == 0) {
            return 4;
        }
        Integer sickRoom = sickBedRepository.findBySickBedID(sickBedID).getSickRoomID();
        Integer treateArea = sickRoomRepository.findBySickRoomID(sickRoom).getTreatAreaID();
        return treateArea;
    }

    @Transactional
    public void deleteSatisfiedPatient(Integer patientId) {
        Integer area = findWhereSickBedLocated(patientRepository.findPatientByPatientID(patientId).getSickBedID());
        patientRepository.deleteByPatientID(patientId);
        careRepository.deleteAllByPatientID(patientId);
        stateRepository.deleteAllByPatientID(patientId);
        nucleicAcidTestRepository.deleteAllByPatientID(patientId);
        allocateSickBedForSomeBody(area);
        logger.info(patientId + "号病人成功出院！");
    }

    public Integer getJob(Integer staffId){
        return staffRepository.findStaffById(staffId).getJob();
    }
    public HashSet<Patient> findPatientsNurseCared(Integer nurseid) {
        HashSet<Care> cares = careRepository.findAllByRoomNurseID(nurseid);
        if(null == cares || 0 == cares.size()){
            return new HashSet<>();
        }
        HashSet<Patient> result = new HashSet<>();
        for (Care care : cares) {
            result.add(patientRepository.findPatientByPatientID(care.getPatientID()));
        }
        logger.info("查找护士" + nurseid + "所有照顾病人的信息");
        return result;
    }

    public boolean ifPreviousDay(Date today, Date previousDay, int previous) {
        String previousStr = previousDay.toString().split(" ")[0];
        Date date1 = new Date(today.getTime() - 86400000 * previous);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 设置日期格式
        String standard = simpleDateFormat.format(date1).split(" ")[0];  // 格式转换
        logger.info(previousDay.toString() + "是" + today.toString() + "的" + previous + "天前吗？" + standard.equals(previousStr));
        return standard.equals(previousStr);
    }
}
