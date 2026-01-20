package com.beyond.basic.b1_basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/post-view")
public class ViewController {
    @GetMapping("/url-encoded")
    public String urlEncodedView(){
        return "1-url_encoded";
    }
    @GetMapping("/multipart-formdata-image")
    public String multiPartFormDataImageView(){
        return "2-mutlipart-formdata-image";
    }
    @GetMapping("/multipart-formdata-images")
    public String multiPartFormDataImagesView(){
        return "2-mutlipart-formdata-images";
    }
    @GetMapping("/json")
    public String jsonView(){
        return "3-json";
    }
    @GetMapping("/json-nested")
    public String jsonNestedView(){
        return "4-json-nested";
    }
    @GetMapping("/json-file")
    public String jsonFileView(){
        return "5-json-file";
    }
}