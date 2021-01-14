package com.fudan.se.database;

import com.fudan.se.database.domain.*;
import com.fudan.se.database.repository.*;
import com.fudan.se.database.request.ImportPatientRequest;
import com.fudan.se.database.service.NurseService;
import com.fudan.se.database.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.HashSet;

@SpringBootApplication
public class DatabaseApplication {
    private DoctorRepository doctorRepository;
    private NurseLeaderRepository nurseLeaderRepository;
    private EmergencyNurseRepository emergencyNurseRepository;
    private RoomNurseRepository roomNurseRepository;
    private StaffRepository staffRepository;
    private TreatAreaRepository treatAreaRepository;
    private SickRoomRepository sickRoomRepository;
    private SickBedRepository sickBedRepository;
    private NurseService nurseService;
    private StaffService staffService;
    @Autowired
    public DatabaseApplication(DoctorRepository doctorRepository, NurseLeaderRepository nurseLeaderRepository, EmergencyNurseRepository emergencyNurseRepository, RoomNurseRepository roomNurseRepository, StaffRepository staffRepository, TreatAreaRepository treatAreaRepository, SickRoomRepository sickRoomRepository, SickBedRepository sickBedRepository, NurseService nurseService, StaffService staffService) {
        this.doctorRepository = doctorRepository;
        this.nurseLeaderRepository = nurseLeaderRepository;
        this.emergencyNurseRepository = emergencyNurseRepository;
        this.roomNurseRepository = roomNurseRepository;
        this.staffRepository = staffRepository;
        this.treatAreaRepository = treatAreaRepository;
        this.sickRoomRepository = sickRoomRepository;
        this.sickBedRepository = sickBedRepository;
        this.nurseService = nurseService;
        this.staffService = staffService;
    }


    public static void main(String[] args) {
        SpringApplication.run(DatabaseApplication.class, args);
    }
    @Bean
    public CommandLineRunner dataLoader(StaffRepository staffRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                for (int i = 1; i < 4; i++) {
                    if (null == treatAreaRepository.findByTreatAreaID(i)){
                        TreatArea treatArea = new TreatArea();
                        treatArea.setTreatAreaID(i);
                        treatAreaRepository.save(treatArea);
                    }
                }
                int roomCount = 1;
                int bedCount = 1;

                int[] roomAndBed = {4,2,1};
                for (int i = 1; i < 4; i++) {//类型
                    for (int i1 = 1; i1 < 4; i1++) {
                        if(null == sickRoomRepository.findBySickRoomID(roomCount)){
                            SickRoom sickRoom = new SickRoom();
                            sickRoom.setSickRoomID(roomCount++);
                            sickRoom.setSickRoomCount(i1);
                            sickRoom.setTreatAreaID(i);
                            sickRoomRepository.save(sickRoom);
                            int bedNum = roomAndBed[i-1];
                            for (int i2 = 1; i2 <= bedNum; i2++) {
                                SickBed sickBed = new SickBed();
                                sickBed.setSickBedID(bedCount++);
                                sickBed.setSickBedCount(i2);
                                sickBed.setSickRoomID(sickRoom.getSickRoomID());
                            sickBedRepository.save(sickBed);
                            }
                        }
                    }
                }

                Staff staff1 = new Staff("cy","123",18,1,0);
                Staff staff2 = new Staff("ly","123",18,1,0);
                Staff staff4 = new Staff("yyh","123",18,0,2);
                Staff staff5 = new Staff("xyt","123",18,0,2);
                Staff staff = new Staff("lcz","123",18,0,1);
                Staff[] doctors = new Staff[]{staff1,staff2};
                Staff[] leaders = new Staff[]{staff4,staff5};
                int docNum = staffRepository.findAllByJob(0).size();
                for (int i = 0; i < (2 - docNum); i++) {
                    Staff doc = doctors[i];
                    staffService.register(doc.getName(),doc.getPassword(),doc.getAge(),doc.getGender(),doc.getJob());
                }
                int leaderNum = staffRepository.findAllByJob(2).size();
                for (int i = 0; i < (2 - leaderNum); i++) {
                    Staff leader = leaders[i];
                    staffService.register(leader.getName(),leader.getPassword(),leader.getAge(),leader.getGender(),leader.getJob());
                }
                staffService.register(staff.getName(),staff.getPassword(),staff.getAge(),staff.getGender(),staff.getJob());

            }

        };
    }
}
