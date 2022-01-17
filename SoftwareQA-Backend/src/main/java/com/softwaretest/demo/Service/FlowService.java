package com.softwaretest.demo.Service;

import com.softwaretest.demo.Controller.Response.FlowResponse;
import com.softwaretest.demo.Domain.Flow;
import com.softwaretest.demo.Repository.FlowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class FlowService {
    @Autowired
    private FlowRepository flowRepository;

    public FlowService(){

    }


    public List<FlowResponse>  findByAccountIdOrderByAmount(Long accountId, String order){
        List<FlowResponse> flowResponses = new LinkedList<>();
        List<Flow> flows ;
        if("asc".equals(order)){ flows = flowRepository.findByAccountIdOrderByAmountAsc(accountId); }
        else if("desc".equals(order)){ flows = flowRepository.findByAccountIdOrderByAmountDesc(accountId); }
        else{ flows = flowRepository.findByAccountId(accountId); }
        for(Flow flow:flows){
            flowResponses.add(new FlowResponse(flow.getType(),flow.getAccountId(),flow.getAmount(),flow.getDate().toString()));
        }
        return flowResponses;
    }
    public List<FlowResponse>  findByAccountIdOrderByDate(Long accountId, String order){
        List<FlowResponse> flowResponses = new LinkedList<>();
        List<Flow> flows = null;
        if("asc".equals(order)){
            flows = flowRepository.findByAccountIdOrderByDateAsc(accountId);
        }
        else if("desc".equals(order)){
            flows = flowRepository.findByAccountIdOrderByDateDesc(accountId);
        }
        else{
            flows = flowRepository.findByAccountId(accountId);
        }
        for(Flow flow:flows){
            flowResponses.add(new FlowResponse(flow.getType(),flow.getAccountId(),flow.getAmount(),flow.getDate().toString()));
        }
        return flowResponses;
    }

    public List<FlowResponse>  findAllOrderByDate(String order){
        List<FlowResponse> flowResponses = new LinkedList<>();
        List<Flow> flows ;
        if("asc".equals(order)){
            flows = flowRepository.findAllOrderByDateAsc();
        }
        else if("desc".equals(order)){
            flows = flowRepository.findAllOrderByDateDesc();
        }
        else{
            flows = flowRepository.findAll();
        }
        for(Flow flow:flows){
            flowResponses.add(new FlowResponse(flow.getType(),flow.getAccountId(),flow.getAmount(),flow.getDate().toString()));
        }
        return flowResponses;
    }

    public List<FlowResponse>  findAllOrderByAmount(String order){
        List<FlowResponse> flowResponses = new LinkedList<>();
        List<Flow> flows ;
        if("asc".equals(order)){
            flows = flowRepository.findAllOrderByAmountAsc();
        }
        else if("desc".equals(order)){
            flows = flowRepository.findAllOrderByAmountDesc();
        } else{
            flows = flowRepository.findAll();
        }
        for(Flow flow:flows){
            flowResponses.add(new FlowResponse(flow.getType(),flow.getAccountId(),flow.getAmount(),flow.getDate().toString()));
        }
        return flowResponses;
    }
    public List<FlowResponse> findAll(){
        List<FlowResponse> flowResponses = new LinkedList<>();
        List<Flow> flows = flowRepository.findAll();
        for(Flow flow:flows){
            flowResponses.add(new FlowResponse(flow.getType(),flow.getAccountId(),flow.getAmount(),flow.getDate().toString()));
        }
        return flowResponses;
    }
}
