package com.example.tutorial;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProtocolBufferExample {
    public static void main(String[] args) {
        PersonProtos.Person person1 = PersonProtos.Person.newBuilder().setName("Zhang San").setEmail("abc@qq.com").setId(1111)
                .addPhone(PersonProtos.Person.PhoneNumber.newBuilder().setNumber("145245556662").setType(0))
                .addPhone(PersonProtos.Person.PhoneNumber.newBuilder().setNumber("010254785444").setType(1)).build();

        try {
            FileOutputStream outputStream = new FileOutputStream("D:\\Study\\workspace\\StudyProject\\yarnStudy\\src\\main\\resources\\example.txt");
            person1.writeTo(outputStream);

            outputStream.close();
        } catch (IOException e) {
            System.out.println("Write Error !");
            e.printStackTrace();
        }

        try {
            FileInputStream inputStream = new FileInputStream("D:\\Study\\workspace\\StudyProject\\yarnStudy\\src\\main\\resources\\example.txt");
            PersonProtos.Person person2 = PersonProtos.Person.parseFrom(inputStream);
            System.out.println("Person2:" + person2);
        } catch (IOException e) {
            System.out.println("Read Error !");
            e.printStackTrace();
        }


    }
}
