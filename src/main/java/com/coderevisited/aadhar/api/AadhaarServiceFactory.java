package com.coderevisited.aadhar.api;


import com.coderevisited.aadhar.impl.DefaultAadhaarService;

/**
 * Author : Suresh
 * Date : 07/06/15.
 */
public class AadhaarServiceFactory {


    public static AadhaarService create(ConnectionSettings settings){

        return new DefaultAadhaarService(settings);

    }

}
