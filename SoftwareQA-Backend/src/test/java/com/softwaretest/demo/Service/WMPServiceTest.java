package com.softwaretest.demo.Service;

import com.softwaretest.demo.Controller.Request.BuyWMPRequest;
import com.softwaretest.demo.Controller.Response.BenifitsDetailResponse;
import com.softwaretest.demo.Domain.Account;
import com.softwaretest.demo.Domain.WMP;
import com.softwaretest.demo.Repository.*;
//import org.junit.Assert;
//import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;


@SpringBootTest
public class WMPServiceTest {
    @Autowired
    private WMPService wmpService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    private WMPRepository wmpRepository;
    

    @BeforeEach
    void setUp() {

        wmpRepository.deleteAll();
        loanRepository.deleteAll();
        installmentRepository.deleteAll();
        accountRepository.deleteAll();
        flowRepository.deleteAll();

        LoanServiceTest loanServiceTest = new LoanServiceTest();
        loanServiceTest.accountRepository = accountRepository;
        loanServiceTest.flowRepository = flowRepository;
        loanServiceTest.installmentRepository = installmentRepository;
        loanServiceTest.loanRepository = loanRepository;
        loanServiceTest.insertAccountA();
        loanServiceTest.insertAccountB();
        loanServiceTest.insertAccountC();
    }

    @Test
    public void buyWMP() {
        // 有罚金，余额不足以支付罚金
        Account account1 = accountRepository.findByIdNumber("567890").get(0);
        BuyWMPRequest request1 = new BuyWMPRequest(account1.getAccountId(), "定期存款产品1",
                1, 1000, "2021-4-23", "2022-4-23", 1);
        int status = wmpService.buyWMP(request1);
        Assert.isTrue(status == 1);

        // 有罚金，余额足够支付罚金但不够支付理财
        Account account2 = accountRepository.findByIdNumber("123456").get(0);
        BuyWMPRequest request2 = new BuyWMPRequest(account2.getAccountId(), "定期存款产品1",
                1, 1000000, "2021-4-23", "2022-4-23", 1);
        status = wmpService.buyWMP(request2);
        Assert.isTrue(status == 2);


        // 有罚金，余额充足
        BuyWMPRequest request3 = new BuyWMPRequest(account2.getAccountId(), "定期存款产品1",
                1, 1000, "2021-4-23", "2022-4-23", 1);
        status = wmpService.buyWMP(request3);
        Assert.isTrue(status == 0);

        request3 = new BuyWMPRequest(account2.getAccountId(), "基金1",
                2, 1000, "2021-4-23", "2022-4-23", 1);
        status = wmpService.buyWMP(request3);
        Assert.isTrue(status == 0);

        request3 = new BuyWMPRequest(account2.getAccountId(), "股票1",
                3, 1000, "2021-4-23", "2022-4-23", 5000);
        status = wmpService.buyWMP(request3);
        Assert.isTrue(status == 0);

    }

    @Test
    public void allWMPs() {
        buyWMP();

        Account account2 = accountRepository.findByIdNumber("123456").get(0);
        HashMap<String, List<WMP>> hashMap = wmpService.allWMPs(account2.getAccountId());
        Assert.notNull(hashMap.get("products"));
        Assert.notNull(hashMap.get("funds"));
        Assert.notNull(hashMap.get("shares"));
    }

    @Test
    public void updateWMPs() {
        buyWMP();

        Account account2 = accountRepository.findByIdNumber("123456").get(0);
        wmpService.updateWMPs(account2.getAccountId());
    }

    @Test
    public void benifitsDetail() {
        buyWMP();
        updateWMPs();

        Account account2 = accountRepository.findByIdNumber("123456").get(0);
        HashMap<String, List<WMP>> hashMap = wmpService.allWMPs(account2.getAccountId());
        WMP product = hashMap.get("products").get(0);
        List<BenifitsDetailResponse> detail = wmpService.benifitsDetail(product.getWmpId());
        Assert.notNull(detail);
    }
}