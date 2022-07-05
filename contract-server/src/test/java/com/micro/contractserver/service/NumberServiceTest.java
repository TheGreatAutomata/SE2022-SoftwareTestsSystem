package com.micro.contractserver.service;

import com.micro.commonserver.service.MinioService;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = NumberService.class)
@ExtendWith(MockitoExtension.class)
class NumberServiceTest {

    @Autowired
    private NumberService numberService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void moneyToChinese() {

        System.out.println(numberService.moneyToChinese(100000000000000000.00));

        Assert.assertEquals("负壹佰叁拾柒亿捌千零壹拾贰万玖千叁佰贰拾壹元整", numberService.moneyToChinese(-13780129321.00));
        Assert.assertEquals("参数非法!", numberService.moneyToChinese(100000000000000000.00));
        Assert.assertEquals("零元整", numberService.moneyToChinese(0.00));

    }

    @Test
    void slToChinese() {

        System.out.println(numberService.slToChinese(100000000000000000.00));

        Assert.assertEquals("负壹佰叁拾柒亿捌千零壹拾贰万玖千叁佰贰拾壹 ", numberService.slToChinese(-13780129321.00));
        Assert.assertEquals("参数非法!", numberService.slToChinese(100000000000000000.00));
        Assert.assertEquals("零", numberService.slToChinese(0.00));

    }

    @Test
    void transFormation() {

        System.out.println(numberService.transFormation("100000000000000000"));

        Assert.assertEquals("壹佰叁拾柒亿捌仟零佰壹拾贰万玖仟叁佰贰拾壹", numberService.transFormation("13780129321"));
        Assert.assertEquals("壹拾零万零仟零佰零拾零万零仟零佰零拾零亿零仟零佰零拾零万零仟零佰零拾零", numberService.transFormation("100000000000000000"));
        Assert.assertEquals("零", numberService.transFormation("0"));

    }
}