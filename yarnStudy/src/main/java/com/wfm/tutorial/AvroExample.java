package com.wfm.tutorial;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AvroExample {
    public static void main(String[] args) {
        PhoneNumber phoneNumber1 = PhoneNumber.newBuilder()
                .setNumber("152455534875")
                .setType(0).build();

        PhoneNumber phoneNumber2 = PhoneNumber.newBuilder()
                .setNumber("125455122")
                .setType(1).build();

        ArrayList<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
        phoneNumbers.add(phoneNumber1);
        phoneNumbers.add(phoneNumber2);

        Person suLu = Person.newBuilder()
                .setName("Su Lu")
                .setEmail("45789521212@qq.com")
                .setId(111)
                .setPhone(phoneNumbers).build();

        File file = new File("D:\\Study\\workspace\\StudyProject\\yarnStudy\\src\\main\\resources\\person.txt");

        try {
            SpecificDatumWriter<Person> personSpecificDatumWriter = new SpecificDatumWriter<Person>(Person.class);
            DataFileWriter<Person> dataFileWriter = new DataFileWriter<Person>(personSpecificDatumWriter);
            dataFileWriter.create(suLu.getSchema(),file);
            dataFileWriter.append(suLu);
            dataFileWriter.close();
        } catch (IOException e) {
            System.out.println("Write Error !");
            e.printStackTrace();
        }

        try {
            SpecificDatumReader<Person> personSpecificDatumReader = new SpecificDatumReader<Person>(Person.class);
            DataFileReader<Person> dataFileReader = new DataFileReader<Person>(file,personSpecificDatumReader);

            suLu = null;
            while (dataFileReader.hasNext()){
                suLu = dataFileReader.next();
                System.out.println(suLu);
            }
        } catch (IOException e) {
            System.out.println("Read Error !");
            e.printStackTrace();
        }


    }
}
