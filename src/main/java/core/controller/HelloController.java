package core.controller;

import core.utils.Encryptor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {

        //return Encryptor.encryptAES("esto es una prue".trim());
        return Encryptor.aesEncryptor("TEXTO DE PRUEBA A ENCRIPTAR");
    }

}
