package com.coderevisited.aadhar;

import com.coderevisited.aadhar.api.AadhaarService;
import com.coderevisited.aadhar.api.AadhaarServiceFactory;
import com.coderevisited.aadhar.api.ConnectionSettings;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author : Suresh
 * Date : 07/06/15.
 */
public class AadhaarServiceTest {


    private static AadhaarService service;

    @BeforeClass
    public static void init() {
        service = AadhaarServiceFactory.create(new ConnectionSettings());
    }

    @Test
    public void demoAuthentication() throws IOException {

        String json = getResource("demo.json");
        String response = service.registerDemoAuthRequest(json);
        System.out.println(response);

    }


    private static String getResource(String name) throws IOException {
        InputStream is = AadhaarServiceTest.class.getClassLoader().getResourceAsStream(name);
        if (is == null) {
            throw new FileNotFoundException("File is not found");
        }

        int c;
        StringBuilder sb = new StringBuilder();
        while ((c = is.read()) != -1) {
            sb.append((char) c);
        }
        return sb.toString();
    }

    @AfterClass
    public static void tearDown() throws IOException {
        service.close();
    }

}
