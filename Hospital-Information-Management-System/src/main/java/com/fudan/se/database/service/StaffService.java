package com.fudan.se.database.service;

import com.fudan.se.database.domain.*;
import com.fudan.se.database.repository.*;
import com.fudan.se.database.request.AddNewRoomNurseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.HashSet;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-24 21:06
 **/
@Service
public class StaffService {
    Logger logger = LoggerFactory.getLogger(StaffService.class);

    private DoctorRepository doctorRepository;
    private NurseLeaderRepository nurseLeaderRepository;
    private EmergencyNurseRepository emergencyNurseRepository;
    private RoomNurseRepository roomNurseRepository;
    private StaffRepository staffRepository;
    private TreatAreaRepository treatAreaRepository;

    @Autowired
    public StaffService(DoctorRepository doctorRepository, NurseLeaderRepository nurseLeaderRepository, EmergencyNurseRepository emergencyNurseRepository, RoomNurseRepository roomNurseRepository, StaffRepository staffRepository, TreatAreaRepository treatAreaRepository) {
        this.doctorRepository = doctorRepository;
        this.nurseLeaderRepository = nurseLeaderRepository;
        this.emergencyNurseRepository = emergencyNurseRepository;
        this.roomNurseRepository = roomNurseRepository;
        this.staffRepository = staffRepository;
        this.treatAreaRepository = treatAreaRepository;
    }





    @Transactional
    public Staff register(String name, String password, Integer age, Integer gender, Integer job, Integer treatAreaID) {
        if (job == 0) {
            if (doctorRepository.findAll().size() >= 3) {
                return null;
            }
        } else if (job == 2) {
            if (nurseLeaderRepository.findAll().size() >= 3) {
                return null;
            }
        }
        Staff staff = new Staff(name, password, age, gender, job);
        staffRepository.save(staff);
        Integer kind = staff.getJob();
        Integer id = staff.getId();
        HashSet<TreatArea> treatAreas = treatAreaRepository.findAll();
        switch (kind) {
            case 0:
                Doctor doctor = new Doctor();
                doctor.setStaffID(id);
                for (TreatArea treatArea : treatAreas) {
                    if (null == doctorRepository.findDoctorByTreatAreaID(treatArea.getTreatAreaID()) && treatArea.getTreatAreaID() != 4) {
                        doctor.setTreatAreaID(treatArea.getTreatAreaID());
                        break;
                    }
                }
                doctorRepository.save(doctor);
                break;
            case 1:
                EmergencyNurse emergencyNurse = new EmergencyNurse(0, id);
                emergencyNurseRepository.save(emergencyNurse);
                break;
            case 2:
                NurseLeader nurseLeader = new NurseLeader();
                nurseLeader.setStaffID(id);
                for (TreatArea treatArea : treatAreas) {
                    if (null == nurseLeaderRepository.findNurseLeaderByTreatAreaID(treatArea.getTreatAreaID()) && treatArea.getTreatAreaID() != 4) {
                        nurseLeader.setTreatAreaID(treatArea.getTreatAreaID());
                        break;
                    }
                }
                nurseLeaderRepository.save(nurseLeader);
                break;
            case 3:
                RoomNurse roomNurse = new RoomNurse(0, id, treatAreaID);
                roomNurseRepository.save(roomNurse);
                break;
            default:
                break;
        }
        return staff;
    }
    @Transactional
    public Staff register(String name, String password, Integer age, Integer gender, Integer job) {
        if (job == 0) {
            if (doctorRepository.findAll().size() >= 3) {
                return null;
            }
        } else if (job == 2) {
            if (nurseLeaderRepository.findAll().size() >= 3) {
                return null;
            }
        }
        Staff staff = new Staff(name, password, age, gender, job);
        staffRepository.save(staff);
        Integer kind = staff.getJob();
        Integer id = staff.getId();
        HashSet<TreatArea> treatAreas = treatAreaRepository.findAll();
        switch (kind) {
            case 0:
                Doctor doctor = new Doctor();
                doctor.setStaffID(id);
                for (TreatArea treatArea : treatAreas) {
                    if (null == doctorRepository.findDoctorByTreatAreaID(treatArea.getTreatAreaID()) && treatArea.getTreatAreaID() != 4) {
                        doctor.setTreatAreaID(treatArea.getTreatAreaID());
                        break;
                    }
                }
                doctorRepository.save(doctor);
                break;
            case 1:
                EmergencyNurse emergencyNurse = new EmergencyNurse(0, id);
                emergencyNurseRepository.save(emergencyNurse);
                break;
            case 2:
                NurseLeader nurseLeader = new NurseLeader();
                nurseLeader.setStaffID(id);
                for (TreatArea treatArea : treatAreas) {
                    if (null == nurseLeaderRepository.findNurseLeaderByTreatAreaID(treatArea.getTreatAreaID()) && treatArea.getTreatAreaID() != 4) {
                        nurseLeader.setTreatAreaID(treatArea.getTreatAreaID());
                        break;
                    }
                }
                nurseLeaderRepository.save(nurseLeader);
                break;
            case 3:
                RoomNurse roomNurse = new RoomNurse(0, id, -1);
                roomNurseRepository.save(roomNurse);
                break;
            default:
                break;
        }
        return staff;
    }

    public Integer findTreatAreaByStaffId(Integer staffId) {
        Staff staff = staffRepository.findStaffById(staffId);
        if(null == staff){
            return 1024;
        }else{
            Integer job = staff.getJob();
            switch (job) {
                case 0:
                    return doctorRepository.findByStaffID(staffId).getTreatAreaID();
                case 2:
                    return nurseLeaderRepository.findByStaffID(staffId).getTreatAreaID();
                case 3:
                    return roomNurseRepository.findByStaffID(staffId).getTreatAreaID();
                default:
                    //急诊护士返回-1
                    return -1;
            }
        }

    }

    public Staff login(Integer id, String password) {
        Staff staff = staffRepository.findStaffById(id);
        if (null == staff || !staff.getPassword().equals(password)) {
            return null;
        }
        return staff;
    }
    @Transactional
    public Staff modifyStaff(Integer id,String password,String name,Integer age,Integer gender){
        Staff staff = staffRepository.findStaffById(id);
        staff.setAge(age);
        staff.setGender(gender);
        staff.setName(name);
        staff.setPassword(password);
        staffRepository.save(staff);
        return staff;
    }

}
