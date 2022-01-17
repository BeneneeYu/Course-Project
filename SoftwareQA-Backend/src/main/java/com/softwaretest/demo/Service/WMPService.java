package com.softwaretest.demo.Service;

import com.softwaretest.demo.Controller.Request.BuyWMPRequest;
import com.softwaretest.demo.Controller.Response.BenifitsDetailResponse;
import com.softwaretest.demo.Domain.Account;
import com.softwaretest.demo.Domain.Flow;
import com.softwaretest.demo.Domain.Loan;
import com.softwaretest.demo.Domain.WMP;
import com.softwaretest.demo.Repository.AccountRepository;
import com.softwaretest.demo.Repository.FlowRepository;
import com.softwaretest.demo.Repository.LoanRepository;
import com.softwaretest.demo.Repository.WMPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class WMPService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WMPRepository wmpRepository;

    @Autowired
    private FlowRepository flowRepository;

    public int buyWMP(BuyWMPRequest request) {
        Account account = accountRepository.findAccountByAccountId(request.getAccountId());
        List<Loan> loans = loanRepository.findByAccountId(request.getAccountId());
        double sum = 0;
        for (Loan loan : loans) {
            sum += loanService.getFine(loan);
        }
        //判断是否有罚金
        if (sum > 0) {
            //还完已有罚金
            if (account.getBalance() < sum) return 1;
            for (Loan loan : loans) {
                loanService.payFine(loan.getId(), loanService.getFine(loan));
            }
            account.setBalance(account.getBalance() - sum);
        }
        //判断账户余额
        if (account.getBalance() < request.getAmount()) return 2;

        account.setBalance(account.getBalance() - request.getAmount());
        accountRepository.save(account);

        WMP wmp = new WMP(request.getAccountId(), request.getTitle(), request.getType(), request.getAmount(), request.getNumber(), request.getStartDate(), request.getEndDate());
        wmpRepository.save(wmp);

        Flow flow = new Flow("理财产品流水 购买 "+request.getTitle(),request.getAccountId(),request.getAmount(),new Timestamp(System.currentTimeMillis()));
        flowRepository.save(flow);

        return 0;
    }

    public void updateWMPs(Long accountId) {
        List<WMP> products = wmpRepository.findByAccountIdAndType(accountId, 1);
        List<WMP> funds = wmpRepository.findByAccountIdAndType(accountId, 2);
        List<WMP> shares = wmpRepository.findByAccountIdAndType(accountId, 3);

        for (WMP e : products) {
            double rate = 0.01;
            Flow benefit = new Flow("理财产品流水 收入 " + e.getTitle(), accountId, e.getAmount() * rate, new Timestamp(System.currentTimeMillis()));
            flowRepository.save(benefit);

            e.getBenifits().add(benefit);
            wmpRepository.save(e);
        }

        for (WMP e : funds) {
            double rate = 0.01;
            Flow benefit = new Flow("理财产品流水 收入 " + e.getTitle(), accountId, e.getAmount() * rate, new Timestamp(System.currentTimeMillis()));
            flowRepository.save(benefit);

            e.getBenifits().add(benefit);
            wmpRepository.save(e);
        }

        for (WMP e : shares) {
            double rate = (Math.random() - 0.5) / 10;
            Flow benefit = new Flow("理财产品流水 收入 " + e.getTitle(), accountId, e.getAmount() * rate , new Timestamp(System.currentTimeMillis()));
            flowRepository.save(benefit);

            e.getBenifits().add(benefit);
            wmpRepository.save(e);
        }
    }

    public HashMap<String, List<WMP>> allWMPs(Long accountId) {
        HashMap<String, List<WMP>> hashMap = new HashMap<>();
        hashMap.put("products", wmpRepository.findByAccountIdAndType(accountId, 1));
        hashMap.put("funds", wmpRepository.findByAccountIdAndType(accountId, 2));
        hashMap.put("shares", wmpRepository.findByAccountIdAndType(accountId, 3));

        return hashMap;
    }

    public List<BenifitsDetailResponse> benifitsDetail(Long wmpId) {
        WMP wmp = wmpRepository.findByWmpId(wmpId);
        List<BenifitsDetailResponse> result = new ArrayList<>();
        for (Flow flow : wmp.getBenifits()) {
            result.add(new BenifitsDetailResponse(flow.getAmount(), flow.getDate()));
        }

        return result;
    }

}
