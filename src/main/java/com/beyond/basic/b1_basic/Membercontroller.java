package com.beyond.basic.b1_basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Controller 어노테이션을 통해 스프링에 의해 객체가 생성되고,
// 관리되어 개발자가 직접 객체를 생성 할 필요 없음.
// Controller 어노테이션과 url매핑을 통해 사용자의 요청이 메서드로 분기처리
@Controller
@RequestMapping("/member")
public class Membercontroller {
//    get 요청 : json, www, multipart : requesett
//    get요청 리턴의 case : text, josn, html(mvc)
//    case1. 서버가 사용자에게 text데이터 return
    @GetMapping("/post/1") // Controller에서 객체가 생성되고 메핑에서 라운팅함
//    data(text, json)를 리턴할때에는 responsebody 사용
//    화면(html)을 리턴할때에는 responsebody 생략.
//    Controller+responsebody = Restcontroller
    @ResponseBody // 리턴을 String data형식으로 전환해주는
    public String textDataReturn(){ //메서드명은 중요x 위 메핑의 url이 중요
        return "hong1";
    }

//    case2. 서버가 사용자에게 json형식의 문자데이터 return
    @GetMapping("/json")
    @ResponseBody
    public Member jsonDataReturn() throws JsonProcessingException {
        Member m1 = new Member("h1","h1@naver.com");
//        직접 json을 직려화 할 필요 없이 return타입에 객체가 있다면 자동으로 직렬화
//        ObjectMapper o1 = new ObjectMapper();
//        String data = o1.writeValueAsString(m1);
        return m1;
    }
//    case3. 서버가 사용자에게 html return
//    case3-1)정적인 html return
    @GetMapping("/html")
//    responsebody가 없고 return타입이 String인 경우
//    스프링은 templates폴더 밑에 simple_Html.html을 찾아서 리턴.
//    타임리프 의존성이 필요
    public String htmlReturn(){

        return "simple_Html";
    }
//    case3. 서버가 사용자에게 html return
//    case3-2)서버에서 화면+데이터를 함께 주는 동적인 화면
//    현재 이 방식은  ssr(서버사이드랜더링)방식.
//    csr방식은 화면은 별도제공하고 서버는 데이터만 제공
    @GetMapping("/html/dynamic")
//    responsebody가 없고 return타입이 String인 경우
//    스프링은 templates폴더 밑에 simple_Html.html을 찾아서 리턴.
//    타임리프 의존성이 필요
    public String dynamicHtmlReturn(Model model){
//        model객체는 데이터를 화면에 전달해주는 역할
//        name=hongildong 형태로 화면에 전달
        model.addAttribute("name","hongildong"); // .html 파일에서 xmlns:th = "http://www.thymeleaf.org" 로 기술을 타임레프기술을 사용하고
        model.addAttribute("email","hongildong@naver.com"); // th:text="${name}" 으로 값을 동적으로 꺼내옴
        return "dynamic_Html";
    }

//    get요청의 url의 데이터 추출방식 : pathvariable, 쿼리파라미터
//    case1 . pathvariable방식을 통해 사용자로부터 url에서 데이터 추출
//    데이터의 형식 : /member/path/1
    @GetMapping("/path/{inputId}")
    @ResponseBody
//    사용자가 url을 칠때까진 String
    public String path(@PathVariable Long inputId){
//        별도의 형변환 없이도, 원하는 자료형으로 형변환되어 매개변수로 주입.
//        매개변수의 변수명이 url의 변수명과 일치해야함
        System.out.println(inputId);

        return "OK";
    }
//    case2. parameter방식을 통한 url에서의 데이터 추출(주로 검색, 정렬 요청등의 상황에서 사용)
//    case2-1. 1개의 파라미터에서 데이터 추출
//    데이터형식 : member/param1?name=hongildong
    @GetMapping("/param1")
    @ResponseBody
    public String param(@RequestParam(value = "name")String inputName){
        System.out.println(inputName);
        return "ok";
    }
    //    case2-2. 2개의 파라미터에서 데이터 추출
//    데이터형식 : member/param2?name=hongildong&email=hong@naver.com
    @GetMapping("/param2")
    @ResponseBody
    public String param1(@RequestParam(value = "name")String inputName, @RequestParam(value = "email")String inputEmail){
        System.out.println(inputName);
        System.out.println(inputEmail);
        return "ok";
    }
    //    case2-3. 파라미터의 개수가 많아질 경우, ModelAttribute를 통한 데이터방식
//    데이터바인딩은 param의 데이터를 모아 객체로 자동 매핑 및 생성
//    데이터형식 : member/param3?name=hongildong&email=hong@naver.com
    @GetMapping("/param3")
    @ResponseBody
//    @ModelAttribute는 생략 가능.
    public String param3(@ModelAttribute Member member1){
//        System.out.println(member1.getEmail()); //객체 형식이 아닌 email만 뽑아옴
        System.out.println(member1);
        return "ok";
    }
//    post요청 처리 case : urlencoded, multipart-formadta, json
//    case1. body의 content-type이 urlencoded형식
//    데이터형식 : body부에 name=hongildong&email=hong@naver.com
    @PostMapping("/url-encoded")
    @ResponseBody
//    형식이 url의 파라미터 방식과 동일하므로 RequestParam또는 데이터 바인딩 가능
    public String urlEncoded(@ModelAttribute Member member){
        System.out.println(member);

        return "ok";
    }
    //    case2. body의 content-type이 multipart-formdata
//        case2-1. 1개의 이미지만 있는 경우
//    데이터형식 : body부에 name=hongildong&email=hong@naver.com&profileImage=xxx
    @PostMapping("/multipart-formdata")
    @ResponseBody
//    형식이 url의 파라미터 방식과 동일하므로 RequestParam또는 데이터 바인딩 가능
//    profileImage 타입으로 이미지 추출
    public String mutlipartFormdata(@ModelAttribute Member member,@RequestParam(value = "profileImage")MultipartFile profileImage){
        System.out.println(member);
        System.out.println(profileImage.getOriginalFilename());
        return "ok";
    }
//    case2-2. N개의 이미지가 있는 경우
    @PostMapping("/multipart-formdata-images")
    @ResponseBody
//    형식이 url의 파라미터 방식과 동일하므로 RequestParam또는 데이터 바인딩 가능
//    profileImage 타입으로 이미지 추출
    public String mutlipartFormdataImages(@ModelAttribute Member member,@RequestParam(value = "profileImages") List<MultipartFile> profileImages){
        System.out.println(member);
        System.out.println(profileImages.size());
        return "ok";
    }
//    case3. body의 content-type이 json
//    case3-1. 일반적인 json데이터 처리
//    데이터형식 : {"name":"hongildong","email":"hong@naver.com"}
    @PostMapping("/json")
    @ResponseBody
//    @RequestBody : json데이터를 객체로 파싱
    public String json(@RequestBody Member member){
        System.out.println(member);
        return "ok";
    }
    //    case3-2. 배열형식의 json데이터 처리
//    데이터형식 : [{"name":"hongildong","email":"hong1@naver.com"}, {"name":"hongildong","email":"hong2@naver.com"}, {"name":"hongildong","email":"hong3@naver.com"}]
    @PostMapping("/json-list")
    @ResponseBody
//    @RequestBody : json데이터를 객체로 파싱
    public String jsonList(@RequestBody List<Member> memberlist){
        System.out.println(memberlist);

        return "ok";
    }

//    case3-3. 중첩된 json 데이터 처리
//    데이터형식 : {"name":"hongildong", "email":"hong1@naver.com", "scores":[{"subject":"math", "point":100}, {"subject":"english", "point":90}, {"subject":"korean", "point":100}]}
    @PostMapping("/json-nested")
    @ResponseBody
    public String jsonNested(@RequestBody Student student){
        System.out.println(student);
        return "ok";
    }
//    case3-4. json + file 이 합께 있는 데이터 처리
//    데이터형식 : member={"name":"xx","email":"yy"}&profileImage=바이너리
//    결론은 multipart-formpart구조안에 json을 넣는 방식.
    @PostMapping("/json-file")
    @ResponseBody
//    json과 file을 함께 처리해야 할 때는 @RequestPart 사용
    public String jsonFile(@RequestPart("member") Member member, @RequestPart("profileImage") MultipartFile profileImage){
        System.out.println(member);
        System.out.println(profileImage.getOriginalFilename());

        return "ok";
    }
}
