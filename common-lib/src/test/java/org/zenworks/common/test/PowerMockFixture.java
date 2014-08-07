package org.zenworks.common.test;

import org.mockito.MockitoAnnotations;
import org.powermock.modules.testng.PowerMockObjectFactory;

import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;

public class PowerMockFixture {

    @BeforeMethod
    public void setUpMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

}

