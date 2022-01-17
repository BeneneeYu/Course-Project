package com.ooad.lab2.service;

import com.ooad.lab2.domain.InstructingPlan;
import com.ooad.lab2.domain.Major;
import com.ooad.lab2.repository.InstructingPlanRepository;
import com.ooad.lab2.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2022-01-07 16:54
 **/
@Transactional
@Service
public class MajorService {
    @Autowired
    MajorRepository majorRepository;

    @Autowired
    InstructingPlanRepository instructingPlanRepository;

    public Major createMajor(Major newMajor){
        return majorRepository.save(newMajor);
    }

    public Set<Major> selectMajors(){
        Set<Major> majors = new HashSet<>();
        Iterator<Major> majorIterator = majorRepository.findAll().iterator();
        while (majorIterator.hasNext()){
            majors.add(majorIterator.next());
        }
        return majors;
    }
}
