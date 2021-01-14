package com.fudan.se.database.controller;

import com.fudan.se.database.domain.NucleicAcidTest;
import com.fudan.se.database.domain.Patient;
import com.fudan.se.database.domain.Staff;
import com.fudan.se.database.domain.State;
import com.fudan.se.database.repository.PatientRepository;
import com.fudan.se.database.request.*;
import com.fudan.se.database.response.SickBedResponse;
import com.fudan.se.database.service.NurseService;
import com.fudan.se.database.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-25 18:40
 **/
@RestController()
public class NurseController {
    NurseService nurseService;
    StaffService staffService;

    Logger logger = LoggerFactory.getLogger(NurseController.class);
    @Autowired
    public NurseController(NurseService nurseService, StaffService staffService) {
        this.nurseService = nurseService;
        this.staffService = staffService;
    }


    @PostMapping("/addNewPatient")
    public ResponseEntity<HashMap<String, Object>> importPatient(HttpServletRequest httpServletRequest, @RequestBody ImportPatientRequest importPatientRequest) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("patient", nurseService.importPatient(importPatientRequest));
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/submitDailyInformation")
    public ResponseEntity<HashMap<String, Object>> submitDailyInformation(HttpServletRequest httpServletRequest, @RequestBody SubmitDailyInformationRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        State state = nurseService.submitDailyInformation(request);
        hashMap.put("status",null == state?-1:1);
        hashMap.put("state", state);
        hashMap.put("patient",nurseService.findPatient(request.getPatientId()));
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/submitNewTest")
    public ResponseEntity<HashMap<String, Object>> submitNewTest(HttpServletRequest httpServletRequest, @RequestBody SubmitNewTest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        NucleicAcidTest test = nurseService.submitNewTest(request);
        hashMap.put("status",null == test?-1:1);
        hashMap.put("nucleicAcidTest",test);
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/searchNurseLeader")
    public ResponseEntity<HashMap<String, Object>> searchNurseLeader(HttpServletRequest httpServletRequest, @RequestBody StaffIdRequest staffIdRequest) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Integer treatArea = staffService.findTreatAreaByStaffId(staffIdRequest.getStaffId());
        hashMap.put("nurseLeader", nurseService.findNurseLeader(treatArea));
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/searchRoomNurse")
    public ResponseEntity<HashMap<String, Object>> searchRoomNurse(HttpServletRequest httpServletRequest, @RequestBody StaffIdRequest staffIdRequest) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Integer treatArea = staffService.findTreatAreaByStaffId(staffIdRequest.getStaffId());
        HashSet<Staff> staffs = nurseService.findRoomNurseInTreatArea(treatArea);
        hashMap.put("roomNurses", staffs);
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/addNewRoomNurse")
    public ResponseEntity<HashMap<String, Object>> addNewRoomNurse(HttpServletRequest httpServletRequest, @RequestBody AddNewRoomNurseRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Staff staff = nurseService.insertNurse(request);
        hashMap.put("roomNurse", staff);
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/deleteRoomNurse")
    public ResponseEntity<HashMap<String, Object>> deleteRoomNurse(HttpServletRequest httpServletRequest, @RequestBody StaffIdRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        boolean flag = nurseService.deleteRoomNurse(request.getStaffId());
        hashMap.put("status", flag ? 1 : -1);
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/searchSickBed")
    public ResponseEntity<HashMap<String, Object>> searchSickBed(HttpServletRequest httpServletRequest, @RequestBody StaffIdRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Integer treatArea = staffService.findTreatAreaByStaffId(request.getStaffId());
        HashSet<SickBedResponse> sickBedResponses = nurseService.findSickBedsInArea(treatArea);
        hashMap.put("sickBeds", sickBedResponses);
        return ResponseEntity.ok(hashMap);

    }

    @PostMapping("/searchSatisfiedPatient")
    public ResponseEntity<HashMap<String, Object>> searchSatisfiedPatient(HttpServletRequest httpServletRequest, @RequestBody StaffIdRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashSet<Patient> patients = new HashSet<>();
        Integer treatArea = staffService.findTreatAreaByStaffId(request.getStaffId());
        patients = nurseService.findPatientsCanLeaveHospital();
        hashMap.put("patients", patients);
        return ResponseEntity.ok(hashMap);

    }


    @PostMapping("/deleteSatisfiedPatient")
    public ResponseEntity<HashMap<String, Object>> searchSatisfiedPatient(HttpServletRequest httpServletRequest, @RequestBody PatientRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        nurseService.deleteSatisfiedPatient(request.getPatientId());
        hashMap.put("status", 1);
        return ResponseEntity.ok(hashMap);

    }

    @PostMapping("/searchSpecialPatient")
    public ResponseEntity<HashMap<String, Object>> searchSpecialPatient(HttpServletRequest httpServletRequest, @RequestBody StaffIdRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Integer treatArea = staffService.findTreatAreaByStaffId(request.getStaffId());
        HashSet<Patient> patients = nurseService.searchSpecialPatient(treatArea);
        hashMap.put("patients", patients);
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/searchPatientByRoomNurseId")
    public ResponseEntity<HashMap<String, Object>> searchPatientByRoomNurseId(HttpServletRequest httpServletRequest, @RequestBody RoomNurseAndStaffRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashSet<Patient> patients = new HashSet<>();
        int status = 0;
        if(request.getRoomNurseId().equals(request.getStaffId())){
            logger.info("护士" + request.getStaffId() + "查询自己的病人");
            if (nurseService.getJob(request.getStaffId()) != 3){
                logger.info(String.valueOf(nurseService.getJob(request.getStaffId())));
                status = 1;
            }else{
                patients = nurseService.findPatientsNurseCared(request.getRoomNurseId());
                if (null == patients || 0 == patients.size()) {
                    status = -1;
                }
            }
        }else{
            //查询人所在区域
            Integer treatArea = staffService.findTreatAreaByStaffId(request.getStaffId());
            //想要查询的护士所在的区域
            Integer nurseArea = staffService.findTreatAreaByStaffId(request.getRoomNurseId());
            if(treatArea.equals(nurseArea)){
                patients = nurseService.findPatientsNurseCared(request.getRoomNurseId());
                if (null == patients || 0 == patients.size()) {
                    status = -1;
                }
            }else{
                status = 1;
            }
        }
        hashMap.put("patients", patients);
        hashMap.put("status", status);
        return ResponseEntity.ok(hashMap);

    }

    @PostMapping("/searchPatientByState")
    public ResponseEntity<HashMap<String, Object>> searchPatientByState(HttpServletRequest httpServletRequest, @RequestBody searchPatientRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("patients", nurseService.searchPatientByState(request));
        return ResponseEntity.ok(hashMap);

    }

    @PostMapping("/transformPatient")
    public ResponseEntity<HashMap<String, Object>> searchSpecialPatient(HttpServletRequest httpServletRequest, @RequestBody PatientRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Patient patient = nurseService.transformPatient(request.getPatientId());
        hashMap.put("patient", patient);
        hashMap.put("status", null == patient ? -1 : 1);
        return ResponseEntity.ok(hashMap);

    }

    @PostMapping("/modifySickLevel")
    public ResponseEntity<HashMap<String, Object>> modifySickLevel(HttpServletRequest httpServletRequest, @RequestBody ModifyStateRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Patient patient = nurseService.findPatient(request.getPatientId());
        patient.setSickLevel(request.getNewSickLevel());
        Patient patient1 = nurseService.modifyPatient(patient);
        hashMap.put("status", null == patient1 ? -1 : 1);
        hashMap.put("patient", patient1);
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/modifyLiveState")
    public ResponseEntity<HashMap<String, Object>> modifyLiveState(HttpServletRequest httpServletRequest, @RequestBody ModifyStateRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Patient patient = nurseService.findPatient(request.getPatientId());
        patient.setLiveState(request.getNewLiveState());
        Patient patient1 = nurseService.modifyPatient(patient);
        hashMap.put("status", null == patient1 ? -1 : 1);
        hashMap.put("patient", patient1);
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/emergencyNurseSearch")
    public ResponseEntity<HashMap<String, Object>> emergencyNurseSearch(HttpServletRequest httpServletRequest, @RequestBody TreatAreaRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashSet<Patient> patients = nurseService.findPatientsInTreatArea(request.getTreatArea());
        hashMap.put("patients", patients);
        return ResponseEntity.ok(hashMap);
    }
}