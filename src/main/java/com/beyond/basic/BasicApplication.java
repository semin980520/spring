package com.beyond.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

// 부트스트랩 코드 (실행하는 코드)
// ComponentScan에 의해 Application파일 하위 경로의 요소들이 싱글톤객체로 Scan
@SpringBootApplication
public class BasicApplication {
	public static void main(String[] args) {
		SpringApplication.run(BasicApplication.class, args);
    }
}
