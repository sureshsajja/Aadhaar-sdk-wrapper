package com.coderevisited.aadhar.api;


import java.io.Closeable;
import java.io.IOException;

/**
 * Author : Suresh
 * Date : 07/06/15.
 */
public interface AadhaarService extends Closeable{

    String registerDemoAuthRequest(String json) throws IOException;

}
