package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class ApplicationTests {

    // Environment는 spring에 들어있는 것을 가져와야 한다.
    @Autowired
    Environment environment;

    @Test
    void contextLoads() {
        assertThat(environment.getProperty("dongwoo.name"))
                .isEqualTo("dongwoo");
    }

}
