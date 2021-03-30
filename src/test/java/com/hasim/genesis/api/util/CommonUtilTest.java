package com.hasim.genesis.api.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hasim.genesis.api.model.ErrorResponse;
import com.hasim.genesis.api.util.CommonUtil;

@RunWith(MockitoJUnitRunner.class)
public class CommonUtilTest {
    @Test
    public void testConvertStringToObject() {

        String expectedCode = "test";
        String expectedMessage = "test message";

        ErrorResponse actualErrorResponse =
            CommonUtil.convertStringToObject("{\"Error\":{\"Code\":\"" + expectedCode
                + "\",\"Message\":\"" + expectedMessage + "\"}}", ErrorResponse.class);
        assertNotNull(actualErrorResponse);
        assertEquals(expectedCode, actualErrorResponse.getCode());
        assertEquals(expectedMessage, actualErrorResponse.getMessage());
    }
    
    
    @Test
    public void testConvertStringToObjectNull() {

       

        ErrorResponse actualErrorResponse =
            CommonUtil.convertStringToObject("sdsd", ErrorResponse.class);
        assertNull(actualErrorResponse);
       
    }
    
    @Test
    public void testConvertObjectToJson() {

        String expectedCode = "test";
        String expectedMessage = "test message";
        String expectedResponse = "{\"Error\":{\"Message\":\"" +expectedMessage  + "\",\"Code\":\""
            + expectedCode + "\"}}";
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(expectedCode);
        errorResponse.setMessage(expectedMessage);
        String actualErrorResponse = null;
        try {
            actualErrorResponse = CommonUtil.convertObjectToJson(errorResponse);
        } catch (JsonProcessingException e) {

        }
        assertNotNull(actualErrorResponse);
        assertEquals(expectedResponse, actualErrorResponse);
    }
    @Test
    public void testConvertObjectToJsonNull() {

 
        String actualErrorResponse = null;
        try {
            actualErrorResponse = CommonUtil.convertObjectToJson(null);
        } catch (JsonProcessingException e) {

        }
        assertNull(actualErrorResponse);
    }
}
