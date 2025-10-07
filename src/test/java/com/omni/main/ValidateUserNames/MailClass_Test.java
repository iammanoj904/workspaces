package com.omni.main.ValidateUserNames;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MailClass_Test {

    @Test
    public void testWithMain(){
        assertDoesNotThrow(()->ValidateUserNamesApplication.main(new String[]{}));
    }
}
