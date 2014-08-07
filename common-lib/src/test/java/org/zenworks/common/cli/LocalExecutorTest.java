package org.zenworks.common.cli;

import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zenworks.common.test.PowerMockFixture;

import java.io.ByteArrayInputStream;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

@PrepareForTest({Runtime.class, LocalExecutor.class})
public class LocalExecutorTest extends PowerMockFixture {

    @Mock
    Runtime mockRuntime;

    @Mock
    Process mockProcess;

    @BeforeMethod
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Runtime.class);
        when(Runtime.getRuntime()).thenReturn(mockRuntime);
        when(mockRuntime.exec(anyString())).thenReturn(mockProcess);
        when(mockProcess.getInputStream()).thenReturn(new ByteArrayInputStream("out1\\nout2\\nout3\\n".getBytes()));

    }

    @Test
    public void executeSingleCommand() throws Exception {
        LocalExecutor executor = new LocalExecutor();
        assertEquals("out1\\nout2\\nout3\\n", executor.executeCommand("cmd /c storm list"));
    }
}
